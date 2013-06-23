package spacecraft.core;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.DimensionManager;
import spacecraft.core.block.*;
import spacecraft.core.block.tile.*;
import spacecraft.core.item.*;
import spacecraft.core.render.RenderOffsetSimple;
import spacecraft.core.render.RenderOffsetSpecial;
import spacecraft.core.utility.ConfigManager;
import spacecraft.core.utility.ConnectionHandlerWorldData;
import spacecraft.core.utility.GuiHandler;
import spacecraft.core.utility.LanguageManager;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.RegistryHelper.RegistryType;
import spacecraft.core.utility.RenderRegistryHelper;
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

	public ItemDebugText itemDebugText;
	public ItemLocator itemLocator;
	
	public BlockPortalSC blockPortalSC;
	public BlockTeleporter blockTeleporter;
	public BlockScreen blockScreen;
	public BlockMonitor blockMonitor;
	
	@Mod.Init
	public void load(FMLInitializationEvent evt) {
		INSTANCE = this;
		RegistryHelper.INSTANCE.finishLoading();
		
		RenderRegistryHelper.regBlockHandler(BlockScreen.class, new RenderOffsetSimple());
		RenderRegistryHelper.regTileEntityHandler(TileEntityScreen.class, new RenderOffsetSpecial());
		
		itemDebugText = new ItemDebugText();
		itemLocator = new ItemLocator();
		
		blockPortalSC = new BlockPortalSC();
		blockTeleporter = new BlockTeleporter();
		blockScreen = new BlockScreen();
		blockMonitor = new BlockMonitor();
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		NetworkRegistry.instance().registerConnectionHandler(new ConnectionHandlerWorldData());
		
		ModLoader.registerBlock(blockPortalSC);
		ModLoader.registerBlock(blockTeleporter);
		ModLoader.registerBlock(blockScreen);
		ModLoader.registerBlock(blockMonitor);
		
	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		RegistryHelper.setItemDefId("DebugText", ItemDebugText.class);
		RegistryHelper.setItemDefId("Locator", ItemLocator.class);
		RegistryHelper.setBlockDefId("PortalSC", BlockPortalSC.class);
		RegistryHelper.setBlockDefId("TeleporterSC", BlockTeleporter.class, TileEntityTeleporter.class);
		RegistryHelper.setBlockDefId("Screen", BlockScreen.class, TileEntityScreen.class);
		RegistryHelper.setBlockDefId("Monitor", BlockMonitor.class, TileEntityMonitor.class);
		RegistryHelper.setDimensionDefId("SpecialSpace", WorldProviderSC.class);
		
		ConfigManager.init(event.getSuggestedConfigurationFile());
		RegistryHelper.readFromConfig();
		ConfigManager.SaveConfig();
		
		LanguageManager.init(new File(event.getModConfigurationDirectory().getPath(), "spacecraft.lang"));
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}
