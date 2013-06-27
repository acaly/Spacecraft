package spacecraft.core.block;

import spacecraft.core.block.tile.TileEntityScreen;
import spacecraft.core.utility.RenderRegistryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockScreen extends BlockContainerBase {
	private int renderId;
	
	public BlockScreen() {
		super(BlockScreen.class);
		renderId = RenderRegistryHelper.getRenderId(BlockScreen.class);
		this.setBlockUnbreakable();
	}
	
	@Override
	public int getRenderType() {
		return renderId;
	}
	
	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return false;
	}
	
	@Override
    public Icon getIcon(int par1, int par2) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityScreen();
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		//TODO open the aim's gui
		return 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}
}
