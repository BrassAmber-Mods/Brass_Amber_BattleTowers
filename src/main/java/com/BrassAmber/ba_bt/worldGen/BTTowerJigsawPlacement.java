package com.BrassAmber.ba_bt.worldGen;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.Logger;

import java.util.*;


public class BTTowerJigsawPlacement {
    private static final Logger LOGGER = BrassAmberBattleTowers.LOGGER;

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext generationContext, Holder<StructureTemplatePool> startPool, int maxDepth, BlockPos startPos, Boolean bury, Boolean randomBury) {
        RegistryAccess registryaccess = generationContext.registryAccess();
        ChunkGenerator chunkgenerator = generationContext.chunkGenerator();
        StructureTemplateManager structuretemplatemanager = generationContext.structureTemplateManager();
        LevelHeightAccessor levelheightaccessor = generationContext.heightAccessor();
        WorldgenRandom worldgenrandom = generationContext.random();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = startPool.value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(structuretemplatemanager, structurepoolelement, startPos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuretemplatemanager, startPos, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;

            int heightChange = 0;

            if (bury) {
                if (randomBury) {
                    heightChange = worldgenrandom.nextIntBetweenInclusive(6, 12);
                } else {
                    heightChange = 5;
                }
            }

            k = startPos.getY() - heightChange;

            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            return Optional.of(new Structure.GenerationStub(new BlockPos(i, k, j), (p_227237_) -> {
                List<PoolElementStructurePiece> list = Lists.newArrayList();
                list.add(poolelementstructurepiece);
                if (maxDepth > 0) {
                    int i1 = 160;
                    AABB aabb = new AABB(i - i1, k - i1, j - i1, i + i1 + 1, k + i1 + 1, j + i1 + 1);VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
                    addPieces(generationContext.randomState(), maxDepth, chunkgenerator, structuretemplatemanager, levelheightaccessor, worldgenrandom, registry, poolelementstructurepiece, list, voxelshape);
                    list.forEach(p_227237_::addPiece);
                }
            }));
        }
    }

    private static void addPieces(RandomState randomState, int maxDepth, ChunkGenerator chunkGenerator, StructureTemplateManager templateManager, LevelHeightAccessor heightAccessor, RandomSource worldgenRandom, Registry<StructureTemplatePool> templatePoolRegistry, PoolElementStructurePiece poolElementStructurePiece, List<PoolElementStructurePiece> pieceList, VoxelShape voxelShape) {
        BTTowerJigsawPlacement.Placer jigsawplacement$placer = new BTTowerJigsawPlacement.Placer(templatePoolRegistry, maxDepth, chunkGenerator, templateManager, pieceList, worldgenRandom);
        jigsawplacement$placer.placing.addLast(new BTTowerJigsawPlacement.PieceState(poolElementStructurePiece, new MutableObject<>(voxelShape), 0));

        while(!jigsawplacement$placer.placing.isEmpty()) {
            BTTowerJigsawPlacement.PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
            jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.depth, heightAccessor, randomState);
        }

    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int depth;

        PieceState(PoolElementStructurePiece p_210311_, MutableObject<VoxelShape> p_210312_, int p_210313_) {
            this.piece = p_210311_;
            this.free = p_210312_;
            this.depth = p_210313_;
        }
    }

    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final ChunkGenerator chunkGenerator;
        private final StructureTemplateManager structureTemplateManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final RandomSource random;
        final Deque<BTTowerJigsawPlacement.PieceState> placing = Queues.newArrayDeque();

        Placer(Registry<StructureTemplatePool> p_227258_, int p_227259_, ChunkGenerator p_227260_, StructureTemplateManager p_227261_, List<? super PoolElementStructurePiece> p_227262_, RandomSource p_227263_) {
            this.pools = p_227258_;
            this.maxDepth = p_227259_;
            this.chunkGenerator = p_227260_;
            this.structureTemplateManager = p_227261_;
            this.pieces = p_227262_;
            this.random = p_227263_;
        }

        void tryPlacingChildren(PoolElementStructurePiece p_227265_, MutableObject<VoxelShape> p_227266_, int p_227267_, LevelHeightAccessor p_227269_, RandomState p_227270_) {
            StructurePoolElement structurepoolelement = p_227265_.getElement();
            BlockPos blockpos = p_227265_.getPosition();
            Rotation rotation = p_227265_.getRotation();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean flag = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            BoundingBox boundingbox = p_227265_.getBoundingBox();
            int i = boundingbox.minY();

            label129:
            for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : structurepoolelement.getShuffledJigsawBlocks(this.structureTemplateManager, blockpos, rotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state());
                BlockPos blockpos1 = structuretemplate$structureblockinfo.pos();
                BlockPos blockpos2 = blockpos1.relative(direction);
                int j = blockpos1.getY() - i;
                int k = -1;
                ResourceKey<StructureTemplatePool> resourcekey = readPoolName(structuretemplate$structureblockinfo);
                Optional<? extends Holder<StructureTemplatePool>> optional = this.pools.getHolder(resourcekey);
                if (optional.isEmpty()) {
                    BTTowerJigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcekey.location());
                } else {
                    Holder<StructureTemplatePool> holder = optional.get();
                    if (holder.value().size() == 0 && !holder.is(Pools.EMPTY)) {
                        BTTowerJigsawPlacement.LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcekey.location());
                    } else {
                        Holder<StructureTemplatePool> holder1 = holder.value().getFallback();
                        if (holder1.value().size() == 0 && !holder1.is(Pools.EMPTY)) {
                            BTTowerJigsawPlacement.LOGGER.warn("Empty or non-existent fallback pool: {}", holder1.unwrapKey().map((p_255599_) -> {
                                return p_255599_.location().toString();
                            }).orElse("<unregistered>"));
                        } else {
                            boolean flag1 = boundingbox.isInside(blockpos2);
                            MutableObject<VoxelShape> mutableobject1;
                            if (flag1) {
                                mutableobject1 = mutableobject;
                                if (mutableobject.getValue() == null) {
                                    mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
                                }
                            } else {
                                mutableobject1 = p_227266_;
                            }

                            List<StructurePoolElement> list = Lists.newArrayList();
                            if (p_227267_ != this.maxDepth) {
                                list.addAll(holder.value().getShuffledTemplates(this.random));
                            }

                            list.addAll(holder1.value().getShuffledTemplates(this.random));

                            for(StructurePoolElement structurepoolelement1 : list) {
                                if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
                                    break;
                                }

                                for(Rotation rotation1 : Rotation.getShuffled(this.random)) {
                                    List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, rotation1, this.random);

                                    for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : list1) {
                                        if (JigsawBlock.canAttach(structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1)) {
                                            BlockPos blockpos3 = structuretemplate$structureblockinfo1.pos();
                                            BlockPos blockpos4 = blockpos2.subtract(blockpos3);
                                            BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureTemplateManager, blockpos4, rotation1);
                                            int i1 = boundingbox2.minY();
                                            StructureTemplatePool.Projection structuretemplatepool$projection1 = structurepoolelement1.getProjection();
                                            boolean flag2 = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                                            int j1 = blockpos3.getY();
                                            int k1 = j - j1 + JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state()).getStepY();
                                            int l1;
                                            if (flag && flag2) {
                                                l1 = i + k1;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_227269_, p_227270_);
                                                }

                                                l1 = k - j1;
                                            }

                                            int i2 = l1 - i1;
                                            BoundingBox boundingbox3 = boundingbox2.moved(0, i2, 0);
                                            BlockPos blockpos5 = blockpos4.offset(0, i2, 0);

                                            if (!Shapes.joinIsNotEmpty(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                                mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST));
                                                int i3 = p_227265_.getGroundLevelDelta();
                                                int k2;
                                                if (flag2) {
                                                    k2 = i3 - k1;
                                                } else {
                                                    k2 = structurepoolelement1.getGroundLevelDelta();
                                                }

                                                PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(this.structureTemplateManager, structurepoolelement1, blockpos5, k2, rotation1, boundingbox3);
                                                int l2;
                                                if (flag) {
                                                    l2 = i + j;
                                                } else if (flag2) {
                                                    l2 = l1 + j1;
                                                } else {
                                                    if (k == -1) {
                                                        k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_227269_, p_227270_);
                                                    }

                                                    l2 = k + k1 / 2;
                                                }

                                                p_227265_.addJunction(new JigsawJunction(blockpos2.getX(), l2 - j + i3, blockpos2.getZ(), k1, structuretemplatepool$projection1));
                                                poolelementstructurepiece.addJunction(new JigsawJunction(blockpos1.getX(), l2 - j1 + k2, blockpos1.getZ(), -k1, structuretemplatepool$projection));
                                                this.pieces.add(poolelementstructurepiece);
                                                if (p_227267_ + 1 <= this.maxDepth) {
                                                    this.placing.addLast(new BTTowerJigsawPlacement.PieceState(poolelementstructurepiece, mutableobject1, p_227267_ + 1));
                                                }
                                                continue label129;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        private static ResourceKey<StructureTemplatePool> readPoolName(StructureTemplate.StructureBlockInfo p_256491_) {
            return ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(p_256491_.nbt().getString("pool")));
        }
    }
}