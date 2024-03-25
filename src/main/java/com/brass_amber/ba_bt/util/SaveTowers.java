package com.brass_amber.ba_bt.util;

import com.brass_amber.ba_bt.BrassAmberBattleTowers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
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

    public static ArrayList<ChunkPos> landTowers = new ArrayList<>();
    public static ArrayList<ChunkPos> oceanTowers = new ArrayList<>();
    public static List<List<ChunkPos>> towers = List.of(landTowers, oceanTowers);

    public static List<String> towerNames = List.of("Land_Tower", "Ocean_Tower");
    public static MinecraftServer server;
    public static Path levelPath = Path.of("");

    public SaveTowers() {}

    public void setServer(MinecraftServer newServer) {
        BrassAmberBattleTowers.LOGGER.info("Server: " + newServer);
        server = newServer;
        towers.get(0).clear();
        towers.get(1).clear();

        getTowers();
    }

    public void serverClosed() {

        for (int i = 0; i < towerNames.size(); i++) {
            String name = towerNames.get(i);

            File towerFile = levelPath.resolve(name).toFile();

            List<String> towerStrings = new ArrayList<>();

            for (ChunkPos xz: towers.get(towerNames.indexOf(name))) {
                towerStrings.add(xz.x + "," + xz.z);
            }
            // BrassAmberBattleTowers.LOGGER.info(name + " Towers Saved:" + towerStrings);

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
                String[] xz = line.split(",");
                towers.get(i).add(new ChunkPos(parseInt(xz[0]), parseInt(xz[1])));
            }
            // BrassAmberBattleTowers.LOGGER.info(" Towers Loaded:" + towers);
        }
    }

    public void addTower(ChunkPos pos, String name) {
        towers.get(towerNames.indexOf(name)).add(pos);
    }


}
