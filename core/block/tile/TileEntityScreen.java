package spacecraft.core.block.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class TileEntityScreen extends TileEntity {

	public void updateEntity() {
		if (worldObj.isRemote) {
			worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
		}
	}
	
	public TileEntity getTileEntityInfo() {
		return null;
	}
	
	public IBlockAccess getBlockInfo() {
		return null;
	}
	
	public Block getBlock() {
		return null;
	}
}
