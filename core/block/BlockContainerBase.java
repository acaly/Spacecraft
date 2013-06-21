package spacecraft.core.block;

import spacecraft.core.utility.RegistryHelper;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockContainerBase extends BlockContainer {

	protected BlockContainerBase(Class c, Material par2Material) {
		super(RegistryHelper.getId(c), par2Material);
		this.setCreativeTab(RegistryHelper.creativeTab);
		this.setUnlocalizedName(RegistryHelper.getName(c));
	}

}
