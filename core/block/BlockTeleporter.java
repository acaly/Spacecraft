package spacecraft.core.block;

import java.util.Random;

import spacecraft.core.world.TeleportManager;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTeleporter extends BlockPortal {

	public BlockTeleporter(int par1) {
		super(par1);
	}

	@Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		//Do nothing
	}
	
	@Override
    public boolean tryToCreatePortal(World par1World, int par2, int par3, int par4) {
		return false;
	}
	
	@Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return true;
	}

	@Override
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par5Entity.ridingEntity == null && par5Entity.riddenByEntity == null)
        {
			if (par5Entity instanceof EntityPlayerMP)
			{
				TeleportManager.teleport((EntityPlayerMP) par5Entity, par1World, par2, par3, par4);
				/*
				EntityPlayerMP thePlayer = (EntityPlayerMP) par5Entity;
				if (par5Entity.dimension != Tutorial.dimension)
				{
					thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, Tutorial.dimension, new TeleporterTutorial(thePlayer.mcServer.worldServerForDimension(Tutorial.dimension)));
				}
				else
				{
					thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0, new TeleporterTutorial(thePlayer.mcServer.worldServerForDimension(0)));
				}
				*/
			}
        }
    }
	
}
