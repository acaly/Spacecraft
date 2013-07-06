package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import spacecraft.core.block.common.ContainerBase;
import spacecraft.core.block.common.GuiButtonBase;
import spacecraft.core.block.common.GuiContainerBase;
import spacecraft.core.block.tile.TileEntityLinkMaintainer;

public class GuiLinkMaintainer extends GuiContainerBase {
	private static final String BACKGROUND = "LinkMaintainer";
	public static final int BUTTONEMIT = 0;

	public GuiLinkMaintainer(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerLinkMaintainer(world, player, x, y, z), world, player, x, y, z);
		setBackground(BACKGROUND);
		handleButtonEvent();
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButtonBase(BUTTONEMIT, guiLeft + 143, guiTop + 32, 20, 20, "X").setIcon(Item.redstone));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		StringTranslate trans = StringTranslate.getInstance();
		String status = trans.translateKey(TileEntityLinkMaintainer.LANG_LINKSTATUS
				[((TileEntityLinkMaintainer)tileEntity).getVar(TileEntityLinkMaintainer.LINKSTATUS)]);
		fontRenderer.drawString(status, 65, 6, 4210752);
	}
}
