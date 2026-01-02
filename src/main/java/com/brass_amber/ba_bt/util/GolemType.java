package com.brass_amber.ba_bt.util;

import javax.annotation.Nullable;

import com.brass_amber.ba_bt.block.blockentity.*;
import com.brass_amber.ba_bt.entity.CoreDestructionEntity;
import com.brass_amber.ba_bt.entity.LandDestructionEntity;
import com.brass_amber.ba_bt.entity.OceanDestructionEntity;
import com.brass_amber.ba_bt.entity.block.BTAbstractObelisk;
import com.brass_amber.ba_bt.entity.block.BTMonolith;
import com.brass_amber.ba_bt.entity.AbstractDestructionEntity;
import com.brass_amber.ba_bt.entity.hostile.BTCultist;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.init.BTBlocks;
import com.brass_amber.ba_bt.init.BTEntityType;
import com.brass_amber.ba_bt.init.BTItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static com.brass_amber.ba_bt.BattleTowersConfig.*;

public enum GolemType implements StringRepresentable {
	LAND("LAND", Component.translatable("entity.ba_bt.land_golem"), "#9BDAE7"),
	OCEAN("OCEAN", Component.translatable("entity.ba_bt.ocean_golem"), "#EAE78A"),
	CORE("CORE", Component.translatable("entity.ba_bt.core_golem"), "#F79B3A"),
	NETHER("NETHER", Component.translatable("entity.ba_bt.nether_golem"), "#88EB63"),
	END("END", Component.translatable("entity.ba_bt.end_golem"), "#BA49EF"),
	SKY("SKY", Component.translatable("entity.ba_bt.sky_golem"), "#FBC1EB"),
	CITY("CITY", Component.literal("~"), ""),
	EMPTY("EMPTY",Component.literal("Empty"), "");

	private final String name;
	private final Component displayName;
	private final String colorCode;

	GolemType(String name, Component displayName, String colorCode) {
		this.name = name;
		this.displayName = displayName;
		this.colorCode = colorCode;
	}

	public static final GolemType[] VALUES = values();

