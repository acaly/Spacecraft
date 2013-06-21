package spacecraft.core.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;
import spacecraft.core.mod_SpaceCraft;
import spacecraft.core.world.WorldProviderSC;

public class RegistryHelper {
	public static final RegistryHelper INSTANCE;
	static {
		INSTANCE = new RegistryHelper();
	}
	
	public enum RegistryType {
		Block,
		Item,
		Dimension
	}
	
	private class RegInfo {
		public int nextId;
		public Map<String, Integer> idMap;
		public RegInfo(int defStartFrom) {
			idMap = new HashMap<String, Integer>();
			nextId = defStartFrom;
		}
		public int getId(String name) {
			return idMap.get(name);
		}
		public void setDef(String name) {
			idMap.put(name, nextId++);
		}
	}
	
	private Map<RegistryType,RegInfo> regInfo;
	private Map<Class<?>, RegistryType> regInfoClassType = new HashMap();
	private Map<Class<?>, String> regInfoClassName = new HashMap();
	private Map<Class<?>, Integer> regInfoClassId;
	private Map<Class<?>, Class<? extends TileEntity>> tileEntityMap = new HashMap();;//only before reg process finished
	
	private RegistryHelper() {
		regInfo = new HashMap<RegistryType,RegInfo>();
		regInfo.put(RegistryType.Block, new RegInfo(300));
		regInfo.put(RegistryType.Item, new RegInfo(14000));
		regInfo.put(RegistryType.Dimension, new RegInfo(5));
	}
	
	public static void readFromConfig() {
		try {
			RegInfo info;
			//block
			info = INSTANCE.regInfo.get(RegistryType.Block);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(ConfigManager.GetBlockID(i.getKey(), i.getValue()));
			}
			
			//item
			info = INSTANCE.regInfo.get(RegistryType.Item);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(ConfigManager.GetItemID(i.getKey(), i.getValue()));
			}
			
			//dimension
			info = INSTANCE.regInfo.get(RegistryType.Dimension);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(Integer.parseInt(ConfigManager.GetGeneralProperties("Reg.Dimension" + i.getKey(), i.getValue().toString())));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getId(RegistryType type, String name) {
		return INSTANCE.regInfo.get(type).getId(name);
	}
	
	public static void setDefId(RegistryType type, String name) {
		INSTANCE.regInfo.get(type).setDef(name);	
	}
	
	public static void setClassDefId(RegistryType type, String name, Class<?> c) {
		setDefId(type, name);
		INSTANCE.regInfoClassType.put(c, type);
		INSTANCE.regInfoClassName.put(c, name);
	}
	
	public static void setItemDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Item, name, c);
	}
	
	public static void setBlockDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Block, name, c);
	}
	
	public static void setBlockDefId(String name, Class<?> block, Class<? extends TileEntity> tileEntity) {
		setClassDefId(RegistryType.Block, name, block);
	}
	
	public static void setDimensionDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Dimension, name, c);
	}
	
	public static int getId(Class<?> c) {
		return INSTANCE.regInfoClassId.get(c);
	}
	
	public static String getName(Class<?> c) {
		return INSTANCE.regInfoClassName.get(c);
	}

	public static void registerWorld(Class c, int id) {
		DimensionManager.registerProviderType(id, c, false);
		DimensionManager.registerDimension(id, id);
	}
	
	public static void registerBlock(Class c) {
		
	}
	
	public void finishLoading() {
		regInfoClassId = new HashMap();
		RegistryType type;
		int id;
		String name;
		for (Class i : regInfoClassName.keySet()) {
			type = regInfoClassType.get(i);
			name = regInfoClassName.get(i);
			id = regInfo.get(type).getId(name);
			
			regInfoClassId.put(i, id);
			if (type == RegistryType.Item) {
				
			} else if (type == RegistryType.Block) {
				//TODO reg block automatically
				if (tileEntityMap.containsKey(i)) {
					ModLoader.registerTileEntity((Class<? extends TileEntity>) tileEntityMap.get(i), name);
				}
			} else if (type == RegistryType.Dimension) {
				registerWorld(i, id);
			}
		}
		regInfoClassType.clear();
		regInfoClassType = null;
		tileEntityMap.clear();
		tileEntityMap = null;
	}
	
	//TODO lang here
	public static CreativeTabs creativeTab = new CreativeTabs("Space Craft") {
		@Override
	    public Item getTabIconItem() {
	    	return mod_SpaceCraft.INSTANCE.itemLocator;
	    }
	};
}
