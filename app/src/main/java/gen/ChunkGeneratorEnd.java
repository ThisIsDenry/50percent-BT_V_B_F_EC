package gen;

import kaptainwutax.seedutils.mc.ChunkRand;

public class ChunkGeneratorEnd {

    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    public final int[][] heightMap = new int[16][16];

    private final NoiseGeneratorSimplex noiseGeneratorSimplex;
    private final ChunkRand random;

    public ChunkGeneratorEnd(long seed, ChunkRand chunkRand) {
        this.lperlinNoise1 = new NoiseGeneratorOctaves(chunkRand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(chunkRand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(chunkRand, 8);
        this.random = new ChunkRand(seed);
        this.random.advance(17292);
        this.noiseGeneratorSimplex = new NoiseGeneratorSimplex(this.random);
    }

    public void prepareHeights(int chunkX, int chunkZ) {
        int i = 2;
        int j = 3;
        int k = 33;
        int l = 3;
        double[] adouble = this.generateNoise(chunkX * 2, 0, chunkZ * 2, 3, 33, 3);

        for(int halfChunkX = 0; halfChunkX < 2; ++halfChunkX) {
            for(int halfChunkZ = 0; halfChunkZ < 2; ++halfChunkZ) {
                for(int fourScaledY = 0; fourScaledY < 32; ++fourScaledY) {
                    double d0 = 0.25D;
                    double d1 = adouble[((halfChunkX + 0) * 3 + halfChunkZ + 0) * 33 + fourScaledY + 0];
                    double d2 = adouble[((halfChunkX + 0) * 3 + halfChunkZ + 1) * 33 + fourScaledY + 0];
                    double d3 = adouble[((halfChunkX + 1) * 3 + halfChunkZ + 0) * 33 + fourScaledY + 0];
                    double d4 = adouble[((halfChunkX + 1) * 3 + halfChunkZ + 1) * 33 + fourScaledY + 0];
                    double d5 = (adouble[((halfChunkX + 0) * 3 + halfChunkZ + 0) * 33 + fourScaledY + 1] - d1) * 0.25D;
                    double d6 = (adouble[((halfChunkX + 0) * 3 + halfChunkZ + 1) * 33 + fourScaledY + 1] - d2) * 0.25D;
                    double d7 = (adouble[((halfChunkX + 1) * 3 + halfChunkZ + 0) * 33 + fourScaledY + 1] - d3) * 0.25D;
                    double d8 = (adouble[((halfChunkX + 1) * 3 + halfChunkZ + 1) * 33 + fourScaledY + 1] - d4) * 0.25D;

                    for(int l1 = 0; l1 < 4; ++l1) {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.125D;
                        double d13 = (d4 - d2) * 0.125D;

                        for(int i2 = 0; i2 < 8; ++i2) {
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.125D;

                            for(int j2 = 0; j2 < 8; ++j2) {
                                if (d15 > 0.0D) {
                                    int blockX = i2 + halfChunkX * 8;
                                    int blockY = l1 + fourScaledY * 4;
                                    int blockZ = j2 + halfChunkZ * 8;
                                    heightMap[blockX][blockZ] = Math.max(heightMap[blockX][blockZ], blockY);
                                }

                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }

    }

    private double[] generateNoise(int scaledChunkX, int scaledChunkY, int scaledChunkZ, int parameter1, int parameter2, int parameter3) {
        // 3 * 33 * 3
        double[] adouble = new double[parameter1 * parameter2 * parameter3];
        double doubleConstant = 684.412D;
        doubleConstant = doubleConstant * 2.0D;
        double[] adouble1 = this.perlinNoise1.func_202647_a(scaledChunkX, scaledChunkY, scaledChunkZ, parameter1, parameter2, parameter3, doubleConstant / 80.0D, 4.277575000000001D, doubleConstant / 80.0D);
        double[] adouble2 = this.lperlinNoise1.func_202647_a(scaledChunkX, scaledChunkY, scaledChunkZ, parameter1, parameter2, parameter3, doubleConstant, 684.412D, doubleConstant);
        double[] adouble3 = this.lperlinNoise2.func_202647_a(scaledChunkX, scaledChunkY, scaledChunkZ, parameter1, parameter2, parameter3, doubleConstant, 684.412D, doubleConstant);
        int i = scaledChunkX / 2;
        int j = scaledChunkZ / 2;
        int k = 0;

        for(int l = 0; l < parameter1; ++l) {
            for(int i1 = 0; i1 < parameter3; ++i1) {
                float f = getHeightValue(i, j, l, i1);

                for(int j1 = 0; j1 < parameter2; ++j1) {
                    double d2 = adouble2[k] / 512.0D;
                    double d3 = adouble3[k] / 512.0D;
                    double d5 = (adouble1[k] / 10.0D + 1.0D) / 2.0D;
                    double d4;
                    if (d5 < 0.0D) {
                        d4 = d2;
                    } else if (d5 > 1.0D) {
                        d4 = d3;
                    } else {
                        d4 = d2 + (d3 - d2) * d5;
                    }

                    d4 = d4 - 8.0D;
                    d4 = d4 + (double)f;
                    int k1 = 2;
                    if (j1 > parameter2 / 2 - k1) {
                        double d6 = (double)((float)(j1 - (parameter2 / 2 - k1)) / 64.0F);
                        d6 = clamp(d6, 0.0D, 1.0D);
                        d4 = d4 * (1.0D - d6) - 3000.0D * d6;
                    }

                    k1 = 8;
                    if (j1 < k1) {
                        double d7 = (double)((float)(k1 - j1) / ((float)k1 - 1.0F));
                        d4 = d4 * (1.0D - d7) - 30.0D * d7;
                    }

                    adouble[k] = d4;
                    ++k;
                }
            }
        }

        return adouble;
    }

    public static float clamp(float num, float min, float max) {
        if (num < min) {
            return min;
        } else {
            return num > max ? max : num;
        }
    }

    public static double clamp(double num, double min, double max) {
        if (num < min) {
            return min;
        } else {
            return num > max ? max : num;
        }
    }

    public float getHeightValue(int p_201536_1_, int p_201536_2_, int p_201536_3_, int p_201536_4_) {
        float f = (float)(p_201536_1_ * 2 + p_201536_3_);
        float f1 = (float)(p_201536_2_ * 2 + p_201536_4_);
        float f2 = 100.0F - (float) Math.sqrt(f * f + f1 * f1) * 8.0F;
        f2 = clamp(f2, -100.0F, 80.0F);

        for(int i = -12; i <= 12; ++i) {
            for(int j = -12; j <= 12; ++j) {
                long k = (long)(p_201536_1_ + i);
                long l = (long)(p_201536_2_ + j);
                if (k * k + l * l > 4096L && this.noiseGeneratorSimplex.getValue((double)k, (double)l) < (double)-0.9F) {
                    float f3 = (Math.abs((float)k) * 3439.0F + Math.abs((float)l) * 147.0F) % 13.0F + 9.0F;
                    f = (float)(p_201536_3_ - i * 2);
                    f1 = (float)(p_201536_4_ - j * 2);
                    float f4 = 100.0F - (float) Math.sqrt(f * f + f1 * f1) * f3;
                    f4 = clamp(f4, -100.0F, 80.0F);
                    f2 = Math.max(f2, f4);
                }
            }
        }

        return f2;
    }
}
