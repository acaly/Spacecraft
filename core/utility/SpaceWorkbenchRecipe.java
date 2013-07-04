package spacecraft.core.utility;

import java.util.ArrayList;
import java.util.List;

import spacecraft.core.item.ItemLicense;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.item.ItemTeleportCrystal;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SpaceWorkbenchRecipe {
	public static class Recipe {
		public ItemStack material, material2;
		public ItemStack result;
		public Recipe(ItemStack material, ItemStack material2, ItemStack result) {
			this.material = material;
			this.material2 = material2;
			this.result = result;
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
			return ItemLicense.getLicensed(material.getStackInSlot(2), 
					getResult(material.getStackInSlot(0), material.getStackInSlot(1)),
					material.getStackInSlot(0));
		}
	}
	
	public static List<Recipe> recipes = new ArrayList();
	static {
		addRecipe(new ItemStack(Item.enderPearl),
				new ItemStack(Item.redstone),
				new ItemStack(RegistryHelper.getItem(ItemLocator.class)));
		addRecipe(new ItemStack(RegistryHelper.getItem(ItemLocator.class)),
				new ItemStack(Item.diamond),
				new ItemStack(RegistryHelper.getItem(ItemTeleportCrystal.class)));
		addRecipe(new ItemStack(Item.writtenBook),
				new ItemStack(Item.enderPearl),
				new ItemStack(RegistryHelper.getItem(ItemLicense.class)));
	}
	
	public static void addRecipe(ItemStack material, ItemStack material2, ItemStack result) {
		recipes.add(new Recipe(material, material2, result));
	}
	
	public static ItemStack getResult(IInventory material) {
		ItemStack r;
		for (Recipe i : recipes) {
			if (!i.match(material))
				continue;
			r = i.getResult(material);
			if (r != null) 
				return r;
		}
		return null;
	}
}
