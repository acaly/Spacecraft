package spacecraft.core.utility;

import java.io.File;
import java.io.IOException;

import net.minecraftforge.common.Configuration;

public final class ConfigManager {
	private static ConfigManager INSTANCE;
	
	public static void init(File configFile) {
		new ConfigManager(configFile);
	}
	private Configuration config;
	
	private ConfigManager(File configFile) {
		INSTANCE = this;
		if (!configFile.exists())
		{
			try
			{
				configFile.createNewFile();
			}
			catch (IOException e)
			{
				System.out.println(e);
				return;
			}
		}

		config = new Configuration(configFile);
		config.load();
	}
	
	public static void InitliazeConfig(File ConfigFile) {
		if (INSTANCE != null) return;
		INSTANCE = new ConfigManager(ConfigFile);
	}
	
	public static String GetGeneralProperties(String PropertyName, String DefaultValue) {
		if (INSTANCE == null) {
			throw new RuntimeException("Not initialized!");
		}
		return INSTANCE.config.get("general", PropertyName, DefaultValue).getString();
	}
	
	public static int GetGeneralProperties(String PropertyName, int DefaultValue) {
		if (INSTANCE == null) {
			throw new RuntimeException("Not initialized!");
		}
		return INSTANCE.config.get("general", PropertyName, DefaultValue).getInt();
	}
	
	public static int GetItemID(String ItemName, int DefaultValue) {
		if (INSTANCE == null) {
			throw new RuntimeException("Not initialized!");
		}
		return INSTANCE.config.getItem("item", "ID." + ItemName, DefaultValue).getInt() - 256;
	}
	
	public static int GetBlockID(String BlockName, int DefaultID) {
		if (INSTANCE == null) {
			throw new RuntimeException("Not initialized!");
		}
		return INSTANCE.config.getBlock("ID." + BlockName, DefaultID).getInt();
	}
	
	public static void SaveConfig() {
		INSTANCE.config.save();
	}
}
