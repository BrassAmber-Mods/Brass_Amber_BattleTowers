package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.init.BTRegistries;
import com.brass_amber.ba_bt.item.ItemPool;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.storage.LevelResource;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static net.minecraftforge.fml.loading.FMLPaths.getOrCreateGameRelativePath;

public class SaveTowers {

    public static ArrayList<Pair<ChunkPos, Rotation>> landTowers = new ArrayList<>();
    public static ArrayList<Pair<ChunkPos, Rotation>> oceanTowers = new ArrayList<>();
    public static List<List<Pair<ChunkPos, Rotation>>> towers = List.of(landTowers, oceanTowers);

    public static Registry<ItemPool> itemPoolReg;
    public static List<ItemPool> itemPools;
    public static List<String> towerNames = List.of("land_tower", "ocean_tower");
    public static MinecraftServer server;
    public static Path levelPath = Path.of("");

    public SaveTowers() {}

    public void setServer(MinecraftServer newServer) {
        BABattleTowers.LOGGER.debug("Server: {}", newServer);
        server = newServer;
        towers.get(0).clear();
        towers.get(1).clear();

        itemPoolReg = newServer.registryAccess().registryOrThrow(BTRegistries.Keys.ITEM_POOLS);
        itemPools = itemPoolReg.holders().map(Holder::value).toList();
        BABattleTowers.LOGGER.debug("Pools {}", itemPools.stream().map(ItemPool::getName).toList());
        BABattleTowers.LOGGER.debug("Pools from tags {}", itemPoolReg.getTags().toList().stream().map(Pair::getSecond).map(holders -> holders.key().location()).toList());

        getTowers();
        // BABTMain.LOGGER.debug("Towers: {}", towers);
    }

    public void serverClosed() {
        writeData();
    }

    private void writeData() {
        for (int i = 0; i < towerNames.size(); i++) {
            String name = towerNames.get(i);

            File towerFile = levelPath.resolve(name).toFile();

            List<String> towerStrings = new ArrayList<>();

            for (Pair<ChunkPos, Rotation> xzr: towers.get(towerNames.indexOf(name))) {
                ChunkPos xz = xzr.getFirst();
                Rotation r = xzr.getSecond();

                towerStrings.add(xz.x + "," + xz.z + "," + r.name());
            }
            // BrassAmberBattleTowers.LOGGER.debug(name + " Towers Saved:" + towerStrings);

            try {
                FileUtils.writeLines(towerFile, towerStrings);
            } catch (IOException x) {
                // System.err.format("IOException: %s%n", x);
            }
        }
    }

    public void getTowers() {
        levelPath = getOrCreateGameRelativePath(server.getWorldPath(LevelResource.ROOT).resolve("battletowers"));

        for (int i = 0; i < towerNames.size(); i++) {
            Path towerPath = levelPath.resolve(towerNames.get(i));

            List<String> lines = new ArrayList<>();
            try {
                lines = Files.readAllLines(towerPath, StandardCharsets.UTF_8);
            } catch (IOException ignored) {

            }

            for (String line: lines) {
                try {
                    String[] xzr = line.split(",");
                    towers.get(i).add(Pair.of(new ChunkPos(parseInt(xzr[0]), parseInt(xzr[1])), xzr.length == 3 ? Rotation.valueOf( xzr[2]) : Rotation.NONE));
                } catch (Exception e) {
                    BABattleTowers.LOGGER.debug(e.getLocalizedMessage());
                }
            }
            // BrassAmberBattleTowers.LOGGER.debug(" Towers Loaded:" + towers);
        }
    }

    public Rotation getTowerRotation(int towerId, ChunkPos pos) {
        // BABTMain.LOGGER.debug("Get Rotation for chunkpos: {}", pos);
        for (Pair<ChunkPos, Rotation> xzr: towers.get(towerId)) {
            ChunkPos xz = xzr.getFirst();
            Rotation r = xzr.getSecond();
            // BABTMain.LOGGER.debug("Checking pos: {}", xz);
            if (xz.x == pos.x && xz.z == pos.z) {
                return r;
            }
        }
        return Rotation.NONE;
    }

    public void addTower(ChunkPos pos, Rotation rotation, int towerId) {
        towers.get(towerId).add(Pair.of(pos, rotation));
        writeData();
    }


}
