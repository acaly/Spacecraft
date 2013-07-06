package spacecraft.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import spacecraft.core.block.common.BlockContainerBase;
import spacecraft.core.block.tile.TileEntityLinkMaintainer;
import spacecraft.core.utility.GuiHandler;

public class BlockLinkMaintainer extends BlockContainerBase {

	public BlockLinkMaintainer() {
		super(BlockLinkMaintainer.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityLinkMaintainer();
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return GuiHandler.BLOCKLINKMAINTAINER;
	}

}
