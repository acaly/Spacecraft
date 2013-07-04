package spacecraft.core.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import spacecraft.core.block.BlockMonitor;
import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.block.tile.TileEntityScreen;
import spacecraft.core.render.LinkBlockInfo;
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
		return (WorldLinkInfo) WorldSavedDataSC.forWorld(world).data[WorldSavedDataSC.DATALINKINFO];
	}
	
	public static WorldLinkInfo forChunkCache(ChunkCache chunk) {
		return (WorldLinkInfo) WorldSavedDataSC.forChunkCache(chunk).data[WorldSavedDataSC.DATALINKINFO];
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
	
	//info.type must be set before calling this method
	public static void addToWorld(World world, int x, int y, int z, TeleporterInfo info, Class block) {
		WorldSavedDataSC worldData =  WorldSavedDataSC.forWorld(world);
		WorldLinkInfo linkInfo = WorldLinkInfo.forWorld(world);
		linkInfo.append(x, y, z, info);
		if (!world.isRemote) {
			//info.type = getTeleporterTypeFromBlock(block);
			NBTTagCompound data = new NBTTagCompound();
			data.setIntArray(COORD, new int[]{x, y, z});
			data.setTag(LINK, info.writeToNBT());
			worldData.onDataChanged(WorldSavedDataSC.DATALINKINFO, WorldSavedDataSC.METHODAPPEND,
					-1, data);
			
			world.setBlock(x, y, z, RegistryHelper.getId(block), 0, 3);
		}
	}
	
	public static void removeFromWorld(World world, int x, int y, int z, boolean blockRemoved) {
		if (!blockRemoved) {
			world.setBlockToAir(x, y, z);
		}
		WorldSavedDataSC worldData = WorldSavedDataSC.forWorld(world);
		WorldLinkInfo linkInfo = WorldLinkInfo.forWorld(world);
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
	
	//called by tileentity, only on server side
	public TileEntity getLinkTileEntity(TeleporterInfo info) {
		return null;
	}
	
	public LinkBlockInfo getBlock(TeleporterInfo info) {
		return null;
	}
	
	public void onWorldChunkWatched(ChunkWatchEvent.Watch event) {
		//find out all tileentities and tell screen to add player into the list
		Chunk chunk = event.player.worldObj
				.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
		for (Object tile : chunk.chunkTileEntityMap.values()) {
			if (tile instanceof TileEntityScreen) {
				((TileEntityScreen) tile).addPlayerToList(event.player);
			}
		}
	}
	
	public void onWorldChunkUnwatched(ChunkWatchEvent.UnWatch event) {
		Chunk chunk = event.player.worldObj
				.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
		for (Object tile : chunk.chunkTileEntityMap.values()) {
			if (tile instanceof TileEntityScreen) {
				((TileEntityScreen) tile).removePlayerFromList(event.player);
			}
		}
	}
	
	public static class ChunkEventHandler {
		@ForgeSubscribe
		public void onChunkWatched(ChunkWatchEvent.Watch event) {
			WorldLinkInfo.forWorld(event.player.worldObj).onWorldChunkWatched(event);
		}
		
		@ForgeSubscribe
		public void onChunkUnwatched(ChunkWatchEvent.UnWatch event) {
			WorldLinkInfo.forWorld(event.player.worldObj).onWorldChunkUnwatched(event);
		}
	}
}
