package spacecraft.core.block.common;

import spacecraft.core.block.gui.ContainerSpaceWorkbench;
import spacecraft.core.item.ItemLicense;
import spacecraft.core.utility.RegistryHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotCraftingSingle extends SlotCrafting {
	private final IInventory craftMatrix;
	private EntityPlayer thePlayer;
	private ContainerSpaceWorkbench container;

	public SlotCraftingSingle(ContainerSpaceWorkbench container, EntityPlayer player, IInventory matrix,
			IInventory parent, int id, int x, int y) {
		super(player, matrix, parent, id, x, y);
		this.craftMatrix = matrix;
		this.thePlayer = player;
		this.container = container;
	}

	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		GameRegistry.onItemCrafted(par1EntityPlayer, par2ItemStack, craftMatrix);
		this.onCrafting(par2ItemStack);

		//first check license
		for (int i = this.craftMatrix.getSizeInventory() - 1; i >= 0; --i) {
			ItemStack itemstack1 = this.craftMatrix.getStackInSlot(i);
			if (itemstack1 == null) continue;
			
			if (itemstack1.itemID == RegistryHelper.getItemId(ItemLicense.class)) {
				if (container.currentRecipe != null && !container.currentRecipe.onLicenseUsed(itemstack1))
					craftMatrix.setInventorySlotContents(i, null);
				continue;
			}
			
			this.craftMatrix.decrStackSize(i, 1);
			if (!itemstack1.getItem().hasContainerItem()) continue;
			ItemStack itemstack2 = itemstack1.getItem().getContainerItemStack(itemstack1);
			if (itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
				MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
				itemstack2 = null;
			}
			if (itemstack2 != null && (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1)
					|| !this.thePlayer.inventory.addItemStackToInventory(itemstack2))) {
				if (this.craftMatrix.getStackInSlot(i) == null) {
					this.craftMatrix.setInventorySlotContents(i, itemstack2);
				} else {
					this.thePlayer.dropPlayerItem(itemstack2);
				}
			}
		}
	}
}
