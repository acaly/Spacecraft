package spacecraft.core.block.tile;

import spacecraft.core.gui.TileEntityInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporter extends TileEntityInventory {
	public static final String INVENTORY = "container.teleporter";
	public static final String EMIT = "emit";
	public int emit = 0;
	
	public TileEntityTeleporter() {
		super(INVENTORY, 1);
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
}
