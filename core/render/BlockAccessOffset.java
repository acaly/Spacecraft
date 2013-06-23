package spacecraft.core.render;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;

public final class BlockAccessOffset implements IBlockAccess {
	private IBlockAccess parent;
	private int x, y, z;
	private int sx, sy, sz;	//start
	private int ex, ey, ez; //end
	
	public BlockAccessOffset(IBlockAccess parent, int x, int y, int z) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setParent(IBlockAccess parent) {
		this.parent = parent;
	}
	
	private void refreshOffset() {
		x = ex - sx;
		y = ey - sy;
		z = ez - sz;
	}
	
	public void setStartPoint(int x, int y, int z) {
		sx = x;
		sy = y;
		sz = z;
		refreshOffset();
	}
	
	public void setEndPoint(int x, int y, int z) {
		ex = x;
		ey = y;
		ez = z;
		refreshOffset();
	}
	
	@Override
	public int getBlockId(int i, int j, int k) {
		return parent.getBlockId(i + x, j + y, k + z);
	}

	@Override
	public TileEntity getBlockTileEntity(int i, int j, int k) {
		return parent.getBlockTileEntity(i + x, j + y, k + z);
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
		return parent.getLightBrightnessForSkyBlocks(i + x, j + y, k + z, l);
	}

	@Override
	public int getBlockMetadata(int i, int j, int k) {
		return parent.getBlockMetadata(i + x, j + y, k + z);
	}

	@Override
	public float getBrightness(int i, int j, int k, int l) {
		return parent.getBrightness(i + x, j + y, k + z, l);
	}

	@Override
	public float getLightBrightness(int i, int j, int k) {
		return parent.getLightBrightness(i + x, j + y, k + z);
	}

	@Override
	public Material getBlockMaterial(int i, int j, int k) {
		return parent.getBlockMaterial(i + x, j + y, k + z);
	}

	@Override
	public boolean isBlockOpaqueCube(int i, int j, int k) {
		return false;//parent.isBlockOpaqueCube(i + x, j + y, k + z);
	}

	@Override
	public boolean isBlockNormalCube(int i, int j, int k) {
		return parent.isBlockNormalCube(i + x, j + y, k + z);
	}

	@Override
	public boolean isAirBlock(int i, int j, int k) {
		return parent.isAirBlock(i + x, j + y, k + z);
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int i, int j) {
		return parent.getBiomeGenForCoords(i + x, j + z);
	}

	@Override
	public int getHeight() {
		return parent.getHeight();
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return parent.extendedLevelsInChunkCache();
	}

	@Override
	public boolean doesBlockHaveSolidTopSurface(int i, int j, int k) {
		return parent.doesBlockHaveSolidTopSurface(i + x, j + y, k + z);
	}

	@Override
	public Vec3Pool getWorldVec3Pool() {
		return parent.getWorldVec3Pool();
	}

	@Override
	public int isBlockProvidingPowerTo(int i, int j, int k, int l) {
		return parent.isBlockProvidingPowerTo(i + x, j + y, k + z, l);
	}
}
