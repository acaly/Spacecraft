package spacecraft.core.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraftforge.common.DimensionManager;
import spacecraft.core.world.WorldProviderSC;

public class RegistryHelper {
	public static final RegistryHelper INSTANCE;
	static {
		INSTANCE = new RegistryHelper();
	}
	
	public enum RegistryType {
		Block,
		Item,
		Dimension,
		WorldProvider
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
	private Map<Class<?>, RegistryType> regInfoClassType = new HashMap<Class<?>, RegistryType>();
	private Map<Class<?>, String> regInfoClassName = new HashMap<Class<?>, String>();
	
	private RegistryHelper() {
		regInfo = new HashMap<RegistryType,RegInfo>();
		regInfo.put(RegistryType.Block, new RegInfo(300));
		regInfo.put(RegistryType.Item, new RegInfo(14000));
		regInfo.put(RegistryType.Dimension, new RegInfo(5));
		regInfo.put(RegistryType.WorldProvider, new RegInfo(5));
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
			
			//worldprovider
			info = INSTANCE.regInfo.get(RegistryType.WorldProvider);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(Integer.parseInt(ConfigManager.GetGeneralProperties("Reg.WorldProvider" + i.getKey(), i.getValue().toString())));
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
	
	public static void setClassForId(RegistryType type, String name, Class<?> c) {
		INSTANCE.regInfoClassType.put(c, type);
		INSTANCE.regInfoClassName.put(c, name);
	}
	
	//TODO save class-id in a new map
	public static int getId(Class<?> c) {
		return INSTANCE.regInfo.get(INSTANCE.regInfoClassType.get(c))
				.getId(INSTANCE.regInfoClassName.get(c));
	}

	public static void registerWorld() {
		int id = getId(WorldProviderSC.class);
		DimensionManager.registerProviderType(id, WorldProviderSC.class, false);
		DimensionManager.registerDimension(id, id);
	}
}
