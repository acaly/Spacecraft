package spacecraft.core.render;

import spacecraft.core.block.tile.TileEntityScreen;
import spacecraft.core.utility.WorldSavedDataSC;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RenderOffsetSpecial extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float f) {
		//World world = tile.worldObj;
		//WorldLinkInfo info = (WorldLinkInfo) WorldSavedDataSC.forWorld(world)
		//		.getData(WorldSavedDataSC.DATALINKINFO);
		//TeleporterInfo teleporter = info.getTeleporter(tile.xCoord, tile.yCoord, tile.zCoord);
		//if (teleporter != null) {
			//TileEntity toRender = world.getBlockTileEntity(teleporter.x, teleporter.y, teleporter.z);
		//TODO check if is a screen
			TileEntity toRender = ((TileEntityScreen) tile).getTileEntityInfo();
			if (toRender == null) return;
			TileEntitySpecialRenderer renderer2 = TileEntityRenderer.instance.getSpecialRendererForEntity(toRender);
			if (renderer2 == null) return;
			renderer2.renderTileEntityAt(toRender, d0, d1, d2, f);
		//}
	}

}
