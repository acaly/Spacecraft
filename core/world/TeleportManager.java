package spacecraft.core.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import spacecraft.core.utility.LanguageManager;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class TeleportManager {
	public static final TeleportManager INSTANCE = new TeleportManager(); 
	public static final String MSG = "sc.teleporter.notallowed";
	
	//teleporter info type
	public static final int NONE = 0;
	public static final int TELEPORT = 1;
	public static final int MONITOR = 2;
	
	static {
		INSTANCE.teleporterTypes.put(TELEPORT, new ITeleporterType(){
			public boolean available(World worldFrom, EntityPlayerMP player, int type, int x, int y, int z) {
				return true;
			}
			@Override
			public boolean needCheck() {
				return false;
			}
		});
		INSTANCE.teleporterTypes.put(MONITOR, new ITeleporterType(){
			public boolean available(World worldFrom, EntityPlayerMP player, int type, int x, int y, int z) {
				return true;
			}
			@Override
			public boolean needCheck() {
				return true;
			}
		});
	}
	
	public static void teleport(EntityPlayerMP player, World worldFrom, int x, int y, int z) {
		//WorldSavedDataSC data = WorldSavedDataSC.forWorld(worldFrom);
		//WorldLinkInfo link = (WorldLinkInfo) data.getData("link");
		WorldLinkInfo link = WorldLinkInfo.forWorld(worldFrom);
		TeleporterInfo teleporter = link.getTeleporter(x, y, z);
		if (teleporter == null) return;
		if (teleporter.getType().available(worldFrom, player, teleporter.type, x, y, z)) {
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
			NetworkHelper.sendPlayerMessage(player, LanguageManager.translate(MSG));
		}
	}
	
	public Map<Integer, ITeleporterType> teleporterTypes = new HashMap();
	
}
