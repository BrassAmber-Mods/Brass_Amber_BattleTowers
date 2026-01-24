package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.block.block.CloudBlock;
import com.brass_amber.ba_bt.init.BTBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brass_amber.ba_bt.BABattleTowers.locate;
import static net.minecraftforge.client.model.generators.ModelProvider.BLOCK_FOLDER;
import static net.minecraftforge.client.model.generators.ModelProvider.ITEM_FOLDER;

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
        corriteChiseledBookshelf();
        corriteLadder();

        simpleBTBlock(BTBlocks.ACTIVE_CORRITE_BLOCK);
        slabBlock(BTBlocks.ACTIVE_CORRITE_SLAB);
        stairBlock(BTBlocks.ACTIVE_CORRITE_STAIR);
        wallBlock(BTBlocks.ACTIVE_CORRITE_WALL);

        simpleBTBlock(BTBlocks.CORE_MATTER);
        slabBlock(BTBlocks.CORE_MATTER_SLAB);
        stairBlock(BTBlocks.CORE_MATTER_STAIR);
        wallBlock(BTBlocks.CORE_MATTER_WALL);

        simpleBlock(BTBlocks.CLOUD.get(), models().getExistingFile(locate("block/cloud")));
        itemModels().singleTexture(BTBlocks.CLOUD.getId().getPath(), locate("block/cloud") , locate("item/cloud_inventory"));

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
        itemModels().withExistingParent(block.getId().getPath(), ITEM_FOLDER + "/chest");
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

    private void corriteLadder() {
        Block block = BTBlocks.CORRITE_LADDER.get();
        String name = BTBlocks.CORRITE_LADDER.getId().getPath();
        ModelFile blockModel = models().getBuilder(name)
                .texture("texture", locate("block/corrite_ladder"))
                .texture("particle", locate("block/corrite_ladder"))
                .renderType("cutout")
                .element().from(0f,0f, 15.2f).to(16f, 16f, 15.2f).shade(false)
                    .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#texture").end()
                    .face(Direction.SOUTH).uvs(16f, 0f, 0f, 16f).texture("#texture").end()
                .end();

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                    .modelForState().modelFile(blockModel).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST)
                    .modelForState().modelFile(blockModel).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH)
                    .modelForState().modelFile(blockModel).rotationY(180).addModel()
                .partialState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST)
                    .modelForState().modelFile(blockModel).rotationY(270).addModel();

        itemModels().withExistingParent(name, ITEM_FOLDER + "/generated")
                .texture("layer0", locate("block/corrite_ladder"));
    }

    private void corriteChiseledBookshelf() {
        Block block = BTBlocks.CORRITE_CHISELED_BOOKSHELF.get();
        String name = BTBlocks.CORRITE_CHISELED_BOOKSHELF.getId().getPath();
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        List<BooleanProperty> slotProperties = List.of(
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_0_OCCUPIED,
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_1_OCCUPIED,
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_2_OCCUPIED,
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_3_OCCUPIED,
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_4_OCCUPIED,
                BlockStateProperties.CHISELED_BOOKSHELF_SLOT_5_OCCUPIED
        );

        ModelFile baseModel = models().withExistingParent(name, BLOCK_FOLDER + "/block")
                .texture("top", locate("block/corrite_chiseled_bookshelf_top"))
                .texture("side", locate("block/corrite_chiseled_bookshelf_side"))
                .texture("particle", "#top")
                .element().from(0f,0f,0f).to(16f, 16f, 16f)
                    .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.EAST).end()
                    .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.WEST).end()
                    .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").cullface(Direction.SOUTH).end()
                    .face(Direction.UP).uvs(0f, 0f, 16f, 16f).texture("#top").cullface(Direction.UP).end()
                    .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#top").cullface(Direction.DOWN).end()
                .end();

        Map.of(Direction.NORTH, 0, Direction.EAST, 90, Direction.SOUTH, 180, Direction.WEST, 270).forEach((direction, rotation) -> {
            builder.part()
                    .rotationY(rotation).uvLock(true)
                    .modelFile(baseModel)
                    .addModel()
                    .condition(BlockStateProperties.HORIZONTAL_FACING, direction)
                    .end();

            for (int slot = 0; slot < slotProperties.size(); slot++) {
                BooleanProperty slotOccupied = slotProperties.get(slot);
                ModelFile occupiedModel = getModel(name, true, slot);
                ModelFile emptyModel = getModel(name, false, slot);
                builder.part()
                        .modelFile(occupiedModel).rotationY(rotation)
                        .addModel()
                        .condition(BlockStateProperties.HORIZONTAL_FACING, direction)
                        .condition(slotOccupied, true)
                        .end();

                builder.part()
                        .modelFile(emptyModel).rotationY(rotation)
                        .addModel()
                        .condition(BlockStateProperties.HORIZONTAL_FACING, direction)
                        .condition(slotOccupied, false)
                        .end();
            }
        });

        ModelFile itemModel = models().withExistingParent(name + "_inventory", BLOCK_FOLDER + "/block")
                .texture("top", locate("block/corrite_chiseled_bookshelf_top"))
                .texture("side", locate("block/corrite_chiseled_bookshelf_side"))
                .texture("front", locate("block/corrite_chiseled_bookshelf_empty"))
                .texture("particle", "#top")
                .element().from(0f, 0f, 0f).to(16f, 16f, 16f)
                .face(Direction.NORTH).uvs(0f, 0f, 16f, 16f).texture("#front").end()
                .face(Direction.EAST).uvs(0f, 0f, 16f, 16f).texture("#side").end()
                .face(Direction.WEST).uvs(0f, 0f, 16f, 16f).texture("#side").end()
                .face(Direction.SOUTH).uvs(0f, 0f, 16f, 16f).texture("#side").end()
                .face(Direction.UP).uvs(0f, 0f, 16f, 16f).texture("#top").end()
                .face(Direction.DOWN).uvs(0f, 0f, 16f, 16f).texture("#top").end()
                .end();

        simpleBlockItem(block, itemModel);
    }

    private ModelFile getModel(String baseName, boolean occupied, int slot) {
        String type = (occupied ? "occupied" : "empty");
        String bookShelfEnd = getBookshelfEnd(slot);
        String name = baseName + "_" + type + bookShelfEnd;
        return models().withExistingParent(name, BLOCK_FOLDER + "/template_chiseled_bookshelf" + bookShelfEnd)
                .texture("texture", locate("block/corrite_chiseled_bookshelf_" + (occupied ? "occupied" : "empty")));
    }

    private String getBookshelfEnd(int slot) {
        return (slot >= 3 ? "_slot_bottom_" : "_slot_top_") + switch (slot % 3) {
            case 1 -> "mid";
            case 2 -> "right";
            default -> "left";
        };
    }
}
