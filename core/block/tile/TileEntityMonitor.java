package spacecraft.core.block.tile;

import spacecraft.core.gui.TileEntityInventory;

public class TileEntityMonitor extends TileEntityInventory {
	public static final String INVENTORY = "container.monitor";
	public int emit;

	public TileEntityMonitor() {
		super(INVENTORY, 1);
	}

	public void setEmit(int value) {
		
	}
}
