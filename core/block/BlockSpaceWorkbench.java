package spacecraft.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import spacecraft.core.block.common.BlockContainerBase;
import spacecraft.core.utility.GuiHandler;

public class BlockSpaceWorkbench extends BlockContainerBase {

	public BlockSpaceWorkbench() {
		super(BlockSpaceWorkbench.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return GuiHandler.BLOCKSPACEWORKBENCH;
	}

}
