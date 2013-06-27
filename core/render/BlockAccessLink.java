package spacecraft.core.render;

import spacecraft.core.world.TeleporterInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3Pool;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class BlockAccessLink implements IBlockAccess {
	public TeleporterInfo tele;
	private LinkBlockInfo blocks;
	private World defaultWorld;
	private int coordX, coordY, coordZ;
	
	public BlockAccessLink() {
		defaultWorld = Minecraft.getMinecraft().theWorld;
	}
	
	public void setCoord(int x, int y, int z) {
		coordX = x;
		coordY = y;
		coordZ = z;
	}
	
	public void setAim(TeleporterInfo tele) {
		this.tele = tele;
	}
	
	public void setBlockInfo(LinkBlockInfo blockInfo) {
		this.blocks = blockInfo;
	}

	private int index;
	private boolean setPos(int x, int y, int z) {
		if (tele == null) return false;
		x = x - coordX + tele.x;
		y = y - coordY + tele.y;
		z = z - coordZ + tele.z;//TODO compare x - coordX with 0
		if (x == tele.x) {
			if (y == tele.y) {
				if (z == tele.z) {
					index = 0;
					return true;
				} else if (z == tele.z + 1) {
					index = 5;
					return true;
				} else if (z == tele.z - 1) {
					index = 6;
					return true;
				}
			} else if (y == tele.y + 1 && z == tele.z) {
				index = 3;
				return true;
			} else if (y == tele.y - 1 && z == tele.z) {
				index = 4;
				return true;
			}
		} else if (x == tele.x + 1) {
			if (y == tele.y && z == tele.z) {
				index = 1;
				return true;
			}
		} else if (x == tele.x - 1) {
			if (y == tele.y && z == tele.z) {
				index = 2;
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getBlockId(int i, int j, int k) {
		if (setPos(i, j, k)) {
			return blocks.id[index];
		}
		return 0;
	}

	@Override
	public TileEntity getBlockTileEntity(int i, int j, int k) {
		return null;
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int i, int j, int k, int l) {
		return defaultWorld.getLightBrightnessForSkyBlocks(i, j, k, l);
	}

	@Override
	public int getBlockMetadata(int i, int j, int k) {
		if (setPos(i, j, k)) {
			return blocks.metadata[index];
		}
		return 0;
	}

	@Override
	public float getBrightness(int i, int j, int k, int l) {
		return defaultWorld.getBrightness(i, j, k, l);//.provider.lightBrightnessTable[15];//14;//TODO
	}

	@Override
	public float getLightBrightness(int i, int j, int k) {
		return defaultWorld.getLightBrightness(i, j, k);//defaultWorld.provider.lightBrightnessTable[15];//TODO
	}

	@Override
	public Material getBlockMaterial(int i, int j, int k) {
        int l = this.getBlockId(i, j, k);
        return l == 0 ? Material.air : Block.blocksList[l].blockMaterial;
	}

	@Override
	public boolean isBlockOpaqueCube(int i, int j, int k) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(int i, int j, int k) {
        Block block = Block.blocksList[this.getBlockId(i, j, k)];
        return block == null ? false : block.blockMaterial.blocksMovement() && block.renderAsNormalBlock();
	}

	@Override
	public boolean isAirBlock(int i, int j, int k) {
		return this.getBlockId(i, j, k) == 0;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int i, int j) {
		return defaultWorld.getBiomeGenForCoords(i, j);
	}

	@Override
	public int getHeight() {
		return 256;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		return false;
	}

	@Override
	public boolean doesBlockHaveSolidTopSurface(int i, int j, int k) {
		return false;
	}

	@Override
	public Vec3Pool getWorldVec3Pool() {
		return defaultWorld.getWorldVec3Pool();
	}

	@Override
	public int isBlockProvidingPowerTo(int i, int j, int k, int l) {
		return 0;//TODO redstone render?
	}

}
