package spacecraft.core.world;

import java.util.Arrays;
import java.util.Map;

import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class TeleportManager {
	public static final TeleportManager INSTANCE; 
	static {
		INSTANCE = new TeleportManager();
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
			player.mcServer.getConfigurationManager().transferPlayerToDimension(
					player, teleporter.dimension,
					new TeleporterSC(player.mcServer.worldServerForDimension(teleporter.dimension), teleporter));
		} else {
			NetworkHelper.sendPlayerMessage(player, "You're not allowed to use this teleporter!");
		}
		/*
		WorldSavedDataSC data = WorldSavedDataSC.forWorld(worldFrom);
		WorldLinkInfo link = (WorldLinkInfo) data.getData("link");
		TeleporterInfo teleporter = link.getTeleporter(x, y, z);
		if (INSTANCE.teleporterTypes.get(teleporter.type)
				.available(worldFrom, player, teleporter.type, x, y, z)) {
			teleporter.placeEntity(player, worldTo);
		} else {
			NetworkHelper.sendPlayerMessage(player, "You're not allowed to use this teleporter!");
		}
		*/
	}
	
	private Map<Integer, ITeleporterType> teleporterTypes;
	
}
