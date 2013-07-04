package spacecraft.core.item;

import java.util.ArrayList;

import spacecraft.core.utility.RegistryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

public class ItemLicense extends ItemBase {
	private static final String COUNT = "count";
	private static final String CRYSTAL_COUNT = "cry_count";
	private static final String CRYSTAL_TIME = "cry_time";
	
	//TODO more flexible
	//TODO load from config
	private static ArrayList<String> needLicense = new ArrayList();
	private static final String NEEDCRYSTAL = "crystal";
	private static final String NEEDLOCATOR = "locator";
	private static final String NEEDLICENSE = "license";

	static {
		needLicense.add(NEEDCRYSTAL);
		//needLicense.add(NEEDLICENSE);
		needLicense.add(NEEDLOCATOR);
	}
	
	public ItemLicense() {
		super(ItemLicense.class);
	}
	
	public static boolean onLicenseUsed(ItemStack license, ItemStack r) {
		if (r.itemID == RegistryHelper.getItemId(ItemLocator.class)) {
			if (!needLicense.contains(NEEDLOCATOR)) return true;
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemTeleportCrystal.class)) {
			if (!needLicense.contains(NEEDCRYSTAL)) return true;
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemLicense.class)) {
			if (!needLicense.contains(NEEDLICENSE)) return true;
		}
		
		if (decreaseCount(license.stackTagCompound, false)) {
			if (getCount(license) > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * setup the crafting result according to license and material1
	 * @param license license in the crafting window
	 * @param r crafting result
	 * @param material1 material1 in the crafting window, which may have extra information
	 * @return result
	 */
	public static ItemStack getLicensed(ItemStack license, ItemStack r, ItemStack material1) {
		if (r.itemID == RegistryHelper.getItemId(ItemLocator.class)) {
			return setupLocator(license, r);
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemTeleportCrystal.class)) {
			ItemLocator.setTeleportInfo(r, ItemLocator.getTeleporterInfo(material1));
			return setupTeleportCrystal(license, r);
		} else
		if (r.itemID == RegistryHelper.getItemId(ItemLicense.class)) {
			setLicenseInfo(material1, r);
			return setupLicense(license, r);
		}
		return null;
	}
	
	//-----------------------setup part-----------------------
	//setup result according to license
	
	public static ItemStack setupLicense(ItemStack license, ItemStack r) {
		NBTTagCompound nbt = getLicenseInfo(license);
		if (!checkLicenseCount(nbt, NEEDLICENSE)) return null;
		return r;
	}
	
	public static ItemStack setupLocator(ItemStack license, ItemStack locator) {
		NBTTagCompound nbt = getLicenseInfo(license);
		if (!checkLicenseCount(nbt, NEEDLOCATOR)) return null;
		return locator;
	}
	
	public static ItemStack setupTeleportCrystal(ItemStack license, ItemStack crystal) {
		NBTTagCompound nbt = getLicenseInfo(license);
		if (!checkLicenseCount(nbt, NEEDCRYSTAL)) return null;
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
		return crystal;
	}

	//-----------------------helper part-----------------------
	//private methods
	
	public static ItemStack setLicenseInfo(ItemStack book, ItemStack r) {
		if (book.itemID != Item.writtenBook.itemID) return null;
		NBTTagCompound nbtBook = book.stackTagCompound;
		if (nbtBook == null || !nbtBook.hasKey("pages")) {
			return null;
		}
		r.stackTagCompound = getLicense(((NBTTagString)nbtBook.getTagList("pages").tagAt(0)).data);
		return null;
	}
	
	private static boolean checkLicenseCount(NBTTagCompound nbt, String type) {
		if (nbt == null) {
			if (needLicense.contains(type)) {
				return false;
			}
		} else {
			if (needLicense.contains(type)) {
				if (!decreaseCount(nbt, true)) return false;
			}
		}
		return true;
	}
	
	private static boolean decreaseCount(NBTTagCompound nbt, boolean sim) {
		if (nbt == null || !nbt.hasKey(COUNT)) return false;
		int l = nbt.getInteger(COUNT);
		if (l < 1) return false;
		if (!sim) nbt.setInteger(COUNT, l - 1);
		return true;
	}
	
	private static int getCount(ItemStack itemStack) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null || !nbt.hasKey(COUNT)) {
			return 0;
		}
		return nbt.getInteger(COUNT);
	}
	
	private static NBTTagCompound getLicenseInfo(ItemStack license) {
		return license == null ? null : license.stackTagCompound;
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
