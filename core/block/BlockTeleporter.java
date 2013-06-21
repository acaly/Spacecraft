package spacecraft.core.block;

import spacecraft.core.mod_SpaceCraft;
import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.utility.RegistryHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockTeleporter extends BlockContainerBase {

	public BlockTeleporter() {
		super(BlockTeleporter.class, Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityTeleporter();
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (par1World.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityTeleporter var10 = (TileEntityTeleporter)par1World.getBlockTileEntity(par2, par3, par4);
			//TODO check if the user is allowed to use the gui
			if (var10 != null)
			{
				par5EntityPlayer.openGui(mod_SpaceCraft.INSTANCE, 10, par1World, par2, par3, par4);
			}
			return true;
		}
	}
}
