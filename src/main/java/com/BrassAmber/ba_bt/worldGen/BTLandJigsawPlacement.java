package com.BrassAmber.ba_bt.worldGen;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.core.*;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Predicate;


public class BTLandJigsawPlacement {
    private static final Logger LOGGER = BrassAmberBattleTowers.LOGGER;

    public static Optional<PieceGenerator<JigsawConfiguration>> addPieces(PieceGeneratorSupplier.Context<JigsawConfiguration> context, BTLandJigsawPlacement.PieceFactory pieceFactory, BlockPos blockPos, boolean placeAtHeightMap, boolean isWatered, boolean isSandy) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        RegistryAccess registryaccess = context.registryAccess();
        JigsawConfiguration jigsawconfiguration = context.config();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureManager structuremanager = context.structureManager();
        LevelHeightAccessor levelheightaccessor = context.heightAccessor();
        StructureFeature.bootstrap();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = jigsawconfiguration.startPool().value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            PoolElementStructurePiece poolelementstructurepiece = pieceFactory.create(structuremanager, structurepoolelement, blockPos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, blockPos, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            int heightChange = 0;

            if (isWatered) {
                heightChange = 5;
            }
            else if (isSandy) {
                if (worldgenrandom.nextInt(100) < 25) {
                    heightChange = worldgenrandom.nextInt(8, 16);
                } else {
                    heightChange = 5;
                }
            }
            if (placeAtHeightMap) {
                k = blockPos.getY() + chunkgenerator.getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor) - 5;
            } else {
                k = blockPos.getY() -heightChange;
            }
            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            return Optional.of((p_210282_, p_210283_) -> {
                List<PoolElementStructurePiece> list = Lists.newArrayList();
                list.add(poolelementstructurepiece);
                if (jigsawconfiguration.maxDepth() > 0) {
                    int i1 = 120;
                    AABB aabb = new AABB(i - i1, k - i1, j - i1, i + i1 + 1, k + i1 + 1, j + i1 + 1);
                    BTLandJigsawPlacement.Placer jigsawplacement$placer = new BTLandJigsawPlacement.Placer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom);
                    jigsawplacement$placer.placing.addLast(new BTLandJigsawPlacement.PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), 0));

