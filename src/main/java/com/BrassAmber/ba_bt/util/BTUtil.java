package com.BrassAmber.ba_bt.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

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
        double dX = self.getX() - entity.getX();
        double dZ = self.getZ() - entity.getZ();
        return Math.sqrt(Math.abs(dX * dX + dZ * dZ));
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
