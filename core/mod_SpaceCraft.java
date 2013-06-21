package spacecraft.core;

import net.minecraftforge.common.DimensionManager;
import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.item.ItemDebugText;
import spacecraft.core.utility.ConfigManager;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import spacecraft.core.world.WorldProviderSC;
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
	public BlockPortalSC blockPortalSC;
	
    @Mod.Init
    public void load(FMLInitializationEvent evt) {
    	INSTANCE = this;
    	
    	RegistryHelper.setClassForId(RegistryType.Item, "DebugText", ItemDebugText.class);
    	itemDebugText = new ItemDebugText();
    	RegistryHelper.setClassForId(RegistryType.Block, "PortalSC", BlockPortalSC.class);
    	blockPortalSC = new BlockPortalSC();
    	RegistryHelper.setClassForId(RegistryType.Dimension, "SpecialSpace", WorldProviderSC.class);
    	RegistryHelper.registerWorld();
    }

    @Mod.PreInit
    public void preInit(FMLPreInitializationEvent event) {
    	RegistryHelper.setDefId(RegistryType.Item, "DebugText");
    	RegistryHelper.setDefId(RegistryType.Block, "PortalSC");
    	RegistryHelper.setDefId(RegistryType.Dimension, "SpecialSpace");
    	
    	ConfigManager.init(event.getSuggestedConfigurationFile());
    	RegistryHelper.readFromConfig();
    	//ConfigManager.SaveConfig();
    }

    @Mod.PostInit
    public void postInit(FMLPostInitializationEvent event) {
    	
    }
}
