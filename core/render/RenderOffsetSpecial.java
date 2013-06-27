package spacecraft.core.render;

import java.util.Random;

import spacecraft.core.block.tile.TileEntityScreen;
import spacecraft.core.utility.WorldSavedDataSC;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RenderOffsetSpecial extends TileEntitySpecialRenderer {
	private Random random = new Random();
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f) {
		TileEntity toRender = ((TileEntityScreen) tile).getTileEntityInfo();
		if (toRender == null || toRender instanceof TileEntityScreen) return;
		TileEntitySpecialRenderer renderer2 = TileEntityRenderer.instance.getSpecialRendererForEntity(toRender);
		if (renderer2 == null) return;
		if (random.nextFloat() > RenderOffsetSimple.SKIPRATE)
			renderer2.renderTileEntityAt(toRender, d0, d1, d2, f);
	}

}
