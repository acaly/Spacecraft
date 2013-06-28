package spacecraft.core.utility;

import java.util.ArrayList;
import java.util.List;

import spacecraft.core.item.ItemLocator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SpaceWorkbenchRecipe {
	public static class Recipe {
		public ItemStack material;
		public ItemStack result;
		public Recipe(ItemStack material, ItemStack result) {
			this.material = material;
			this.result = result;
		}
		public ItemStack getResult(ItemStack material) {
			if (material != null && this.material.isItemEqual(material)) {
				return result.copy();
			}
			return null;
		}
	}
	
	public static List<Recipe> recipes = new ArrayList();
	static {
		addRecipe(new ItemStack(Item.enderPearl), new ItemStack(RegistryHelper.getItem(ItemLocator.class)));
	}
	
	public static void addRecipe(ItemStack material, ItemStack result) {
		recipes.add(new Recipe(material, result));
	}
	
	public static ItemStack getResult(ItemStack material) {
		ItemStack r;
		for (Recipe i : recipes) {
			r = i.getResult(material);
			if (r != null) 
				return r;
		}
		return null;
	}
}
