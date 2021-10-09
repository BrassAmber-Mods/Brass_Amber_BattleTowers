package com.BrassAmber.ba_bt.entity;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntity;
import com.BrassAmber.ba_bt.entity.hostile.BTGolemEntityAbstract;

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

	public static final EntityType<BTGolemEntity> GOLEM = buildEntityType("land_golem", EntityType.Builder.of(BTGolemEntity::new, EntityClassification.MONSTER).sized(BTGolemEntityAbstract.SCALE * 0.6F, BTGolemEntityAbstract.SCALE * 2).setTrackingRange(10).fireImmune());

	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(Register<EntityType<?>> event) {
		completeEntity(GOLEM, MobEntity::checkMobSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(GOLEM, BTGolemEntityAbstract.createMobAttributes().build());
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
	private static <T extends MobEntity> void completeEntity(EntityType<T> entityType, EntitySpawnPlacementRegistry.IPlacementPredicate<T> placementPredicate) {
		EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}
}