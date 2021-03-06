package spacecraft.core.block.tile;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.WorldLinkInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortalSC extends TileEntity {
	private static final int DECREASE = 15;
	private static final int INCREASE = 20;
	private static final int MAXVALUE = 300;
	
	private HashMap<EntityPlayerMP, Integer> time = new HashMap();
	
	private static final String COUNTLEFT = "countleft";
	private static final String TIMELEFT = "timeleft";
	
	public int countLeft;
	public long timeLeft = -1;
	
	public void setPlayer(EntityPlayerMP player) {
		int last = time.containsKey(player) ? time.get(player) : 0;
		last += INCREASE;
		if (last > MAXVALUE) {
			TeleportManager.teleport(player, worldObj, xCoord, yCoord, zCoord);
			--countLeft;
			time.remove(player);
		} else {
			time.put(player, last);
		}
	}
	
	public void updateEntity() {
		if (worldObj.isRemote) return;
		Iterator<Entry<EntityPlayerMP, Integer>> i = time.entrySet().iterator();
		int value;
		while (i.hasNext()) {
			Entry<EntityPlayerMP, Integer> e = i.next();
			value = e.getValue() - DECREASE;
			if (value > 0) {
				e.setValue(value);
			} else {
				i.remove();
			}
		}
		
		if (timeLeft < 0) return;
		--timeLeft;
		if (timeLeft == 0 || countLeft <= 0) {
			WorldLinkInfo.removeFromWorld(worldObj, xCoord, yCoord, zCoord, false);
		}
	}
	
	public void setTimeLeft(long timeLeft) {
		this.timeLeft = timeLeft;
	}
	
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		countLeft = par1NBTTagCompound.getInteger(COUNTLEFT);
		timeLeft = par1NBTTagCompound.getLong(TIMELEFT);
	}
	
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger(COUNTLEFT, countLeft);
		par1NBTTagCompound.setLong(TIMELEFT, timeLeft - System.currentTimeMillis());
	}
}
