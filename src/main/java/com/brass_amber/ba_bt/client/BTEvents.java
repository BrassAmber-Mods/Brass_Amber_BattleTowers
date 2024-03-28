package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;


import static com.brass_amber.ba_bt.BrassAmberBattleTowers.SAVE_TOWERS;


@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BTEvents {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onDeath(PlayerEvent.PlayerRespawnEvent event) {
        BrassAmberBattleTowers.LOGGER.info("In Respawn code-");
        Player player = event.getEntity();

        if (player.level().isClientSide() || !event.isEndConquered()) {
            Minecraft mc = Minecraft.getInstance();
            MusicManager musicManager = mc.getMusicManager();
            if (musicManager.isPlayingMusic(BTSoundEvents.LAND_TOWER_MUSIC) || musicManager.isPlayingMusic(BTSoundEvents.LAND_GOLEM_FIGHT_MUSIC)) {
                musicManager.stopPlaying();
            }
        }
    }

    @SubscribeEvent
    public static void serverSetup(ServerAboutToStartEvent event) {
        SAVE_TOWERS.setServer(event.getServer());
    }


    @SubscribeEvent
    public static void serverStop(ServerStoppedEvent event) {
        SAVE_TOWERS.serverClosed();
    }

}