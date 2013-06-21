package spacecraft.core.block.tile;

import spacecraft.core.gui.TileEntityInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporter extends TileEntityInventory {
	public static final String INVENTORY = "container.teleporter";

	public TileEntityTeleporter() {
		super(INVENTORY, 1);
	}

}
