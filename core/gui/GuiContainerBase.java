package spacecraft.core.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public abstract class GuiContainerBase extends GuiContainer {
	private static final String PATH_PREFIX = "/mods/spacecraft/textures/gui/";
	private static final String PATH_SUFFIX = ".png";
	private boolean preventExchange = false;
	private boolean handleButtonEvent = false;
	

	public GuiContainerBase(ContainerBase par1Container) {
		super(par1Container);
	}

	//TODO useless?
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		//GuiButtonBase i;
		//for (Object button : buttonList) {
		//	i = (GuiButtonBase) button;
		//	if (i.item != null)
		//		drawItem(i.item, i.iconLeft - guiLeft, i.iconTop - guiTop);
		//}
	}
	
	@Override
	protected boolean checkHotbarKeys(int par1) {
		if (preventExchange) {
			return false;
		} else {
			return super.checkHotbarKeys(par1);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (!handleButtonEvent) return;
		((ContainerBase) inventorySlots).onGuiEvent(par1GuiButton.id);
	}

	private void drawItem(ItemStack item, int x, int y) {
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, item, x, y);
	}
	
	protected void preventExchange() {
		preventExchange = true;
	}
	
	protected void handleButtonEvent() {
		handleButtonEvent = true;
	}
	
	protected String makeTexturePath(String name) {
		return PATH_PREFIX + name + PATH_SUFFIX;
	}
	
}
