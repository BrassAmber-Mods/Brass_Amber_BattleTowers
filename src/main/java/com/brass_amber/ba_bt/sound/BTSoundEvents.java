package com.brass_amber.ba_bt.sound;

import com.brass_amber.ba_bt.BABTMain;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class BTSoundEvents {

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, BABTMain.MODID);

	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_HURT = registerSoundEvent("entity.golem.hurt");
	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_DEATH = registerSoundEvent("entity.golem.death");
	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_CHARGE = registerSoundEvent("entity.golem.charge");
	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_AWAKEN = registerSoundEvent("entity.golem.awaken");
	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_AMBIENT = registerSoundEvent("entity.golem.ambient");
	public static final RegistryObject<SoundEvent> ENTITY_GOLEM_SPECIAL = registerSoundEvent("entity.golem.special");

	public static final RegistryObject<SoundEvent> MUSIC_LAND_GOLEM_FIGHT = registerSoundEvent("entity.golem.land.fight");
	public static final RegistryObject<SoundEvent> MUSIC_OCEAN_GOLEM_FIGHT = registerSoundEvent("entity.golem.ocean.fight");
	public static final RegistryObject<SoundEvent> MUSIC_CORE_GOLEM_FIGHT = registerSoundEvent("entity.golem.core.fight");
	public static final RegistryObject<SoundEvent> MUSIC_NETHER_GOLEM_FIGHT = registerSoundEvent("entity.golem.nether.fight");
	public static final RegistryObject<SoundEvent> MUSIC_END_GOLEM_FIGHT = registerSoundEvent("entity.golem.end.fight");
	public static final RegistryObject<SoundEvent> MUSIC_SKY_GOLEM_FIGHT = registerSoundEvent("entity.golem.sky.fight");

	public static final RegistryObject<SoundEvent> MONOLITH_SPAWN_GOLEM = registerSoundEvent("monolith.spawn.golem");

	public static final RegistryObject<SoundEvent> TOWER_BREAK_START = registerSoundEvent("tower.break.start");
	public static final RegistryObject<SoundEvent> TOWER_BREAK_CRUMBLE = registerSoundEvent("tower.break.crumble");
	public static final RegistryObject<SoundEvent> TOWER_COLLAPSE = registerSoundEvent("tower.break.collapse");

	public static final RegistryObject<SoundEvent> MUSIC_LAND_TOWER = registerSoundEvent("tower.ambient.music.land");
	public static final RegistryObject<SoundEvent> MUSIC_OCEAN_TOWER = registerSoundEvent("tower.ambient.music.ocean");
	public static final RegistryObject<SoundEvent> MUSIC_CORE_TOWER = registerSoundEvent("tower.ambient.music.core");
	public static final RegistryObject<SoundEvent> MUSIC_NETHER_TOWER = registerSoundEvent("tower.ambient.music.nether");
	public static final RegistryObject<SoundEvent> MUSIC_END_TOWER = registerSoundEvent("tower.ambient.music.end");
	public static final RegistryObject<SoundEvent> MUSIC_SKY_TOWER = registerSoundEvent("tower.ambient.music.sky");
	public static final RegistryObject<SoundEvent> MUSIC_CITY = registerSoundEvent("tower.ambient.music.city");


	/**
	 * Helper method for registering all SoundEvents
	 */
	private static RegistryObject<SoundEvent> registerSoundEvent(String registryName) {
		SoundEvent soundEvent = SoundEvent.createFixedRangeEvent(BABTMain.locate(registryName), 50);
		return SOUND_EVENTS.register(registryName, () -> soundEvent);
	}

	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}

}
