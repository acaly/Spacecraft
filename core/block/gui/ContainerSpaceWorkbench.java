package spacecraft.core.block.gui;

import spacecraft.core.block.common.ContainerBase;
import spacecraft.core.block.common.SlotCraftingSingle;
import spacecraft.core.utility.SpaceWorkbenchRecipe;
import spacecraft.core.world.SpaceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpaceWorkbench extends ContainerBase {
	public static final String INVENTORY = "container.spaceworkbench";
	private static final int EVENTPICKUPRESULT = 1;
	
	private InventoryBasic inventory = new InventoryBasic(INVENTORY, false, 1);
	private InventoryCrafting material = new InventoryCrafting(this, 1, 3);
	
	public SpaceWorkbenchRecipe.Recipe currentRecipe;
	
	public ContainerSpaceWorkbench(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
		
		this.addPlayerSlots(player);
		this.addSlotToContainer(new Slot(material, 0, 31, 41));
		this.addSlotToContainer(new Slot(material, 1, 56, 41));
		this.addSlotToContainer(new Slot(material, 2, 91, 23));
		this.addSlotToContainer(new SlotCraftingSingle(this, player, material, inventory, 0, 128, 41));
	}
	
	private void updateResult() {
		if (!SpaceManager.onCraftingOnSpaceWorkbench(player.username, material)) {
			return;
		}
		currentRecipe = SpaceWorkbenchRecipe.getResult(material);
		ItemStack result = currentRecipe != null ? currentRecipe.getResult(material) : null;
		inventory.setInventorySlotContents(0, result);
		this.getSlot(36 + 3).putStack(result);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		updateResult();
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		if (!this.world.isRemote) {
			ItemStack item = material.getStackInSlot(0);
			if (item != null) {
				par1EntityPlayer.dropPlayerItem(item);
			}
			item = material.getStackInSlot(1);
			if (item != null) {
				par1EntityPlayer.dropPlayerItem(item);
			}
			item = material.getStackInSlot(2);
			if (item != null) {
				par1EntityPlayer.dropPlayerItem(item);
			}
		}
	}

	@Override
	public void onGuiEvent(int param) {}
}
