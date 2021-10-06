package com.BrassAmber.ba_bt.sound;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(BrassAmberBattleTowers.MOD_ID)
@EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Bus.MOD)
public class BTSoundEvents {
	public static final SoundEvent ENTITY_GOLEM_HURT = newSoundEvent("entity.golem.hurt");
	public static final SoundEvent ENTITY_GOLEM_DEATH = newSoundEvent("entity.golem.death");
	public static final SoundEvent ENTITY_GOLEM_CHARGE = newSoundEvent("entity.golem.charge");
	public static final SoundEvent ENTITY_GOLEM_AWAKEN = newSoundEvent("entity.golem.awaken");
	public static final SoundEvent ENTITY_GOLEM_AMBIENT = newSoundEvent("entity.golem.ambient");
	public static final SoundEvent ENTITY_GOLEM_SPECIAL = newSoundEvent("entity.golem.special");
	
	public static final SoundEvent TOWER_BREAK_START = newSoundEvent("tower.break.start");
	public static final SoundEvent TOWER_BREAK_CRUMBLE = newSoundEvent("tower.break.crumble");
	
	private static SoundEvent newSoundEvent(String key) {
		return new SoundEvent(BrassAmberBattleTowers.locate(key));
	}
	
	@SubscribeEvent
	public static void onRegisterSoundEvents(Register<SoundEvent> event) {
		register(event.getRegistry(), ENTITY_GOLEM_HURT);
		register(event.getRegistry(), ENTITY_GOLEM_DEATH);
		register(event.getRegistry(), ENTITY_GOLEM_CHARGE);
		register(event.getRegistry(), ENTITY_GOLEM_AWAKEN);
		register(event.getRegistry(), ENTITY_GOLEM_AMBIENT);
		register(event.getRegistry(), ENTITY_GOLEM_SPECIAL);
		
		register(event.getRegistry(), TOWER_BREAK_START);
		register(event.getRegistry(), TOWER_BREAK_CRUMBLE);
		
		BrassAmberBattleTowers.LOGGER.info("SoundEvents Registered");
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, T object) {
		SoundEvent soundEvent = (SoundEvent) object;
		object.setRegistryName(soundEvent.getLocation());
		registry.register(object);
	}
}
