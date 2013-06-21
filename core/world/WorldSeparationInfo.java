package spacecraft.core.world;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import spacecraft.core.utility.ISavedData;

public class WorldSeparationInfo implements ISavedData {
	private int nextXPos = 0;
	private ArrayList<WorldSeparation> sepList = new ArrayList<WorldSeparation>();
	private boolean dirty;
	
	//TODO for this version, size will be ignored
	public WorldSeparation append(int type, int xSize, int zSize, String owner) {
		WorldSeparation w = new WorldSeparation();
		w.type = type;
		w.xSize = xSize;
		w.zSize = zSize;
		w.xPos = nextXPos;
		nextXPos += 1; //TODO fix the size
		w.zPos = 0;
		w.owner = owner;
		sepList.add(w);
		dirty = true;
		return w;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		sepList.clear();
		int count = nbttagcompound.getInteger("count");
		for (int i = 0; i < count; ++i) {
			sepList.add(WorldSeparation.readFromNBT(nbttagcompound.getCompoundTag(String.valueOf(i))));
		}
		nextXPos = nbttagcompound.getInteger("nextXPos");
		dirty = false;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		int count = sepList.size();
		nbttagcompound.setInteger("count", count);
		for (int i = 0; i < count; ++i) {
			nbttagcompound.setCompoundTag(String.valueOf(i), sepList.get(i).writeToNBT());
		}
		nbttagcompound.setInteger("nextXPos", nextXPos);
		dirty = false;
	}

	@Override
	public final boolean isDirty() {
		return dirty;
	}

}
