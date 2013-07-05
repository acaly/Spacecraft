package spacecraft.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import spacecraft.core.block.common.BlockContainerBase;

public class BlockLinkMaintainer extends BlockContainerBase {

	public BlockLinkMaintainer() {
		super(BlockLinkMaintainer.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return 0;
	}

}
