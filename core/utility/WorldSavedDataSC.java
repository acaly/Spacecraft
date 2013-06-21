package spacecraft.core.utility;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class WorldSavedDataSC extends WorldSavedData {
	public static String dataId = "spacecraft.worlddata";

	public String debugString = "";
	private Map<String, ISavedData> dataMap = new HashMap<String, ISavedData>();
	
	public WorldSavedDataSC() {
		super(dataId);
	}
	
	//used by MapStorage
	public WorldSavedDataSC(String fileName) {
		super(dataId);
	}
	
	public static WorldSavedDataSC forWorld(World world) {
		if (world == null) return null;
		MapStorage storage = world.perWorldStorage;
		WorldSavedDataSC result = (WorldSavedDataSC)storage.loadData(WorldSavedDataSC.class, dataId);
		if (result == null) {
			result = new WorldSavedDataSC();
			storage.setData(dataId, result);
		}
		return result;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		debugString = nbttagcompound.getString("debug");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setString("debug", debugString);
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
}
