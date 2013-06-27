package spacecraft.core.block.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import spacecraft.core.world.TeleportManager;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPortalSC extends TileEntity {
	private static final int DECREASE = 15;
	private static final int INCREASE = 20;
	private static final int MAXVALUE = 300;
	
	private HashMap<EntityPlayerMP, Integer> time = new HashMap();
	
	public void setPlayer(EntityPlayerMP player) {
		int last = time.containsKey(player) ? time.get(player) : 0;
		last += INCREASE;
		if (last > MAXVALUE) {
			TeleportManager.teleport(player, worldObj, xCoord, yCoord, zCoord);
			time.remove(player);
		} else {
			time.put(player, last);
		}
	}
	
	public void updateEntity() {
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
	}
}
