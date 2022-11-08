package com.BrassAmber.ba_bt.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.BrassAmber.ba_bt.BrassAmberBattleTowers.LOGGER;
import static java.lang.Integer.parseInt;
import static net.minecraftforge.fml.loading.LogMarkers.CORE;

public class SaveTowers {

    public static ArrayList<ChunkPos> landTowers = new ArrayList<>();
    public static ArrayList<ChunkPos> oceanTowers = new ArrayList<>();
    public static List<List<ChunkPos>> towers = List.of(landTowers, oceanTowers);

    public static List<String> towerNames = List.of("Land_Towers", "Ocean_Towers");
    public static Minecraft minecraft;
    public static Path levelPath = Path.of("");


    public SaveTowers() {
        minecraft = Minecraft.getInstance();
    }

    public static Path getOrCreateDirectory(Path dirPath, String dirLabel) {
        if (!Files.isDirectory(dirPath.getParent())) {
            getOrCreateDirectory(dirPath.getParent(), "parent of "+dirLabel);
        }
        if (!Files.isDirectory(dirPath))
        {
            LOGGER.debug(CORE,"Making {} directory : {}", dirLabel, dirPath);
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                if (e instanceof FileAlreadyExistsException) {
                    LOGGER.fatal(CORE,"Failed to create {} directory - there is a file in the way", dirLabel);
                } else {
                    LOGGER.fatal(CORE,"Problem with creating {} directory (Permissions?)", dirLabel, e);
                }
                throw new RuntimeException("Problem creating directory", e);
            }
            LOGGER.debug(CORE,"Created {} directory : {}", dirLabel, dirPath);
        }
        return dirPath;
    }

    public static Path getOrCreateGameRelativePath(Path path, String name) {
        return getOrCreateDirectory(FMLPaths.GAMEDIR.get().resolve(path), name);
    }

    public void getTowers() {
        towers.get(0).clear();
        towers.get(1).clear();

        try {
            levelPath = getOrCreateGameRelativePath(minecraft.getSingleplayerServer().getWorldPath(LevelResource.ROOT).resolve("battletowers"), "battletowers");

        } catch (Exception e) {
            // BrassAmberBattleTowers.LOGGER.debug("SaveTowers" + e.getMessage());
            try {
                levelPath = getOrCreateGameRelativePath(minecraft.level.getServer().getWorldPath(LevelResource.ROOT).resolve("battletowers"), "battletowers");
            } catch (Exception f) {
                // BrassAmberBattleTowers.LOGGER.debug("SaveTowers" + f.getMessage());
            }
        }

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
