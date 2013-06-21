package spacecraft.core.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class TeleportManager {
	public static TeleportManager INSTANCE = new TeleportManager(); 
	static {
		INSTANCE.teleporterTypes.put(0, new ITeleporterType(){
			public boolean available(World worldFrom, EntityPlayerMP player, int type, int x, int y, int z) {
				return true;
			}
		});
	}
	
	public static void teleport(EntityPlayerMP player, World worldFrom, int x, int y, int z) {
		WorldSavedDataSC data = WorldSavedDataSC.forWorld(worldFrom);
		WorldLinkInfo link = (WorldLinkInfo) data.getData("link");
		TeleporterInfo teleporter = link.getTeleporter(x, y, z);
		if (teleporter == null) return;
		if (INSTANCE.teleporterTypes.get(teleporter.type)
				.available(worldFrom, player, teleporter.type, x, y, z)) {
			if (teleporter.dimension == worldFrom.provider.dimensionId) {
				//teleport immediately
				teleporter.placeEntity(player, SpaceManager.getWorldForServer(teleporter.dimension));
			} else {
				//inter-dimension transfer
				player.mcServer.getConfigurationManager().transferPlayerToDimension(
						player, teleporter.dimension,
						new TeleporterSC(SpaceManager.getWorldForServer(teleporter.dimension), teleporter));
			}
		} else {
			NetworkHelper.sendPlayerMessage(player, "You're not allowed to use this teleporter!");
		}
	}
	
	private Map<Integer, ITeleporterType> teleporterTypes = new HashMap();
	
}
