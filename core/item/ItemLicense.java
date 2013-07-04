package spacecraft.core.item;

import java.util.ArrayList;
import java.util.List;

import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.SpaceWorkbenchRecipe;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.StringTranslate;

public class ItemLicense extends ItemBase {
	private static final String TYPE = "type";
	private static final String COUNT = "count";
	
	public static final String CRYSTAL_COUNT = "cry_count";
	public static final String CRYSTAL_TIME = "cry_time";

	public static final String TYPE_ROOT = "root";
	public static final String TYPE_NULL = "null";
	public static final String TYPE_CRYSTAL = SpaceWorkbenchRecipe.NEED_CRYSTAL;
	public static final String TYPE_LOCATOR = SpaceWorkbenchRecipe.NEED_LOCATOR;
	public static final String TYPE_LICENSE = SpaceWorkbenchRecipe.NEED_LICENSE;
	
	public static final String LANG_LICENCE_COUNT = "item.license.inf.count";
	public static final String LANG_LICENCE_TYPE = "item.license.inf.type";
	
	private static final String ROOT_ITEM = "type=root\ncount=1";
	
	public static final String LANG_LICENCE_TYPE(String type) {
		return "item.license.dis.type." + type;
	}
	
	public ItemLicense() {
		super(ItemLicense.class);
		this.setMaxStackSize(1);
	}
	
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
	
	public static String getType(NBTTagCompound nbt) {
		if (nbt == null || !nbt.hasKey(TYPE)) {
			return TYPE_NULL;
		}
		return nbt.getString(TYPE);
	}
	
	public static boolean getTypeAvailable(NBTTagCompound nbt, String type2) {
		String type = getType(nbt);
		return type.equals(TYPE_ROOT) || type.equals(type2);
	}
	
	private static NBTTagCompound getLicense(String book) {
		if (book.length() == 0) return null;
		NBTTagCompound r = new NBTTagCompound();
		int last_i = -1, i = 0, len = book.length();
		int equ;
		String line;
		String key, value;
		i = book.indexOf('\n', 0);
		while ((last_i) < len) {
			if (i == -1) i = len;
			line = book.substring(last_i + 1, i);
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
			i = book.indexOf('\n', last_i + 1);
		}
		return r;
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		StringTranslate trans = StringTranslate.getInstance();
		par3List.add(trans.translateKeyFormat(LANG_LICENCE_COUNT, getCount(par1ItemStack)));
		String type = trans.translateKey(LANG_LICENCE_TYPE(getType(par1ItemStack.stackTagCompound)));
		par3List.add(trans.translateKeyFormat(LANG_LICENCE_TYPE, type));
	}
	
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		ItemStack root = new ItemStack(par1, 1, 0);
		root.stackTagCompound = getLicense(ROOT_ITEM);
		par3List.add(root);
	}
}
