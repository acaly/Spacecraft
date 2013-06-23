package spacecraft.core.block.tile;

import ic2.api.Direction;
import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.gui.TileEntityInventory;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.world.TeleporterInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporter extends TileEntityInventory {
	public static final String INVENTORY = "container.teleporter";
	public static final String EMIT = "emit";
	public int emit = 0;
	
	public TileEntityTeleporter() {
		super(INVENTORY, 1);
		setWrenchEnabled(true);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		if (par1NBTTagCompound.hasKey(EMIT)) {
			emit = par1NBTTagCompound.getInteger(EMIT);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger(EMIT, emit);
	}
	
	public void setEmit(int value) {
		if (worldObj.isRemote) {
			emit = value;
			return;
		}
		if (emit != value) {
			emit = value;
			if (emit > 0) {
				TeleporterInfo info = null;
				if (this.getStackInSlot(0) != null) {
					info = ItemLocator.getTeleporterInfo(this.getStackInSlot(0));
					if (info != null) {
						//TODO consider facing
						BlockPortalSC.setPortalBlock(worldObj, xCoord, yCoord + 1, zCoord, info);
						setWrenchEnabled(false);
						return;
					}
				}
				emit = 0;
			} else {
				BlockPortalSC.removePortalBlock(worldObj, xCoord, yCoord + 1, zCoord, false);
				setWrenchEnabled(true);
			}
		}
	}
}
