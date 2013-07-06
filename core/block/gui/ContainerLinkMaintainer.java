package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import spacecraft.core.block.common.ContainerBase;
import spacecraft.core.block.tile.TileEntityLinkMaintainer;

public class ContainerLinkMaintainer extends ContainerBase {

	public ContainerLinkMaintainer(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
		
		this.addPlayerSlots(player);
		this.addSlotToContainer(new Slot(tileEntity, 0, 80, 57));
	}

	@Override
	public void onGuiEvent(int param) {
		if (param == GuiLinkMaintainer.BUTTONEMIT) {
			((TileEntityLinkMaintainer) tileEntity).switchStatus();
		}
	}

}
