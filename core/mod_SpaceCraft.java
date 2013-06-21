package spacecraft.core;

import spacecraft.core.item.ItemDebugText;
import spacecraft.core.utility.ConfigManager;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "SpaceCraftMod", name = "Space Craft Mod", version = "0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)

public class mod_SpaceCraft {
	public static mod_SpaceCraft INSTANCE;

	public ItemDebugText itemDebugText;
	
    @Mod.Init
    public void load(FMLInitializationEvent evt) {
    	INSTANCE = this;
    	
    	RegistryHelper.setClassForId(RegistryType.Item, "DebugText", ItemDebugText.class);
    	itemDebugText = new ItemDebugText();
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
    	RegistryHelper.setDefId(RegistryType.Item, "DebugText");
    	
    	ConfigManager.init(event.getSuggestedConfigurationFile());
    	RegistryHelper.readFromConfig();
    	//ConfigManager.SaveConfig();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
}
