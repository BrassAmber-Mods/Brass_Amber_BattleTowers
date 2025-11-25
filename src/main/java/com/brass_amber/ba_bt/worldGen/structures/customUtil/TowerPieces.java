package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTStructurePieces;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.slf4j.Logger;

import java.util.*;

import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerGenInfo.*;


public class TowerPieces {
    static final Logger LOGGER = LogUtils.getLogger();

    public static void generateTower(StructureTemplateManager templateManager, BlockPos blockPos, List<TowerPiece> towerPieces, RandomSource randomSource, String towerName, String variant) {
        LOGGER.debug("Tower name: {} Variant: {}", towerName, variant);
        TowerGenInfo towerGenInfo = getTypeForName(towerName);
        Direction baseDirection = Direction.Plane.HORIZONTAL.getRandomDirection(randomSource);
        Direction flippedDirection = baseDirection.getOpposite();
        
        String baseName = towerName + "/";
        String variantName = "";

        Pair<WeightedPiece, String> pieceResult;
        WeightedPiece piece;

        // offset tower to account for size of tower pieces (29/29)
        blockPos = switch (towerGenInfo) {
            case CORE -> blockPos.offset(-16, 0, -16).atY(-60);
            default -> blockPos.offset(-14, 0, -14);
        };

        // Add tower shell to list first so it is generated first
        pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.START, variant, randomSource);
        if (pieceResult != null) {
            piece = pieceResult.getFirst();
            variantName = baseName + pieceResult.getSecond() + "/";
            towerPieces.add(
                    new TowerPiece(
                            templateManager, variantName + piece.name(),
                            blockPos.offset(piece.offset()), baseDirection, piece.structureProcessors(), false
                    )
            );
        }

        int floorHeight = getFloorHeight(towerGenInfo);

        blockPos = switch (towerGenInfo) {
            case CORE -> blockPos.offset(2, 1, 2);
            case OCEAN -> blockPos.offset(0, floorHeight, 0);
            default -> blockPos.offset(0, towerPieces.get(0).getHeight(), 0);
        };

