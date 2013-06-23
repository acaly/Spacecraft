package spacecraft.core.utility;

import spacecraft.core.block.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int BLOCKTELEPORTER = 10;
	public static final int BLOCKMONITOR = 11;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case BLOCKTELEPORTER:
			return new ContainerTeleporter(world, player, x, y, z);
		case BLOCKMONITOR:
			return new ContainerMonitor(world, player, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case BLOCKTELEPORTER:
			return new GuiTeleporter(world, player, x, y, z);
		case BLOCKMONITOR:
			return new GuiMonitor(world, player, x, y, z);
		}
		return null;
	}

}
