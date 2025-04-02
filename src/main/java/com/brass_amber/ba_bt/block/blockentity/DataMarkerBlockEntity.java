package com.brass_amber.ba_bt.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.init.BTBlockEntityType;

import static com.brass_amber.ba_bt.util.BTStatics.*;

public class DataMarkerBlockEntity extends BlockEntity {

    // Container type can be any type listed in containerTypes in
    protected int[] lootTypes = new int[1];
    protected int rarity = -1; // -1 == set by room height. 1-4 == set by data
    protected BlockState placeBlockState = Blocks.BEDROCK.defaultBlockState();
    protected CompoundTag nbt = new CompoundTag();

    public DataMarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BTBlockEntityType.DATA_MARKER.get(), blockPos, blockState);
    }


    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        // BABTMain.LOGGER.debug("Loading Marker Data {}", compoundTag);
        try {
            this.lootTypes = compoundTag.getIntArray("Loot");
        } catch (Exception ignored) {
            this.lootTypes = new int[1];
        }

        try {
            this.rarity = compoundTag.getInt("Rarity");
        } catch (Exception ignored) {
            this.rarity = -1;
        }
        if (this.rarity <= -5 || this.rarity >= 5) {
            this.rarity = -1;
        }
        this.placeBlockState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), compoundTag.getCompound("BlockState"));

        this.nbt = compoundTag.getCompound("nbt");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        // BABTMain.LOGGER.info("Saving Marker Data");

        compoundTag.putIntArray("Loot", this.lootTypes);
        compoundTag.putInt("Rarity", rarity);

        compoundTag.put("BlockState", NbtUtils.writeBlockState(this.placeBlockState));

        compoundTag.put("nbt", this.nbt);
    }

    public void setLootTypes(int[] lootTypes) {
        this.lootTypes = lootTypes;
        setChanged();
    }

    public void setRarity(int rarity) {
        BABTMain.LOGGER.debug("Set Rarity");
        this.rarity = rarity;
        setChanged();
    }

    public void setPlaceBlockState(BlockState placeBlockState) {
        this.placeBlockState = placeBlockState;
        setChanged();
    }

    public void setNbt(CompoundTag nbt) {
        this.nbt = nbt;
    }

    public BlockState getPlaceBlockState() {
        return this.placeBlockState;
    }

    public CompoundTag getNbt() {
        return nbt;
    }

    public ArrayList<String> getLootTypes() {
        ArrayList<String> lootTypesFromInts = new ArrayList<>();

        for (int index : lootTypes) {
            lootTypesFromInts.add(lootNames.get(index));
        }
        // Already protected from errors by check in load() function
        return lootTypesFromInts;
    }

    public int getRarity() {
        return rarity;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
    }

}


