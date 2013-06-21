package spacecraft.core.world;

import net.minecraft.nbt.NBTTagCompound;

public class WorldSeparation {
	public int type;
	
	public int xSize;
	public int zSize;
	
	public int xPos;
	public int zPos;
	
	public String owner;
	
	private static final String DATA = "data";
	private static final String OWNER = "owner";
	
	public int makeId() {
		//TODO fix this method
		return xPos;
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound r = new NBTTagCompound();
		r.setIntArray(DATA, new int[] {
				type, xSize, zSize, xPos, zPos
		});
		r.setString(OWNER, owner);
		return r;
	}

	public static WorldSeparation readFromNBT(NBTTagCompound compound) {
		int data[] = compound.getIntArray(DATA);
		WorldSeparation r = new WorldSeparation();
		r.type = data[0];
		r.xSize = data[1];
		r.zSize = data[2];
		r.xPos = data[3];
		r.zPos = data[4];
		r.owner = compound.getString(OWNER);
		return r;
	}
}
