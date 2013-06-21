package spacecraft.core.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import cpw.mods.fml.common.registry.LanguageRegistry;

public class LanguageManager {
	public static LanguageManager INSTANCE = new LanguageManager();
	private Properties table;
	
	public static void init(File langFile) {
		INSTANCE.table = new Properties();
		FileInputStream is;
		try {
			if (!langFile.exists()) {
				langFile.createNewFile();
			}
			is = new FileInputStream(langFile);
			INSTANCE.table.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSTANCE.fillTable();
		LanguageRegistry.instance().addStringLocalization(INSTANCE.table);
	}
	
	private void fillTable() {
		//Item
		table.setProperty("item.DebugText.name", "测试物品");
		table.setProperty("item.Locator", "定位器");
		//Block
		table.setProperty("tile.PortalSC.name", "传送方块");
		table.setProperty("tile.Teleporter.name", "传送器");
		//CreativeTab
		table.setProperty("itemGroup.SpaceCraft", "SpaceCraft");
	}
}
