package com.brass_amber.ba_bt.worldGen.structures.customUtil;

import com.brass_amber.ba_bt.BABattleTowers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

import java.util.*;

import static com.brass_amber.ba_bt.worldGen.structures.customUtil.TowerProcessors.*;

public enum TowerGenInfo implements StringRepresentable {
    EMPTY("", new String[]{},
            List.of(new String[]{}, new String[]{}),
            Collections.emptyList(),
            Collections.emptyList(),
            new String[]{},
            new float[]{},
            Collections.emptyList()
    ),
    LAND("land_tower", new String[]{"normal", "overgrown", "sandy", "icy", "ruined"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{},
                    new String[]{}, new String[]{}
            ),
            List.of(NORMAL_LAND, NORMAL_STAIRS_LAND, NORMAL_FLOOR_LAND),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{"barracks_abandoned", "barracks_open", "barracks", "kitchen", "library"},
            new float[]{.05f, .15f, .15f, .3f, .2f},
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), List.of(CARPET_PLACER))
    ),
    OCEAN("ocean_tower", new String[]{"normal", "gilded", "island"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{}
            ),
            List.of(NORMAL_OCEAN, NORMAL_STAIRS_OCEAN, NORMAL_FLOOR_OCEAN, WATERLOGGED),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{"garden", "guardian_nest", "shark_pens"},
            new float[]{0.3f, 0.3f, 0.3f},
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList())
    ),
    CORE("core_tower", new String[]{"normal", "frozen", "colossal"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{}
            ),
            Collections.emptyList(),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{},
            new float[]{},
            Collections.emptyList()
    ),
    NETHER("nether_tower", new String[]{"normal", "crimson", "blue", "anomaly"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{}, new String[]{}
            ),
            Collections.emptyList(),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{},
            new float[]{},
            Collections.emptyList()
    ),
    END("end_tower", new String[]{"normal", "disturbance", "city"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{}
            ),
            Collections.emptyList(),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{},
            new float[]{},
            Collections.emptyList()
    ),
    SKY("sky_tower", new String[]{"normal", "village", "hanging_gardens"},
            List.of(
                    new String[]{}, new String[]{}, new String[]{}
            ),
            Collections.emptyList(),
            List.of(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()),
            new String[]{},
            new float[]{},
            Collections.emptyList()
    );

    private final String towerName;
    private final String[] variants;
    private final HashMap<String, String[]> shellOverwritePieces = new HashMap<>();
    private final List<StructureProcessor> shellProcessors;
    private final HashMap<String, List<StructureProcessor>> extraProcessors = new HashMap<>();
    private final ArrayList<String> rooms = new ArrayList<>();
    private final HashMap<String, List<StructureProcessor>> roomProcessors = new HashMap<>();

    TowerGenInfo(
            String towerName, String[] variants, List<String[]> shellOverwritePieces,
            List<StructureProcessor> shellProcessors,
            List<List<StructureProcessor>> extraProcessors,
            String[] roomNames, float[] room_chances,
            List<List<StructureProcessor>> roomProcessors
    ) {
        this.towerName = towerName;
        this.variants = variants;
        this.shellProcessors = shellProcessors;

        for (int i = 0; i < variants.length; i++) {
            this.shellOverwritePieces.put(variants[i], shellOverwritePieces.get(i));
            this.extraProcessors.put(variants[i], extraProcessors.get(i));
        }

        int amount = 0;
        float percent;

        for (int i = 0; i < roomNames.length; i++) {
            percent = room_chances[i];
            amount = (int) (percent * 20);
            for (int e = 0; e < amount; e++) {
                this.rooms.add(roomNames[i]);
            }
            this.roomProcessors.put(roomNames[i], roomProcessors.get(i));
        }
    }

    public static String[] getShellOverwritePieces(TowerGenInfo towerGenInfo, String variant) {
        return towerGenInfo.shellOverwritePieces.get(variant);
    }

    public static ResourceLocation getRandomVariantShellPiece(TowerGenInfo towerGenInfo, String variant, RandomSource randomSource) {
        return new ResourceLocation(BABattleTowers.MOD_ID,
                towerGenInfo.towerName + "/" + variant + "_" +
                        TowerGenInfo.getShellOverwritePieces(towerGenInfo, variant)[
                                randomSource.nextInt(towerGenInfo.shellOverwritePieces.size())
                                ]
        );

    }

    public ArrayList<String> getRooms() {
        return rooms;
    }

    public static TowerGenInfo getTypeForName(String name) {
        return switch (name) {
            default -> EMPTY;
            case "land_tower" -> TowerGenInfo.LAND;
            case "ocean_tower" -> TowerGenInfo.OCEAN;
            case "core_tower" -> TowerGenInfo.CORE;
            case "nether_tower" -> TowerGenInfo.NETHER;
            case "end_tower" -> TowerGenInfo.END;
            case "sky_tower" -> TowerGenInfo.SKY;
        };
    }

    public static String getRandomRoom(TowerGenInfo towerGenInfo, RandomSource randomSource) {
        return towerGenInfo.rooms.get(randomSource.nextInt(towerGenInfo.rooms.size()));
    }

    public static List<StructureProcessor> getRoomProcessors(TowerGenInfo towerGenInfo, String roomName) {
        return towerGenInfo.roomProcessors.get(roomName);
    }

    public static List<StructureProcessor> getShellProcessors(TowerGenInfo towerGenInfo, String variant) {
        return towerGenInfo.shellProcessors;
    }

    public static List<StructureProcessor> getVariantProcessors(TowerGenInfo towerGenInfo, String variant) {
        return towerGenInfo.extraProcessors.get(variant);
    }

    public static int getFloorHeight(TowerGenInfo towerGenInfo) {
        return switch (towerGenInfo) {
            case OCEAN -> -12;
            case CORE -> 9;
            case NETHER -> 10;
            case SKY -> 12;
            default -> 11;
        };
    }


    @Override
    public String getSerializedName() {
        return this.towerName;
    }

    public String[] getVariants() {
        return variants;
    }
}
