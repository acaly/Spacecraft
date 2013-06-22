package spacecraft.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public abstract class TileEntityInventory extends TileEntity implements IInventory, ISidedInventory {
	public static final String INVENTORY = "inventory";
	private static final String SLOT = "slot";
	
	protected InventoryBasic inventory;
	protected int[] availableSide;
	private String name;
	
	public TileEntityInventory(String name, int count) {
		inventory = new InventoryBasic(name, false, count);
		this.name = name;
		availableSide = new int[count];
		for (int i = 0; i < count; ++i) {
			availableSide[i] = i;
		}
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return inventory.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInvName() {
		return name;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return availableSide;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		readInventoryFromNBT(par1NBTTagCompound.getTagList(INVENTORY), inventory);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList list = new NBTTagList();
		writeInventoryToNBT(list, inventory);
		par1NBTTagCompound.setTag(INVENTORY, list);
	}
	
	public static void readInventoryFromNBT(NBTTagList nbt, IInventory inventory) {
		int count = inventory.getSizeInventory();
		int slot;
		NBTTagCompound item;
		ItemStack itemStack;
		for (int i = 0; i < count; ++i) {
			item = (NBTTagCompound) nbt.tagAt(i);
			slot = item.getByte(SLOT);
			itemStack = ItemStack.loadItemStackFromNBT(item);
			if (itemStack != null) {
				inventory.setInventorySlotContents(slot, itemStack);
			}
		}
	}
	
	public static void writeInventoryToNBT(NBTTagList nbt, IInventory inventory) {
		int count = inventory.getSizeInventory();
		NBTTagCompound item;
		ItemStack itemStack;
		for (int i = 0; i < count; ++i) {
			item = new NBTTagCompound();
			itemStack = inventory.getStackInSlot(i);
			if (itemStack != null) {
				item.setByte(SLOT, (byte) i);
				itemStack.writeToNBT(item);
			}
			nbt.appendTag(item);
		}
	}
}
