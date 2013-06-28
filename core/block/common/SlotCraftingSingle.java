package spacecraft.core.block.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * used only by ContainerSpaceWorkbench. Craft only one item each time, and use one slot as material.
 * Will not call GameRegistry.onItemCrafted when crafted.
 * @author wzw
 *
 */
public class SlotCraftingSingle extends Slot {
	private ContainerBase eventHandler;
	private int eventId;

	public SlotCraftingSingle(IInventory par1iInventory, int par2, int par3, int par4, 
			ContainerBase eventHandler, int eventId) {
		super(par1iInventory, par2, par3, par4);
		this.eventHandler = eventHandler;
		this.eventId = eventId;
	}

	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}
	
	@Override
	protected void onCrafting(ItemStack par1ItemStack, int par2) {
		this.onCrafting(par1ItemStack);
	}
	
	@Override
	protected void onCrafting(ItemStack par1ItemStack) {
		par1ItemStack.onCrafting(eventHandler.world, eventHandler.player, 1);
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
		if (!eventHandler.world.isRemote) {
			eventHandler.onGuiEvent(eventId);
		}
	}
}
