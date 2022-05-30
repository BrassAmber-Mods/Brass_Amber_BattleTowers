package com.BrassAmber.ba_bt.entity.block;

import com.BrassAmber.ba_bt.util.GolemType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class BTLandObelisk extends BTAbstractObelisk {
    public BTLandObelisk(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public BTLandObelisk(GolemType golemType, Level level) {
        super(golemType, level);
    }
}
