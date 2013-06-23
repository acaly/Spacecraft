package spacecraft.core.utility;

import java.util.HashMap;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderRegistryHelper {
	private static HashMap<Class, Integer> renderIdMap = new HashMap();
	
	//handler must already have an id
	public static void regBlockHandler(Class<?> block, ISimpleBlockRenderingHandler handler) {
		int id = renderIdMap.get(handler.getClass());
		renderIdMap.put(block, id);
		RenderingRegistry.registerBlockHandler(handler);
	}
	
	public static void regTileEntityHandler(Class<?> tileEntity, TileEntitySpecialRenderer handler) {
		TileEntityRenderer.instance.specialRendererMap.put(tileEntity, handler);
	}
	
	public static int getRenderId(Class<?> c) {
		if (!renderIdMap.containsKey(c)) {
			int id = RenderingRegistry.getNextAvailableRenderId();
			renderIdMap.put(c, id);
			return id;
		} else {
			return renderIdMap.get(c);
		}
	}
}
