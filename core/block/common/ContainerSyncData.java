package spacecraft.core.block.common;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.world.World;

public abstract class ContainerSyncData extends ContainerBase {

	public ContainerSyncData(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
	}
	
	int syncDirty[] = new int[TileEntityInventory.SNYCDATALEN];

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		SyncDataTileEntity data;
		for (int i = 0; i < TileEntityInventory.SNYCDATALEN; ++i) {
			data = tileEntity.syncData[i];
			if (data == null) continue;
			if (data.isDirty(syncDirty[i])) {
				syncDirty[i] = data.getDirty();
				Packet132TileEntityData packet = tileEntity.getDataPacket(i);
				for (Object player : this.crafters) {
					PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
				}
			}
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		for (int i = 0; i < TileEntityInventory.SNYCDATALEN; ++i) {
			if (tileEntity.syncData[i] == null) continue;
			Packet132TileEntityData packet = tileEntity.getDataPacket(i);
			PacketDispatcher.sendPacketToPlayer(packet, (Player) par1ICrafting);
		}
	}
}
