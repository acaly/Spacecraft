package spacecraft.core.item;

import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemBase extends Item {

	public ItemBase(Class<? extends ItemBase> c) {
		super(RegistryHelper.getId(c));
		this.setUnlocalizedName(RegistryHelper.getName(c));
		this.setCreativeTab(RegistryHelper.creativeTab);
	}
	
	public ItemBase(String name) {
		super(RegistryHelper.getId(RegistryType.Item, name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(RegistryHelper.creativeTab);
	}
	
	public NBTTagCompound getOrCreateNBT(ItemStack itemStack) {
		NBTTagCompound r = itemStack.stackTagCompound;
		if (r == null) {
			r = new NBTTagCompound();
			itemStack.stackTagCompound = r;
		}
		return r;
	}
}
