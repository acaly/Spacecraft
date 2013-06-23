package spacecraft.core.utility;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import spacecraft.core.world.WorldLinkInfo;
import spacecraft.core.world.WorldSeparationInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class WorldSavedDataSC extends WorldSavedData {
	public static final String dataId = "spacecraft.worlddata";
	public static final String DATALINKINFO = "link";
	public static final String DATASEPARATION = "separation";
	public static final String DATADIMENSION = "dim";

	public String debugString = "";
	private Map<String, ISavedData> dataMap = new HashMap<String, ISavedData>();
	private int dimension;
	
	private static final String DATA = "data";
	private static final String DEBUG = "debug";
	
	public static final int METHODAPPEND = 1;
	public static final int METHODREMOVE = 2;
	
	private static Field chunkWorld;
	static {
		try {
			chunkWorld = ChunkCache.class.getDeclaredField("worldObj");
			chunkWorld.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WorldSavedDataSC() {
		super(dataId);
	}
	
	//used by MapStorage
	public WorldSavedDataSC(String fileName) {
		super(dataId);
	}
	
	public static WorldSavedDataSC forWorld(World world) {
		if (world == null) return null;
		if (world.isRemote) {
			return null;
		} else {
			MapStorage storage = world.perWorldStorage;
			WorldSavedDataSC result = (WorldSavedDataSC)storage.loadData(WorldSavedDataSC.class, dataId);
			if (result == null) {
				result = new WorldSavedDataSC();
				result.init();
				storage.setData(dataId, result);
			}
			return result;
		}
	}
	
	private void init() {
		setData(DATALINKINFO, new WorldLinkInfo());
		setData(DATASEPARATION, new WorldSeparationInfo());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		dataMap.clear();
		init();
		
		debugString = nbttagcompound.getString(DEBUG);
		
		NBTTagCompound list = nbttagcompound.getCompoundTag(DATA);
		for (Entry<String, ISavedData> i : dataMap.entrySet()) {
			if (list.hasKey(i.getKey()))
				i.getValue().readFromNBT(list.getCompoundTag(i.getKey()));
		}
		
		dimension = nbttagcompound.getInteger(DATADIMENSION);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString(DEBUG, debugString);
		
		NBTTagCompound list = new NBTTagCompound();
		NBTTagCompound item;
		for (Entry<String, ISavedData> i : dataMap.entrySet()) {
			item = new NBTTagCompound();
			i.getValue().writeToNBT(item);
			list.setCompoundTag(i.getKey(), item);
		}
		nbttagcompound.setCompoundTag(DATA, list);
		
		nbttagcompound.setInteger(DATADIMENSION, dimension);
	}
	
	public void setData(String key, ISavedData data) {
		dataMap.put(key, data);
	}
	
	public ISavedData getData(String key) {
		return dataMap.get(key);
	}
	
	@Override
	public boolean isDirty() {
		boolean r = false;
		for (ISavedData i : dataMap.values()) {
			r = r || i.isDirty();
		}
		return r;
	}
	
	public static WorldSavedDataSC forChunkCache(ChunkCache chunk) {
		try {
			return forWorld((World) chunkWorld.get(chunk));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//data sync
	public static void sendDataToPlayer(EntityPlayerMP player) {
		//TODO send all data
	}
	
	public void onDataChanged(String id, int method, int dataId, NBTTagCompound data) {
		//TODO send part of the data
	}
	
	//client storage
	@SideOnly(Side.CLIENT)
	private HashMap<Integer, WorldSavedDataSC> worldDataMap;
	
	public static void onReceivedData(int dimension, String id, int method, int dataId, NBTTagCompound data) {
		//TODO client received data
	}
	
	public static void onReceivedWorldData(int dimension, NBTTagCompound data) {
		//TODO receive data
	}
}
