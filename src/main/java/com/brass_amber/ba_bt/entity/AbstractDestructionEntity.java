package com.brass_amber.ba_bt.entity;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.BattleTowersConfig;
import com.brass_amber.ba_bt.sound.BTSoundEvents;
import com.brass_amber.ba_bt.util.GolemType;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.brass_amber.ba_bt.sound.BTMusic.TOWER_COLLAPSE_MUSIC;
import static com.brass_amber.ba_bt.util.BTUtil.doNoOutputCommand;
import static com.brass_amber.ba_bt.util.BTUtil.doNoOutputPostionedCommand;
import static net.minecraft.util.Mth.floor;

public abstract class AbstractDestructionEntity extends Entity {
    static final Logger LOGGER = LogUtils.getLogger();

    //Other Parameters
    protected GolemType golemType;
    protected List<BlockPos> blocksToRemove = new ArrayList<>();

    protected int currentTicks = 0;
    protected int startTicks = 300;
    protected int crumbleStartY;
    protected int crumbleStopY;

    // Must be initialized in subclass
    protected int blockSearchDistance;
    protected double destructionRadius;
    protected int titleRadius;
    protected int crumbleDirection; // -1 = top to bottom || 1 = bottom to top

    protected DestructionState destructionState = DestructionState.START_DELAY;
    protected TitleState titleState = TitleState.DEFEATED_TITLE;

    protected String colorCode;
    protected Component golemName;
    protected Component golemDefeatText;
    protected Component golemFateText;
    protected Component collapseFlavorText;

    // Data Strings
    protected final String crumbleStartName = "CrumbleStartY";
    protected final String crumbleStopName = "CrumbleStopY";
    protected final String golemTypeName = "GolemType";
    protected final String destructionStateName = "DestructionState";
    protected final String titleStateName = "TitleState";

