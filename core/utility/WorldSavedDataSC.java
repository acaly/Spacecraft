package spacecraft.core.utility;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import spacecraft.core.world.WorldLinkInfo;
import spacecraft.core.world.WorldSeparationInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

public class WorldSavedDataSC extends WorldSavedData {
	public static final String dataId = "spacecraft.worlddata";
	public static final int DATASEPARATION = 0;
	public static final int DATALINKINFO = 1;
	
	public static final String PACKETDATAID = "id";
	public static final String PACKETHEAD = "head";
	public static final String PACKETDATA = "data";

	public String debugString = "";

	public ISavedData[] data = new ISavedData[2];
	private String[] dataKey = new String[] {"separation", "link"};
	
	private int dimension;
	
	private static final String DATA = "data";
	private static final String DEBUG = "debug";
	private static final String DIMENSION = "dim";
	
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
		init();
	}
	
	//used by MapStorage
	public WorldSavedDataSC(String fileName) {
		super(dataId);
	}
	
	public static WorldSavedDataSC forWorld(World world) {
		if (world == null) return null;
		if (world.isRemote) {
			WorldSavedDataSC r = worldDataMap.get(world.provider.dimensionId);
			if (r == null) {
				r = new WorldSavedDataSC();
				worldDataMap.put(world.provider.dimensionId, r);
			}
			return r;
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

		data[DATASEPARATION] = new WorldSeparationInfo();
		data[DATALINKINFO] = new WorldLinkInfo();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		init();
		
		debugString = nbttagcompound.getString(DEBUG);
		
		NBTTagCompound list = nbttagcompound.getCompoundTag(DATA);
		for (int i = 0; i < data.length; ++i) {
			if (list.hasKey(dataKey[i])) {
				data[i].readFromNBT(list.getCompoundTag(dataKey[i]));
			}
		}
		
		dimension = nbttagcompound.getInteger(DIMENSION);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString(DEBUG, debugString);
		
		NBTTagCompound list = new NBTTagCompound();
		NBTTagCompound item;
		for (int i = 0; i < data.length; ++i) {
			item = new NBTTagCompound();
			data[i].writeToNBT(item);
			list.setCompoundTag(dataKey[i], item);
		}
		nbttagcompound.setCompoundTag(DATA, list);
		
		nbttagcompound.setInteger(DIMENSION, dimension);
	}
	
	@Override
	public boolean isDirty() {
		boolean r = false;
		for (int i = 0; i < data.length; ++i) {
			r = r || data[i].isDirty();
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
		WorldServer[] worlds = MinecraftServer.getServer().worldServers;
		NBTTagCompound worldNBT;
		for (WorldServer world : worlds) {
			worldNBT = new NBTTagCompound();
			WorldSavedDataSC.forWorld(world).writeToNBT(worldNBT);
			PacketDispatcher.sendPacketToPlayer(
					PacketSendWorldData.createPacket(PacketSendWorldData.WORLDDATA, world.provider.dimensionId, worldNBT),
					(Player) player);
		}
	}
	
	public void onDataChanged(int id, int method, int dataId, NBTTagCompound data) {
		NBTTagCompound packetNBT = new NBTTagCompound();
		packetNBT.setInteger(PACKETDATAID, id);
		packetNBT.setIntArray(PACKETHEAD, new int[]{method, dataId});
		packetNBT.setTag(PACKETDATA, data);
		PacketDispatcher.sendPacketToAllPlayers(
				PacketSendWorldData.createPacket(PacketSendWorldData.CHANGEDATA, dimension, packetNBT));
	}
	
	//client storage
	@SideOnly(Side.CLIENT)
	private static HashMap<Integer, WorldSavedDataSC> worldDataMap = new HashMap();
	
	public static void onReceivedWorldData(int dimension, NBTTagCompound data) {
		if (worldDataMap.containsKey(dimension)) {
			worldDataMap.remove(dimension);
		}
		WorldSavedDataSC worldData = new WorldSavedDataSC();
		worldData.readFromNBT(data);
		worldData.dimension = dimension;
		worldDataMap.put(dimension, worldData);
	}
	
	public static void onReceivedData(int dimension, NBTTagCompound data) {
		int id = data.getInteger(PACKETDATAID);
		int[] head = data.getIntArray(PACKETHEAD);
		NBTTagCompound nbt = data.getCompoundTag(PACKETDATA);
		worldDataMap.get(dimension).data[id].onPacketRecieved(head[0], head[1], nbt);
	}
}
