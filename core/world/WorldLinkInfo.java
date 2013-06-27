package spacecraft.core.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import spacecraft.core.block.BlockMonitor;
import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.utility.ISavedData;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;

public class WorldLinkInfo implements ISavedData {
	private Map<List, TeleporterInfo> teleporterMap = new HashMap();
	private boolean dirty = false;
	private static final String MAP = "map";
	private static final String KEY = "key";
	private static final String VALUE = "value";
	
	private static final String COORD = "coord";
	private static final String LINK = "link";
	
	public TeleporterInfo getTeleporter(int x, int y, int z) {
		return teleporterMap.get(Arrays.asList(x, y, z));
	}
	
	public static WorldLinkInfo forWorld(World world) {
		return (WorldLinkInfo) WorldSavedDataSC.forWorld(world).getData(WorldSavedDataSC.DATALINKINFO);
	}
	
	public static WorldLinkInfo forChunkCache(ChunkCache chunk) {
		return (WorldLinkInfo) WorldSavedDataSC.forChunkCache(chunk).getData(WorldSavedDataSC.DATALINKINFO);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		teleporterMap.clear();
		NBTTagList map = nbttagcompound.getTagList(MAP);
		int count = map.tagCount();
		int[] keyList;
		NBTTagCompound item;
		for (int i = 0; i < count; ++i) {
			item = (NBTTagCompound) map.tagAt(i);
			keyList = item.getIntArray(KEY);
			teleporterMap.put(Arrays.asList(keyList[0], keyList[1], keyList[2]),
					TeleporterInfo.readFromNBT(item.getCompoundTag(VALUE)));
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
			item.setIntArray(KEY, new int[]{keyList[0], keyList[1], keyList[2]});
			item.setCompoundTag(VALUE, i.getValue().writeToNBT());
			map.appendTag(item);
		}
		nbttagcompound.setTag(MAP, map);
		dirty = false;
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}

	//must call through addToWorld/removeFromWorld in order to send changes
	//except for this.onPacketRecieved on client
	//x, y, z: cord from
	private void append(int x, int y, int z, TeleporterInfo info) {
		teleporterMap.put(Arrays.asList(x, y, z), info);
		dirty = true;
	}
	
	private void remove(int x, int y, int z) {
		teleporterMap.remove(Arrays.asList(x, y, z));
	}
	
	private static int getTeleporterTypeFromBlock(Class block) {
		if (block.equals(BlockPortalSC.class)) {
			return TeleportManager.TELEPORT;
		} else if (block.equals(BlockMonitor.class)) {
			return TeleportManager.MONITOR;
		} else {
			return 0;
		}
	}
	
	public static void addToWorld(World world, int x, int y, int z, TeleporterInfo info, Class block) {
		WorldSavedDataSC worldData =  WorldSavedDataSC.forWorld(world);
		WorldLinkInfo linkInfo = (WorldLinkInfo) worldData.getData(WorldSavedDataSC.DATALINKINFO);
		linkInfo.append(x, y, z, info);
		if (!world.isRemote) {
			info.type = getTeleporterTypeFromBlock(block);
			world.setBlock(x, y, z, RegistryHelper.getId(block), 0, 3);
			
			NBTTagCompound data = new NBTTagCompound();
			data.setIntArray(COORD, new int[]{x, y, z});
			data.setTag(LINK, info.writeToNBT());
			worldData.onDataChanged(WorldSavedDataSC.DATALINKINFO, WorldSavedDataSC.METHODAPPEND,
					-1, data);
		}
	}
	
	public static void removeFromWorld(World world, int x, int y, int z, boolean blockRemoved) {
		if (!blockRemoved) {
			world.setBlockToAir(x, y, z);
		}
		WorldSavedDataSC worldData = WorldSavedDataSC.forWorld(world);
		WorldLinkInfo linkInfo = (WorldLinkInfo) worldData.getData(WorldSavedDataSC.DATALINKINFO);
		linkInfo.remove(x, y, z);
		if (!world.isRemote) {
			NBTTagCompound data = new NBTTagCompound();
			data.setIntArray(COORD, new int[]{x, y, z});
			worldData.onDataChanged(WorldSavedDataSC.DATALINKINFO, WorldSavedDataSC.METHODREMOVE,
					-1, data);
		}
	}

	@Override
	public void onPacketRecieved(int method, int id, NBTTagCompound nbt) {
		if (method == WorldSavedDataSC.METHODAPPEND) {
			int coord[] = nbt.getIntArray(COORD);
			append(coord[0], coord[1], coord[2], TeleporterInfo.readFromNBT(nbt.getCompoundTag(LINK)));
		} else {
			int coord[] = nbt.getIntArray(COORD);
			remove(coord[0], coord[1], coord[2]);
		}
	}
	
	public void onWorldChunkLoad(int x, int z) {
		int blockX, blockZ;
		for (Entry<List, TeleporterInfo> i : teleporterMap.entrySet()) {
			blockX = (Integer) i.getKey().get(0);
			blockZ = (Integer) i.getKey().get(2);
			if (blockX >> 4 == x && blockZ >> 4 == z) {
				
			}
		}
	}

	public void onWorldChunkUnload(int x, int z) {
		
	}
	
	public static class ChunkEventHandler {
		@ForgeSubscribe
		public void onChunkLoad(ChunkEvent.Load event) {
			if (event.world.isRemote) return;
			WorldLinkInfo.forWorld(event.world)
					.onWorldChunkLoad(event.getChunk().xPosition, event.getChunk().zPosition);
		}
		
		@ForgeSubscribe
		public void onChunkUnload(ChunkEvent.Unload event) {
			if (event.world.isRemote) return;
			WorldLinkInfo.forWorld(event.world)
					.onWorldChunkUnload(event.getChunk().xPosition, event.getChunk().zPosition);
		}
	}
}
