package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.BrassAmber.ba_bt.entity.hostile.SilverSkeletonEntity;
import com.BrassAmber.ba_bt.entity.hostile.SkyMinionEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.entity.hostile.golem.EndGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.OceanGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.golem.SkyGolemEntity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Bus.MOD)
public class BTEntityTypes {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BrassAmberBattleTowers.MOD_ID);

	public static final EntityType<BTGolemEntity> LAND_GOLEM = buildGolemEntityType("land_golem", BTGolemEntity::new);
	public static final EntityType<OceanGolemEntity> OCEAN_GOLEM = buildGolemEntityType("ocean_golem", OceanGolemEntity::new);
	public static final EntityType<BTGolemEntity> NETHER_GOLEM = buildGolemEntityType("nether_golem", BTGolemEntity::new);
	public static final EntityType<BTGolemEntity> CORE_GOLEM = buildGolemEntityType("core_golem", BTGolemEntity::new);
	public static final EntityType<EndGolemEntity> END_GOLEM = buildGolemEntityType("end_golem", EndGolemEntity::new);
	public static final EntityType<SkyGolemEntity> SKY_GOLEM = buildGolemEntityType("sky_golem", SkyGolemEntity::new);

	public static final EntityType<SkyMinionEntity> SKY_MINION = buildEntityType("sky_minion", EntityType.Builder.of(SkyMinionEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.8F, 1.9F).clientTrackingRange(8));
	public static final EntityType<SilverSkeletonEntity> SILVER_SKELETON = buildEntityType("silver_skeleton", EntityType.Builder.of(SilverSkeletonEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8));
	
	public static final EntityType<MonolithEntity> LAND_MONOLITH = buildMonolithEntityType("land_monolith");
	public static final EntityType<MonolithEntity> OCEAN_MONOLITH = buildMonolithEntityType("ocean_monolith");
	public static final EntityType<MonolithEntity> NETHER_MONOLITH = buildMonolithEntityType("nether_monolith");
	public static final EntityType<MonolithEntity> CORE_MONOLITH = buildMonolithEntityType("core_monolith");
	public static final EntityType<MonolithEntity> END_MONOLITH = buildMonolithEntityType("end_monolith");
	public static final EntityType<MonolithEntity> SKY_MONOLITH = buildMonolithEntityType("sky_monolith");

	public static final EntityType<DestroyTowerEntity> DESTROY_TOWER = buildEntityType("destroy_tower",
			EntityType.Builder.<DestroyTowerEntity>of(DestroyTowerEntity::new, EntityClassification.MISC)
					.sized(1.0F, 1.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT));
	public static final EntityType<ExplosionPhysicsEntity> PHYSICS_EXPLOSION = buildEntityType("explosion_physics",
			EntityType.Builder.<ExplosionPhysicsEntity>of(ExplosionPhysicsEntity::new, EntityClassification.MISC)
					.sized(0.0F, 0.0F).setTrackingRange(100).immuneTo(Blocks.TNT).fireImmune().noSave());

	
	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(Register<EntityType<?>> event) {
		registerSpawnPlacement(LAND_GOLEM, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(OCEAN_GOLEM, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(NETHER_GOLEM, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(CORE_GOLEM, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(END_GOLEM, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(SKY_GOLEM, MobEntity::checkMobSpawnRules);

		registerSpawnPlacement(SKY_MINION, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(SILVER_SKELETON, MonsterEntity::checkMonsterSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(LAND_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(OCEAN_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(NETHER_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(CORE_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(END_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(SKY_GOLEM, BTGolemEntityAbstract.createBattleGolemAttributes().build());

		event.put(SKY_MINION, SkyMinionEntity.createAttributes().build());
		event.put(SILVER_SKELETON, SilverSkeletonEntity.createAttributes().build());
	}

	/**
	 * Helper method for creating Golem EntityTypes
	 */
	private static <T extends BTGolemEntityAbstract> EntityType<T> buildGolemEntityType(String registryName, EntityType.IFactory<T> typeFactory) {
		Builder<T> golemBuilder = EntityType.Builder.of(typeFactory, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE * 2 * 0.6F, BTGolemEntityAbstract.SCALE * 2 * 2).setTrackingRange(10).fireImmune();
		return buildEntityType(registryName, golemBuilder);
	}

	/**
	 * Helper method for creating Monolith EntityTypes
	 */
	private static EntityType<MonolithEntity> buildMonolithEntityType(String registryName) {
		EntityType.Builder<MonolithEntity> monolithBuilder = EntityType.Builder.<MonolithEntity>of(MonolithEntity::new, EntityClassification.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune();
		return buildEntityType(registryName, monolithBuilder);
	}

	/**
	 * Helper method for creating EntityTypes
	 */
	private static <T extends Entity> EntityType<T> buildEntityType(String registryName, EntityType.Builder<T> builder) {
		EntityType<T> type = builder.build(registryName);
		ENTITY_TYPES.register(registryName, () -> type);
		return type;
	}

	/**
	 * Helper method for registering Entity Spawning
	 */
	private static <T extends MobEntity> void registerSpawnPlacement(EntityType<T> entityType, EntitySpawnPlacementRegistry.IPlacementPredicate<T> placementPredicate) {
		EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}
}