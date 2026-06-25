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
                    MobList.CODEC.listOf().optionalFieldOf("mobs_by_floor").forGetter(entityData -> Optional.ofNullable(entityData.mobsByFloor)),
                    MobList.CODEC.optionalFieldOf("mobs_by_tower").forGetter(entityData -> Optional.ofNullable(entityData.mobsByTower)),
                    Music.CODEC.fieldOf("tower_music").forGetter(entityData -> entityData.towerMusic),
                    Music.CODEC.fieldOf("boss_music").forGetter(entityData -> entityData.bossMusic),
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("golem_entity").forGetter(entityData -> entityData.golemEntity),
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("monolith_entity").forGetter(entityData -> entityData.monolithEntity),
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("obelisk_entity").forGetter(entityData -> entityData.obeliskEntity),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("tower_chest").forGetter(entityData -> entityData.towerChest),
                    ForgeRegistries.BLOCKS.getCodec().fieldOf("golem_chest").forGetter(entityData -> entityData.golemChest),
                    MultiStateKeys.CODEC.fieldOf("key_items").forGetter(entityData -> entityData.multiStateKeys)
            ).apply(towerTypeInstance, EntityData::new)
    );

    private final String name;
    private final String colorCode;
    private final Music towerMusic;
    private final Music bossMusic;
    private final EntityType<?> golemEntity;
    private final EntityType<?> monolithEntity;
    private final EntityType<?> obeliskEntity;
    private final Block towerChest;
    private final Block golemChest;
    private final Block spawnerBlock;


    @Nullable
    private final List<MobList> mobsByFloor;
    @Nullable
    private final MobList mobsByTower;
    private final MultiStateKeys multiStateKeys;

    // TODO TOWER GENERATION VALUES

    public EntityData(
            String name, String colorCode, Music towerMusic, Music bossMusic,
            EntityType<?> golemEntity, EntityType<?> monolithEntity, EntityType<?> obeliskEntity,
            Block towerChest, Block golemChest, Block spawnerBlock,
            Optional<List<MobList>> mobsByFloor, Optional<MobList> mobsByTower, List<SpawnDataRecord> spawnDataRecords,
            MultiStateKeys multiStateKeys
    ) {
        this.name = name;
        this.colorCode = colorCode;
        this.towerMusic = towerMusic;
        this.bossMusic = bossMusic;
        this.mobsByFloor = mobsByFloor.orElse(null);
        this.mobsByTower = mobsByTower.orElse(null);

        this.towerChest = towerChest;
        this.golemChest = golemChest;
        this.multiStateKeys = multiStateKeys;
        if (this.mobsByTower == null && this.mobsByFloor == null) {
            throw new RuntimeException("""
                    TowerType with name %s is invalid.
                    Both mobsByFloor and mobsByTower are missing.
                    Please correct this error so that the tower can be initialized correctly
                    """.formatted(this.name)
            );
        }
        this.golemEntity = golemEntity;
        this.monolithEntity = monolithEntity;
        this.obeliskEntity = obeliskEntity;

    }

    public String getName() {
        return name;
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
            return EntityType.MINECART;
        }
    }

    public Music getTowerMusic() {
        return towerMusic;
    }

    public Music getBossMusic() {
        return bossMusic;
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
                        ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("keys").forGetter(multiStateKeys -> multiStateKeys.keys),
                        com.mojang.serialization.Codec.INT.listOf().fieldOf("keysPerState").forGetter(multiStateKeys -> multiStateKeys.keysPerState)
                ).apply(multiStateInstance, MultiStateKeys::new)
        );

        private int id;
        private final int length;
        private int state;
        public List<Item> keys;
        public List<Integer> keysPerState;

        public MultiStateKeys(List<Item> keys, List<Integer> keysPerState) {
            this.id = 0;
            this.length = keys.size();
            this.state = 0;
            this.keys = keys;
            this.keysPerState = keysPerState;
        }

        public boolean shouldUnlock() {
            return this.id == this.length;
        }

        public int getState() {
            return this.state;
        }

        public boolean keyMatch(ItemStack keyTest) {
            if (keyTest.is(this.keys.get(this.id))) {
                this.id++;
                if (this.id == this.keysPerState.get(this.state)) {
                    this.state++;
                }
                return true;
            }
            return false;
        }

        public Optional<Item> getNextKey() {
            return this.id + 1 < this.keys.size() ? Optional.of(this.keys.get(this.id + 1)) : Optional.empty();
        }

        public Optional<Item> getKey(int key_id) {
            return key_id < this.keys.size() ? Optional.of(this.keys.get(key_id)) : Optional.empty();
        }
    }
}
