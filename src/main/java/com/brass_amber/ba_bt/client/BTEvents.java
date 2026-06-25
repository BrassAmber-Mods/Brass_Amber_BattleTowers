package com.brass_amber.ba_bt.client;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.entity.CorreyeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import static com.brass_amber.ba_bt.BABattleTowers.SAVE_TOWERS;
import static com.brass_amber.ba_bt.sound.BTMusic.LAND_GOLEM_FIGHT_MUSIC;
import static com.brass_amber.ba_bt.sound.BTMusic.LAND_TOWER_MUSIC;


@Mod.EventBusSubscriber(modid = BABattleTowers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BTEvents {

    @SubscribeEvent
    public static void onDeath(PlayerEvent.PlayerRespawnEvent event) {
        BABattleTowers.LOGGER.debug("In Respawn code-");
        Player player = event.getEntity();

        if (player.level().isClientSide() && !event.isEndConquered()) {
            Minecraft mc = Minecraft.getInstance();
            MusicManager musicManager = mc.getMusicManager();
            if (musicManager.isPlayingMusic(LAND_TOWER_MUSIC) || musicManager.isPlayingMusic(LAND_GOLEM_FIGHT_MUSIC)) {
                musicManager.stopPlaying();
            }
        }
    }

    @SubscribeEvent
    public static void blockBreak(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide() && !player.isSpectator() && !player.isCreative()) {
            CorreyeEntity correyeEntity = player.level().getNearestEntity(
                    CorreyeEntity.class, TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight(), player,
                    player.getX(), player.getY(), player.getZ(), player.getBoundingBox().inflate(10D, 10D, 10D));

            if (correyeEntity != null) {
                event.setNewSpeed(2f);
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