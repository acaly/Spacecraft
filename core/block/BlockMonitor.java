package spacecraft.core.block;

import spacecraft.core.block.tile.TileEntityMonitor;
import spacecraft.core.utility.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMonitor extends BlockContainerBase {
	
	public BlockMonitor() {
		super(BlockMonitor.class, Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityMonitor();
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return GuiHandler.BLOCKMONITOR;
	}

}
