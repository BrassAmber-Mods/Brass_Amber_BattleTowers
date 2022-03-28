package com.BrassAmber.ba_bt.sound;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BTMusics {

    public static final Music GOLEM_FIGHT = new Music(BTSoundEvents.MUSIC_GOLEM_FIGHT, 5000, 6000, false);
    public static final Music TOWER = new Music(BTSoundEvents.MUSIC_TOWER, 7000, 8000, false);

}
