package com.brass_amber.ba_bt.init;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.entity.LandDestructionEntity;
import com.brass_amber.ba_bt.entity.ExplosionPhysics;
import com.brass_amber.ba_bt.entity.OceanDestructionEntity;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.brass_amber.ba_bt.entity.block.BTLandObelisk;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.entity.block.BTOceanObelisk;
import com.brass_amber.ba_bt.entity.hostile.BTCultist;
import com.brass_amber.ba_bt.entity.hostile.SkyMinion;
import com.brass_amber.ba_bt.entity.hostile.golem.*;
import com.brass_amber.ba_bt.entity.hostile.golem.BTLandGolem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BTEntityType {


	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BrassAmberBattleTowers.MODID);

	//*********************** GOLEMS *********************\\
	public static final DeferredHolder<EntityType<?>, EntityType<BTLandGolem>> LAND_GOLEM = ENTITY_TYPES.register("land_golem", () -> EntityType.Builder.of( BTLandGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("land_golem"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTOceanGolem>> OCEAN_GOLEM = ENTITY_TYPES.register("ocean_golem", () -> EntityType.Builder.of( BTOceanGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("ocean_golem"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTCoreGolem>> CORE_GOLEM = ENTITY_TYPES.register("core_golem", () -> EntityType.Builder.of( BTCoreGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("core_golem"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTNetherGolem>> NETHER_GOLEM = ENTITY_TYPES.register("nether_golem", () -> EntityType.Builder.of(BTNetherGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("nether_golem"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTEndGolem>> END_GOLEM = ENTITY_TYPES.register("end_golem", () -> EntityType.Builder.of( BTEndGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("end_golem"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTSkyGolem>> SKY_GOLEM = ENTITY_TYPES.register("sky_golem", () -> EntityType.Builder.of( BTSkyGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("sky_golem"));

	//*********************** MONOLITHS *********************\\
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> LAND_MONOLITH = ENTITY_TYPES.register("land_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("land_monolith"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> OCEAN_MONOLITH = ENTITY_TYPES.register("ocean_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("ocean_monolith"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> CORE_MONOLITH = ENTITY_TYPES.register("core_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("core_monolith"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> NETHER_MONOLITH = ENTITY_TYPES.register("nether_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("nether_monolith"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> END_MONOLITH = ENTITY_TYPES.register("end_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("end_monolith"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTMonolith>> SKY_MONOLITH = ENTITY_TYPES.register("sky_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("sky_monolith"));

	//*********************** OBELISKS *********************\\
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> LAND_OBELISK = ENTITY_TYPES.register("land_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTLandObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("land_obelisk"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> OCEAN_OBELISK = ENTITY_TYPES.register("ocean_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTOceanObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("ocean_obelisk"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> CORE_OBELISK = ENTITY_TYPES.register("core_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("core_obelisk"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> NETHER_OBELISK = ENTITY_TYPES.register("nether_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("nether_obelisk"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> END_OBELISK = ENTITY_TYPES.register("end_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("end_obelisk"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTAbstractObelisk>> SKY_OBELISK = ENTITY_TYPES.register("sky_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).noSummon().sized(1.0F, 3.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).build("sky_obelisk"));

	//*********************** TOWER MOBS *********************\\
	public static final DeferredHolder<EntityType<?>, EntityType<SkyMinion>> SKY_MINION = ENTITY_TYPES.register("sky_minion", () -> EntityType.Builder.of(SkyMinion::new, MobCategory.MONSTER).fireImmune().sized(0.8F, 1.9F).clientTrackingRange(8).build("sky_minion"));
	public static final DeferredHolder<EntityType<?>, EntityType<BTCultist>> BT_CULTIST = ENTITY_TYPES.register("bt_cultist", () -> EntityType.Builder.of(BTCultist::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build("bt_cultist"));


	//*********************** MISC ENTITIES *********************\\
	public static final DeferredHolder<EntityType<?>, EntityType<LandDestructionEntity>> LAND_DESTRUCTION = ENTITY_TYPES.register("land_destruction",
			() -> EntityType.Builder.<LandDestructionEntity>of(LandDestructionEntity::new, MobCategory.MISC)
					.sized(1.0F, 1.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("land_destruction"));

	public static final DeferredHolder<EntityType<?>, EntityType<OceanDestructionEntity>> OCEAN_DESTRUCTION = ENTITY_TYPES.register("ocean_destruction",
			() -> EntityType.Builder.<OceanDestructionEntity>of(OceanDestructionEntity::new, MobCategory.MISC)
					.sized(1.0F, 1.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("ocean_destruction"));

	public static final DeferredHolder<EntityType<?>, EntityType<ExplosionPhysics>> PHYSICS_EXPLOSION = ENTITY_TYPES.register("explosion_physics",
			() -> EntityType.Builder.<ExplosionPhysics>of(ExplosionPhysics::new, MobCategory.MISC)
					.sized(0.0F, 0.0F).setTrackingRange(100).immuneTo(Blocks.TNT).fireImmune().noSave().noSummon().build("explosion_physics"));

	
	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(SpawnPlacementRegisterEvent event) {
		registerSpawnPlacement(event, LAND_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, OCEAN_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, NETHER_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, CORE_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, END_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, SKY_GOLEM.get(), Mob::checkMobSpawnRules);

		registerSpawnPlacement(event, SKY_MINION.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(event, BT_CULTIST.get(), Mob::checkMobSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(LAND_GOLEM.get(), BTLandGolem.createBattleGolemAttributes().build());
		event.put(OCEAN_GOLEM.get(), BTOceanGolem.createBattleGolemAttributes().build());
		event.put(NETHER_GOLEM.get(), BTNetherGolem.createBattleGolemAttributes().build());
		event.put(CORE_GOLEM.get(), BTCoreGolem.createBattleGolemAttributes().build());
		event.put(END_GOLEM.get(), BTEndGolem.createBattleGolemAttributes().build());
		event.put(SKY_GOLEM.get(), BTSkyGolem.createBattleGolemAttributes().build());

		event.put(SKY_MINION.get(), SkyMinion.createAttributes().build());
		event.put(BT_CULTIST.get(), BTCultist.createAttributes().build());
	}
	/**
	 * Helper method for registering Entity Spawning
	 */
	private static <T extends Mob> void registerSpawnPlacement(SpawnPlacementRegisterEvent event, EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> placementPredicate) {
		event.register(entityType, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placementPredicate, SpawnPlacementRegisterEvent.Operation.REPLACE);
	}

	public static void register(IEventBus eventBus) {
		ENTITY_TYPES.register(eventBus);
	}
}