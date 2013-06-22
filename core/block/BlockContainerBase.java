package spacecraft.core.block;

import spacecraft.core.mod_SpaceCraft;
import spacecraft.core.utility.GuiHandler;
import spacecraft.core.utility.RegistryHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainerBase extends BlockContainer {
	
	protected BlockContainerBase(Class c, Material par2Material) {
		super(RegistryHelper.getId(c), par2Material);
		this.setCreativeTab(RegistryHelper.creativeTab);
		this.setUnlocalizedName(RegistryHelper.getName(c));
	}
	
	//return < 0 means not allowed to use the gui
	protected abstract int openGui(World par1World, TileEntity tile, EntityPlayer player);

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			TileEntity var10 = (TileEntity)par1World.getBlockTileEntity(par2, par3, par4);
			if (var10 != null) {
				int id = openGui(par1World, var10, par5EntityPlayer);
				if (id >= 0)
				{
					par5EntityPlayer.openGui(mod_SpaceCraft.INSTANCE, id, par1World, par2, par3, par4);
				}
			}
			return true;
		}
	}
}
