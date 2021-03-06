package spacecraft.core.block.tile;

import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;

import spacecraft.core.render.BlockAccessLink;
import spacecraft.core.render.LinkBlockInfo;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInstance;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityScreen extends TileEntity {
	private static final String DATA_ID = "id";
	private static final String DATA_META = "meta";
	private static final String DATA_TILE = "tile";
	private static final int ACTIONTYPE = 10;	//used in Packet132TileEntityData
	
	private static final int UPDATETICK = 5;
	private static final int TILESYNCTICK = 100;
	
	private TileEntity tileEntityAim;
	private LinkBlockInfo blockInfoAim;
	
	//Client side only
	@SideOnly(Side.CLIENT)
	private BlockAccessLink blockAccessAim = new BlockAccessLink();
	
	//Server side only
	private Collection<EntityPlayerMP> playerList = new ArrayDeque();
	
	private int ticksCheck, ticksTileCheck;
	public void updateEntity() {
		
		if (worldObj.isRemote) {
			//client side, update renderer
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
			
			if (blockAccessAim.tele == null) {
				blockAccessAim.setCoord(xCoord, yCoord, zCoord);
				blockAccessAim.tele = WorldLinkInfo.forWorld(worldObj).getTeleporter(xCoord, yCoord, zCoord);
			}
		} else {
			//server side, check if data needs to update
			if (blockInfoAim == null) {
				//initialize
				blockInfoAim = makeData();
				createPlayerList();
				sendDataToPlayers();
			} else {
				//check update
				if (++ticksCheck >= UPDATETICK) {
					ticksCheck = 0;
					
					LinkBlockInfo newInfo = makeData();
					if (!newInfo.equals(blockInfoAim)) {
						blockInfoAim = newInfo;
						sendDataToPlayers();
					}
				}
				if (++ticksTileCheck >= TILESYNCTICK) {
					ticksTileCheck = 0;
					
					sendTileEntityToPlayers();
				}
			}
		}
	}
	
	//server only
	private void createPlayerList() {
		PlayerManager manager = MinecraftServer.getServer()
				.worldServerForDimension(worldObj.provider.dimensionId).getPlayerManager();
		PlayerInstance players = manager.getOrCreateChunkWatcher(xCoord>>4, zCoord>>4, false);
		if (players != null) {
			Method m;
			List list;
			try {
				m = PlayerInstance.class.getDeclaredMethod("getPlayersInChunk", PlayerInstance.class);
				m.setAccessible(true);
				list = (List) m.invoke(null, players);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			for (Object i : list) {
				this.playerList.add((EntityPlayerMP) i);
			}
		}
	}
	
	private void sendTileEntityToPlayer(EntityPlayerMP player) {
		TeleporterInfo info = WorldLinkInfo.forWorld(worldObj).getTeleporter(xCoord, yCoord, zCoord);
		if (info == null) return;
		World worldAim = MinecraftServer.getServer().worldServerForDimension(info.dimension);
		if (!worldAim.blockExists(info.x, info.y, info.z) ||
				!worldAim.blockHasTileEntity(info.x, info.y, info.z)) return;
		TileEntity tile = worldAim.getBlockTileEntity(info.x, info.y, info.z);
		
		NBTTagCompound data = new NBTTagCompound();
		NBTTagCompound tileNBT = new NBTTagCompound();
		tile.writeToNBT(tileNBT);
		data.setCompoundTag(DATA_TILE, tileNBT);
		Packet132TileEntityData packet = new Packet132TileEntityData(
				xCoord, yCoord, zCoord, ACTIONTYPE, data);
		PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
	}
	
	private LinkBlockInfo makeData() {
		TeleporterInfo info = WorldLinkInfo.forWorld(worldObj).getTeleporter(xCoord, yCoord, zCoord);
		if (info == null) return null;
		
		World worldAim = MinecraftServer.getServer().worldServerForDimension(info.dimension);
		if (!worldAim.blockExists(info.x + 1, 0, info.z) || !worldAim.blockExists(info.x - 1, 0, info.z) ||
				!worldAim.blockExists(info.x, 0, info.z + 1) || !worldAim.blockExists(info.x, 0, info.z - 1)) {
			return null;
		}
		
		LinkBlockInfo result = new LinkBlockInfo();
		result.id = new int[7];
		result.metadata = new int[7];
		int x = info.x, y = info.y, z = info.z;
		result.id[0] = worldAim.getBlockId(x, y, z);
		result.metadata[0] = worldAim.getBlockMetadata(x, y, z);
		//+-x
		x = info.x + 1;
		result.id[1] = worldAim.getBlockId(x, y, z);
		result.metadata[1] = worldAim.getBlockMetadata(x, y, z);
		x = info.x - 1;
		result.id[2] = worldAim.getBlockId(x, y, z);
		result.metadata[2] = worldAim.getBlockMetadata(x, y, z);
		x = info.x;
		//+-y
		y = info.y + 1;
		result.id[3] = worldAim.getBlockId(x, y, z);
		result.metadata[3] = worldAim.getBlockMetadata(x, y, z);
		y = info.y - 1;
		result.id[4] = worldAim.getBlockId(x, y, z);
		result.metadata[4] = worldAim.getBlockMetadata(x, y, z);
		y = info.y;
		//+-z
		z = info.z + 1;
		result.id[5] = worldAim.getBlockId(x, y, z);
		result.metadata[5] = worldAim.getBlockMetadata(x, y, z);
		z = info.z - 1;
		result.id[6] = worldAim.getBlockId(x, y, z);
		result.metadata[6] = worldAim.getBlockMetadata(x, y, z);
		z = info.z;
		
		return result;
	}
	
	private void sendDataToPlayers() {
		NBTTagCompound data = new NBTTagCompound();
		data.setIntArray(DATA_ID, blockInfoAim.id);
		data.setIntArray(DATA_META, blockInfoAim.metadata);
		Packet132TileEntityData packet = new Packet132TileEntityData(
				xCoord, yCoord, zCoord, ACTIONTYPE, data);
		for (EntityPlayerMP player : playerList) {
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
		}
	}
	
	private void sendTileEntityToPlayers() {
		TeleporterInfo info = WorldLinkInfo.forWorld(worldObj).getTeleporter(xCoord, yCoord, zCoord);
		if (info == null) return;
		World worldAim = MinecraftServer.getServer().worldServerForDimension(info.dimension);
		if (!worldAim.blockExists(info.x, info.y, info.z) ||
				!worldAim.blockHasTileEntity(info.x, info.y, info.z)) return;
		TileEntity tile = worldAim.getBlockTileEntity(info.x, info.y, info.z);
		
		NBTTagCompound data = new NBTTagCompound();
		NBTTagCompound tileNBT = new NBTTagCompound();
		tile.writeToNBT(tileNBT);
		data.setCompoundTag(DATA_TILE, tileNBT);
		Packet132TileEntityData packet = new Packet132TileEntityData(
				xCoord, yCoord, zCoord, ACTIONTYPE, data);
		for (EntityPlayerMP player : playerList) {
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
		}
	}
	
	private void sendDataToPlayer(EntityPlayerMP player) {
		NBTTagCompound data = new NBTTagCompound();
		data.setIntArray(DATA_ID, blockInfoAim.id);
		data.setIntArray(DATA_META, blockInfoAim.metadata);
		Packet132TileEntityData packet = new Packet132TileEntityData(
				xCoord, yCoord, zCoord, ACTIONTYPE, data);
		PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
	}
	
	//called by WorldLinkInfo
	public void addPlayerToList(EntityPlayerMP player) {
		playerList.add(player);
		sendDataToPlayer(player);
		sendTileEntityToPlayer(player);
	}
	
	public void removePlayerFromList(EntityPlayerMP player) {
		playerList.remove(player);
	}
	
	//data received
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		if (pkt.actionType != ACTIONTYPE) return;
		if (blockInfoAim == null) blockInfoAim = new LinkBlockInfo();
		if (pkt.customParam1.hasKey(DATA_ID)) {
			blockInfoAim.id = pkt.customParam1.getIntArray(DATA_ID);
			blockInfoAim.metadata = pkt.customParam1.getIntArray(DATA_META);
			blockAccessAim.setBlockInfo(blockInfoAim);
		} else {
			if (tileEntityAim == null) {
				Block block = Block.blocksList[blockInfoAim.id[0]];
				if (block.hasTileEntity(blockInfoAim.metadata[0])) {
					tileEntityAim = block.createTileEntity(worldObj, blockInfoAim.metadata[0]);
					tileEntityAim.yCoord = -2;
				} else {
					return;
				}
				blockInfoAim.tileEntity = tileEntityAim;
			}
			tileEntityAim.readFromNBT(pkt.customParam1.getCompoundTag(DATA_TILE));
			tileEntityAim.yCoord = -2;
		}
	}
	
	//called by renderer
	@SideOnly(Side.CLIENT)
	public TileEntity getTileEntityInfo() {
		if (blockInfoAim == null || blockInfoAim.id[0] == 0) return null;
		TeleporterInfo info = WorldLinkInfo.forWorld(worldObj).getTeleporter(xCoord, yCoord, zCoord);
		if (info != null && info.dimension == worldObj.provider.dimensionId &&
				worldObj.getChunkFromBlockCoords(info.x, info.z).isChunkLoaded) {
			return worldObj.getBlockTileEntity(info.x, info.y, info.z);
		} else {
			Block block = Block.blocksList[blockInfoAim.id[0]];
			if (!block.hasTileEntity(blockInfoAim.metadata[0])) {
				return null;
			} else {
				if (tileEntityAim == null) {
					tileEntityAim = block.createTileEntity(worldObj, blockInfoAim.metadata[0]);
				}
				return tileEntityAim;
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public IBlockAccess getBlockInfo() {
		return blockAccessAim;
	}
	
	@SideOnly(Side.CLIENT)
	public Block getBlock() {
		if (blockInfoAim == null || blockInfoAim.id[0] == 0) return null;
		return Block.blocksList[blockInfoAim.id[0]];
	}
}
