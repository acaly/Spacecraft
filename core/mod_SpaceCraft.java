package spacecraft.core;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import spacecraft.core.block.*;
import spacecraft.core.block.tile.*;
import spacecraft.core.item.*;
import spacecraft.core.render.RenderOffsetSimple;
import spacecraft.core.render.RenderOffsetSpecial;
import spacecraft.core.utility.ConfigManager;
import spacecraft.core.utility.ConnectionHandlerWorldData;
import spacecraft.core.utility.EventHandler;
import spacecraft.core.utility.GuiHandler;
import spacecraft.core.utility.LanguageManager;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import spacecraft.core.utility.RenderRegistryHelper;
import spacecraft.core.utility.SpaceWorkbenchRecipe;
import spacecraft.core.world.WorldLinkInfo;
import spacecraft.core.world.WorldProviderSC;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "SpaceCraftMod", name = "Space Craft Mod", version = "0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
		channels={NetworkHelper.CHANNEL}, packetHandler = NetworkHelper.class)

public class mod_SpaceCraft {
	public static mod_SpaceCraft INSTANCE;
	
	private void initRegistry() {
		RegistryHelper.setItemDefId("DebugText", ItemDebugText.class);
		RegistryHelper.setItemDefId("Locator", ItemLocator.class);
		RegistryHelper.setItemDefId("TeleportCrystal", ItemTeleportCrystal.class);
		RegistryHelper.setItemDefId("License", ItemLicense.class);
		
		RegistryHelper.setBlockDefId("PortalSC", BlockPortalSC.class, TileEntityPortalSC.class);
		RegistryHelper.setBlockDefId("TeleporterSC", BlockTeleporter.class, TileEntityTeleporter.class);
		RegistryHelper.setBlockDefId("Screen", BlockScreen.class, TileEntityScreen.class);
		RegistryHelper.setBlockDefId("Monitor", BlockMonitor.class, TileEntityMonitor.class);
		RegistryHelper.setBlockDefId("SpaceWorkbench", BlockSpaceWorkbench.class);
		RegistryHelper.setBlockDefId("LinkMaintainer", BlockLinkMaintainer.class, TileEntityLinkMaintainer.class);
		RegistryHelper.setBlockDefId("LinkInventory", BlockLinkInventory.class);
		
		RegistryHelper.setDimensionDefId("SpecialSpace", WorldProviderSC.class);
	}
	
	@Mod.Init
	public void load(FMLInitializationEvent evt) {
		INSTANCE = this;
		RegistryHelper.INSTANCE.finishLoading();
		
		RenderRegistryHelper.regBlockHandler(BlockScreen.class, new RenderOffsetSimple());
		RenderRegistryHelper.regTileEntityHandler(TileEntityScreen.class, new RenderOffsetSpecial());
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandlerWorldData());

		RegistryHelper.INSTANCE.createItemsAndBlocks();
		
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
		SpaceWorkbenchRecipe.initRecipes();
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		//config
		ConfigManager.init(event.getSuggestedConfigurationFile());
		initRegistry();
		RegistryHelper.readFromConfig();
		SpaceWorkbenchRecipe.readConfig();
		ConfigManager.SaveConfig();
		
		//lang
		LanguageManager.init(new File(event.getModConfigurationDirectory().getPath(), "spacecraft.lang"));
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
