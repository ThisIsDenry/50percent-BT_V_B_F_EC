package kaptainwutax.featureutils.structure;

import gen.ChunkGeneratorEnd;
import gen.Rotation;
import kaptainwutax.biomeutils.Biome;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.Dimension;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.VersionMap;

import java.util.Random;

public class EndCity extends TriangularStructure<EndCity> {

	public static final VersionMap<RegionStructure.Config> CONFIGS = new VersionMap<RegionStructure.Config>()
			.add(MCVersion.v1_9, new RegionStructure.Config(20, 11, 10387313));

	public EndCity(MCVersion version) {
		this(CONFIGS.getAsOf(version), version);
	}

	public EndCity(RegionStructure.Config config, MCVersion version) {
		super(config, version);
	}

	private static int getYPosForStructure(int chunkX, int chunkZ, long structureSeed) {
		Random random = new Random((long)(chunkX + chunkZ * 10387313));
		Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
		ChunkRand chunkRand = new ChunkRand(structureSeed);
		ChunkGeneratorEnd chunkEnd = new ChunkGeneratorEnd(structureSeed, chunkRand);
		chunkEnd.prepareHeights(chunkX, chunkZ);
		int i = 5;
		int j = 5;
		if (rotation == Rotation.CLOCKWISE_90) {
			i = -5;
		} else if (rotation == Rotation.CLOCKWISE_180) {
			i = -5;
			j = -5;
		} else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
			j = -5;
		}

		int k = chunkEnd.heightMap[7][7];
		int l = chunkEnd.heightMap[7][7 + j];
		int i1 = chunkEnd.heightMap[7 + i][7];
		int j1 = chunkEnd.heightMap[7 + i][7 + j];
		return Math.min(Math.min(k, l), Math.min(i1, j1));
	}

	@Override
	public boolean canStart(Data<EndCity> data, long structureSeed, ChunkRand rand) {
		if(!super.canStart(data, structureSeed, rand))return false;
		if (getYPosForStructure(data.chunkX, data.chunkZ, structureSeed) < 60) return false;
		return true;
	}

	@Override
	public boolean isValidDimension(Dimension dimension) {
		return dimension == Dimension.END;
	}

	@Override
	public boolean isValidBiome(Biome biome) {
		return biome == Biome.END_MIDLANDS || biome == Biome.END_HIGHLANDS;
	}

}
