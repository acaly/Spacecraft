package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import spacecraft.core.block.tile.TileEntityMonitor;
import spacecraft.core.gui.ContainerBase;
import spacecraft.core.gui.GuiButtonBase;
import spacecraft.core.gui.GuiContainerBase;

public class GuiMonitor extends GuiContainerBase {
	private static final String BACKGROUND = "Monitor";
	public static final int BUTTONEMIT = 0;
	private TileEntityMonitor tileEntity;

	public GuiMonitor(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerMonitor(world, player, x, y, z), world, player, x, y, z);
		setBackground(BACKGROUND);
		handleButtonEvent();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonBase(BUTTONEMIT, guiLeft + 143, guiTop + 32, 20, 20, "X").setIcon(Item.redstone));
	}
}