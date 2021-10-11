package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.block.MonolithEntity;
import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntityAbstract;
import com.BrassAmber.ba_bt.entity.hostile.EndGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.OceanGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.SkyGolemEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
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

	public static final EntityType<BTGolemEntity> LAND = buildEntityType("land_golem", EntityType.Builder.of(BTGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());
	public static final EntityType<BTGolemEntity> CORE = buildEntityType("core_golem", EntityType.Builder.of(BTGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());
	public static final EntityType<BTGolemEntity> NETHER = buildEntityType("nether_golem", EntityType.Builder.of(BTGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());
	public static final EntityType<EndGolemEntity> END = buildEntityType("end_golem", EntityType.Builder.of(EndGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());
	public static final EntityType<SkyGolemEntity> SKY = buildEntityType("sky_golem", EntityType.Builder.of(SkyGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());
	public static final EntityType<OceanGolemEntity> OCEAN = buildEntityType("ocean_golem", EntityType.Builder.of(OceanGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE*2 * 0.6F, BTGolemEntityAbstract.SCALE*2 * 2).setTrackingRange(10).fireImmune());

	public static final EntityType<MonolithEntity> MONOLITH = buildEntityType("monolith", EntityType.Builder.<MonolithEntity>of(MonolithEntity::new, EntityClassification.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune());
	
	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(Register<EntityType<?>> event) {
		registerSpawnPlacement(LAND, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(CORE, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(NETHER, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(END, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(SKY, MobEntity::checkMobSpawnRules);
		registerSpawnPlacement(OCEAN, MobEntity::checkMobSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(LAND, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(CORE, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(NETHER, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(END, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(SKY, BTGolemEntityAbstract.createBattleGolemAttributes().build());
		event.put(OCEAN, BTGolemEntityAbstract.createBattleGolemAttributes().build());
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