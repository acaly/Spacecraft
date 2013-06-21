package spacecraft.core.world;

import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class TeleportManager {
	public static void teleport(EntityPlayerMP player, World worldFrom, int x, int y, int z) {
		WorldSavedDataSC data = WorldSavedDataSC.forWorld(worldFrom);
		WorldLinkInfo link = (WorldLinkInfo) data.getData("link");
		//TODO test if the player is allowed to use this portal
	}
}
