package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructurePieces;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraftforge.common.world.PieceBeardifierModifier;
import org.slf4j.Logger;

import java.util.*;

import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.*;


public class TowerPieces {
    static final Logger LOGGER = LogUtils.getLogger();

    public static void generateTower(StructureTemplateManager templateManager, BlockPos blockPos, Rotation rotation, List<TowerPiece> towerPieces, RandomSource randomSource, String towerName, String variant) {
        LOGGER.debug("Tower name: {} Variant: {}", towerName, variant);
        TowerGenInfo towerGenInfo = TowerGenInfo.getTypeForName(towerName);

        // decide on wall/stair processors (different for each variant)
        List<StructureProcessor> shellProcessors = TowerGenInfo.getShellProcessors(towerGenInfo, variant);
        List<StructureProcessor> variantProcessors = TowerGenInfo.getVariantProcessors(towerGenInfo, variant);
        ArrayList<StructureProcessor> oddFloorProcessors = new ArrayList<>(shellProcessors);
        //if (towerGenInfo == TowerGenInfo.OCEAN) {
        //    oddFloorProcessors.add(BONE_REMOVE);
        //}

        // offset tower to account for size of tower pieces (29/29)
        blockPos = blockPos.offset(-14 , 0, -14);
        blockPos = switch (towerGenInfo) {
            case CORE -> blockPos.offset(-16, 0, -16).atY(-60);
            default -> blockPos.offset(-14, 0, -14);
        };

        // Add tower shell to list first so it is generated first
        towerPieces.add(new StartPiece(templateManager, "start", towerName, blockPos, rotation, ""));
        int floorHeight = TowerGenInfo.getFloorHeight(towerGenInfo);
        int doubledFloorHeight = floorHeight * 2;

        blockPos = switch (towerGenInfo) {
            case CORE -> blockPos.offset(2, 1, 2);
            case OCEAN -> blockPos.offset(0, floorHeight, 0);
            default -> blockPos.offset(0, towerPieces.get(0).getHeight(), 0);
        };

        for (int i = 0; i < 4; i++) {
            towerPieces.add(new ShellPiece(templateManager, "shell", towerName, blockPos.offset(0, i*doubledFloorHeight, 0), rotation.getRotated(Rotation.CLOCKWISE_180), "", shellProcessors, variantProcessors));
            towerPieces.add(new ShellPiece(templateManager, "shell", towerName, blockPos.offset(0, floorHeight + i*doubledFloorHeight, 0), rotation, "", oddFloorProcessors, variantProcessors));
        }

        List<StructureProcessor> endShellProcessors;

        endShellProcessors = switch (towerGenInfo) {
            case CORE -> List.of(CORE_ROOF, CORE_FLOOR, CORE_WALL);
            default -> List.of();
        };
        towerPieces.add(new ShellPiece(templateManager, "end", towerName, blockPos.offset(0,floorHeight*8, 0), rotation, "", endShellProcessors, List.of()));

        LOGGER.debug("{} placed shell", towerName);
        // Add shell variant changes (if variant)
        if (!variant.equals("normal")) {
            // TODO add shell variation
        }

        String roomName;
        Map<String, Integer> usedRooms = new HashMap<>();
        Rotation roomRotation;
        BlockPos roomPos;
        int failSafe;

        List<StructureProcessor> startFloorProcessors;

        startFloorProcessors =  switch (towerGenInfo) {
            case LAND -> List.of(LAND_NORMAL_FLOOR);
            case OCEAN -> List.of(WATERLOGGED);
            default -> List.of();
        };

        switch (towerGenInfo) {
            case OCEAN -> towerPieces.add(new RoomPiece(templateManager, "start_floor", towerName, blockPos, rotation.getRotated(Rotation.CLOCKWISE_180), startFloorProcessors,0));
            default -> towerPieces.add(new RoomPiece(templateManager, "start_floor", towerName, blockPos, rotation, startFloorProcessors,0));
        }
        // LOGGER.debug("{} placed start floor", towerName);
        // Add random internal rooms (skipping entry floor)
        for (int i = 1; i < 7; i++) {
            failSafe = 0;
            // Get random room
            do {
                roomName = TowerGenInfo.getRandomRoom(towerGenInfo, randomSource);
                failSafe ++;
                if (failSafe > 80) {
                    roomName = towerGenInfo.getRooms().get(0);
                    break;
                }
                if (usedRooms.isEmpty()) {
                    usedRooms.put(roomName, 1);
                }
                // LOGGER.debug("{} in do while {}", towerName, failSafe);
            }
            while (usedRooms.getOrDefault(roomName, 0) == 2);

            if (usedRooms.getOrDefault(roomName, 0) < 2) {
                usedRooms.put(roomName, usedRooms.getOrDefault(roomName, 0)+1);
            }

            if ((i & 1) == 0) {
                // even
                roomRotation = rotation.getRotated(Rotation.CLOCKWISE_180);
            } else {
                // odd
                roomRotation = rotation;
            }
            roomPos = blockPos;

            towerPieces.add(new RoomPiece(
                    templateManager, roomName, towerName,
                    roomPos.offset(0, i*floorHeight, 0), roomRotation,
                    TowerGenInfo.getRoomProcessors(towerGenInfo, roomName), i
            ));

        }

        List<StructureProcessor> endFloorProcessors;

        endFloorProcessors =  switch (towerGenInfo) {
            case LAND, CORE -> List.of(LAND_CARPET_PLACER);
            case OCEAN -> List.of(OCEAN_NORMAL_FLOOR);
            default -> List.of();
        };

        towerPieces.add(new RoomPiece(templateManager, "end_floor", towerName, blockPos.offset(0, floorHeight*7, 0), rotation, endFloorProcessors, 8));

        LOGGER.debug("{} placed floors", towerName);
    }