                    while(!jigsawplacement$placer.placing.isEmpty()) {
                        BTLandJigsawPlacement.PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
                        jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth,  levelheightaccessor);
                    }

                    list.forEach(p_210282_::addPiece);
                }
            });
        }
    }

    public static void addPieces(RegistryAccess registryAccess, PoolElementStructurePiece structurePiece, int maxDepth, BTLandJigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolElementStructurePiece> pieces, Random random, LevelHeightAccessor heightAccessor) {
        Registry<StructureTemplatePool> registry = registryAccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        BTLandJigsawPlacement.Placer jigsawplacement$placer = new BTLandJigsawPlacement.Placer(registry, maxDepth, pieceFactory, chunkGenerator, structureManager, pieces, random);
        jigsawplacement$placer.placing.addLast(new BTLandJigsawPlacement.PieceState(structurePiece, new MutableObject<>(Shapes.INFINITY), 0));

        while(!jigsawplacement$placer.placing.isEmpty()) {
            BTLandJigsawPlacement.PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
            jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, heightAccessor);
        }

    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager manager, StructurePoolElement poolElement, BlockPos blockPos, int groundLevel, Rotation rotation, BoundingBox boundingBox);
    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int depth;

        PieceState(PoolElementStructurePiece pieceIn, MutableObject<VoxelShape> freeIn, int depthIn) {
            this.piece = pieceIn;
            this.free = freeIn;
            this.depth = depthIn;
        }
    }

    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final BTLandJigsawPlacement.PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final Random random;
        final Deque<BTLandJigsawPlacement.PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructureTemplatePool> poolsIn, int maxDepth, BTLandJigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolElementStructurePiece> structurePieces, Random random) {
            this.pools = poolsIn;
            this.maxDepth = maxDepth;
            this.factory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.pieces = structurePieces;
            this.random = random;
        }

        void tryPlacingChildren(PoolElementStructurePiece structurePiece, MutableObject<VoxelShape> mutableObject, int currentDepth, LevelHeightAccessor heightAccessor) {
            StructurePoolElement structurepoolelement = structurePiece.getElement();
            BlockPos blockpos = structurePiece.getPosition();
            Rotation rotation = structurePiece.getRotation();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean startIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            BoundingBox boundingbox = structurePiece.getBoundingBox();
            int bb_y = boundingbox.minY();

            label139:
            for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                Direction structureBlockFacing = JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state);
                BlockPos structureBlockPos = structuretemplate$structureblockinfo.pos;
                BlockPos inFrontSBlockPos = structureBlockPos.relative(structureBlockFacing);
                int sBlockRelativeY = structureBlockPos.getY() - bb_y;
                int landHeight = -1;
                ResourceLocation poolLocation = new ResourceLocation(structuretemplate$structureblockinfo.nbt.getString("pool"));
                Optional<StructureTemplatePool> optional = this.pools.getOptional(poolLocation);
                if (optional.isPresent() && (optional.get().size() != 0 || Objects.equals(poolLocation, Pools.EMPTY.location()))) {
                    ResourceLocation poolFallback = optional.get().getFallback();
                    Optional<StructureTemplatePool> optional1 = this.pools.getOptional(poolFallback);
                    if (optional1.isPresent() && (optional1.get().size() != 0 || Objects.equals(poolFallback, Pools.EMPTY.location()))) {
                        boolean insideBBFlag = boundingbox.isInside(inFrontSBlockPos);
                        MutableObject<VoxelShape> mutableobject1;
                        if (insideBBFlag) {
                            mutableobject1 = mutableobject;
                            if (mutableobject.getValue() == null) {
                                mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
                            }
                        } else {
                            mutableobject1 = mutableObject;
                        }

                        List<StructurePoolElement> shuffledElements = Lists.newArrayList();
                        if (currentDepth != this.maxDepth) {
                            shuffledElements.addAll(optional.get().getShuffledTemplates(this.random));
                        }

                        shuffledElements.addAll(optional1.get().getShuffledTemplates(this.random));

                        for(StructurePoolElement shuffledElement : shuffledElements) {
                            if (shuffledElement == EmptyPoolElement.INSTANCE) {
                                break;
                            }

                            for(Rotation shuffledRotation : Rotation.getShuffled(this.random)) {
                                List<StructureTemplate.StructureBlockInfo> connectedElements = shuffledElement.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, shuffledRotation, this.random);
                                BoundingBox shuffledElementBB = shuffledElement.getBoundingBox(this.structureManager, BlockPos.ZERO, shuffledRotation);

                                for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : connectedElements) {
                                    if (JigsawBlock.canAttach(structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1)) {
                                        BlockPos connectedSBlockPos = structuretemplate$structureblockinfo1.pos;
                                        BlockPos connectedSInFront = inFrontSBlockPos.subtract(connectedSBlockPos);
                                        BoundingBox connectedBB = shuffledElement.getBoundingBox(this.structureManager, connectedSInFront, shuffledRotation);
                                        int connectedBBMinY = connectedBB.minY();
                                        StructureTemplatePool.Projection structuretemplatepool$projection1 = shuffledElement.getProjection();
                                        boolean connectedIsRigid = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                                        int ConnectedSY = connectedSBlockPos.getY();
                                        int sBlock2RelativeY = sBlockRelativeY - ConnectedSY + JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state).getStepY();
                                        int rigidAdjust;
                                        if (startIsRigid && connectedIsRigid) {
                                            rigidAdjust = bb_y + sBlock2RelativeY;
                                        } else {
                                            if (landHeight == -1) {
                                                landHeight = this.chunkGenerator.getFirstFreeHeight(structureBlockPos.getX(), structureBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                            }

                                            rigidAdjust = landHeight - ConnectedSY;
                                        }

                                        int distanceToNewY = rigidAdjust - connectedBBMinY;
                                        BoundingBox rigidAdjustBB = connectedBB.moved(0, distanceToNewY, 0);
                                        BlockPos rigidAdjConnectedSInFront = connectedSInFront.offset(0, distanceToNewY, 0);

                                        mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(rigidAdjustBB)), BooleanOp.ONLY_FIRST));
                                        int sLandHeight = structurePiece.getGroundLevelDelta();
                                        int connectedSLandHeight;
                                        if (connectedIsRigid) {
                                            connectedSLandHeight = sLandHeight - sBlock2RelativeY;
                                        } else {
                                            connectedSLandHeight = shuffledElement.getGroundLevelDelta();
                                        }

                                        PoolElementStructurePiece newPiece = this.factory.create(this.structureManager, shuffledElement, rigidAdjConnectedSInFront, connectedSLandHeight, shuffledRotation, rigidAdjustBB);
                                        int newPieceY;
                                        if (startIsRigid) {
                                            newPieceY = bb_y + sBlockRelativeY;
                                        } else if (connectedIsRigid) {
                                            newPieceY = rigidAdjust + ConnectedSY;
                                        } else {
                                            if (landHeight == -1) {
                                                landHeight = this.chunkGenerator.getFirstFreeHeight(structureBlockPos.getX(), structureBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor);
                                            }

                                            newPieceY = landHeight + sBlock2RelativeY / 2;
                                        }

                                        structurePiece.addJunction(new JigsawJunction(inFrontSBlockPos.getX(), newPieceY - sBlockRelativeY + sLandHeight, inFrontSBlockPos.getZ(), sBlock2RelativeY, structuretemplatepool$projection1));
                                        newPiece.addJunction(new JigsawJunction(structureBlockPos.getX(), newPieceY - ConnectedSY + connectedSLandHeight, structureBlockPos.getZ(), -sBlock2RelativeY, structuretemplatepool$projection));
                                        this.pieces.add(newPiece);
                                        if (currentDepth + 1 <= this.maxDepth) {
                                            this.placing.addLast(new BTLandJigsawPlacement.PieceState(newPiece, mutableobject1, currentDepth + 1));
                                        }
                                        continue label139;
                                    }
                                }
                            }
                        }
                    } else {
                        BTLandJigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", poolFallback);
                    }
                } else {
                    BTLandJigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", poolLocation);
                }
            }

        }
    }
}