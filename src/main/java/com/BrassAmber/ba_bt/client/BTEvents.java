package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.sound.BTMusics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BTEvents {

    @SubscribeEvent
    public static void onDeath(PlayerEvent.PlayerRespawnEvent event) {
        BrassAmberBattleTowers.LOGGER.info("In Respawn code-");
        Player player = event.getPlayer();

        if (player.level.isClientSide() || !event.isEndConquered()) {
            Minecraft mc = Minecraft.getInstance();
            MusicManager musicManager = mc.getMusicManager();
            if (musicManager.isPlayingMusic(BTMusics.LAND_TOWER) || musicManager.isPlayingMusic(BTMusics.LAND_GOLEM_FIGHT)) {
                musicManager.stopPlaying();
            }
        }
    }

}
