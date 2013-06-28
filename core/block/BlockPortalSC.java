package spacecraft.core.block;

import java.util.Random;

import spacecraft.core.block.common.BlockContainerBase;
import spacecraft.core.block.tile.TileEntityPortalSC;
import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import tutorial.TeleporterTutorial;
import tutorial.Tutorial;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPortalSC extends BlockContainerBase {

	public BlockPortalSC() {
		//super(RegistryHelper.getId(BlockPortalSC.class), Material.portal);
		super(BlockPortalSC.class);
	}
	
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		if (!par1World.isRemote && 
				par1World.getBlockId(par2, par3, par4) != RegistryHelper.getId(BlockTeleporter.class)) {
			WorldLinkInfo.forWorld(par1World).removeFromWorld(par1World, par2, par3, par4, false);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if (par5Entity.ridingEntity == null && par5Entity.riddenByEntity == null) {
			if (par5Entity instanceof EntityPlayerMP) {
				((TileEntityPortalSC) par1World.getBlockTileEntity(par2, par3, par4))
						.setPlayer((EntityPlayerMP) par5Entity);
			}
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5) {
		super.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
		WorldLinkInfo.removeFromWorld(par1World, par2, par3, par4, true);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPortalSC();
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
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
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}
	
}