        // Always add normal shell
        for (int i = 0; i < 8; i++) {
            pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.SHELL, "normal", randomSource);
            if (pieceResult != null) {
                piece = pieceResult.getFirst();
                variantName = baseName + pieceResult.getSecond() + "/";
                towerPieces.add(
                        new TowerPiece(
                                templateManager, variantName + piece.name(),
                                blockPos.offset(0, i * floorHeight, 0).offset(piece.offset()),
                                baseDirection, piece.structureProcessors(), (i & 1) == 0
                        )
                );
            }
        }

        // Add shell variant changes (if variant)
        if (!variant.equals("normal")) {
            for (int i = 0; i < 8; i++) {
                if (i == 0 && variant.equals("overgrown")) {
                    continue;
                }
                pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.SHELL, variant, randomSource);
                if (pieceResult != null) {
                    piece = pieceResult.getFirst();
                    variantName = baseName + pieceResult.getSecond() + "/";
                    towerPieces.add(
                            new TowerPiece(
                                    templateManager, variantName + piece.name(),
                                    blockPos.offset(0, i * floorHeight, 0).offset(piece.offset()),
                                    baseDirection,  piece.structureProcessors(), (i & 1) == 0
                            )
                    );
                }
            }
        }

        pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.END, variant, randomSource);
        if (pieceResult != null) {
            piece = pieceResult.getFirst();
            variantName = baseName + pieceResult.getSecond() + "/";
            towerPieces.add(
                    new TowerPiece(
                            templateManager, variantName + piece.name(),
                            blockPos.offset(0, floorHeight * 8, 0).offset(piece.offset()),
                            baseDirection,  piece.structureProcessors(), false
                    )
            );
        }

        LOGGER.debug("{} added shell to piece list", towerName);


        String roomName = "";
        Map<String, Integer> usedRooms = new HashMap<>();
        BlockPos roomPos;
        int failSafe;

        pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.FIRST_FLOOR, variant, randomSource);
        if (pieceResult != null) {
            piece = pieceResult.getFirst();
            variantName = baseName + pieceResult.getSecond() + "/";
            switch (towerGenInfo) {
                case OCEAN -> towerPieces.add(
                        new TowerPiece(
                                templateManager, variantName + "rooms/" + piece.name(),
                                blockPos.offset(piece.offset()), 
                                flippedDirection, piece.structureProcessors(), false
                        )
                );
                case CORE -> {

                }
                default -> towerPieces.add(
                        new TowerPiece(
                                templateManager, variantName + "rooms/" + piece.name(),
                                blockPos.offset(piece.offset()), 
                                baseDirection, piece.structureProcessors(), false
                        )
                );
            }
        }


        // LOGGER.debug("{} placed start floor", towerName);
        // Add random internal rooms (skipping entry floor)
        for (int i = 1; i < (towerGenInfo == CORE ? 1 : 7); i++) {
            failSafe = 0;
            // Get random room
            do {
                failSafe ++;
                if (failSafe > 60) {
                    piece = towerGenInfo.getVariantPieces().get("normal").middleFloorPieces().get(0);
                    roomName = piece.name();
                    break;
                }

                pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.MIDDLE_FLOOR, variant, randomSource);
                piece = pieceResult.getFirst();
                variantName = baseName + pieceResult.getSecond() + "/";
                roomName = piece.name();

                if (usedRooms.isEmpty()) {
                    usedRooms.put(roomName, 1);
                }
                // LOGGER.debug("{} in do while {}", towerName, failSafe);
            }
            while (usedRooms.getOrDefault(roomName, 0) == 2);

            if (usedRooms.getOrDefault(roomName, 0) < 2) {
                usedRooms.put(roomName, usedRooms.getOrDefault(roomName, 0)+1);
            }

            roomPos = blockPos;

            towerPieces.add(
                    new TowerPiece(
                            templateManager, variantName + "rooms/" + piece.name(),
                            roomPos.offset(0, i * floorHeight, 0).offset(piece.offset()), 
                            baseDirection, piece.structureProcessors(), (i & 1) == 0
                    )
            );

        }

        if (towerGenInfo != CORE) {
            pieceResult = towerGenInfo.getRandomVariantPieceFrom(PieceListType.FINAL_FLOOR, variant, randomSource);
            if (pieceResult != null) {
                piece = pieceResult.getFirst();
                variantName = baseName + pieceResult.getSecond() + "/";
                towerPieces.add(
                        new TowerPiece(
                                templateManager, variantName + "rooms/" + piece.name(),
                                blockPos.offset(0, floorHeight * 7, 0).offset(piece.offset()),
                                baseDirection, piece.structureProcessors(), false
                        )
                );
            }

        }

        LOGGER.debug("{} added floors to piece list", towerName);
    }

    public static class TowerPiece extends TemplateStructurePiece {

        public TowerPiece(StructureTemplateManager templateManager, String templateName, BlockPos blockPos, Direction direction,  List<StructureProcessor> processors, boolean mirrored) {
            super(
                    BTStructurePieces.TOWER_PIECE.get(), 0, templateManager,
                    makeLocation(templateName), templateName,
                    makeSettings(Mirror.NONE, mirrored ? Rotation.CLOCKWISE_180 : Rotation.NONE),
                    blockPos
            );
            this.setOrientation(mirrored ? direction.getOpposite() : direction);
            this.addProcessors(processors);
        }

        public TowerPiece(StructureTemplateManager templateManager, CompoundTag compoundTag) {
            super(
                    BTStructurePieces.TOWER_PIECE.get(), 0, templateManager,
                    makeLocation(compoundTag.getString("Template")),
                    compoundTag.getString("Template"),
                    makeSettings(Mirror.valueOf(compoundTag.getString("Mi")), Rotation.valueOf(compoundTag.getString("Rot"))),
                    new BlockPos(compoundTag.getInt("TPX"), compoundTag.getInt("TPY"), compoundTag.getInt("TPZ"))
            );
            int i = compoundTag.getInt("O");
            this.setOrientation(i == -1 ? null : Direction.from2DDataValue(i));
            this.addProcessors(StructureProcessorType.LIST_CODEC.parse(NbtOps.INSTANCE, compoundTag.get("Processors")).result().get().get().list());
        }

        public int getHeight() {
            return this.template.getBoundingBox(this.placeSettings(), this.templatePosition).getYSpan();
        }

        public ResourceLocation makeTemplateLocation() {
            return makeLocation(this.templateName);
        }

        protected static ResourceLocation makeLocation(String templateName) {
            return new ResourceLocation(BABattleTowers.MOD_ID, templateName);
        }


        protected static StructurePlaceSettings makeSettings(Mirror mirror, Rotation rotation) {
            return (new StructurePlaceSettings()).setMirror(mirror).setRotation(rotation).setRotationPivot(new BlockPos(14, 0, 14)).setIgnoreEntities(false).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        protected void addProcessors(List<StructureProcessor> processors) {
            for (StructureProcessor processor : processors) {
                this.placeSettings.addProcessor(processor);
            }
        }


        protected void addAdditionalSaveData(StructurePieceSerializationContext serializationContext, CompoundTag compoundTag) {
            super.addAdditionalSaveData(serializationContext, compoundTag);
            StructureProcessorList processorList = new StructureProcessorList(this.placeSettings.getProcessors());
            compoundTag.put(
                    "Processors", StructureProcessorType.LIST_CODEC.encodeStart(NbtOps.INSTANCE, Holder.direct(processorList)).getOrThrow(false, LOGGER::error)
            );
        }

        @Override
        protected void handleDataMarker(String string, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, RandomSource randomSource, BoundingBox boundingBox) {

        }
    }
}
