package com.BrassAmber.ba_bt.init;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.entity.DestroyTower;
import com.BrassAmber.ba_bt.entity.ExplosionPhysics;
import com.BrassAmber.ba_bt.entity.block.BTAbstractObelisk;
import com.BrassAmber.ba_bt.entity.block.BTMonolith;
import com.BrassAmber.ba_bt.entity.hostile.BTCultist;
import com.BrassAmber.ba_bt.entity.hostile.PlatinumSkeleton;
import com.BrassAmber.ba_bt.entity.hostile.SkyMinion;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.BTAbstractGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.EndGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.OceanGolem;
import com.BrassAmber.ba_bt.entity.hostile.golem.SkyGolem;

import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
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
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Bus.MOD)
public class BTEntityTypes {


	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BrassAmberBattleTowers.MOD_ID);

	//*********************** GOLEMS *********************\\
	public static final RegistryObject<EntityType<BTGolem>> LAND_GOLEM = ENTITY_TYPES.register("land_golem", () -> EntityType.Builder.of( BTGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("land_golem"));
	public static final RegistryObject<EntityType<OceanGolem> >OCEAN_GOLEM = ENTITY_TYPES.register("ocean_golem", () -> EntityType.Builder.of( OceanGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("ocean_golem"));
	public static final RegistryObject<EntityType<BTGolem>> CORE_GOLEM = ENTITY_TYPES.register("core_golem", () -> EntityType.Builder.of( BTGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("core_golem"));
	public static final RegistryObject<EntityType<BTGolem>> NETHER_GOLEM = ENTITY_TYPES.register("nether_golem", () -> EntityType.Builder.of( BTGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("nether_golem"));
	public static final RegistryObject<EntityType<EndGolem>> END_GOLEM = ENTITY_TYPES.register("end_golem", () -> EntityType.Builder.of( EndGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("end_golem"));
	public static final RegistryObject<EntityType<SkyGolem>> SKY_GOLEM = ENTITY_TYPES.register("sky_golem", () -> EntityType.Builder.of( SkyGolem::new, MobCategory.MONSTER).sized(BTAbstractGolem.SCALE * 2 * 0.6F, BTAbstractGolem.SCALE * 2 * 2).setTrackingRange(10).fireImmune().build("sky_golem"));

	//*********************** MONOLITHS *********************\\
	public static final RegistryObject<EntityType<BTMonolith>> LAND_MONOLITH = ENTITY_TYPES.register("land_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("land_monolith"));
	public static final RegistryObject<EntityType<BTMonolith>> OCEAN_MONOLITH = ENTITY_TYPES.register("ocean_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("ocean_monolith"));
	public static final RegistryObject<EntityType<BTMonolith>> CORE_MONOLITH = ENTITY_TYPES.register("core_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("core_monolith"));
	public static final RegistryObject<EntityType<BTMonolith>> NETHER_MONOLITH = ENTITY_TYPES.register("nether_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("nether_monolith"));
	public static final RegistryObject<EntityType<BTMonolith>> END_MONOLITH = ENTITY_TYPES.register("end_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("end_monolith"));
	public static final RegistryObject<EntityType<BTMonolith>> SKY_MONOLITH = ENTITY_TYPES.register("sky_monolith", () -> EntityType.Builder.<BTMonolith>of(BTMonolith::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(16).updateInterval(Integer.MAX_VALUE).fireImmune().immuneTo(Blocks.TNT).build("sky_monolith"));

	//*********************** OBELISKS *********************\\
	public static final RegistryObject<EntityType<BTAbstractObelisk>> LAND_OBELISK = ENTITY_TYPES.register("land_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("land_obelisk"));
	public static final RegistryObject<EntityType<BTAbstractObelisk>> OCEAN_OBELISK = ENTITY_TYPES.register("ocean_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("ocean_obelisk"));
	public static final RegistryObject<EntityType<BTAbstractObelisk>> CORE_OBELISK = ENTITY_TYPES.register("core_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("core_obelisk"));
	public static final RegistryObject<EntityType<BTAbstractObelisk>> NETHER_OBELISK = ENTITY_TYPES.register("nether_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("nether_obelisk"));
	public static final RegistryObject<EntityType<BTAbstractObelisk>> END_OBELISK = ENTITY_TYPES.register("end_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("end_obelisk"));
	public static final RegistryObject<EntityType<BTAbstractObelisk>> SKY_OBELISK = ENTITY_TYPES.register("sky_obelisk", () -> EntityType.Builder.<BTAbstractObelisk>of(BTAbstractObelisk::new, MobCategory.MISC).sized(1.0F, 2.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("sky_obelisk"));

	//*********************** TOWER MOBS *********************\\
	public static final RegistryObject<EntityType<SkyMinion>> SKY_MINION = ENTITY_TYPES.register("sky_minion", () -> EntityType.Builder.of(SkyMinion::new, MobCategory.MONSTER).fireImmune().sized(0.8F, 1.9F).clientTrackingRange(8).build("sky_minion"));
	public static final RegistryObject<EntityType<PlatinumSkeleton>> PLATINUM_SKELETON = ENTITY_TYPES.register("platinum_skeleton", () -> EntityType.Builder.of(PlatinumSkeleton::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build("platinum_skeleton"));
	public static final RegistryObject<EntityType<BTCultist>> BT_CULTIST = ENTITY_TYPES.register("bt_cultist", () -> EntityType.Builder.of(BTCultist::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build("bt_cultist"));


	//*********************** MISC ENTITIES *********************\\
	public static final RegistryObject<EntityType<DestroyTower>> DESTROY_TOWER = ENTITY_TYPES.register("destroy_tower",
			() -> EntityType.Builder.<DestroyTower>of(DestroyTower::new, MobCategory.MISC)
					.sized(1.0F, 1.0F).setTrackingRange(100).fireImmune().immuneTo(Blocks.TNT).noSummon().build("destroy_tower"));

	public static final RegistryObject<EntityType<ExplosionPhysics>> PHYSICS_EXPLOSION = ENTITY_TYPES.register("explosion_physics",
			() -> EntityType.Builder.<ExplosionPhysics>of(ExplosionPhysics::new, MobCategory.MISC)
					.sized(0.0F, 0.0F).setTrackingRange(100).immuneTo(Blocks.TNT).fireImmune().noSave().noSummon().build("explosion_physics"));

	
	/**
	 * Register Spawn Rules
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRegisterEntityTypes(Register<EntityType<?>> event) {
		registerSpawnPlacement(LAND_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(OCEAN_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(NETHER_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(CORE_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(END_GOLEM.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(SKY_GOLEM.get(), Mob::checkMobSpawnRules);

		registerSpawnPlacement(SKY_MINION.get(), Mob::checkMobSpawnRules);
		registerSpawnPlacement(PLATINUM_SKELETON.get(), Monster::checkMonsterSpawnRules);
		registerSpawnPlacement(BT_CULTIST.get(), Mob::checkMobSpawnRules);
	}

	/**
	 * Initialize Mob Attributes
	 */
	@SubscribeEvent
	public static void initializeAttributes(EntityAttributeCreationEvent event) {
		event.put(LAND_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(OCEAN_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(NETHER_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(CORE_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(END_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());
		event.put(SKY_GOLEM.get(), BTAbstractGolem.createBattleGolemAttributes().build());

		event.put(SKY_MINION.get(), SkyMinion.createAttributes().build());
		event.put(PLATINUM_SKELETON.get(), PlatinumSkeleton.createAttributes().build());
		event.put(BT_CULTIST.get(), BTCultist.createAttributes().build());
	}
	/**
	 * Helper method for registering Entity Spawning
	 */
	private static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.SpawnPredicate<T> placementPredicate) {
		SpawnPlacements.register(entityType, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, placementPredicate);
	}
}