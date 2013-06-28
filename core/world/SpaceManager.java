package spacecraft.core.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SpaceManager {
	public static WorldServer getSpecialWorldForServer() {
		return MinecraftServer.getServer().worldServerForDimension(RegistryHelper.getId(WorldProviderSC.class));
	}
	
	public static WorldServer getWorldForServer(int dimension) {
		return MinecraftServer.getServer().worldServerForDimension(dimension);
	}
	
	public static TeleporterInfo createDebugSpaceAndTeleporter() {
		World world = getSpecialWorldForServer();
		//WorldSeparationInfo info = ((WorldSeparationInfo)WorldSavedDataSC.forWorld(world)
		//		.getData(WorldSavedDataSC.DATASEPARATION));
		WorldSeparationInfo info = WorldSeparationInfo.forWorld(world);
		WorldSeparation sep = info.append(0, 1, 1, "anyone");
		TeleporterInfo tele = new TeleporterInfo();
		tele.dimension = world.provider.dimensionId;
		tele.owner = "anyone";
		tele.type = 0;
		tele.x = (sep.xPos << 4) + 2;
		tele.y = 2;
		tele.z = (sep.zPos << 4) + 2;
		return tele;
	}
	
	public static boolean onCreateNewSpace(String owner, int xSize, int zSize) {
		return xSize == 1 && zSize == 1;
	}
}