	/*********************************************************** Monolith Spawning ********************************************************/

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<?> getGolemFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityType.LAND_GOLEM.get();
			case OCEAN -> BTEntityType.OCEAN_GOLEM.get();
			case CORE -> BTEntityType.CORE_GOLEM.get();
			case NETHER -> BTEntityType.NETHER_GOLEM.get();
			case END -> BTEntityType.END_GOLEM.get();
			case SKY -> BTEntityType.SKY_GOLEM.get();
		};
	}

	/**
	 * Get the correct Monolith key for the Correct Monolith Entity
	 */
	public static @NotNull EntityType<BTMonolith> getMonolithFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityType.LAND_MONOLITH.get();
			case OCEAN -> BTEntityType.OCEAN_MONOLITH.get();
			case CORE -> BTEntityType.CORE_MONOLITH.get();
			case NETHER -> BTEntityType.NETHER_MONOLITH.get();
			case END -> BTEntityType.END_MONOLITH.get();
			case SKY -> BTEntityType.SKY_MONOLITH.get();
		};
	}

	/*********************************************************** Obelisk ********************************************************/

	/**
	 * Get the correct Obelisk for the Golem Type.
	 */
	@NotNull
	public static EntityType<BTAbstractObelisk> getObeliskFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY, LAND -> BTEntityType.LAND_OBELISK.get();
			case OCEAN -> BTEntityType.OCEAN_OBELISK.get();
			case CORE -> BTEntityType.CORE_OBELISK.get();
			case NETHER -> BTEntityType.NETHER_OBELISK.get();
			case END -> BTEntityType.END_OBELISK.get();
			case SKY -> BTEntityType.SKY_OBELISK.get();
		};
	}

	@NotNull
	public static GolemType getTypeForObelisk(BTAbstractObelisk entity) {
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
		return EMPTY;
    }


	/*********************************************************** Monolith ********************************************************/

	/**
	 * Get the correct Monolith Item for the Correct Monolith Entity.
	 */

	public static Item getMonolithItemFor(GolemType golemType) {
		return switch (golemType) {
			default -> BTItems.LAND_MONOLITH.get();
			case OCEAN -> BTItems.OCEAN_MONOLITH.get();
			case CORE -> BTItems.CORE_MONOLITH.get();
			case NETHER -> BTItems.NETHER_MONOLITH.get();
			case END -> BTItems.END_MONOLITH.get();
			case SKY -> BTItems.SKY_MONOLITH.get();
		};
	}

	/**
	 * Return the correct GolemType for each Monolith Entity.
	 */
	public static GolemType getTypeForMonolith(BTMonolith BTMonolithEntity) {
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

	public static GolemType getTypeForChest(BlockEntityType<? extends BTChestBlockEntity> blockEntityType) {

		if (BTBlockEntityType.LAND_CHEST.get().equals(blockEntityType) || BTBlockEntityType.LAND_GOLEM_CHEST.get().equals(blockEntityType)) {
			return LAND;
		} else if (BTBlockEntityType.OCEAN_CHEST.get().equals(blockEntityType) || BTBlockEntityType.OCEAN_GOLEM_CHEST.get().equals(blockEntityType)) {
			return OCEAN;
		} else if (BTBlockEntityType.CORE_CHEST.get().equals(blockEntityType) || BTBlockEntityType.CORE_GOLEM_CHEST.get().equals(blockEntityType)) {
			return CORE;
		} else if (BTBlockEntityType.NETHER_CHEST.get().equals(blockEntityType) || BTBlockEntityType.NETHER_GOLEM_CHEST.get().equals(blockEntityType)) {
			return NETHER;
		} else if (BTBlockEntityType.END_CHEST.get().equals(blockEntityType) || BTBlockEntityType.END_GOLEM_CHEST.get().equals(blockEntityType)) {
			return END;
		} else if (BTBlockEntityType.SKY_CHEST.get().equals(blockEntityType) || BTBlockEntityType.SKY_GOLEM_CHEST.get().equals(blockEntityType)) {
			return SKY;
		}

		// Couldn't get EntityType
		return EMPTY;
	}
	
	public static GolemType getTypeForChest(Block block) {

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

	public static GolemType getTypeForDestructionEntity(AbstractDestructionEntity entity) {
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
	/**
	 * Return the correct ChestBlock Entity for each GolemType.
	 */
	public static BlockEntityType<? extends BTChestBlockEntity> getChestForType(GolemType golemType, boolean golemChest) {
		if (golemChest) {
			return switch (golemType) {
				case OCEAN -> BTBlockEntityType.OCEAN_GOLEM_CHEST.get();
				case CORE -> BTBlockEntityType.CORE_GOLEM_CHEST.get();
				case NETHER -> BTBlockEntityType.NETHER_GOLEM_CHEST.get();
				case END -> BTBlockEntityType.END_GOLEM_CHEST.get();
				case SKY -> BTBlockEntityType.SKY_GOLEM_CHEST.get();
				default -> BTBlockEntityType.LAND_GOLEM_CHEST.get();
			};
		}
		return switch (golemType) {
			case OCEAN -> BTBlockEntityType.OCEAN_CHEST.get();
			case CORE -> BTBlockEntityType.CORE_CHEST.get();
			case NETHER -> BTBlockEntityType.NETHER_CHEST.get();
			case END -> BTBlockEntityType.END_CHEST.get();
			case SKY -> BTBlockEntityType.SKY_CHEST.get();
			default ->BTBlockEntityType.LAND_CHEST.get();
		};
	}

	public static boolean isGolemChest(BlockEntityType<?> blockEntityType) {
        return BTBlockEntityType.LAND_GOLEM_CHEST.get().equals(blockEntityType)
                || BTBlockEntityType.OCEAN_GOLEM_CHEST.get().equals(blockEntityType)
                || BTBlockEntityType.CORE_GOLEM_CHEST.get().equals(blockEntityType)
                || BTBlockEntityType.NETHER_GOLEM_CHEST.get().equals(blockEntityType)
                || BTBlockEntityType.END_GOLEM_CHEST.get().equals(blockEntityType)
                || BTBlockEntityType.SKY_GOLEM_CHEST.get().equals(blockEntityType);
    }

	public static boolean isGolemChest(Block block) {
		return BTBlocks.LAND_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.OCEAN_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.CORE_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.NETHER_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.END_GOLEM_CHEST.get().equals(block)
				|| BTBlocks.SKY_GOLEM_CHEST.get().equals(block);
	}


	public static EntityType<?> getDestructionEntityForType(GolemType golemType) {
		return switch (golemType) {
            case OCEAN -> BTEntityType.OCEAN_DESTRUCTION.get();
			case CORE -> BTEntityType.CORE_DESTRUCTION.get();
			case NETHER -> BTEntityType.NETHER_DESTRUCTION.get();
			case END -> BTEntityType.END_DESTRUCTION.get();
			case SKY -> BTEntityType.SKY_DESTRUCTION.get();
            default -> BTEntityType.LAND_DESTRUCTION.get();
        };
	}

	/**
	 * Return the correct ChestBlock Entity for each GolemType.
	 */
	public static Block getChestBlockForType(GolemType golemType, boolean golemChest) {
		if (golemChest) {
			return switch (golemType) {
				case OCEAN -> BTBlocks.OCEAN_GOLEM_CHEST.get();
				case CORE -> BTBlocks.CORE_GOLEM_CHEST.get();
				case NETHER -> BTBlocks.NETHER_GOLEM_CHEST.get();
				case END -> BTBlocks.END_GOLEM_CHEST.get();
				case SKY -> BTBlocks.SKY_GOLEM_CHEST.get();
				default -> BTBlocks.LAND_GOLEM_CHEST.get();
			};
		}
		return switch (golemType) {
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
	public static Item getEyeFor(GolemType golemType) {
		return switch (golemType) {
			case EMPTY, CITY -> null;
			case LAND -> BTItems.LAND_GUARDIAN_EYE.get();
			case OCEAN -> BTItems.OCEAN_GUARDIAN_EYE.get();
			case CORE -> BTItems.CORE_GUARDIAN_EYE.get();
			case NETHER -> BTItems.NETHER_GUARDIAN_EYE.get();
			case END -> BTItems.END_GUARDIAN_EYE.get();
			case SKY -> BTItems.SKY_GUARDIAN_EYE.get();
		};
	}

	/**
	 * Return the previous GolemType in fighting order.
	 */

	public static GolemType getPreviousGolemType(GolemType golemType) {
		return switch (golemType) {
			case OCEAN -> LAND;
			case CORE -> OCEAN;
			case NETHER -> CORE;
			case END -> NETHER;
			case SKY -> END;
			default -> EMPTY;
		};
	}

	/*********************************************************** Keys ********************************************************/

	@Nullable
	public static Item getKeyFor(GolemType golemType) {
		return switch (golemType) {
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

	public static AbstractDestructionEntity getDestructionEntity(GolemType golemType, Level level, BlockPos pos) {
		return switch (golemType) {
			case OCEAN -> new OceanDestructionEntity(level, pos);
			case CORE -> new CoreDestructionEntity(level, pos);
			default -> new LandDestructionEntity(level, pos);
		};
	}

	public static String getTowerChestPool(GolemType golemType, int index) {
		return switch (golemType) {
			case LAND -> landTowerChestLootTables.get(index);
			case OCEAN -> oceanTowerChestLootTables.get(index);
			case CORE -> coreTowerChestLootTables.get(index);
			case NETHER -> netherTowerChestLootTables.get(index);
			case END -> endTowerChestLootTables.get(index);
			case SKY -> skyTowerChestLootTables.get(index);
			default -> "";

		};
	}

	public static Entity getSpecialEnemy(GolemType golemType, ServerLevel serverLevel) {
		return switch (golemType) {
			case LAND -> BTEntityType.BT_CULTIST.get().create(serverLevel);
			case OCEAN -> EntityType.GUARDIAN.create(serverLevel);
			case CORE -> EntityType.MAGMA_CUBE.create(serverLevel);
			case NETHER -> EntityType.WITHER_SKELETON.create(serverLevel);
			case END -> EntityType.ENDERMAN.create(serverLevel);
			case SKY -> EntityType.SKELETON.create(serverLevel);
			default -> null;
		};
	}

	public static EntityType<?> getSpecialEnemyType(GolemType golemType) {
		return switch (golemType) {
			case LAND -> BTEntityType.BT_CULTIST.get();
			case OCEAN -> EntityType.GUARDIAN;
			case CORE -> EntityType.MAGMA_CUBE;
			case NETHER -> EntityType.WITHER_SKELETON;
			case END -> EntityType.ENDERMAN;
			case SKY -> EntityType.SKELETON;
			default -> null;
		};
	}

	public static Class<? extends Entity> getSpecialEnemyClass(GolemType golemType) {
		return switch (golemType) {
			case LAND -> BTCultist.class;
			case OCEAN -> Guardian.class;
			case CORE -> MagmaCube.class;
			case NETHER -> WitherSkeleton.class;
			case END -> EnderMan.class;
			case SKY -> Skeleton.class;
			default -> null;
		};
	}

	public static Integer getNumForType(GolemType golemType) {
		return switch (golemType) {
			case LAND -> 0;
			case OCEAN -> 1;
			case CORE -> 2;
			case NETHER -> 3;
			case END -> 4;
			case SKY -> 5;
			case CITY -> 6;
			default -> null;
		};
	}

	public static Item getResonanceCrystalForType(GolemType golemType) {
		return switch (golemType) {
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

	public static int getDestructionDelay(GolemType golemType) {
		return switch (golemType) {
			case LAND -> landTimeBeforeCollapse;
			case OCEAN -> oceanTimeBeforeCollapse;
			case CORE -> coreTimeBeforeCollapse;
			case NETHER -> netherTimeBeforeCollapse;
			case END -> endTimeBeforeCollapse;
			case SKY -> skyTimeBeforeCollapse;
			default -> 0;
		};
	}

	public static double getDestructionPercent(GolemType golemType) {
		return switch (golemType) {
			case LAND -> landTowerCrumblePercent;
			case OCEAN -> oceanTowerCrumblePercent;
			case CORE -> coreTowerCrumblePercent;
			case NETHER -> netherTowerCrumblePercent;
			case END -> endTowerCrumblePercent;
			case SKY -> skyTowerCrumblePercent;
			default -> 0;
		};
	}

	public static double getMaxHealthFor(GolemType golemType) {
		return switch (golemType) {
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
}
