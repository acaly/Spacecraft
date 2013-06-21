package spacecraft.core.world;

import spacecraft.core.utility.LanguageManager;
import spacecraft.core.utility.RegistryHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderSC extends WorldProvider {
	public static final String DIMENSION = "dimension.specialspace.name";

	public void registerWorldChunkManager() {
		this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.beach, 0.8F, 0.0F);
		this.dimensionId = RegistryHelper.getId(WorldProviderSC.class);
	}

	@Override
	public String getDimensionName()  {
		return LanguageManager.translate(DIMENSION);
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderSC(worldObj, worldObj.getSeed(), true);
	}

	@Override
	public boolean isSurfaceWorld() {
		return false;
	}
	
	@Override
	public boolean isDaytime() {
		return false;
	}
	
	@Override
	public int getAverageGroundLevel() {
		return 2;
	}
	
	@Override
	public double getHorizon() {
		return 2;
	}
	
	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.0f;
	}
	
	@Override
	public boolean getWorldHasVoidParticles() {
		return true;
	}
	
	@Override
	public double getVoidFogYFactor() {
		return 1.0;
	}

}
