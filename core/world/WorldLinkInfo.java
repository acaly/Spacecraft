package spacecraft.core.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import spacecraft.core.utility.ISavedData;

public class WorldLinkInfo implements ISavedData {
	private Map<List, TeleporterInfo> teleporterMap = new HashMap();
	private boolean dirty = false;
	
	public TeleporterInfo getTeleporter(int x, int y, int z) {
		return teleporterMap.get(Arrays.asList(x, y, z));
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		teleporterMap.clear();
		NBTTagList map = nbttagcompound.getTagList("map");
		int count = map.tagCount();
		int[] keyList;
		NBTTagCompound item;
		for (int i = 0; i < count; ++i) {
			item = (NBTTagCompound) map.tagAt(i);
			keyList = item.getIntArray("key");
			teleporterMap.put(Arrays.asList(keyList[0], keyList[1], keyList[2]),
					TeleporterInfo.readFromNBT(item.getCompoundTag("value")));
		}
		dirty = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		NBTTagList map = new NBTTagList();
		Integer[] keyList;
		NBTTagCompound item;
		for (Entry<List, TeleporterInfo> i : teleporterMap.entrySet()) {
			item = new NBTTagCompound();
			keyList = (Integer[]) i.getKey().toArray();
			item.setIntArray("key", new int[]{keyList[0], keyList[1], keyList[2]});
			item.setCompoundTag("value", i.getValue().writeToNBT());
		}
		nbttagcompound.setTag("map", map);
		dirty = false;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	//x, y, z: cord from
	public void append(int x, int y, int z, TeleporterInfo info) {
		teleporterMap.put(Arrays.asList(x, y, z), info);
		dirty = true;
	}
	
	public void remove(int x, int y, int z) {
		teleporterMap.remove(Arrays.asList(x, y, z));
	}
}
