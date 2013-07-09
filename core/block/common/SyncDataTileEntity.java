package spacecraft.core.block.common;

import net.minecraft.nbt.NBTTagCompound;
/**
 * used to get data from client. will not save in world file
 * @author wzw
 *
 */
public abstract class SyncDataTileEntity {
	public abstract void readFromNBT(NBTTagCompound nbt);
	public abstract void writeToNBT(NBTTagCompound nbt);
	
	public SyncDataTileEntity(int id) {
		this.id = id;
	}
	
	public final int id;
	
	private int dirty = 0;
	public int getDirty() {
		return dirty;
	}
	public boolean isDirty(int last) {
		return last != dirty;
	}
	
	private static final int DIRTYMAX = 100;
	public void setDirty() {
		if (++dirty > DIRTYMAX) dirty = 0;
	}
}
