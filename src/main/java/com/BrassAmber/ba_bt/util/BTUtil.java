package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class BTUtil {

    public static java.util.function.Supplier<SoundEvent> getTowerMusic(GolemType type) {
        return switch (type) {
            case OCEAN -> () -> BTSoundEvents.MUSIC_OCEAN_TOWER;
            case CORE -> () -> BTSoundEvents.MUSIC_CORE_TOWER;
            case NETHER -> () -> BTSoundEvents.MUSIC_NETHER_TOWER;
            case END -> () -> BTSoundEvents.MUSIC_END_TOWER;
            case SKY -> () -> BTSoundEvents.MUSIC_SKY_TOWER;
            case CITY -> () -> BTSoundEvents.MUSIC_CITY;
            default -> () -> BTSoundEvents.MUSIC_LAND_TOWER;
        };
    }

    public static Item itemByString(String id) {
        return ForgeRegistries.ITEMS.getHolder(ResourceLocation.tryParse(id)).orElseGet(() -> Holder.direct(Items.DIRT)).value();
    }

    /**
     * Returns the squared horizontal distance as a positive double.
     */
    public static double sqrDistanceTo2D(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.abs(dX * dX + dZ * dZ);
    }

    public static double sqrDistanceTo2D(Entity self, Entity target) {
        double dX = self.getX() - target.getX();
        double dZ = self.getZ() - target.getZ();
        return Math.abs(dX * dX + dZ * dZ);
    }

    public static int chunkDistanceTo(ChunkPos start, ChunkPos end) {
        return Mth.floor(distanceTo2D(start.x - end.x, start.z - end.z));
    }

    /**
     * Returns the horizontal distance as a positive double.
     */
    public static double distanceTo2D(Entity self, double targetX, double targetZ) {
        double dX = self.getX() - targetX;
        double dZ = self.getZ() - targetZ;
        return Math.sqrt(Math.abs(dX * dX + dZ * dZ));
    }

    public static double distanceTo2D(Entity self, Entity entity) {
        double dX = Math.abs(self.getX() - entity.getX());
        double dZ = Math.abs(self.getZ() - entity.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(Entity self, BlockPos end) {
        double dX = Math.abs(self.getX() - end.getX());
        double dZ = Math.abs(self.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(BlockPos origin, BlockPos end) {
        double dX = Math.abs(origin.getX() - end.getX());
        double dZ = Math.abs(origin.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(double side, double side2) {
        return  Math.sqrt(side * side + side2 * side2);
    }


    public static double distanceTo3D(Entity self, double targetX, double targetY, double targetZ) {
        double dXZ = distanceTo2D(self, targetX, targetZ);
        double dY = self.getY() - targetY;
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, Entity entity) {
        double dXZ = distanceTo2D(self, entity);
        double dY = self.getY() - entity.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, BlockPos end) {
        double dXZ = distanceTo2D(self, end);
        double dY = self.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(BlockPos origin, BlockPos end) {
        double dXZ = distanceTo2D(origin, end);
        double dY = origin.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static void removeBodyOfWater(BlockPos start, Level level) {
        Set<BlockPos> waterPositions = new HashSet<>();
        int recursion = 0;
        removeBodyOWater(waterPositions, start, recursion, level);

        waterPositions.forEach((pos) -> level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0));
    }

    public static void removeBodyOWater(Set<BlockPos> storage, BlockPos position, int recursion, Level level) {
        if(!level.isWaterAt(position) || recursion == 250) {
            return;
        }
        if(!storage.contains(position)) {
            storage.add(position);
        } else {
            return;
        }
        removeBodyOWater(storage, position.north(), recursion + 1, level);
        removeBodyOWater(storage, position.east(), recursion + 1, level);
        removeBodyOWater(storage, position.south(), recursion + 1, level);
        removeBodyOWater(storage, position.west(), recursion + 1, level);
        removeBodyOWater(storage, position.below(), recursion + 1, level);
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
