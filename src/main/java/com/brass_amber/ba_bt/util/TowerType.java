package com.brass_amber.ba_bt.util;

import javax.annotation.Nullable;

import com.brass_amber.ba_bt.block.blockentity.chest.BTChestBlockEntity;
import com.brass_amber.ba_bt.entity.CoreDestructionEntity;
import com.brass_amber.ba_bt.entity.LandDestructionEntity;
import com.brass_amber.ba_bt.entity.OceanDestructionEntity;
import com.brass_amber.ba_bt.entity.block.AbstractObeliskEntity;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.entity.AbstractDestructionEntity;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.Music;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;
import static com.brass_amber.ba_bt.sound.BTMusic.*;

public enum TowerType implements StringRepresentable {
	LAND("LAND", Component.translatable("entity.ba_bt.land_golem"), "#9BDAE7", landTowerMobs,
			LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC, 98, landTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 200, 220, 1, 4, 11, 6),
					new TowerFloor(2, 195, 215, 1, 4, 11, 6),
					new TowerFloor(2, 190, 210, 2, 4, 12, 6),
					new TowerFloor(2, 185, 205, 2, 4, 12, 6),
					new TowerFloor(3, 180, 200, 3, 4, 12, 6),
					new TowerFloor(3, 175, 195, 3, 4, 12, 6),
					new TowerFloor(3, 170, 190, 3, 4, 13, 6),
					new TowerFloor(4, 165, 185, 3, 4, 13, 6),
			}
	),
	OCEAN("OCEAN", Component.translatable("entity.ba_bt.ocean_golem"), "#EAE78A", oceanTowerMobs,
			OCEAN_TOWER_MUSIC, OCEAN_GOLEM_FIGHT_MUSIC, -110, oceanTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 240, 280, 2, 4, 12, 6),
					new TowerFloor(2, 235, 275, 2, 4, 12, 6),
					new TowerFloor(2, 230, 270, 3, 4, 13, 6),
					new TowerFloor(3, 225, 265, 3, 4, 13, 6),
					new TowerFloor(3, 220, 260, 3, 4, 13, 6),
					new TowerFloor(3, 215, 255, 3, 4, 13, 6),
					new TowerFloor(4, 210, 250, 4, 4, 14, 6),
					new TowerFloor(4, 205, 245, 4, 4, 14, 6),
			}
	),
	CORE("CORE", Component.translatable("entity.ba_bt.core_golem"), "#F79B3A", coreTowerMobs,
			CORE_TOWER_MUSIC, CORE_GOLEM_FIGHT_MUSIC, 98, coreTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 200, 220, 2, 5, 11, 6),
					new TowerFloor(2, 195, 215, 2, 5, 11, 6),
					new TowerFloor(3, 190, 210, 3, 5, 12, 6),
					new TowerFloor(3, 185, 205, 3, 5, 12, 6),
					new TowerFloor(3, 180, 200, 3, 6, 12, 6),
					new TowerFloor(3, 175, 195, 3, 6, 12, 6),
					new TowerFloor(4, 170, 190, 4, 6, 13, 6),
					new TowerFloor(4, 165, 185, 4, 6, 13, 6),
			}
	),
	NETHER("NETHER", Component.translatable("entity.ba_bt.nether_golem"), "#88EB63", netherTowerMobs,
			NETHER_TOWER_MUSIC, NETHER_GOLEM_FIGHT_MUSIC, 98, netherTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 200, 220, 1, 4, 11, 6),
					new TowerFloor(2, 195, 215, 1, 4, 11, 6),
					new TowerFloor(2, 190, 210, 2, 4, 12, 6),
					new TowerFloor(2, 185, 205, 2, 4, 12, 6),
					new TowerFloor(3, 180, 200, 3, 4, 12, 6),
					new TowerFloor(3, 175, 195, 3, 4, 12, 6),
					new TowerFloor(3, 170, 190, 3, 4, 13, 6),
					new TowerFloor(4, 165, 185, 3, 4, 13, 6),
			}
	),
	END("END", Component.translatable("entity.ba_bt.end_golem"), "#BA49EF", endTowerMobs,
			END_TOWER_MUSIC, END_GOLEM_FIGHT_MUSIC, 98, endTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 200, 220, 1, 4, 11, 6),
					new TowerFloor(2, 195, 215, 1, 4, 11, 6),
					new TowerFloor(2, 190, 210, 2, 4, 12, 6),
					new TowerFloor(2, 185, 205, 2, 4, 12, 6),
					new TowerFloor(3, 180, 200, 3, 4, 12, 6),
					new TowerFloor(3, 175, 195, 3, 4, 12, 6),
					new TowerFloor(3, 170, 190, 3, 4, 13, 6),
					new TowerFloor(4, 165, 185, 3, 4, 13, 6),
			}
	),
	SKY("SKY", Component.translatable("entity.ba_bt.sky_golem"), "#FBC1EB", skyTowerMobs,
			SKY_TOWER_MUSIC, SKY_GOLEM_FIGHT_MUSIC, 98, skyTowerCrumblePercent,
			new TowerFloor[]{
					new TowerFloor(2, 200, 220, 1, 4, 11, 6),
					new TowerFloor(2, 195, 215, 1, 4, 11, 6),
					new TowerFloor(2, 190, 210, 2, 4, 12, 6),
					new TowerFloor(2, 185, 205, 2, 4, 12, 6),
					new TowerFloor(3, 180, 200, 3, 4, 12, 6),
					new TowerFloor(3, 175, 195, 3, 4, 12, 6),
					new TowerFloor(3, 170, 190, 3, 4, 13, 6),
					new TowerFloor(4, 165, 185, 3, 4, 13, 6),
			}
	),
	CITY("CITY", Component.literal("~"), "", List.of(),
			LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC, 98, landTowerCrumblePercent,
			new TowerFloor[]{}
	),
	AETHER("AETHER", Component.literal("~"), "", List.of(),
			LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC, 98, landTowerCrumblePercent,
			new TowerFloor[]{}
	),
	DEEPER_DARK("DEEPER_DARK", Component.literal("~"), "", List.of(),
			LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC, 98, landTowerCrumblePercent,
			new TowerFloor[]{}
	),
	EMPTY("EMPTY",Component.literal("Empty"), "", List.of(),
			LAND_TOWER_MUSIC, LAND_GOLEM_FIGHT_MUSIC, 98, landTowerCrumblePercent,
			new TowerFloor[]{}
	);

	private final String name;
	private final Component displayName;
	private final String colorCode;
	private final List<EntityType<?>> towerMobs;
	private final TowerFloor[] towerFloors;
	private final Music towerMusic;
	private final Music bossMusic;
	private final int destructionOffset;
	private final double destructionPercent;

	TowerType(String name, Component displayName, String colorCode, List<EntityType<?>> towerMobs, Music towerMusic, Music bossMusic, int destructionOffset, double destructionPercent, TowerFloor[] towerFloors) {
		this.name = name;
		this.displayName = displayName;
		this.colorCode = colorCode;
		this.towerMobs = towerMobs;
		this.towerFloors = towerFloors;
		this.towerMusic = towerMusic;
		this.bossMusic = bossMusic;
		this.destructionOffset = destructionOffset;
		this.destructionPercent = destructionPercent;
	}

	public static final TowerType[] VALUES = values();

	public List<EntityType<?>> getTowerMobs() {
		return towerMobs;
	}

	public TowerFloor[] getTowerFloors() {
		return towerFloors;
	}

	public TowerFloor getTowerFloor(int floorNum) {
		return towerFloors[floorNum];
	}

	public Music getTowerMusic() {
		return towerMusic;
	}

	public Music getBossMusic() {
		return bossMusic;
	}


	public int getDestructionOffset() {
		return destructionOffset;
	}

	public double getDestructionPercent() {
		return destructionPercent;
	}


	/*********************************************************** Monolith Spawning ********************************************************/

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<?> getGolemFor(TowerType towerType) {
		return switch (towerType) {
			case OCEAN -> BTEntityType.OCEAN_GOLEM.get();
			case CORE -> BTEntityType.CORE_GOLEM.get();
			case NETHER -> BTEntityType.NETHER_GOLEM.get();
			case END -> BTEntityType.END_GOLEM.get();
			case SKY -> BTEntityType.SKY_GOLEM.get();
			default -> BTEntityType.LAND_GOLEM.get();
		};
	}

	/*********************************************************** Obelisk ********************************************************/

	/**
	 * Get the correct Obelisk for the Golem Type.
	 */
	@NotNull
	public static EntityType<BTAbstractObelisk> getObeliskFor(TowerType towerType) {
		return switch (towerType) {
			case OCEAN -> BTEntityType.OCEAN_OBELISK.get();
			case CORE -> BTEntityType.CORE_OBELISK.get();
			case NETHER -> BTEntityType.NETHER_OBELISK.get();
			case END -> BTEntityType.END_OBELISK.get();
			case SKY -> BTEntityType.SKY_OBELISK.get();
			default -> BTEntityType.LAND_OBELISK.get();
		};
	}

	@NotNull
	public  static EntityType<AbstractDestructionEntity> getDestructionEntityFor(TowerType towerType) {
		return switch (towerType) {
			case OCEAN -> BTEntityType.OCEAN_DESTRUCTION.get();
			case CORE -> BTEntityType.CORE_DESTRUCTION.get();
			case NETHER -> BTEntityType.NETHER_DESTRUCTION.get();
			case END -> BTEntityType.END_DESTRUCTION.get();
			case SKY -> BTEntityType.SKY_DESTRUCTION.get();
			default -> BTEntityType.LAND_DESTRUCTION.get();
		};
	}

	/*********************************************************** Monolith ********************************************************/

	/**
	 * Get the correct Monolith Item for the Correct Monolith Entity.
	 */

	public static Item getMonolithItemFor(TowerType towerType) {
		return switch (towerType) {
            case OCEAN -> BTItems.OCEAN_MONOLITH.get();
			case CORE -> BTItems.CORE_MONOLITH.get();
			case NETHER -> BTItems.NETHER_MONOLITH.get();
			case END -> BTItems.END_MONOLITH.get();
			case SKY -> BTItems.SKY_MONOLITH.get();
            default -> BTItems.LAND_MONOLITH.get();
        };
	}

	/**
	 * Return the correct GolemType for each Monolith Entity.
	 */
	public static TowerType getTypeForMonolith(BTMonolith BTMonolithEntity) {
		EntityType<?> entityType = BTMonolithEntity.getType();
        if (entityType.equals(BTEntityType.LAND_MONOLITH.get())) {
            return LAND;
        } else if (entityType.equals(BTEntityType.OCEAN_MONOLITH.get())) {
            return OCEAN;
        } else if (entityType.equals(BTEntityType.CORE_MONOLITH.get())) {
            return CORE;
        } else if (entityType.equals(BTEntityType.NETHER_MONOLITH.get())) {
            return NETHER;
        } else if (entityType.equals(BTEntityType.END_MONOLITH.get())) {
            return END;
        } else if (entityType.equals(BTEntityType.SKY_MONOLITH.get())) {
            return SKY;
        }

        // Couldn't get EntityType
		return EMPTY;
	}

	/*********************************************************** Monolith ********************************************************/
	/**
	 * Return the correct GolemType for each ChestBlock Entity.
	 */
	
	public static TowerType getTypeForChest(Block block) {

		if (BTBlocks.LAND_CHEST.get().equals(block) || BTBlocks.LAND_GOLEM_CHEST.get().equals(block)) {
			return LAND;
		} else if (BTBlocks.OCEAN_CHEST.get().equals(block) || BTBlocks.OCEAN_GOLEM_CHEST.get().equals(block)) {
			return OCEAN;
		} else if (BTBlocks.CORE_CHEST.get().equals(block) || BTBlocks.CORE_GOLEM_CHEST.get().equals(block)) {
			return CORE;
		} else if (BTBlocks.NETHER_CHEST.get().equals(block) || BTBlocks.NETHER_GOLEM_CHEST.get().equals(block)) {
			return NETHER;
		} else if (BTBlocks.END_CHEST.get().equals(block) || BTBlocks.END_GOLEM_CHEST.get().equals(block)) {
			return END;
		} else if (BTBlocks.SKY_CHEST.get().equals(block) || BTBlocks.SKY_GOLEM_CHEST.get().equals(block)) {
			return SKY;
		}

		// Couldn't get EntityType
		return EMPTY;
	}

	public static TowerType getTypeForDestructionEntity(AbstractDestructionEntity entity) {
		EntityType<?> entityType = entity.getType();

		if (entityType.equals(BTEntityType.LAND_DESTRUCTION.get())) {
			return LAND;
		} else if (entityType.equals(BTEntityType.OCEAN_DESTRUCTION.get())) {
			return OCEAN;
		} else if (entityType.equals(BTEntityType.CORE_DESTRUCTION.get())) {
			return CORE;
		} else if (entityType.equals(BTEntityType.NETHER_DESTRUCTION.get())) {
			return NETHER;
		} else if (entityType.equals(BTEntityType.END_DESTRUCTION.get())) {
			return END;
		} else if (entityType.equals(BTEntityType.SKY_DESTRUCTION.get())) {
			return SKY;
		}

		// Couldn't get EntityType
		return EMPTY;
    }

	public static TowerType getTypeForObeliskEntity(AbstractObeliskEntity entity) {
		EntityType<?> entityType = entity.getType();

		if (entityType.equals(BTEntityType.LAND_OBELISK.get())) {
			return LAND;
		} else if (entityType.equals(BTEntityType.OCEAN_OBELISK.get())) {
			return OCEAN;
		} else if (entityType.equals(BTEntityType.CORE_OBELISK.get())) {
			return CORE;
		} else if (entityType.equals(BTEntityType.NETHER_OBELISK.get())) {
			return NETHER;
		} else if (entityType.equals(BTEntityType.END_OBELISK.get())) {
			return END;
		} else if (entityType.equals(BTEntityType.SKY_OBELISK.get())) {
			return SKY;
		}

		// Couldn't get EntityType
		return EMPTY;
	}

	public static boolean isGolemChest(Block block) {
		return BTBlocks.LAND_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.OCEAN_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.CORE_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.NETHER_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.END_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.SKY_GOLEM_CHEST.get().equals(block);
	}

	/**
	 * Return the correct ChestBlock Entity for each GolemType.
	 */
	public static Block getChestBlockForType(TowerType towerType, boolean golemChest) {
		if (golemChest) {
			return switch (towerType) {
				case OCEAN -> BTBlocks.OCEAN_GOLEM_CHEST.get();
				case CORE -> BTBlocks.CORE_GOLEM_CHEST.get();
				case NETHER -> BTBlocks.NETHER_GOLEM_CHEST.get();
				case END -> BTBlocks.END_GOLEM_CHEST.get();
				case SKY -> BTBlocks.SKY_GOLEM_CHEST.get();
				default -> BTBlocks.LAND_GOLEM_CHEST.get();
			};
		}
		return switch (towerType) {
			case OCEAN -> BTBlocks.OCEAN_CHEST.get();
			case CORE -> BTBlocks.CORE_CHEST.get();
			case NETHER -> BTBlocks.NETHER_CHEST.get();
			case END -> BTBlocks.END_CHEST.get();
			case SKY -> BTBlocks.SKY_CHEST.get();
			default -> BTBlocks.LAND_CHEST.get();
		};
	}

	/*********************************************************** Eyes ********************************************************/

	/**
	 * Return the matching Guardian Eye for each GolemType.
	 */
	@Nullable
	public static Item getEyeFor(TowerType towerType) {
		return switch (towerType) {
			case LAND -> BTItems.LAND_GOLEM_EYE.get();
			case OCEAN -> BTItems.OCEAN_GOLEM_EYE.get();
			case CORE -> BTItems.CORE_GOLEM_EYE.get();
			case NETHER -> BTItems.NETHER_GOLEM_EYE.get();
			case END -> BTItems.END_GOLEM_EYE.get();
			case SKY -> BTItems.SKY_GOLEM_EYE.get();
			default -> null;
		};
	}

	/**
	 * Return the previous GolemType in fighting order.
	 */

	public TowerType getPreviousGolemType() {
		int ordinalVal = this.ordinal() - 1;
		return ordinalVal > 0 ? TowerType.VALUES[ordinalVal] : EMPTY;
	}

	/*********************************************************** Keys ********************************************************/

	@Nullable
	public static Item getKeyFor(TowerType towerType) {
		return switch (towerType) {
			case LAND -> BTItems.LAND_MONOLITH_KEY.get();
			case OCEAN -> BTItems.OCEAN_MONOLITH_KEY.get();
			case CORE -> BTItems.CORE_MONOLITH_KEY.get();
			case NETHER -> BTItems.NETHER_MONOLITH_KEY.get();
			case END -> BTItems.END_MONOLITH_KEY.get();
			case SKY -> BTItems.SKY_MONOLITH_KEY.get();
			default -> null;
		};
	}

	/*********************************************************** Extra ********************************************************/

	public static AbstractDestructionEntity getDestructionEntity(TowerType towerType, Level level) {
		return switch (towerType) {
			case OCEAN -> new OceanDestructionEntity(level);
			case CORE -> new CoreDestructionEntity(level);
			default -> new LandDestructionEntity(level);
		};
	}

	public static String getTowerChestPool(TowerType towerType, int index) {
		return switch (towerType) {
			case LAND -> landTowerChestLootTables.get(index);
			case OCEAN -> oceanTowerChestLootTables.get(index);
			case CORE -> coreTowerChestLootTables.get(index);
			case NETHER -> netherTowerChestLootTables.get(index);
			case END -> endTowerChestLootTables.get(index);
			case SKY -> skyTowerChestLootTables.get(index);
			default -> "";

		};
	}

	public static Entity getSpecialEnemy(TowerType towerType, ServerLevel serverLevel) {
		return switch (towerType) {
			case LAND -> BTEntityType.BT_CULTIST.get().create(serverLevel);
			case OCEAN -> EntityType.GUARDIAN.create(serverLevel);
			case CORE -> EntityType.MAGMA_CUBE.create(serverLevel);
			case NETHER -> EntityType.WITHER_SKELETON.create(serverLevel);
			case END -> EntityType.ENDERMAN.create(serverLevel);
			case SKY -> EntityType.SKELETON.create(serverLevel);
			default -> null;
		};
	}

	public static Item getResonanceCrystalForType(TowerType towerType) {
		return switch (towerType) {
			case LAND -> BTItems.LAND_RESONANCE_CRYSTAL.get();
			case OCEAN -> BTItems.OCEAN_RESONANCE_CRYSTAL.get();
			case CORE -> BTItems.CORE_RESONANCE_CRYSTAL.get();
			case NETHER -> BTItems.NETHER_RESONANCE_CRYSTAL.get();
			case END -> BTItems.END_RESONANCE_CRYSTAL.get();
			case SKY -> BTItems.SKY_RESONANCE_CRYSTAL.get();
			case CITY -> BTItems.CITY_RESONANCE_CRYSTAL.get();
			default -> null;
		};
	}

	public static int getDestructionDelay(TowerType towerType) {
		return switch (towerType) {
			case LAND -> landTimeBeforeCollapse;
			case OCEAN -> oceanTimeBeforeCollapse;
			case CORE -> coreTimeBeforeCollapse;
			case NETHER -> netherTimeBeforeCollapse;
			case END -> endTimeBeforeCollapse;
			case SKY -> skyTimeBeforeCollapse;
			default -> 0;
		};
	}


	public static double getMaxHealthFor(TowerType towerType) {
		return switch (towerType) {
            case OCEAN -> oceanGolemHP;
			case CORE -> coreGolemHP;
			case NETHER -> netherGolemHP;
			case END -> endGolemHP;
			case SKY -> skyGolemHP;
			default -> landGolemHP;
		};
	}

    @Override
	public @NotNull String getSerializedName() {
		return this.name;
	}

	public String getLowercaseName() {
		return this.getSerializedName().toLowerCase(Locale.ROOT);
	}

	public Component getDisplayName() {
		return this.displayName;
	}

    public String getColorCode() {
        return colorCode;
    }

    public record TowerFloor(int spawnerAmt, int minSpawnDelay, int maxSpawnDelay, int spawnCount, int maxNearbyEntities, int requiredPlayerRange, int spawnRange) {

	}
}
