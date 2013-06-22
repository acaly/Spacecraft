package spacecraft.core.gui;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GuiButtonBase extends GuiButton {
	String tooltip;
	ItemStack item;
	int iconLeft;
	int iconTop;

	public GuiButtonBase(int id, int x, int y, int w, int h, String text) {
		super(id, x, y, w, h, text);
		iconLeft = x + w / 2 - 8;
		iconTop = y + h / 2 - 8;
	}
	
	public GuiButtonBase setTooltip(String text) {
		this.tooltip = text;
		return this;
	}

	public GuiButtonBase setIcon(Item item) {
		this.item = new ItemStack(item);
		return this;
	}
	
}
