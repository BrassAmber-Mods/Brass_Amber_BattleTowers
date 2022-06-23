package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BattleTowersConfig;
import com.BrassAmber.ba_bt.init.BTBlockEntityTypes;
import com.BrassAmber.ba_bt.init.BTBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.BrassAmber.ba_bt.BattleTowersConfig.landTowerMobs;
import static com.BrassAmber.ba_bt.BattleTowersConfig.oceanTowerMobs;

public class BTUtil {



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

    public static double horizontalDistanceTo(Entity self, BlockPos end) {
        double dX = Math.abs(self.getX() - end.getX());
        double dZ = Math.abs(self.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double horizontalDistanceTo(BlockPos origin, BlockPos end) {
        double dX = Math.abs(origin.getX() - end.getX());
        double dZ = Math.abs(origin.getZ() - end.getZ());
        return Math.sqrt(dX * dX + dZ * dZ);
    }

    public static double distanceTo2D(double side, double side2) {
        return  Math.sqrt(side * side + side2 * side2);
    }

    public static double distanceTo3D(Entity self, double targetX, double targetY, double targetZ) {
        double dXZ = horizontalDistanceTo(self, targetX, targetZ);
        double dY = self.getY() - targetY;
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, Entity entity) {
        double dXZ = horizontalDistanceTo(self, entity);
        double dY = self.getY() - entity.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(Entity self, BlockPos end) {
        double dXZ = horizontalDistanceTo(self, end);
        double dY = self.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
    }

    public static double distanceTo3D(BlockPos origin, BlockPos end) {
        double dXZ = horizontalDistanceTo(origin, end);
        double dY = origin.getY() - end.getY();
        return Math.sqrt(Math.abs(dXZ * dXZ + dY * dY));
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
