package spacecraft.core.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class TeleporterInfo {
	public int type;
	public int dimension;	//target
	public int x, y, z;		//target
	public String owner;
	
	public void placeEntity(Entity entity, World world) {
		int x = this.x, y = this.y, z = this.z;
		if (world.getBlockId(x, y, z) != 0) {
			if (world.getBlockId(x + 1, y, z) == 0) {
				++x;
			} else if (world.getBlockId(x - 1, y, z) == 0) {
				--x;
			} else if (world.getBlockId(x, y, z + 1) == 0) {
				++z;
			} else if (world.getBlockId(x, y, z - 1) == 0) {
				--z;
			}
		}
		if (entity instanceof EntityPlayerMP) {
			//((EntityPlayerMP) entity).setPositionAndUpdate((double) x, (double) y, (double) z);
			EntityPlayerMP playerMP = (EntityPlayerMP) entity;

            EnderTeleportEvent event = new EnderTeleportEvent(playerMP, (double) x, (double) y, (double) z, 5);
            if (!MinecraftForge.EVENT_BUS.post(event)){
            	playerMP.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
            	playerMP.fallDistance = 0.0F;
            	playerMP.attackEntityFrom(DamageSource.fall, event.attackDamage);
            }
		} else {
			entity.setLocationAndAngles((double) x, (double) y, (double) z, entity.rotationYaw, 0.0f);
		}
	}
	
	public static TeleporterInfo readFromNBT(NBTTagCompound nbttagcompound) {
		TeleporterInfo r = new TeleporterInfo();
		int[] data = nbttagcompound.getIntArray("data");
		r.type = data[0];
		r.dimension = data[1];
		r.x = data[2];
		r.y = data[3];
		r.z = data[4];
		r.owner = nbttagcompound.getString("owner");
		return r;
	}

	public NBTTagCompound writeToNBT() {
		NBTTagCompound r = new NBTTagCompound();
		r.setIntArray("data", new int[]{
				type, dimension, x, y, z 
				});
		r.setString("owner", owner);
		return r;
	}
}
