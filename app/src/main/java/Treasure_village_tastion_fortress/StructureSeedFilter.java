package Treasure_village_tastion_fortress;

import FeatureProperties.BastionRemnantProperties;
import FeatureProperties.DragonGatewayProperties;
import kaptainwutax.biomeutils.source.NetherBiomeSource;
import kaptainwutax.featureutils.structure.*;
import kaptainwutax.seedutils.mc.ChunkRand;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.BPos;
import kaptainwutax.seedutils.mc.pos.CPos;
import kaptainwutax.seedutils.mc.pos.RPos;
import kaptainwutax.seedutils.util.math.DistanceMetric;
import kaptainwutax.seedutils.util.math.Vec3i;

import java.util.ArrayList;

public class StructureSeedFilter {
    public static ArrayList<Long> filteredSeeds = new ArrayList<>();

    public static final EndCity END_CITY = new EndCity(MCVersion.v1_16);

    public static int floor(double double1) {
        int integer3 = (int)double1;
        return (double1 < integer3) ? (integer3 - 1) : integer3;
    }

    public static void filterStructureSeeds(int threadCount, long startSeed, int offset, long END_SEED) {
        ChunkRand chunkRand = new ChunkRand();
        BuriedTreasure buriedTreasure = new BuriedTreasure(MCVersion.v1_16_1);
        Village village = new Village(MCVersion.v1_16_1);
        BastionRemnant bastionRemnant = new BastionRemnant(MCVersion.v1_16_1);
        Fortress fortress = new Fortress(MCVersion.v1_16_1);
        EndCity endCity = new EndCity(MCVersion.v1_16_1);

        for (long structureSeed = startSeed + offset; structureSeed < END_SEED; structureSeed += threadCount) {
            CPos treasure;
            boolean tCheck = false;
            for (int chunkX = -2; chunkX < 1; chunkX++) {
                for (int chunkZ = -2; chunkZ < 1; chunkZ++) {
                    treasure = buriedTreasure.getInRegion(structureSeed, chunkX, chunkZ, chunkRand);
                    if (treasure != null) tCheck = true;
                }

                if (tCheck) break;
            }
            if (!tCheck) continue;

            CPos village1 = village.getInRegion(structureSeed, 0, 0, chunkRand);
            if (village1.distanceTo(new Vec3i(0, 0, 0), DistanceMetric.EUCLIDEAN_SQ) > 25.0) continue;

            CPos bastion1 = bastionRemnant.getInRegion(structureSeed, 0, 0, chunkRand);
            CPos fortress1 = fortress.getInRegion(structureSeed, -1, 0, chunkRand);
            if (fortress1 == null || bastion1 == null) continue;
            BastionRemnantProperties bastionRemnantProperties = new BastionRemnantProperties(structureSeed, bastion1);
            if (bastionRemnantProperties.getType(chunkRand) != 2) continue;
            double bDistance = bastion1.distanceTo(new Vec3i(0, 0, 0), DistanceMetric.EUCLIDEAN_SQ);
            if (bDistance > 1.1) continue;
            double fDistance = fortress1.distanceTo(new Vec3i(0, 0, 0), DistanceMetric.EUCLIDEAN_SQ);
            if (fDistance > 150.0) continue;

            NetherBiomeSource netherBiomeSource = new NetherBiomeSource(MCVersion.v1_16_1, structureSeed);
            if (!bastionRemnant.canSpawn(bastion1.getX(), bastion1.getZ(), netherBiomeSource)) continue;


            DragonGatewayProperties gatewayProperties = new DragonGatewayProperties(structureSeed);
            ArrayList<Integer> gatewayOrder = gatewayProperties.getGatewayOrder();
            int index = gatewayOrder.get(19);
            int gx = floor(1000.0 * Math.cos(2.0 * (-3.141592653589793 + 0.15707963267948966 * index)));
            int gz = floor(1000.0 * Math.sin(2.0 * (-3.141592653589793 + 0.15707963267948966 * index)));
            BPos gBPos = new BPos(gx, 0, gz);
            RPos gRPos = gBPos.toChunkPos().toRegionPos(20);

            CPos city1 = new CPos(0,0);

            boolean cCheck = false;
            for (int rx = gRPos.getX() - 1; rx < gRPos.getX() + 2; rx++) {
                for (int rz = gRPos.getX() - 1; rz < gRPos.getX() + 2; rz++) {
                    city1 = endCity.getInRegion(structureSeed, rx, rz, chunkRand);
                    if (city1.toBlockPos().distanceTo(new Vec3i(gx, 0, gz), DistanceMetric.EUCLIDEAN) < 64) {
                        cCheck = true;
                        break;
                    }
                }
                if (cCheck) break;
            }

            if (!cCheck) continue;

            RegionStructure.Data<?> city = END_CITY.at(city1.getX(), city1.getZ());
            if (!endCity.canStart((RegionStructure.Data<EndCity>) city, structureSeed, chunkRand)) continue;
            System.out.println(structureSeed);
            filteredSeeds.add(structureSeed);
        }
    }
}
