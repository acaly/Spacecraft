package spacecraft.core.render;

import spacecraft.core.utility.RenderRegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderOffsetSimple implements ISimpleBlockRenderingHandler {
	private int renderId;
	private BlockAccessOffset world = new BlockAccessOffset(null, 0, 0, 0);
	
	public RenderOffsetSimple() {
		renderId = RenderRegistryHelper.getRenderId(RenderOffsetSimple.class);
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block,
			int modelId, RenderBlocks renderer) {
		WorldLinkInfo info = (WorldLinkInfo) WorldSavedDataSC.forWorld((World) world).getData(WorldSavedDataSC.DATALINKINFO);
		TeleporterInfo tele = info.getTeleporter(x, y, z);
		if (world.getBlockId(tele.x, tele.y, tele.z) == 0) return false;
		
		this.world.setParent(renderer.blockAccess);
		this.world.setStartPoint(x, y, z);
		this.world.setEndPoint(tele.x, tele.y, tele.z);
		IBlockAccess backup = renderer.blockAccess;
		renderer.blockAccess = this.world;
		boolean result = renderer.renderBlockByRenderType(
				Block.blocksList[world.getBlockId(tele.x, tele.y, tele.z)], x, y, z);
		renderer.blockAccess = backup;
		return result;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}

}
