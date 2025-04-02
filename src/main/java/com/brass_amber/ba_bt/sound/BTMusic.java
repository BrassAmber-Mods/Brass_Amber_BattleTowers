package com.brass_amber.ba_bt.sound;

import net.minecraft.sounds.Music;

import static com.brass_amber.ba_bt.sound.BTSoundEvents.*;

public class BTMusic {

    	public static final Music TOWER_COLLAPSE_MUSIC = new Music(TOWER_COLLAPSE.getHolder().get(), 1500, 2100, false);

	public static final Music LAND_TOWER_MUSIC = new Music(MUSIC_LAND_TOWER.getHolder().get(), 3940, 4240, false);
	public static final Music LAND_GOLEM_FIGHT_MUSIC = new Music(MUSIC_LAND_GOLEM_FIGHT.getHolder().get(), 2340, 2640, false);
	public static final Music OCEAN_TOWER_MUSIC = new Music(MUSIC_OCEAN_TOWER.getHolder().get(), 4780, 5070, false);
	public static final Music OCEAN_GOLEM_FIGHT_MUSIC = new Music(MUSIC_OCEAN_GOLEM_FIGHT.getHolder().get(), 1980, 2280, false);
	public static final Music CORE_TOWER_MUSIC = new Music(MUSIC_CORE_TOWER.getHolder().get(), 4440, 4740, false);
	public static final Music CORE_GOLEM_FIGHT_MUSIC = new Music(MUSIC_CORE_GOLEM_FIGHT.getHolder().get(), 2400, 2700, false);
	public static final Music NETHER_TOWER_MUSIC = new Music(MUSIC_NETHER_TOWER.getHolder().get(), 3880, 4180, false);
	public static final Music NETHER_GOLEM_FIGHT_MUSIC = new Music(MUSIC_NETHER_GOLEM_FIGHT.getHolder().get(), 2440, 2740, false);
	public static final Music END_TOWER_MUSIC = new Music(MUSIC_END_TOWER.getHolder().get(), 5060, 5360, false);
	public static final Music END_GOLEM_FIGHT_MUSIC = new Music(MUSIC_END_GOLEM_FIGHT.getHolder().get(), 2980, 3280, false);
	public static final Music SKY_TOWER_MUSIC = new Music(MUSIC_SKY_TOWER.getHolder().get(), 3940, 4240, false);
	public static final Music SKY_GOLEM_FIGHT_MUSIC = new Music(MUSIC_SKY_GOLEM_FIGHT.getHolder().get(), 2040, 2340, false);
	public static final Music CITY_TOWER_MUSIC = new Music(MUSIC_CITY.getHolder().get(), 6560, 6860, false);
}
