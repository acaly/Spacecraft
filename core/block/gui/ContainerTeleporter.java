package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.gui.ContainerBase;
import spacecraft.core.utility.PacketContainerEvent;

public class ContainerTeleporter extends ContainerBase {
	private TileEntityTeleporter tile;

	public ContainerTeleporter(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player);
		this.tile = (TileEntityTeleporter) world.getBlockTileEntity(x, y, z);
		
		this.addPlayerSlots(player);
		//this.addSlotToContainer(new Slot(tile, 0, 80, 57));
	}

	@Override
	public void onServerNetworkEvent(PacketContainerEvent packet) {}

	@Override
	public void onClientNetworkEvent(PacketContainerEvent packet) {}

	@Override
	public void onGuiEvent(int param) {
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
