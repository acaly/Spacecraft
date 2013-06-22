package spacecraft.core.block.gui;

import org.lwjgl.opengl.GL11;

import spacecraft.core.gui.GuiButtonBase;
import spacecraft.core.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class GuiTeleporter extends GuiContainerBase {
	private static final String BACKGROUND = "Teleporter";

	public GuiTeleporter(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerTeleporter(world, player, x, y, z));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		this.mc.renderEngine.bindTexture(makeTexturePath(BACKGROUND));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonBase(0, guiLeft, guiTop, 20, 20, "X"));
	}
}
