package spacecraft.core.block.gui;

import spacecraft.core.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class GuiTeleporter extends GuiContainerBase {

	public GuiTeleporter(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerTeleporter(world, player, x, y, z));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

	}
}
