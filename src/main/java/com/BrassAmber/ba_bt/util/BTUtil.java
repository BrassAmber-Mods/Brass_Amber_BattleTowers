package com.BrassAmber.ba_bt.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

public class BTUtil {

    public static final List<String> landTowerNames;
    public static final List<List<ResourceKey<Biome>>> landTowerBiomes;
    public static final List<List<Integer>> towerSpawnerAmounts;
    public static final List<List<Integer>> towerChestUnlocking;

    static {
        landTowerNames = List.of("Land", "Overgrown", "Sandy", "Icy");

        landTowerBiomes = List.of(
                // Land
                List.of(
                        Biomes.FLOWER_FOREST,
                        Biomes.BIRCH_FOREST,
                        Biomes.DARK_FOREST,
                        Biomes.OLD_GROWTH_BIRCH_FOREST,
                        Biomes.WINDSWEPT_FOREST,
                        Biomes.MEADOW,
                        Biomes.PLAINS,
                        Biomes.TAIGA,
                        Biomes.OLD_GROWTH_PINE_TAIGA,
                        Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                        Biomes.SAVANNA,
                        Biomes.SUNFLOWER_PLAINS,
                        Biomes.GROVE,
                        Biomes.WINDSWEPT_HILLS,
                        Biomes.WINDSWEPT_GRAVELLY_HILLS
                ),
                // Overgrown
                List.of(
                        Biomes.SWAMP,
                        Biomes.JUNGLE,
                        Biomes.BAMBOO_JUNGLE,
                        Biomes.SPARSE_JUNGLE
                ),
                // Sandy
                List.of(
                        Biomes.DESERT
                )
        );

        towerSpawnerAmounts = Arrays.asList(
                Arrays.asList(2, 2, 2, 2, 3, 3, 3, 4),
                Arrays.asList(2, 2, 2, 3, 3, 3, 4, 4),
                Arrays.asList(2, 2, 3, 3, 3, 4, 4, 4),
                Arrays.asList(2, 3, 3, 3, 3, 4, 4, 5),
                Arrays.asList(3, 3, 3, 3, 4, 4, 4, 5),
                Arrays.asList(3, 3, 3, 4, 4, 4, 5, 5)
        );
        towerChestUnlocking = Arrays.asList(
                Arrays.asList(6, 14, 21),
                Arrays.asList(9, 23),
                Arrays.asList(10, 25),
                Arrays.asList(11, 27),
                Arrays.asList(12, 29),
                Arrays.asList(13, 31)
        );
    }

    /**
     * Returns the squared horizontal distance as a positive double.
     */
    public static double horizontalDistanceToSqr(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.abs(dX * dX + dZ * dZ);
    }

    public static double horizontalDistanceToSqr(Entity self, Entity target) {
        double dX = self.getX() - target.getX();
        double dZ = self.getZ() - target.getZ();
        return Math.abs(dX * dX + dZ * dZ);
    }

    /**
     * Returns the horizontal distance as a positive double.
     */
    public static double horizontalDistanceTo(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.sqrt(Math.abs(dX * dX + dZ * dZ));
    }

    public static double horizontalDistanceTo(Entity self, Entity entity) {
        double dX = Math.abs(self.getX() - entity.getX());
        double dZ = Math.abs(self.getZ() - entity.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double horizontalDistanceTo(BlockPos origin, BlockPos end) {
        double dX = Math.abs(origin.getX() - end.getX());
        double dZ = Math.abs(origin.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static void doCommand(Entity self, String command) {

        self.level.getServer().getCommands().performCommand(self.createCommandSourceStack().withPermission(4), command);
    }

    public static void doNoOutputCommand(Entity self, String command) {
        self.level.getServer().getCommands().performCommand(self.createCommandSourceStack().withPermission(4).withSuppressedOutput(), command);
    }

    public static void doNoOutputPostionedCommand(Entity self, String command, Vec3 vec) {
        self.level.getServer().getCommands().performCommand(self.createCommandSourceStack().withPermission(4).withPosition(vec), command);
    }

}
