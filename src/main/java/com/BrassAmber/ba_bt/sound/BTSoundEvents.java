package com.BrassAmber.ba_bt.sound;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTSoundEvents {

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BrassAmberBattleTowers.MOD_ID);

	public static final SoundEvent ENTITY_GOLEM_HURT = registerSoundEvent("entity.golem.hurt");
	public static final SoundEvent ENTITY_GOLEM_DEATH = registerSoundEvent("entity.golem.death");
	public static final SoundEvent ENTITY_GOLEM_CHARGE = registerSoundEvent("entity.golem.charge");
	public static final SoundEvent ENTITY_GOLEM_AWAKEN = registerSoundEvent("entity.golem.awaken");
	public static final SoundEvent ENTITY_GOLEM_AMBIENT = registerSoundEvent("entity.golem.ambient");
	public static final SoundEvent ENTITY_GOLEM_SPECIAL = registerSoundEvent("entity.golem.special");

	public static final SoundEvent MONOLITH_SPAWN_GOLEM = registerSoundEvent("monolith.spawn.golem");

	public static final SoundEvent TOWER_BREAK_START = registerSoundEvent("tower.break.start");
	public static final SoundEvent TOWER_BREAK_CRUMBLE = registerSoundEvent("tower.break.crumble");

	/**
	 * Helper method for registering all SoundEvents
	 */
	private static SoundEvent registerSoundEvent(String registryName) {
		SoundEvent soundEvent = new SoundEvent(BrassAmberBattleTowers.locate(registryName));
		SOUND_EVENTS.register(registryName, () -> soundEvent);
		return soundEvent;
	}
}
