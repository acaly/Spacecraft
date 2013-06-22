package spacecraft.core.block;

import spacecraft.core.mod_SpaceCraft;
import spacecraft.core.block.tile.TileEntityTeleporter;
import spacecraft.core.utility.GuiHandler;
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
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return GuiHandler.BLOCKTELEPORTER;
	}
}
