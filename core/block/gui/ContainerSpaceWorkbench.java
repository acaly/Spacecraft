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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSpaceWorkbench extends ContainerBase {
	public static final String INVENTORY = "container.spaceworkbench";
	private static final int EVENTPICKUPRESULT = 1;
	
	private InventoryBasic inventory = new InventoryBasic(INVENTORY, false, 1);
	private InventoryCrafting material = new InventoryCrafting(this, 1, 1);
	
	public ContainerSpaceWorkbench(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
		
		this.addPlayerSlots(player);
		this.addSlotToContainer(new Slot(material, 0, 44, 36));
		this.addSlotToContainer(new SlotCraftingSingle(inventory, 0, 116, 36, this, EVENTPICKUPRESULT));
	}
	
	private void updateResult() {
		ItemStack itemMat = material.getStackInSlot(0);
		if (!SpaceManager.onCraftingOnSpaceWorkbench(player.username, itemMat)) {
			return;
		}
		ItemStack result = SpaceWorkbenchRecipe.getResult(itemMat);
		inventory.setInventorySlotContents(0, result);
		this.getSlot(36 + 1).putStack(result);
	}

	@Override
	public void onGuiEvent(int param) {
		if (param == EVENTPICKUPRESULT) {
			this.getSlot(36).decrStackSize(1);
			updateResult();
			this.detectAndSendChanges();
		}
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
		}
	}
}
