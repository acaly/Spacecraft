package spacecraft.core.utility;

import net.minecraft.nbt.NBTTagCompound;

public interface ISavedData {
	void readFromNBT(NBTTagCompound nbttagcompound);
	void writeToNBT(NBTTagCompound nbttagcompound);
	boolean isDirty();
	
	void onPacketRecieved(int method, int id, NBTTagCompound nbt);
}
