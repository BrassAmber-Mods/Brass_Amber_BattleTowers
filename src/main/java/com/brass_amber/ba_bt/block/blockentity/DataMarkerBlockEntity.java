package com.brass_amber.ba_bt.block.blockentity;

import com.brass_amber.ba_bt.BABTMain;
import com.brass_amber.ba_bt.init.BTBlockEntityType;
import com.brass_amber.ba_bt.inventory.DataMarkerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.brass_amber.ba_bt.util.BTStatics.*;
import static com.brass_amber.ba_bt.util.BTUtil.listFromTag;
import static com.brass_amber.ba_bt.util.BTUtil.newStringList;

public class DataMarkerBlockEntity extends BlockEntity implements MenuProvider {

    // Container type can be any type listed in containerTypes in
    protected ArrayList<String> lootTypes = new ArrayList<>();
    protected int rarity = -1; // -1 == set by room height. 1-4 == set by data
    protected BlockState placeBlockState = Blocks.BEDROCK.defaultBlockState();
    protected boolean saveMetaData = true;
    protected CompoundTag nbt = new CompoundTag();

    private final ItemStackHandler itemStackHandler = new ItemStackHandler() {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof BlockItem;
        }
    };

    private static final int BLOCK_LOAD_SLOT = 0;
    public static final int RARITY_DATA = 1;
    public static final int SAVE_METADATA = 2;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;

    public DataMarkerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BTBlockEntityType.DATA_MARKER.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {

                if (index == 16) {
                    return DataMarkerBlockEntity.this.rarity;
                } else {
                    try {
                        return lootNames.indexOf(DataMarkerBlockEntity.this.lootTypes.get(index));
                    }
                    catch (IndexOutOfBoundsException e) {
                        return -1;
                    }
                }
            }

            @Override
            public void set(int index, int value) {
                BABTMain.LOGGER.debug("Set Data: {} {}", index, value);
                if (index == 0) {
                    DataMarkerBlockEntity.this.setRarity(value);
                } else {
                    String lootType = lootNames.get(index);
                    if (value > 0) {
                        DataMarkerBlockEntity.this.lootTypes.add(lootType);
                    } else {
                        DataMarkerBlockEntity.this.lootTypes.remove(lootType);
                    }
                }
            }

            @Override
            public int getCount() {
                return 17;
            }
        };

    }


    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        BABTMain.LOGGER.debug("Loading Marker Data");

        itemStackHandler.deserializeNBT(compoundTag.getCompound("Inventory"));

        this.lootTypes = listFromTag(compoundTag.getCompound("Loot"), lootNames);


        try {
            this.rarity = compoundTag.getInt("Rarity");
        } catch (Exception ignored) {
            this.rarity = -1;
        }
        if (this.rarity <= -5 || this.rarity >= 5) {
            this.rarity = -1;
        }

        try {
            this.placeBlockState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), compoundTag.getCompound("BlockState"));
        } catch (Exception ignored) {
        }

        this.nbt = compoundTag.getCompound("nbt");

    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        // BABTMain.LOGGER.info("Saving Marker Data");

        compoundTag.put("Inventory", itemStackHandler.serializeNBT());


        compoundTag.put("Loot", newStringList(this.lootTypes));
        compoundTag.putInt("Rarity", rarity);

        ListTag blockTag = new ListTag();

        blockTag.add(NbtUtils.writeBlockState(this.placeBlockState));
        compoundTag.put("BlockState", blockTag);

        compoundTag.put("nbt", this.nbt);

    }

    public void setLootTypes(ArrayList<String> lootTypes) {
        this.lootTypes = lootTypes;
    }

    public void setRarity(int rarity) {
        BABTMain.LOGGER.debug("Set Rarity");
        this.rarity = rarity;
    }

    public void setPlaceBlockState(BlockState placeBlockState) {
        this.placeBlockState = placeBlockState;
        this.itemStackHandler.setStackInSlot(0, placeBlockState.getBlock().asItem().getDefaultInstance());
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
        // Already protected from errors by check in load() function
        return lootTypes;
    }

    public int getRarity() {
        return rarity;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ba_bt.data_marker");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new DataMarkerMenu(containerId, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void setSaveMetaData(boolean saveMetaData) {
        this.saveMetaData = saveMetaData;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (this.itemStackHandler.getStackInSlot(0).getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() != placeBlockState.getBlock()) {
                this.placeBlockState = blockItem.getBlock().defaultBlockState();
            }
        }
        this.saveWithoutMetadata();
    }

}


