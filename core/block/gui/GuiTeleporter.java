package spacecraft.core.block.gui;

import org.lwjgl.opengl.GL11;

import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.gui.GuiButtonBase;
import spacecraft.core.gui.GuiContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GuiTeleporter extends GuiContainerBase {
	private static final String BACKGROUND = "Teleporter";
	public static final int BUTTONEMIT = 0;

	public GuiTeleporter(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerTeleporter(world, player, x, y, z), world, player, x, y, z);
		setBackground(BACKGROUND);
		handleButtonEvent();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonBase(BUTTONEMIT, guiLeft + 143, guiTop + 32, 20, 20, "X").setIcon(Item.book));
	}
}
