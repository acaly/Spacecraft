package spacecraft.core.block.tile;

import spacecraft.core.gui.TileEntityInventory;

public class TileEntityMonitor extends TileEntityInventory {
	public static final String INVENTORY = "container.monitor";
	public static final int EMITID = 0;
	public int emit;

	public TileEntityMonitor() {
		super(INVENTORY, 1, 1);
	}

	@Override
	protected void onVarChanged(int id, int value) {
		//TODO change status
	}
}
