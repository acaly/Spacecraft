package spacecraft.core.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemLicense extends ItemBase {
	private static final String CRYSTAL_COUNT = "cry_count";
	private static final String CRYSTAL_TIME = "cry_time";

	public ItemLicense() {
		super(ItemLicense.class);
	}

	public static void setupTeleportCrystal(ItemStack license, ItemStack crystal) {
		NBTTagCompound nbt = license == null ? null : license.stackTagCompound;
		if (nbt == null) {
			ItemTeleportCrystal.setCount(crystal, 1);
			ItemTeleportCrystal.setTime(crystal, 3000);
		} else {
			if (nbt.hasKey(CRYSTAL_COUNT)) {
				ItemTeleportCrystal.setCount(crystal, nbt.getInteger(CRYSTAL_COUNT));
			}
			if (nbt.hasKey(CRYSTAL_TIME)) {
				ItemTeleportCrystal.setTime(crystal, nbt.getInteger(CRYSTAL_TIME));
			}
		}
	}
}
