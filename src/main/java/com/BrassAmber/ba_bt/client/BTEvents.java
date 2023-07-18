package com.BrassAmber.ba_bt.client;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.sound.BTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.BrassAmber.ba_bt.BrassAmberBattleTowers.SAVETOWERS;


@Mod.EventBusSubscriber(modid = BrassAmberBattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
    public static void serverSetup(ServerStartedEvent event) {
        SAVETOWERS.setServer(event.getServer());
    }

    @SubscribeEvent
    public static void serverStop(ServerStoppedEvent event) {
        SAVETOWERS.serverClosed();
    }

}