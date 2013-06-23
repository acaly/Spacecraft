package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import spacecraft.core.block.tile.TileEntityMonitor;
import spacecraft.core.gui.ContainerBase;
import spacecraft.core.utility.PacketContainerEvent;

public class ContainerMonitor extends ContainerBase<TileEntityMonitor> {
	private static final int EMITID = 0;

	public ContainerMonitor(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
		
		this.addPlayerSlots(player);
		this.addSlotToContainer(new Slot(tileEntity, 0, 80, 57));
		
		initProgress(1);
	}

	@Override
	public void onGuiEvent(int param) {
		if (param == GuiMonitor.BUTTONEMIT) {
			tileEntity.setEmit(1 - tileEntity.emit);
		}
	}

	@Override
	protected void refreshProgress() {
		progress[EMITID] = tileEntity.emit;
	}

	@Override
	public void updateProgressBar(int id, int value) {
		if (id == EMITID) {
			tileEntity.setEmit(value);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
