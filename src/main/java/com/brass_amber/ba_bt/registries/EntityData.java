package com.brass_amber.ba_bt.registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityData {

    public static final Codec<EntityData> CODEC = RecordCodecBuilder.create(towerTypeInstance ->
            towerTypeInstance.group(
                    Codec.STRING.fieldOf("name").forGetter(entityData -> entityData.name),
                    Codec.STRING.fieldOf("color_code").forGetter(entityData -> entityData.colorCode),
                    Codec.STRING.fieldOf("translation_key").forGetter(towerData -> towerData.translationKey),
                    Music.CODEC.fieldOf("tower_music").forGetter(entityData -> entityData.towerMusic),
                    Music.CODEC.fieldOf("boss_music").forGetter(entityData -> entityData.bossMusic),
                    // Blocks
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("tower_chest").forGetter(entityData -> entityData.towerChest),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("golem_chest").forGetter(entityData -> entityData.golemChest),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("spawner_block").forGetter(entityData -> entityData.spawnerBlock),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("monolith_fill").forGetter(entityData -> entityData.monolithFillBlock),
                    // Items
                    ForgeRegistries.ITEMS.getCodec().fieldOf("monolith_item").forGetter(entityData -> entityData.monolithItem),
                    // Entity Reference
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("golem_entity").forGetter(entityData -> entityData.golemEntity),
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("monolith_entity").forGetter(entityData -> entityData.monolithEntity),
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("obelisk_entity").forGetter(entityData -> entityData.obeliskEntity),
                    // Progression Related
                    MobList.CODEC.listOf().optionalFieldOf("mobs_by_floor").forGetter(entityData -> Optional.ofNullable(entityData.mobsByFloor)),
                    MobList.CODEC.optionalFieldOf("mobs_by_tower").forGetter(entityData -> Optional.ofNullable(entityData.mobsByTower)),
                    MultiStateKeys.CODEC.fieldOf("key_items").forGetter(entityData -> entityData.multiStateKeys)
            ).apply(towerTypeInstance, EntityData::new)
    );

    private final String name;
    private final String colorCode;
    private final String translationKey;
    private final Music towerMusic;
    private final Music bossMusic;

    private final Block towerChest;
    private final Block golemChest;
    private final Block spawnerBlock;
    private final Block monolithFillBlock;

    private final Item monolithItem;

    private final EntityType<?> golemEntity;
    private final EntityType<?> monolithEntity;
    private final EntityType<?> obeliskEntity;

    @Nullable
    private final List<MobList> mobsByFloor;
    @Nullable
    private final MobList mobsByTower;
    private final MultiStateKeys multiStateKeys;

    // TODO TOWER GENERATION VALUES

    public EntityData(
            String name, String colorCode, String translationKey, Music towerMusic, Music bossMusic,
            Block towerChest, Block golemChest, Block spawnerBlock, Block monolithFillBlock, Item monolithItem,
            EntityType<?> golemEntity, EntityType<?> monolithEntity, EntityType<?> obeliskEntity,
            Optional<List<MobList>> mobsByFloor, Optional<MobList> mobsByTower, MultiStateKeys multiStateKeys
    ) {
        this.name = name;
        this.colorCode = colorCode;
        this.translationKey = translationKey;
        this.towerMusic = towerMusic;
        this.bossMusic = bossMusic;

        this.towerChest = towerChest;
        this.golemChest = golemChest;
        this.spawnerBlock = spawnerBlock;
        this.monolithFillBlock = monolithFillBlock;

        this.monolithItem = monolithItem;

        this.golemEntity = golemEntity;
        this.monolithEntity = monolithEntity;
        this.obeliskEntity = obeliskEntity;

        this.mobsByFloor = mobsByFloor.orElse(null);
        this.mobsByTower = mobsByTower.orElse(null);

        if (this.mobsByTower == null && this.mobsByFloor == null) {
            throw new RuntimeException("""
                    TowerType with name %s is invalid.
                    Both mobsByFloor and mobsByTower are missing.
                    Please correct this error so that the tower can be initialized correctly
                    """.formatted(this.name)
            );
        }
        this.multiStateKeys = multiStateKeys;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getColorCode() {
        return colorCode;
    }

    public EntityType<?> getSpawnerMobForFloor(int floor, RandomSource randomSource) {
        List<EntityType<?>> mobs;
        if (this.mobsByTower != null) {
            mobs = this.mobsByTower.getMobs();
            return mobs.get(randomSource.nextInt(mobs.size()));
        } else if (this.mobsByFloor != null) {
            mobs = this.mobsByFloor.get(floor).getMobs();
            return mobs.get(randomSource.nextInt(mobs.size()));
        } else {
            // Return an obviously wrong entity so that the game doesn't hard crash
            return EntityType.MINECART;
        }
    }

    public Music getTowerMusic() {
        return towerMusic;
    }

    public Music getBossMusic() {
        return bossMusic;
    }

    public Block getTowerChest() {
        return towerChest;
    }

    public Block getGolemChest() {
        return golemChest;
    }

    public Block getSpawnerBlock() {
        return spawnerBlock;
    }

    public Block getMonolithFillBlock() {
        return monolithFillBlock;
    }

    public Item getMonolithItem() {
        return monolithItem;
    }

    public EntityType<?> getGolemEntity() {
        return golemEntity;
    }

    public EntityType<?> getMonolithEntity() {
        return monolithEntity;
    }

    public MultiStateKeys getMultiStateKeys() {
        return multiStateKeys;
    }

    public int getKeyMax(int state) {
        return this.multiStateKeys.getKeyMax(state);
    }

    public record MobList(List<String> mobIds) {
        public static final Codec<MobList> CODEC = RecordCodecBuilder.create(mobListInstance ->
                mobListInstance.group(
                        Codec.STRING.listOf().fieldOf("mobIds").forGetter(MobList::mobIds)
                ).apply(mobListInstance, MobList::new)
        );

        public List<EntityType<?>> getMobs() {
            return this.mobIds.stream()
                    .filter(mobId -> ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(mobId)))
                    .map(mobId -> ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(mobId)))
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    // IF STATE == 0 USE FIRST TEXTURE ETC FOR MONOLITHS

    public static class MultiStateKeys {
        public static final MapCodec<MultiStateKeys> CODEC = RecordCodecBuilder.mapCodec(multiStateInstance ->
                multiStateInstance.group(
                        ForgeRegistries.ITEMS.getCodec().listOf().listOf().fieldOf("keys").forGetter(multiStateKeys -> multiStateKeys.keys)
                ).apply(multiStateInstance, MultiStateKeys::new)
        );

        private final int length;
        public List<List<Item>> keys;

        public MultiStateKeys(List<List<Item>> keys) {

            this.length = keys.size();
            this.keys = keys;
            if (this.keys.isEmpty()) {
                throw new RuntimeException("""
                        Key Items in Entity Data is invalid.
                        keys value does not contain valid items.
                        Please correct this error so that the tower can be initialized correctly
                        """
                );
            }
        }


        public boolean keyMatch(ItemStack keyTest, int state, int id) {
            return keyTest.is(this.keys.get(state).get(id));
        }

        public int getKeyMax(int state){
            return this.keys.get(state).size();
        }

        public int getMaxState() {
            return this.length-1;
        }
    }
}
