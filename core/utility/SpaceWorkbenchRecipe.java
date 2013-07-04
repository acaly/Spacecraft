package spacecraft.core.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import spacecraft.core.item.ItemLicense;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.item.ItemTeleportCrystal;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SpaceWorkbenchRecipe {
	
	private static final String PROPERTY_NEED = "CraftingLicense";

	public static final String NEED_CRYSTAL = "crystal";
	public static final String NEED_LOCATOR = "locator";
	public static final String NEED_LICENSE = "license";
	
	public static class Recipe {
		public ItemStack material, material2, result;
		public String need;
		public Recipe(String need, ItemStack material, ItemStack material2, ItemStack result) {
			this.material = material;
			this.material2 = material2;
			this.result = result;
			this.need = need;
		}
		
		public boolean match(IInventory material) {
			ItemStack a = material.getStackInSlot(0), b = material.getStackInSlot(1);
			return a != null && this.material.isItemEqual(a) &&
					b != null && this.material2.isItemEqual(b);
		}
		
		public ItemStack getResult(ItemStack material, ItemStack material2) {
			return result.copy();
		}
		
		public ItemStack getResult(IInventory material) {
			ItemStack r = getResult(material.getStackInSlot(0), material.getStackInSlot(1));
			ItemStack itemLicense = material.getStackInSlot(2);
			NBTTagCompound license = itemLicense == null ? null : itemLicense.stackTagCompound;
			if (!checkLicenseCount(license, need)) return null;
			setupResult(r, license, material.getStackInSlot(0));
			return r;
		}
		
		public void setupResult(ItemStack r, NBTTagCompound license, ItemStack material1){}
		
		public boolean onLicenseUsed(ItemStack license) {
			if (!needLicense.contains(need)) return true;
			if (ItemLicense.decreaseCount(license.stackTagCompound, false)) {
				if (ItemLicense.getCount(license) > 0) {
					return true;
				}
			}
			return false;
		}
	}

	public static List<Recipe> recipes = new ArrayList();
	public static List<String> needLicense;
	
	public static void readConfig() {
		String str = ConfigManager.GetGeneralProperties(PROPERTY_NEED, "crystal,locator");
		needLicense = Arrays.asList(str.split(","));
	}

	public static void initRecipes() {
		recipes.add(new Recipe(NEED_LOCATOR, new ItemStack(Item.enderPearl),
				new ItemStack(Item.redstone),
				new ItemStack(RegistryHelper.getItem(ItemLocator.class))));
		
		recipes.add(new Recipe(NEED_CRYSTAL, new ItemStack(RegistryHelper.getItem(ItemLocator.class)),
				new ItemStack(Item.diamond),
				new ItemStack(RegistryHelper.getItem(ItemTeleportCrystal.class))){
			public void setupResult(ItemStack r, NBTTagCompound license, ItemStack material1) {
				ItemLocator.setTeleportInfo(r, ItemLocator.getTeleporterInfo(material1));
				if (license == null) {
					ItemTeleportCrystal.setCount(r, 1);
					ItemTeleportCrystal.setTime(r, 3000);
				} else {
					if (license.hasKey(ItemLicense.CRYSTAL_COUNT)) {
						ItemTeleportCrystal.setCount(r, license.getInteger(ItemLicense.CRYSTAL_COUNT));
					} else {
						ItemTeleportCrystal.setCount(r, 1);
					}
					if (license.hasKey(ItemLicense.CRYSTAL_TIME)) {
						ItemTeleportCrystal.setTime(r, license.getInteger(ItemLicense.CRYSTAL_TIME));
					} else {
						ItemTeleportCrystal.setTime(r, 3000);
					}
				}
			}
			
		});
		
		recipes.add(new Recipe(NEED_LICENSE, new ItemStack(Item.writtenBook),
				new ItemStack(Item.enderPearl),
				new ItemStack(RegistryHelper.getItem(ItemLicense.class))){
			public void setupResult(ItemStack r, NBTTagCompound license, ItemStack material1) {
				ItemLicense.setLicenseInfo(material1, r);
			}
		});
	}

	public static boolean checkLicenseCount(NBTTagCompound nbt, String type) {
		if (nbt == null) {
			if (SpaceWorkbenchRecipe.needLicense.contains(type)) {
				return false;
			}
		} else {
			if (SpaceWorkbenchRecipe.needLicense.contains(type)) {
				if (!ItemLicense.getTypeAvailable(nbt, type)) return false;
				if (!ItemLicense.decreaseCount(nbt, true)) return false;
			}
		}
		return true;
	}
	
	public static Recipe getResult(IInventory material) {
		for (Recipe i : recipes) {
			if (i.match(material))
				return i;
		}
		return null;
	}
}
