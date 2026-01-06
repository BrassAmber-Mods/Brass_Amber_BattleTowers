package com.brass_amber.ba_bt.datagen;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static com.brass_amber.ba_bt.BABattleTowers.locate;

public class BTModelProvider extends ItemModelProvider {
    public BTModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BABattleTowers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(BTItems.TAB_ICON);

        simpleExtraFolderItem(BTItems.LAND_MONOLITH_KEY, "monolith_key");
        simpleExtraFolderItem(BTItems.OCEAN_MONOLITH_KEY, "monolith_key");
        simpleExtraFolderItem(BTItems.CORE_MONOLITH_KEY, "monolith_key");
        simpleExtraFolderItem(BTItems.NETHER_MONOLITH_KEY, "monolith_key");
        simpleExtraFolderItem(BTItems.END_MONOLITH_KEY, "monolith_key");
        simpleExtraFolderItem(BTItems.SKY_MONOLITH_KEY, "monolith_key");

        simpleExtraFolderItem(BTItems.LAND_GOLEM_EYE, "guardian_eye");
        simpleExtraFolderItem(BTItems.OCEAN_GOLEM_EYE, "guardian_eye");
        simpleExtraFolderItem(BTItems.CORE_GOLEM_EYE, "guardian_eye");
        simpleExtraFolderItem(BTItems.NETHER_GOLEM_EYE, "guardian_eye");
        simpleExtraFolderItem(BTItems.END_GOLEM_EYE, "guardian_eye");
        simpleExtraFolderItem(BTItems.SKY_GOLEM_EYE, "guardian_eye");

        monolithItem(BTItems.LAND_MONOLITH);
        monolithItem(BTItems.OCEAN_MONOLITH);
        monolithItem(BTItems.CORE_MONOLITH);
        monolithItem(BTItems.NETHER_MONOLITH);
        monolithItem(BTItems.END_MONOLITH);
        monolithItem(BTItems.SKY_MONOLITH);

        simpleItem(BTItems.LAND_CHEST_SHARD);
        simpleItem(BTItems.OCEAN_CHEST_SHARD);
        simpleItem(BTItems.CORE_CHEST_SHARD);
        simpleItem(BTItems.NETHER_CHEST_SHARD);
        simpleItem(BTItems.END_CHEST_SHARD);
        simpleItem(BTItems.SKY_CHEST_SHARD);

        simpleExtraFolderItem(BTItems.LAND_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.OCEAN_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.CORE_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.NETHER_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.END_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.SKY_RESONANCE_CRYSTAL, "resonance_crystal");
        simpleExtraFolderItem(BTItems.CITY_RESONANCE_CRYSTAL, "resonance_crystal");

        wallInventory(BTBlocks.CORRITE_WALL.getId().getPath() + "_inventory", locate("block/corrite_block"));
    }

    private ItemModelBuilder emptyItem(ResourceLocation location) {
        return getBuilder(location.toString());
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BABattleTowers.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleExtraFolderItem(RegistryObject<Item> item, String folder) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(BABattleTowers.MOD_ID, "item/"+ folder + "/" + item.getId().getPath()));
    }

    private ItemModelBuilder resonanceStoneItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/template_music_disc")).texture("texture",
                new ResourceLocation(BABattleTowers.MOD_ID, "item/resonance_crystal/" + item.getId().getPath()));
    }


    private ItemModelBuilder monolithItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation(BABattleTowers.MOD_ID, "item/monolith_template")).texture("texture",
                new ResourceLocation(BABattleTowers.MOD_ID, "item/monolith/" + item.getId().getPath()));
    }
}
