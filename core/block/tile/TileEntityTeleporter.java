package spacecraft.core.block.tile;

import ic2.api.Direction;
import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.block.common.TileEntityInventory;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporter extends TileEntityInventory {
	public static final String INVENTORY = "container.teleporter";
	public static final String EMIT = "emit";
	//public int emit = 0;
	public static final int EMITID = 0;
	
	public TileEntityTeleporter() {
		super(INVENTORY, 1, 1);
		setWrenchEnabled(true);
	}
	
	@Override
	protected void onVarChanged(int id, int value) {
		if (id == EMITID) {
			if (value > 0) {
				TeleporterInfo info = null;
				if (this.getStackInSlot(0) == null) return;
				info = ItemLocator.getTeleporterInfo(this.getStackInSlot(0));
				if (info == null) return;
				WorldLinkInfo.addToWorld(worldObj, xCoord, yCoord + 1, zCoord, info, BlockPortalSC.class);
				setWrenchEnabled(false);
			} else {
				WorldLinkInfo.removeFromWorld(worldObj, xCoord, yCoord + 1, zCoord, false);
				setWrenchEnabled(true);
			}
		}
	}
	
}
