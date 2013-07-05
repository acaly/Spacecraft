package spacecraft.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import spacecraft.core.block.common.BlockContainerBase;

public class BlockLinkInventory extends BlockContainerBase {

	public BlockLinkInventory() {
		super(BlockLinkInventory.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		// TODO Auto-generated method stub
		return 0;
	}

}