    public AbstractDestructionEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.golemType = GolemType.getTypeForDestructionEntity(this);
        this.colorCode = golemType.getColorCode();
        this.golemName = golemType.getDisplayName();
        this.golemDefeatText = Component.translatable("title.ba_bt." + golemType.getSerializedName().toLowerCase(Locale.ROOT) +"_golem_defeated");
        this.golemFateText = Component.translatable("title.ba_bt." + golemType.getSerializedName().toLowerCase(Locale.ROOT) + "_golem_fate");
        this.collapseFlavorText = Component.translatable("title.ba_bt." + golemType.getSerializedName().toLowerCase(Locale.ROOT) + "_collapse_flavor");
        this.startTicks = GolemType.getDestructionDelay(this.golemType) * 20;
        this.setInvulnerable(true);
        this.setInvisible(true);
    }

    public void setPos(BlockPos obeliskPos, int destroyOffset) {
        super.setPos(obeliskPos.getX(), obeliskPos.getY() + destroyOffset, obeliskPos.getZ());
        this.crumbleStartY = this.getBlockY();
        this.crumbleStopY = obeliskPos.getY() + Mth.floor(destroyOffset * BattleTowersConfig.landTowerCrumblePercent);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt(this.crumbleStartName, this.crumbleStartY);
        compoundTag.putInt(this.crumbleStopName, this.crumbleStopY);
        compoundTag.putString(this.golemTypeName, this.golemType.getSerializedName());
        compoundTag.putInt(this.destructionStateName, this.destructionState.value);
        compoundTag.putInt(this.titleStateName, this.titleState.value);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.crumbleStartY = compoundTag.getInt(this.crumbleStartName);
        this.crumbleStopY = compoundTag.getInt(this.crumbleStopName);
        this.golemType = GolemType.valueOf(compoundTag.getString(this.golemTypeName));
        this.destructionState = DestructionState.getState(compoundTag.getInt(this.destructionStateName));
        this.titleState = TitleState.values()[compoundTag.getInt(this.titleStateName)];
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            MusicManager music = Minecraft.getInstance().getMusicManager();
            if (this.level().getNearestPlayer(this,64) != null) {
                if (!music.isPlayingMusic(TOWER_COLLAPSE_MUSIC)) {
                    music.stopPlaying();
                    music.startPlaying(TOWER_COLLAPSE_MUSIC);
                }
            } else {
                music.stopPlaying();
            }
            return;
        }

        if (this.destructionState != DestructionState.FINISHED) {
            this.currentTicks++;
            if (this.destructionState == DestructionState.START_DELAY && this.currentTicks == this.startTicks) {
                this.currentTicks = 0;
                this.destructionState = DestructionState.PLAY_TITLES;
                this.titleRadius = floor(this.destructionRadius * 4);
                return;
            }
            switch (this.destructionState) {
                case PLAY_TITLES -> this.playTitles();
                case COLLECT_BLOCK_LISTS -> this.collectBlocks();
                case DESTROY_TOWER -> this.destroyTower();
                case CLEANUP_TOWER_ZONE -> this.cleanupTowerZone();
                default -> {}
            }
        } else {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public void playTitles() {
        BABattleTowers.LOGGER.debug("In Title Sequence");
        if (this.currentTicks == this.titleState.getTickDelay()) {
            if (this.titleState != TitleState.TITLES_FINISHED) {
                Component text = switch (this.titleState) {
                    case DEFEATED_TITLE -> this.golemDefeatText;
                    case GOLEM_FATE_TITLE -> this.golemFateText;
                    case COLLAPSE_FLAVOR_TITLE -> this.collapseFlavorText;
                    default -> Component.empty();
                };
                doNoOutputPostionedCommand(this, "/title @a[distance=0.." + this.titleRadius + "] times 30 40 20", this.position());
                doNoOutputCommand(this, "/title @a[distance=0.." + this.titleRadius + "] subtitle {\"text\":\"" + text.getString()
                        + "\",\"color\":\"" + this.colorCode + "\"}"
                );
                doNoOutputCommand(this, "/title @a[distance=0.." + this.titleRadius + "] title \"\""
                );
                this.level().playSound(null, this.blockPosition().below(6),
                        BTSoundEvents.TOWER_BREAK_START.get(), SoundSource.AMBIENT, 4.0F, 1F);

                // Old color codes for fate & flavor titles: #aaaaaa & #aa0000
                this.currentTicks = 0;
                this.titleState = this.titleState.getNext();
            } else {
                this.destructionState = DestructionState.COLLECT_BLOCK_LISTS;
            }
        }
    }

    public abstract void collectBlocks();

    public abstract void destroyTower();

    public abstract void cleanupTowerZone();

    public enum DestructionState {
        START_DELAY(0),
        PLAY_TITLES(1),
        COLLECT_BLOCK_LISTS(2),
        DESTROY_TOWER(3),
        CLEANUP_TOWER_ZONE(4),
        FINISHED(5);

        private final int value;

        DestructionState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public DestructionState getNext() {
            return switch (this.value) {
                case 1 -> COLLECT_BLOCK_LISTS;
                case 2 -> DESTROY_TOWER;
                case 3 -> CLEANUP_TOWER_ZONE;
                case 4 -> FINISHED;
                default -> PLAY_TITLES;
            };
        }

        public static DestructionState getState(int value) {
            return switch (value) {
                case 1 -> PLAY_TITLES;
                case 2, 3 -> COLLECT_BLOCK_LISTS;
                case 4 -> CLEANUP_TOWER_ZONE;
                case 5 -> FINISHED;
                default -> START_DELAY;
            };
        }
    }

    public enum TitleState {
        DEFEATED_TITLE(0, 20),
        GOLEM_FATE_TITLE(1, 360),
        COLLAPSE_FLAVOR_TITLE(2, 120),
        TITLES_FINISHED(3, 60);

        private final int value;
        private final int tickDelay;

        TitleState(int value, int tickDelay) {
            this.value = value;
            this.tickDelay = tickDelay;
        }

        public int getValue() {
            return value;
        }

        public int getTickDelay() {
            return tickDelay;
        }

        public TitleState getNext() {
            return switch (this.value) {
                case 1 -> COLLAPSE_FLAVOR_TITLE;
                case 2 -> TITLES_FINISHED;
                default -> GOLEM_FATE_TITLE;
            };
        }
    }
}
