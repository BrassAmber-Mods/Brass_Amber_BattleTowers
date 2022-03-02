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
import net.minecraft.world.level.levelgen.feature.structures.*;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;


public class BTLandJigsawPlacement {
    private static final Logger LOGGER = BrassAmberBattleTowers.LOGGER;


    public static Optional<PieceGenerator<BTJigsawConfiguration>> addPieces(PieceGeneratorSupplier.Context<BTJigsawConfiguration> context, BTLandJigsawPlacement.PieceFactory pieceFactory, BlockPos placementPos, boolean boundaryAdjust, boolean useHeightMap, @Nullable Rotation copyRotation) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        RegistryAccess registryaccess = context.registryAccess();
        BTJigsawConfiguration jigsawconfiguration = context.config();
        ChunkGenerator chunkgenerator = context.chunkGenerator();
        StructureManager structuremanager = context.structureManager();
        LevelHeightAccessor levelheightaccessor = context.heightAccessor();
        Predicate<Biome> predicate = context.validBiome();
        StructureFeature.bootstrap();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation;
        if (copyRotation == null) {
            rotation = Rotation.getRandom(worldgenrandom);
        } else {
            rotation = copyRotation;
        }

        StructureTemplatePool structuretemplatepool = jigsawconfiguration.startPool().get();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            PoolElementStructurePiece poolelementstructurepiece = pieceFactory.create(structuremanager, structurepoolelement, placementPos, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuremanager, placementPos, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int bbX = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int bbZ = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int ppY;
            if (useHeightMap) {
                ppY = placementPos.getY() + chunkgenerator.getFirstFreeHeight(bbX, bbZ, Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor);
            } else {
                ppY = placementPos.getY();
            }

            if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(bbX), QuartPos.fromBlock(ppY), QuartPos.fromBlock(bbZ)))) {
                return Optional.empty();
            } else {
                int pieceYlevel = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
                poolelementstructurepiece.move(0, ppY - pieceYlevel, 0);
                return Optional.of((piecesBuilder, pieceGenContext) -> {
                    List<PoolElementStructurePiece> list = Lists.newArrayList();
                    list.add(poolelementstructurepiece);
                    if (jigsawconfiguration.maxDepth() > 0) {
                        int sizeLimit = 120;
                        AABB aabb = new AABB(bbX - sizeLimit, ppY - sizeLimit, bbZ - sizeLimit, bbX + sizeLimit + 1, ppY + sizeLimit + 1, bbZ + sizeLimit + 1);
                        BTLandJigsawPlacement.Placer BTLandJigsawPlacement$placer = new BTLandJigsawPlacement.Placer(registry, jigsawconfiguration.maxDepth(), pieceFactory, chunkgenerator, structuremanager, list, worldgenrandom);
                        BTLandJigsawPlacement$placer.placing.addLast(new BTLandJigsawPlacement.PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), 0));

                        while(!BTLandJigsawPlacement$placer.placing.isEmpty()) {
                            BTLandJigsawPlacement.PieceState BTLandJigsawPlacement$piecestate = BTLandJigsawPlacement$placer.placing.removeFirst();
                            BTLandJigsawPlacement$placer.tryPlacingChildren(BTLandJigsawPlacement$piecestate.piece, BTLandJigsawPlacement$piecestate.free, BTLandJigsawPlacement$piecestate.depth, boundaryAdjust, levelheightaccessor);
                        }

                        list.forEach(piecesBuilder::addPiece);
                    }
                });
            }
        }
    }

    public static Optional<PieceGenerator<BTJigsawConfiguration>> addAllPieces(List<PieceGeneratorSupplier.Context<BTJigsawConfiguration>> contexts, PieceFactory pieceFactory, BlockPos placementPos, boolean boundaryAdjust, boolean useHeightMap, @Nullable Rotation copyRotation) {
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenrandom.setLargeFeatureSeed(contexts.get(0).seed(), contexts.get(0).chunkPos().x, contexts.get(0).chunkPos().z);
        RegistryAccess registryaccess = contexts.get(0).registryAccess();
        List<BTJigsawConfiguration> jigsawconfigurations = new ArrayList<>();
        for (PieceGeneratorSupplier.Context<BTJigsawConfiguration> context : contexts) {
            jigsawconfigurations.add(context.config());
        }
        ChunkGenerator chunkgenerator = contexts.get(0).chunkGenerator();
        StructureManager structuremanager = contexts.get(0).structureManager();
        LevelHeightAccessor levelheightaccessor = contexts.get(0).heightAccessor();
        Predicate<Biome> predicate = contexts.get(0).validBiome();
        StructureFeature.bootstrap();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation;
        if (copyRotation == null) {
            rotation = Rotation.getRandom(worldgenrandom);
        } else {
            rotation = copyRotation;
        }

        List<StructureTemplatePool> structuretemplatepools = new ArrayList<>();
        for (BTJigsawConfiguration config : jigsawconfigurations) {
            structuretemplatepools.add(config.startPool().get());
        }
        List<StructurePoolElement> structurepoolelements = new ArrayList<>();
        for (StructureTemplatePool templatePool : structuretemplatepools) {
            structurepoolelements.add(templatePool.getRandomTemplate(worldgenrandom));
        }
        if (structurepoolelements.get(0) == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            List<PoolElementStructurePiece> poolelementstructurepieces = new ArrayList<>();
            for (StructurePoolElement element: structurepoolelements) {
                poolelementstructurepieces.add(pieceFactory.create(structuremanager, element, placementPos, element.getGroundLevelDelta(), rotation,element.getBoundingBox(structuremanager, placementPos, rotation)));
            }
            int f = 0;
            StructurePiecesBuilder builder = new StructurePiecesBuilder();
            List<PoolElementStructurePiece> pieces = Lists.newArrayList();

            for (PoolElementStructurePiece poolelementstructurepiece: poolelementstructurepieces) {
                BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
                int bbX = (boundingbox.maxX() + boundingbox.minX()) / 2;
                int bbZ = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
                int y;
                if (useHeightMap) {
                    y = placementPos.getY() + chunkgenerator.getFirstFreeHeight(bbX, bbZ, Heightmap.Types.WORLD_SURFACE_WG, levelheightaccessor);
                } else {
                    y = placementPos.getY();
                }

                if (!predicate.test(chunkgenerator.getNoiseBiome(QuartPos.fromBlock(bbX), QuartPos.fromBlock(y), QuartPos.fromBlock(bbZ)))) {
                    return Optional.empty();
                } else {
                    int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
                    poolelementstructurepiece.move(0, y - l, 0);
                    pieces.add(poolelementstructurepiece);
                    if (jigsawconfigurations.get(f).maxDepth() > 0) {
                        int sizeLimit = 120;
                        AABB aabb = new AABB(bbX - sizeLimit, y - sizeLimit, bbZ - sizeLimit, bbX + sizeLimit + 1, y + sizeLimit + 1, bbZ + sizeLimit + 1);
                        BTLandJigsawPlacement.Placer BTLandJigsawPlacement$placer = new BTLandJigsawPlacement.Placer(registry, jigsawconfigurations.get(f).maxDepth(), pieceFactory, chunkgenerator, structuremanager, pieces, worldgenrandom);
                        BTLandJigsawPlacement$placer.placing.addLast(new BTLandJigsawPlacement.PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), 0));

                        while(!BTLandJigsawPlacement$placer.placing.isEmpty()) {
                            BTLandJigsawPlacement.PieceState BTLandJigsawPlacement$piecestate = BTLandJigsawPlacement$placer.placing.removeFirst();
                            BTLandJigsawPlacement$placer.tryPlacingChildren(BTLandJigsawPlacement$piecestate.piece, BTLandJigsawPlacement$piecestate.free, BTLandJigsawPlacement$piecestate.depth, boundaryAdjust, levelheightaccessor);
                        }

                        pieces.forEach(builder::addPiece);
                    }
                }
                f++;
            }

            return Optional.of((b, p) -> {
                b = builder;
                p = new PieceGenerator.Context<>(jigsawconfigurations.get(0), chunkgenerator, structuremanager, contexts.get(0).chunkPos(), levelheightaccessor, worldgenrandom, worldgenrandom.nextLong());
            });
        }
    }

    public static void addPieces(RegistryAccess p_161625_, PoolElementStructurePiece p_161626_, int p_161627_, BTLandJigsawPlacement.PieceFactory p_161628_, ChunkGenerator p_161629_, StructureManager p_161630_, List<? super PoolElementStructurePiece> p_161631_, Random p_161632_, LevelHeightAccessor p_161633_) {
        Registry<StructureTemplatePool> registry = p_161625_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        BTLandJigsawPlacement.Placer BTLandJigsawPlacement$placer = new BTLandJigsawPlacement.Placer(registry, p_161627_, p_161628_, p_161629_, p_161630_, p_161631_, p_161632_);
        BTLandJigsawPlacement$placer.placing.addLast(new BTLandJigsawPlacement.PieceState(p_161626_, new MutableObject<>(Shapes.INFINITY), 0));

        while(!BTLandJigsawPlacement$placer.placing.isEmpty()) {
            BTLandJigsawPlacement.PieceState BTLandJigsawPlacement$piecestate = BTLandJigsawPlacement$placer.placing.removeFirst();
            BTLandJigsawPlacement$placer.tryPlacingChildren(BTLandJigsawPlacement$piecestate.piece, BTLandJigsawPlacement$piecestate.free, BTLandJigsawPlacement$piecestate.depth, false, p_161633_);
        }

    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager p_68965_, StructurePoolElement p_68966_, BlockPos p_68967_, int p_68968_, Rotation rotation, BoundingBox p_68970_);
    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int depth;

        PieceState(PoolElementStructurePiece p_191509_, MutableObject<VoxelShape> p_191510_, int p_191511_) {
            this.piece = p_191509_;
            this.free = p_191510_;
            this.depth = p_191511_;
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

        Placer(Registry<StructureTemplatePool> templatePoolRegistry, int mDepth, BTLandJigsawPlacement.PieceFactory pieceFactory, ChunkGenerator chunkGenerator, StructureManager structureManager, List<? super PoolElementStructurePiece> structurePieceList, Random random) {
            this.pools = templatePoolRegistry;
            this.maxDepth = mDepth;
            this.factory = pieceFactory;
            this.chunkGenerator = chunkGenerator;
            this.structureManager = structureManager;
            this.pieces = structurePieceList;
            this.random = random;
        }

        void tryPlacingChildren(PoolElementStructurePiece structurePiece, MutableObject<VoxelShape> voxelShape, int depth, boolean p_191516_, LevelHeightAccessor p_191517_) {
            StructurePoolElement structurepoolelement = structurePiece.getElement();
            BlockPos blockpos = structurePiece.getPosition();
            Rotation rotation = structurePiece.getRotation();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean flag = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            BoundingBox boundingbox = structurePiece.getBoundingBox();
            int bbMinY = boundingbox.minY();

            label139:
            for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo : structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state);
                BlockPos blockpos1 = structuretemplate$structureblockinfo.pos;
                BlockPos blockpos2 = blockpos1.relative(direction);
                int templateHeight = blockpos1.getY() - bbMinY;
                int k = -1;
                ResourceLocation resourcelocation = new ResourceLocation(structuretemplate$structureblockinfo.nbt.getString("pool"));
                Optional<StructureTemplatePool> optional = this.pools.getOptional(resourcelocation);
                if (optional.isPresent() && (optional.get().size() != 0 || Objects.equals(resourcelocation, Pools.EMPTY.location()))) {
                    ResourceLocation resourcelocation1 = optional.get().getFallback();
                    Optional<StructureTemplatePool> optional1 = this.pools.getOptional(resourcelocation1);
                    if (optional1.isPresent() && (optional1.get().size() != 0 || Objects.equals(resourcelocation1, Pools.EMPTY.location()))) {
                        boolean flag1 = boundingbox.isInside(blockpos2);
                        MutableObject<VoxelShape> mutableobject1;
                        if (flag1) {
                            mutableobject1 = mutableobject;
                            if (mutableobject.getValue() == null) {
                                mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
                            }
                        } else {
                            mutableobject1 = voxelShape;
                        }

                        List<StructurePoolElement> list = Lists.newArrayList();
                        if (depth != this.maxDepth) {
                            list.addAll(optional.get().getShuffledTemplates(this.random));
                        }

                        list.addAll(optional1.get().getShuffledTemplates(this.random));

                        for(StructurePoolElement structurepoolelement1 : list) {
                            if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
                                break;
                            }

                            for(Rotation rotation1 : Rotation.getShuffled(this.random)) {
                                List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
                                BoundingBox boundingbox1 = structurepoolelement1.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation1);
                                int l;
                                if (p_191516_ && boundingbox1.getYSpan() <= 16) {
                                    l = list1.stream().mapToInt((p_69032_) -> {
                                        if (!boundingbox1.isInside(p_69032_.pos.relative(JigsawBlock.getFrontFacing(p_69032_.state)))) {
                                            return 0;
                                        } else {
                                            ResourceLocation resourcelocation2 = new ResourceLocation(p_69032_.nbt.getString("pool"));
                                            Optional<StructureTemplatePool> optional2 = this.pools.getOptional(resourcelocation2);
                                            Optional<StructureTemplatePool> optional3 = optional2.flatMap((p_161646_) -> {
                                                return this.pools.getOptional(p_161646_.getFallback());
                                            });
                                            int j3 = optional2.map((p_161644_) -> {
                                                return p_161644_.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            int k3 = optional3.map((p_161635_) -> {
                                                return p_161635_.getMaxSize(this.structureManager);
                                            }).orElse(0);
                                            return Math.max(j3, k3);
                                        }
                                    }).max().orElse(0);
                                } else {
                                    l = 0;
                                }

                                for(StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : list1) {
                                    if (JigsawBlock.canAttach(structuretemplate$structureblockinfo, structuretemplate$structureblockinfo1)) {
                                        BlockPos blockpos3 = structuretemplate$structureblockinfo1.pos;
                                        BlockPos blockpos4 = blockpos2.subtract(blockpos3);
                                        BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureManager, blockpos4, rotation1);
                                        int i1 = boundingbox2.minY();
                                        StructureTemplatePool.Projection structuretemplatepool$projection1 = structurepoolelement1.getProjection();
                                        boolean flag2 = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                                        int j1 = blockpos3.getY();
                                        int k1 = templateHeight - j1 + JigsawBlock.getFrontFacing(structuretemplate$structureblockinfo.state).getStepY();
                                        int l1;
                                        if (flag && flag2) {
                                            l1 = bbMinY + k1;
                                        } else {
                                            if (k == -1) {
                                                k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_191517_);
                                            }

                                            l1 = k - j1;
                                        }

                                        int i2 = l1 - i1;
                                        BoundingBox boundingbox3 = boundingbox2.moved(0, i2, 0);
                                        BlockPos blockpos5 = blockpos4.offset(0, i2, 0);
                                        if (l > 0) {
                                            int j2 = Math.max(l + 1, boundingbox3.maxY() - boundingbox3.minY());
                                            boundingbox3.encapsulate(new BlockPos(boundingbox3.minX(), boundingbox3.minY() + j2, boundingbox3.minZ()));
                                        }

                                        if (!Shapes.joinIsNotEmpty(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                            mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST));
                                            int i3 = structurePiece.getGroundLevelDelta();
                                            int k2;
                                            if (flag2) {
                                                k2 = i3 - k1;
                                            } else {
                                                k2 = structurepoolelement1.getGroundLevelDelta();
                                            }

                                            PoolElementStructurePiece poolelementstructurepiece = this.factory.create(this.structureManager, structurepoolelement1, blockpos5, k2, rotation1, boundingbox3);
                                            int l2;
                                            if (flag) {
                                                l2 = bbMinY + templateHeight;
                                            } else if (flag2) {
                                                l2 = l1 + j1;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, p_191517_);
                                                }

                                                l2 = k + k1 / 2;
                                            }

                                            structurePiece.addJunction(new JigsawJunction(blockpos2.getX(), l2 - templateHeight + i3, blockpos2.getZ(), k1, structuretemplatepool$projection1));
                                            poolelementstructurepiece.addJunction(new JigsawJunction(blockpos1.getX(), l2 - j1 + k2, blockpos1.getZ(), -k1, structuretemplatepool$projection));
                                            this.pieces.add(poolelementstructurepiece);
                                            if (depth + 1 <= this.maxDepth) {
                                                this.placing.addLast(new BTLandJigsawPlacement.PieceState(poolelementstructurepiece, mutableobject1, depth + 1));
                                            }
                                            continue label139;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        LOGGER.warn("Empty or non-existent fallback pool: {}", (Object)resourcelocation1);
                    }
                } else {
                    LOGGER.warn("Empty or non-existent pool: {}", (Object)resourcelocation);
                }
            }

        }
    }
}