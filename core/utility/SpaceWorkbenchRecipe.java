package spacecraft.core.utility;

import java.util.ArrayList;
import java.util.List;

import spacecraft.core.item.ItemLocator;

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
		public ItemStack getResult(ItemStack material, ItemStack material2) {
			if (ItemStack.areItemStacksEqual(material, this.material) &&
					ItemStack.areItemStacksEqual(material2, this.material2)) {
				return result.copy();
			}
			return null;
		}
	}
	
	public static List<Recipe> recipes = new ArrayList();
	static {
		addRecipe(new ItemStack(Item.enderPearl), new ItemStack(Item.redstone),
				new ItemStack(RegistryHelper.getItem(ItemLocator.class)));
	}
	
	public static void addRecipe(ItemStack material, ItemStack material2, ItemStack result) {
		recipes.add(new Recipe(material, material2, result));
	}
	
	public static ItemStack getResult(IInventory material) {
		ItemStack r;
		for (Recipe i : recipes) {
			r = i.getResult(material.getStackInSlot(0), material.getStackInSlot(1));
			if (r != null) 
				return r;
		}
		return null;
	}
}
