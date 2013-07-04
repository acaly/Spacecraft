package spacecraft.core.item;

import java.util.ArrayList;

import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.SpaceWorkbenchRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

public class ItemLicense extends ItemBase {
	//private static final String TYPE = "type";
	private static final String COUNT = "count";
	
	public static final String CRYSTAL_COUNT = "cry_count";
	public static final String CRYSTAL_TIME = "cry_time";
	
	public ItemLicense() {
		super(ItemLicense.class);
	}
	/*
	public static ItemStack getLicensed(NBTTagCompound license, ItemStack r, ItemStack material1) {
		if (r.itemID == RegistryHelper.getItemId(ItemLocator.class)) {
			if (!checkLicenseCount(license, NEEDLOCATOR)) return null;
			return setupLocator(license, r);
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemTeleportCrystal.class)) {
			if (!checkLicenseCount(license, NEEDCRYSTAL)) return null;
			ItemLocator.setTeleportInfo(r, ItemLocator.getTeleporterInfo(material1));
			return setupTeleportCrystal(license, r);
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemLicense.class)) {
			setLicenseInfo(material1, r);
			if (!checkLicenseCount(license, NEEDLICENSE)) return null;
			return setupLicense(license, r);
		}
		return null;
	}*/
	
	//-----------------------setup part-----------------------
	//setup result according to license
	/*
	public static ItemStack setupLicense(NBTTagCompound license, ItemStack r) {
		return r;
	}
	
	public static ItemStack setupLocator(NBTTagCompound license, ItemStack locator) {
		return locator;
	}
	
	public static ItemStack setupTeleportCrystal(NBTTagCompound license, ItemStack crystal) {
		if (license == null) {
			ItemTeleportCrystal.setCount(crystal, 1);
			ItemTeleportCrystal.setTime(crystal, 3000);
		} else {
			if (license.hasKey(CRYSTAL_COUNT)) {
				ItemTeleportCrystal.setCount(crystal, license.getInteger(CRYSTAL_COUNT));
			}
			if (license.hasKey(CRYSTAL_TIME)) {
				ItemTeleportCrystal.setTime(crystal, license.getInteger(CRYSTAL_TIME));
			}
		}
		return crystal;
	}
	*/
	//-----------------------helper part-----------------------
	
	public static ItemStack setLicenseInfo(ItemStack book, ItemStack r) {
		if (book.itemID != Item.writtenBook.itemID) return null;
		NBTTagCompound nbtBook = book.stackTagCompound;
		if (nbtBook == null || !nbtBook.hasKey("pages")) {
			return null;
		}
		r.stackTagCompound = getLicense(((NBTTagString)nbtBook.getTagList("pages").tagAt(0)).data);
		return null;
	}
	
	public static boolean decreaseCount(NBTTagCompound nbt, boolean sim) {
		if (nbt == null || !nbt.hasKey(COUNT)) return false;
		int l = nbt.getInteger(COUNT);
		if (l < 1) return false;
		if (!sim) nbt.setInteger(COUNT, l - 1);
		return true;
	}
	
	public static int getCount(ItemStack itemStack) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null || !nbt.hasKey(COUNT)) {
			return 0;
		}
		return nbt.getInteger(COUNT);
	}
	
	private static NBTTagCompound getLicense(String book) {
		if (book.length() == 0) return null;
		NBTTagCompound r = new NBTTagCompound();
		int last_i = 0, i = 0, len = book.length();
		int equ;
		String line;
		String key, value;
		i = book.indexOf('\n', last_i);
		while ((last_i) < len) {
			if (i == -1) i = len;
			line = book.substring(last_i, i);
			if ((equ = line.indexOf('=')) < line.length()) {
				key = line.substring(0, equ);
				value = line.substring(equ + 1);
				try {
					r.setInteger(key, Integer.parseInt(value));
				} catch (NumberFormatException e) {
					r.setString(key, value);
				}
			} else continue;
			last_i = i;
		}
		return r;
	}
}
