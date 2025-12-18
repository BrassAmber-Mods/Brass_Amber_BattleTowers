package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

import static com.brass_amber.ba_bt.BABattleTowers.locate;

public class BTBlocksStateProvider extends BlockStateProvider {
    public BTBlocksStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, BABattleTowers.MOD_ID, exFileHelper);
    }

    private static final Map<BTBookSlotModelCacheKey, ResourceLocation> CHISELED_BOOKSHELF_SLOT_MODEL_CACHE = new HashMap<>();

    static record BTBookSlotModelCacheKey(ModelTemplate template, String modelSuffix) {
    }

    @Override
    protected void registerStatesAndModels() {
        chestBlock(BTBlocks.LAND_CHEST);
        chestBlock(BTBlocks.LAND_GOLEM_CHEST);
        chestBlock(BTBlocks.OCEAN_CHEST);
        chestBlock(BTBlocks.OCEAN_GOLEM_CHEST);
        chestBlock(BTBlocks.CORE_CHEST);
        chestBlock(BTBlocks.CORE_GOLEM_CHEST);
        chestBlock(BTBlocks.NETHER_CHEST);
        chestBlock(BTBlocks.NETHER_GOLEM_CHEST);
        chestBlock(BTBlocks.END_CHEST);
        chestBlock(BTBlocks.END_GOLEM_CHEST);
        chestBlock(BTBlocks.SKY_CHEST);
        chestBlock(BTBlocks.SKY_GOLEM_CHEST);

        spawnerBlock(BTBlocks.LAND_SPAWNER);
        spawnerBlock(BTBlocks.OCEAN_SPAWNER);
        spawnerBlock(BTBlocks.CORE_SPAWNER);
        spawnerBlock(BTBlocks.NETHER_SPAWNER);
        spawnerBlock(BTBlocks.END_SPAWNER);
        spawnerBlock(BTBlocks.SKY_SPAWNER);
        spawnerBlock(BTBlocks.SPAWNER_MARKER);

        simpleBlock(BTBlocks.AIR_FILL.get(), models().getBuilder(BTBlocks.AIR_FILL.getId().getPath()));
        simpleBTBlock(BTBlocks.DATA_MARKER);

        simpleBTBlock(BTBlocks.CORRITE_BLOCK);
        slabBlock(BTBlocks.CORRITE_SLAB);
        stairBlock(BTBlocks.CORRITE_STAIR);
        wallBlock(BTBlocks.CORRITE_WALL);
        // Bookshelf & Ladder done in actual files

        simpleBTBlock(BTBlocks.ACTIVE_CORRITE_BLOCK);
        slabBlock(BTBlocks.ACTIVE_CORRITE_SLAB);
        stairBlock(BTBlocks.ACTIVE_CORRITE_STAIR);
        wallBlock(BTBlocks.ACTIVE_CORRITE_WALL);

        simpleBTBlock(BTBlocks.CORE_MATTER);
        slabBlock(BTBlocks.CORE_MATTER_SLAB);
        stairBlock(BTBlocks.CORE_MATTER_STAIR);
        wallBlock(BTBlocks.CORE_MATTER_WALL);
    }

    public void simpleBTBlock(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), models().cubeAll(block.getId().getPath(), locate("block/" + block.getId().getPath())));
    }

    public void simpleBTBlock(RegistryObject<Block> block, String extra) {
        simpleBlockWithItem(block.get(), models().cubeAll(block.getId().getPath(), locate("block/" + extra + block.getId().getPath())));
    }

    private void chestBlock(RegistryObject<Block> block) {
        simpleBlock(
                block.get(),
                models().getBuilder(block.getId().getPath())
                        .texture("particle", new ResourceLocation("minecraft:block/stone"))
        );
    }

    private void spawnerBlock(RegistryObject<Block> block) {
        simpleBTBlock(
                block,
                "spawner/"
        );
    }

    private void slabBlock(RegistryObject<Block> block) {
        String baseName = block.getId().getPath();
        ResourceLocation location = locate("block/" + baseName.replace("slab", "block"));
        ModelFile bottom = models().slab(baseName, location, location, location);
        ModelFile top = models().slabTop(baseName + "_top", location, location, location);
        ModelFile doubleslab = models().getExistingFile(location);
        slabBlock((SlabBlock) block.get(), bottom, top, doubleslab);
        simpleBlockItem(block.get(), bottom);
    }

    private void stairBlock(RegistryObject<Block> block) {
        String baseName = block.getId().getPath();
        ResourceLocation location = locate("block/" + baseName.replace("stair", "block"));
        ModelFile stairs = models().stairs(baseName, location, location, location);
        ModelFile stairsInner = models().stairsInner(baseName + "_inner", location, location, location);
        ModelFile stairsOuter = models().stairsOuter(baseName + "_outer", location, location, location);
        stairsBlock((StairBlock) block.get(), stairs, stairsInner, stairsOuter);
        simpleBlockItem(block.get(), stairs);
    }

    private void wallBlock(RegistryObject<Block> block) {
        String baseName = block.getId().getPath();
        ResourceLocation location = locate("block/" + baseName.replace("wall", "block"));

        ModelFile post = models().wallPost(baseName, location);
        ModelFile side = models().wallSide(baseName + "_inner", location);
        ModelFile sideTall = models().wallSideTall(baseName + "_outer", location);
        wallBlock((WallBlock) block.get(), post, side, sideTall);
        simpleBlockItem(block.get(), itemModels().wallInventory(baseName + "_inventory", location));
    }

}
