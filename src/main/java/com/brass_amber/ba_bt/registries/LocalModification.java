package com.brass_amber.ba_bt.registries;

import com.brass_amber.ba_bt.entity.block.ObeliskEntity;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.util.BTUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Optional;
import java.util.function.Function;

public abstract class LocalModification {
    /**
     * Codec for (de)serializing Style modifiers inline.
     * Mods can use this for data generation.
     */
    public static final Codec<LocalModification> DIRECT_CODEC = ExtraCodecs.lazyInitializedCodec(() -> BTRegistries.LOCAL_MODIFICATION_TYPE_SERIALIZERS.get().getCodec())
            .dispatch(LocalModification::codec, Function.identity());

    /**
     * Codec for referring to Style modifiers by id in other datapack registry files.
     * Can only be used with {@link RegistryOps}.
     */
    public static final Codec<Holder<LocalModification>> REFERENCE_CODEC = RegistryFileCodec.create(BTRegistries.Keys.LOCAL_MODIFICATIONS, DIRECT_CODEC);

    protected final LocalModification.Settings settings;
    public GenerationState generationState;
    public int top = 0;
    public int bottom = 0;

    protected LocalModification(LocalModification.Settings settings){
        this.settings = settings;
        this.generationState = GenerationState.getState(settings.state());
    }

    public void run (ObeliskEntity obeliskEntity) {

        switch (this.generationState) {
            case REMOVE_MOTION_BLOCKS -> this.cleanupModificationArea(obeliskEntity);
            case GATHER_AREA_BLOCKS -> this.gatherAreaBlocks(obeliskEntity);
            case REMOVE_AREA_BLOCKS -> this.removeAreaBlocks(obeliskEntity);
            case ADD_AREA_FEATURES -> this.addAreaFeatures(obeliskEntity);
        }
    }

    /**
     * Top and Bottom should be set by an override call before this is run
     * @param obeliskEntity the obelisk entity whose position and level reference is used to run the function
     */
    public void cleanupModificationArea(ObeliskEntity obeliskEntity) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockState state;
        BlockPos bottomCorner = new BlockPos(obeliskEntity.getBlockX() - Mth.floor(this.settings.cleanupDistance), this.bottom, obeliskEntity.getBlockZ() - Mth.floor(this.settings.cleanupDistance));
        BlockPos topCorner = new BlockPos(obeliskEntity.getBlockX() + Mth.floor(this.settings.cleanupDistance), this.top, obeliskEntity.getBlockZ() + Mth.floor(this.settings.cleanupDistance));
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(bottomCorner, topCorner);

        for (BlockPos checkPos : blocksToCheck) {
            double distance2d = BTUtil.distanceTo2D(obeliskEntity.blockPosition().atY(this.top), blockpos$mutableblockpos);

            if (distance2d < this.settings.cleanupDistance) {
                removeCleanupTagBlock(obeliskEntity.level(), checkPos);
            }
        }
        this.generationState = GenerationState.GATHER_AREA_BLOCKS;
    }

    /**
     * Function used to allow inheriting types to override how the blocks are replaced
     * @param level level to call for replacing blocks
     * @param pos position of the block being replaced
     */
    public void removeCleanupTagBlock(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (this.settings.cleanupBlocksTag.isPresent() && state.is(this.settings.cleanupBlocksTag.get())) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
        if (!state.getFluidState().isEmpty() && this.settings.clearFluid()) {
            try {
                level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 2);
            } catch (Exception e) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }

    public abstract void gatherAreaBlocks(ObeliskEntity obeliskEntity);

    public abstract void removeAreaBlocks(ObeliskEntity obeliskEntity);

    public abstract void addAreaFeatures(ObeliskEntity obeliskEntity);

    public abstract Codec<? extends LocalModification> codec();

    public abstract LocalModificationType<?> type();

    public enum GenerationState {
        REMOVE_MOTION_BLOCKS(0),
        GATHER_AREA_BLOCKS(1),
        REMOVE_AREA_BLOCKS(2),
        ADD_AREA_FEATURES(3),
        FINISHED(4);

        private final int value;

        GenerationState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static GenerationState getState(int value) {
            return switch (value) {
                case 1, 2 -> GenerationState.GATHER_AREA_BLOCKS;
                case 3 -> GenerationState.ADD_AREA_FEATURES;
                case 4 -> GenerationState.FINISHED;
                default -> GenerationState.REMOVE_MOTION_BLOCKS;
            };
        }
    }

    public static <S extends LocalModification> RecordCodecBuilder<S, Settings> settingsCodec(RecordCodecBuilder.Instance<S> instance) {
        return Settings.CODEC.forGetter(style -> style.settings); // FORGE: Patch codec to ignore field redirect coremods.
    }

    public record Settings(
            int state, int topOffset, int bottomOffset, boolean clearFluid, double cleanupDistance, Optional<TagKey<Block>> cleanupBlocksTag
    ) {
        public static final MapCodec<Settings> CODEC = RecordCodecBuilder.mapCodec(
                p_351995_ -> p_351995_.group(
                                Codec.INT.fieldOf("state").forGetter(Settings::state),
                                Codec.INT.fieldOf("top_offset").forGetter(Settings::topOffset),
                                Codec.INT.fieldOf("bottom_offset").forGetter(Settings::bottomOffset),
                                Codec.BOOL.fieldOf("clear_fluid").forGetter(Settings::clearFluid),
                                Codec.DOUBLE.fieldOf("cleanup_distance").forGetter(Settings::cleanupDistance),
                                TagKey.hashedCodec(Registries.BLOCK).optionalFieldOf("cleanup_blocks").forGetter(Settings::cleanupBlocksTag)
                        )
                        .apply(p_351995_, Settings::new)
        );
    }
}



