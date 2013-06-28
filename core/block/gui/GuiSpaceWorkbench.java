package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import spacecraft.core.block.common.ContainerBase;
import spacecraft.core.block.common.GuiContainerBase;

public class GuiSpaceWorkbench extends GuiContainerBase {
	private static final String BACKGROUND = "SpaceWorkbench";
	
	public GuiSpaceWorkbench(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerSpaceWorkbench(world, player, x, y, z), world, player, x, y, z);
		setBackground(BACKGROUND);
	}

}
