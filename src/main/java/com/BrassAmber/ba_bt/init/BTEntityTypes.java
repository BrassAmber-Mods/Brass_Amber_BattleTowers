package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.DestroyTower;
import com.BrassAmber.ba_bt.entity.ExplosionPhysics;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.BrassAmber.ba_bt.entity.hostile.SilverSkeleton;
import com.BrassAmber.ba_bt.entity.hostile.SkyMinion;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.EndGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.OceanGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.SkyGolem;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityType.Builder;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
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

	public static final EntityType<BTGolem> LAND_GOLEM = buildGolemEntityType("land_golem", BTGolem::new);
	public static final EntityType<OceanGolem> OCEAN_GOLEM = buildGolemEntityType("ocean_golem", OceanGolem::new);
	public static final EntityType<BTGolem> NETHER_GOLEM = buildGolemEntityType("nether_golem", BTGolem::new);
	public static final EntityType<BTGolem> CORE_GOLEM = buildGolemEntityType("core_golem", BTGolem::new);
	public static final EntityType<EndGolem> END_GOLEM = buildGolemEntityType("end_golem", EndGolem::new);
	public static final EntityType<SkyGolem> SKY_GOLEM = buildGolemEntityType("sky_golem", SkyGolem::new);

	public static final EntityType<SkyMinion> SKY_MINION = buildEntityType("sky_minion", EntityType.Builder.of(SkyMinion::new, MobCategory.MONSTER).fireImmune().sized(0.8F, 1.9F).clientTrackingRange(8));
	public static final EntityType<SilverSkeleton> SILVER_SKELETON = buildEntityType("silver_skeleton", EntityType.Builder.of(SilverSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8));
	
	public static final EntityType<MonolithEntity> LAND_MONOLITH = buildMonolithEntityType("land_monolith");
	public static final EntityType<MonolithEntity> OCEAN_MONOLITH = buildMonolithEntityType("ocean_monolith");
	public static final EntityType<MonolithEntity> NETHER_MONOLITH = buildMonolithEntityType("nether_monolith");
	public static final EntityType<MonolithEntity> CORE_MONOLITH = buildMonolithEntityType("core_monolith");
	public static final EntityType<MonolithEntity> END_MONOLITH = buildMonolithEntityType("end_monolith");
	public static final EntityType<MonolithEntity> SKY_MONOLITH = buildMonolithEntityType("sky_monolith");

	public static final EntityType<DestroyTower> DESTROY_TOWER = buildEntityType("destroy_tower",
			EntityType.Builder.<DestroyTower>of(DestroyTower::new, MobCategory.MISC)
					.sized(1.0F, 1.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT));
	public static final EntityType<ExplosionPhysics> PHYSICS_EXPLOSION = buildEntityType("explosion_physics",
			EntityType.Builder.<ExplosionPhysics>of(ExplosionPhysics::new, MobCategory.MISC)
					.sized(0.0F, 0.0F).setTrackingRange(100).immuneTo(Blocks.TNT).fireImmune().noSave());

	
	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(Register<EntityType<?>> event) {
		registerSpawnPlacement(LAND_GOLEM, Mob::checkMobSpawnRules);
		registerSpawnPlacement(OCEAN_GOLEM, Mob::checkMobSpawnRules);
		registerSpawnPlacement(NETHER_GOLEM, Mob::checkMobSpawnRules);
		registerSpawnPlacement(CORE_GOLEM, Mob::checkMobSpawnRules);
		registerSpawnPlacement(END_GOLEM, Mob::checkMobSpawnRules);
		registerSpawnPlacement(SKY_GOLEM, Mob::checkMobSpawnRules);

		registerSpawnPlacement(SKY_MINION, Mob::checkMobSpawnRules);
		registerSpawnPlacement(SILVER_SKELETON, Monster::checkMonsterSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(LAND_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(OCEAN_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(NETHER_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(CORE_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(END_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(SKY_GOLEM, BTAbstractGolem.createBattleGolemAttributes().build());

		event.put(SKY_MINION, SkyMinion.createAttributes().build());
		event.put(SILVER_SKELETON, SilverSkeleton.createAttributes().build());
	}

	/**
	 * Helper method for creating Golem EntityTypes
	 */
	private static <T extends BTAbstractGolem> EntityType<T> buildGolemEntityType(String registryName, EntityType.IFactory<T> typeFactory) {
		Builder<T> golemBuilder = EntityType.Builder.of(typeFactory, EntityClassification.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune();
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
	private static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> placementPredicate) {
		SpawnPlacements.register(entityType, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}
}