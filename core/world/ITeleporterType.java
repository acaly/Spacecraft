package spacecraft.core.world;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public interface ITeleporterType {
	boolean available(World worldFrom, EntityPlayerMP player, int type, int x, int y, int z);
}
