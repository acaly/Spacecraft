package spacecraft.core.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import spacecraft.core.block.*;
import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.item.*;
import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.WorldProviderSC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class LanguageManager {
	public static LanguageManager INSTANCE = new LanguageManager();
	private static final String HEADER = "Language file for Spacecraft.";
	
	private Properties table;
	
	public static String translate(String str) {
		return StringTranslate.getInstance().translateKey(str);
	}
	
	public static void init(File langFile) {
		INSTANCE.table = new Properties();
		try {
			if (!langFile.exists()) {
				langFile.createNewFile();
			}
			FileInputStream is = new FileInputStream(langFile);
			INSTANCE.table.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSTANCE.fillTable();
		LanguageRegistry.instance().addStringLocalization(INSTANCE.table);
		try {
			FileOutputStream os = new FileOutputStream(langFile);
			INSTANCE.table.store(os, HEADER);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String forClass(Class c) {
		if (Item.class.isAssignableFrom(c)) {
			return "item." + RegistryHelper.getName(c) + ".name";
		} else {// if (Block.class.isAssignableFrom(c)) {
			return "tile." + RegistryHelper.getName(c) + ".name";
		}
	}
	
	private void fillTable() {
		//Item
		table.setProperty(forClass(ItemDebugText.class), "测试物品");
		table.setProperty(forClass(ItemLocator.class), "定位仪");
		table.setProperty(forClass(ItemLicense.class), "许可证");
		table.setProperty(forClass(ItemTeleportCrystal.class), "传送水晶");
		//Block
		table.setProperty(forClass(BlockPortalSC.class), "传送方块");
		table.setProperty(forClass(BlockTeleporter.class), "传送器");
		table.setProperty(forClass(BlockScreen.class), "屏幕");
		table.setProperty(forClass(BlockMonitor.class), "显示器");
		table.setProperty(forClass(BlockSpaceWorkbench.class), "空间工作台");
		//inventory
		table.setProperty(TileEntityTeleporter.INVENTORY, "传送器");
		//CreativeTab
		table.setProperty(RegistryHelper.CREATIVEPAGENAME_UNL, "SpaceCraft");
		//dimension
		table.setProperty(WorldProviderSC.DIMENSION, "异空间");
		//printed text
		table.setProperty(TeleportManager.MSG, "You're not allowed to use this teleporter!");
		//item.license.inf
		table.setProperty(ItemLicense.LANG_LICENCE_COUNT, "剩余次数：%1$s");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE, "允许操作：%1$s");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE(ItemLicense.TYPE_ROOT), "全部");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE(ItemLicense.TYPE_NULL), "无");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE(ItemLicense.TYPE_CRYSTAL), "制作传送水晶");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE(ItemLicense.TYPE_LOCATOR), "制作定位器");
		table.setProperty(ItemLicense.LANG_LICENCE_TYPE(ItemLicense.TYPE_LICENSE), "制作许可");
		//item.locator.inf
		table.setProperty(ItemLocator.LANG_LOCATION, "目标地点：%1$s (%2$s, %3$s, %4$s)");
		//item.telecrystal.inf
		table.setProperty(ItemTeleportCrystal.LANG_LOCATION, "目标地点：%1$s (%2$s, %3$s, %4$s)");
		table.setProperty(ItemTeleportCrystal.LANG_COUNT, "最大传送人数：%1$s");
		table.setProperty(ItemTeleportCrystal.LANG_TIME, "持续时间：%1$s s");
		
	}
}
