package spacecraft.core.item;

import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import net.minecraft.item.Item;

public abstract class ItemBase extends Item {

	public ItemBase(int par1) {
		super(par1);
	}

	public ItemBase(Class<? extends ItemBase> c) {
		super(RegistryHelper.getId(c));
	}
	
	public ItemBase(String name) {
		super(RegistryHelper.getId(RegistryType.Item, name));
	}
}
