package com.BrassAmber.ba_bt.util;

import com.BrassAmber.ba_bt.BrassAmberBattleTowers;
import com.BrassAmber.ba_bt.worldGen.structures.LandBattleTower;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;
import oshi.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class SaveTowers {

    public static Path btDataPath = FMLPaths.getOrCreateGameRelativePath(FMLPaths.GAMEDIR.get().resolve("battletowers"), "battletowers");

    public List<ChunkPos> towers = new ArrayList<>();
    public Path levelPath;
    public long seed;
    public String name;

    public SaveTowers(String name) {
        this.seed = 0;
        this.name = name;
    }

    /**
     * Always first function called during tower generation filling the list with towers from the file if there is one.
     * @param seedIn seed of the currently loaded minecraft world.
     */
    public void setSeed(Long seedIn) {
        if (this.seed != seedIn) {
            this.seed = seedIn;
            this.levelPath = FMLPaths.getOrCreateGameRelativePath(btDataPath.resolve(String.valueOf(seed)), "level_folder");
            towerLocations();
        }
    }

    /**
     * Reads tower locations from file via chunkpos x/z coordinates.
     */
    public void towerLocations() {
        if (this.seed != 0) {
            //
            Path towerPath = this.levelPath.resolve(this.name);

            List<String> lines;
            lines = FileUtil.readFile(towerPath.toString());

            for (String line: lines) {
                String[] xz = line.split(",");
                this.towers.add(new ChunkPos(parseInt(xz[0]), parseInt(xz[1])));
            }
            BrassAmberBattleTowers.LOGGER.info("Towers Loaded:" + towers);
        }
    }

    public void addTower(ChunkPos pos) {
        this.towers.add(pos);
        saveToFile();
    }

    public void saveToFile() {
        if (this.seed != 0) {

            File towerFile = this.levelPath.resolve(this.name).toFile();

            List<String> towerStrings = new ArrayList<>();

            for (ChunkPos xz: this.towers) {
                towerStrings.add(xz.x + "," + xz.z);
            }
            BrassAmberBattleTowers.LOGGER.info("Towers Saved:" + towerStrings);

            try {
                FileUtils.writeLines(towerFile, towerStrings);
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        }
    }
}