    public static class TowerPiece extends TemplateStructurePiece {
        protected String towerName;  // Used to find the piece for the correct tower type (Land, Ocean, Etc)
        protected String variant;  // Used to find the pieces for the tower variant. If no variant this will be ""
        public TowerPiece(StructureTemplateManager templateManager, String templateName, String towerName, BlockPos blockPos, Rotation rotation, String variant) {
            super(BTStructurePieces.TOWER_PIECE.get(), 0, templateManager, makeLocation(towerName, templateName, variant), templateName, makeSettings(rotation), blockPos);
            this.towerName = towerName;
            this.variant = variant;
        }

        public TowerPiece(StructureTemplateManager templateManager, CompoundTag compoundTag) {
            super(BTStructurePieces.TOWER_PIECE.get(), compoundTag, templateManager,
                    (resourceLocation) -> makeSettings(Rotation.valueOf(compoundTag.getString("Rotation")))
            );
            this.towerName = compoundTag.getString("TowerName");
            this.variant = compoundTag.getString("Variant");

        }

        public int getHeight() {
            return this.template.getBoundingBox(this.placeSettings(), this.templatePosition).getYSpan();
        }

        protected ResourceLocation makeTemplateLocation() {
            return makeLocation(this.towerName, this.templateName, this.variant);
        }

        protected static ResourceLocation makeLocation(String towerName, String templateName, String variant) {
            // Example: land_tower/normal/entry
            if (variant == null || variant.isEmpty()) {
                return new ResourceLocation(BABattleTowers.MOD_ID, towerName + "/" + templateName);
            }
            return new ResourceLocation(BABattleTowers.MOD_ID, towerName + "/" + variant  + "/" + templateName);
        }


        protected static StructurePlaceSettings makeSettings(Rotation rotation) {
            return (new StructurePlaceSettings()).setRotationPivot(new BlockPos(14, 0, 14)).setIgnoreEntities(false).setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK).setFinalizeEntities(true);
        }

        protected void addAdditionalSaveData(StructurePieceSerializationContext serializationContext, CompoundTag compoundTag) {
            super.addAdditionalSaveData(serializationContext, compoundTag);
            compoundTag.putString("Rotation", this.placeSettings.getRotation().name());
            compoundTag.putString("TowerName", this.towerName);
            compoundTag.putString("Variant", this.variant);
        }

        @Override
        protected void handleDataMarker(String string, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

        }
    }

    public static class ShellPiece extends TowerPiece {

        public ShellPiece(
                StructureTemplateManager templateManager, String templateName, String towerName,
                BlockPos blockPos, Rotation rotation, String variant,
                List<StructureProcessor> shellProcessors, List<StructureProcessor> variantProcessors
        ) {
            super(templateManager, templateName, towerName, blockPos, rotation, variant);

            for (StructureProcessor processor : shellProcessors) {
                this.placeSettings.addProcessor(processor);
            }
            for (StructureProcessor processor : variantProcessors) {
                this.placeSettings.addProcessor(processor);
            }
        }

    }
    public static class StartPiece extends TowerPiece implements PieceBeardifierModifier {

        public StartPiece(
                StructureTemplateManager templateManager, String templateName, String towerName,
                BlockPos blockPos, Rotation rotation, String variant
        ) {
            super(templateManager, templateName, towerName, blockPos, rotation, variant);

            this.placeSettings.addProcessor(BASE_PROTECTED);

            switch (TowerGenInfo.getTypeForName(towerName)) {
                case OCEAN -> this.placeSettings.addProcessor(OCEAN_NORMAL);
                default -> this.placeSettings.addProcessor(LAND_WALL);
            }

        }

        @Override
        public BoundingBox getBeardifierBox() {
            return switch (TowerGenInfo.getTypeForName(towerName)) {
                case LAND -> this.getBoundingBox();
                default -> this.getBoundingBox();
            };
        }

        @Override
        public TerrainAdjustment getTerrainAdjustment() {
            return switch (TowerGenInfo.getTypeForName(towerName)) {
                case LAND -> TerrainAdjustment.BURY;
                default -> TerrainAdjustment.NONE;
            };
        }

        @Override
        public int getGroundLevelDelta() {
            Random rand = new Random();
            return switch (TowerGenInfo.getTypeForName(towerName)) {
                case LAND -> rand.nextInt(0,3);
                default -> 0;
            };
        }
    }


    public static class RoomPiece extends TowerPiece {
        public int towerlayer;
        public RoomPiece(StructureTemplateManager templateManager, String templateName, String towerName, BlockPos blockPos, Rotation rotation, List<StructureProcessor> proccessors, int towerLayer) {
            super(templateManager, templateName, towerName, blockPos, rotation, "normal");
            for (StructureProcessor processor : proccessors) {
                this.placeSettings.addProcessor(processor);
            }
            this.towerlayer = towerLayer;
        }

        public RoomPiece(StructureTemplateManager templateManager, CompoundTag compoundTag) {
            super(templateManager, compoundTag);
            this.towerlayer = compoundTag.getInt("TowerLayer");
        }


    }
}
