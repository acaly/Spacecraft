package spacecraft.core.block.tile;

import spacecraft.core.block.BlockScreen;
import spacecraft.core.block.common.TileEntityInventory;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;

public class TileEntityMonitor extends TileEntityInventory {
	public static final String INVENTORY = "container.monitor";
	public static final int EMITID = 0;
	public int emit;

	public TileEntityMonitor() {
		super(INVENTORY, 1, 1);
	}

	@Override
	protected void onVarChanged(int id, int value) {
		if (id == EMITID) {
			if (value > 0) {
				TeleporterInfo info = null;
				if (this.getStackInSlot(0) == null) return;
				info = ItemLocator.getTeleporterInfo(this.getStackInSlot(0));
				if (info == null) return;
				info.type = TeleportManager.MONITOR;
				WorldLinkInfo.addToWorld(worldObj, xCoord, yCoord + 1, zCoord, info, BlockScreen.class);
				setWrenchEnabled(false);
			} else {
				WorldLinkInfo.removeFromWorld(worldObj, xCoord, yCoord + 1, zCoord, false);
				setWrenchEnabled(true);
			}
		}
	}
}
