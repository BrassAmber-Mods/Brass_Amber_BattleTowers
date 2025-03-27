package com.brass_amber.ba_bt.inventory;

import com.brass_amber.ba_bt.BABTMain;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Objects;

import static com.brass_amber.ba_bt.util.BTStatics.lootNames;

@OnlyIn(Dist.CLIENT)
public class DataMarkerScreen extends AbstractContainerScreen<DataMarkerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(BABTMain.MODID, "textures/gui/datamarker_gui.png");
    private static final int BUTTONS1_START_X = 8;
    private static final int BUTTONS2_START_X = 98;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int SCROLLER_WIDTH = 6;
    private static final int SCROLL_BAR_HEIGHT = 70;
    private static final int SCROLLABLES_TOP_POS_Y = 8;
    private static final int SCROLL_BAR_START_X = 72;
    private static final int SCROLL_BAR2_START_X = 162;
    private int lootIndex = 0;
    private int lootIndex2 = 0;
    private final DataMarkerScreen.LootTypeButton[] lootTypeButtons = new DataMarkerScreen.LootTypeButton[16];
    private final ArrayList<LootTypeAddedButton> lootTypeAddedButtons = new ArrayList<>();
    private EditBox rarity;
    int buttons1Scroll;
    int buttons2Scroll;
    private boolean isDragging;
    private boolean isDragging2;

    int TEXTURE_SIZE = 256;

    public DataMarkerScreen(DataMarkerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        int index = 0;

        for (String name: lootNames) {
            if (!Objects.equals(name, "Invalid")) {
                this.lootTypeButtons[index] = new LootTypeButton(this.leftPos + BUTTONS1_START_X, this.topPos + SCROLLABLES_TOP_POS_Y, index, this::lootTypeButtonSelected, name);
            }
            this.addRenderableWidget(this.lootTypeButtons[index]);
            index++;
        }

        for (String lootType : this.getMenu().getLootNames()) {
            LootTypeAddedButton lootTypeAddedButton = new LootTypeAddedButton(
                    this.leftPos + BUTTONS2_START_X, this.topPos + SCROLLABLES_TOP_POS_Y, this.lootTypeAddedButtons.size(),
                    lootNames.indexOf(lootType), this::lootTypeButtonSelected, lootType);
            this.lootTypeAddedButtons.add(lootTypeAddedButton);
            this.addRenderableWidget(lootTypeAddedButton);
        }

        this.rarity = new EditBox(this.font, this.leftPos + 82, this.topPos + 40, 12, 12, Component.literal("Rarity"));
        this.rarity.setCanLoseFocus(true);
        this.rarity.setTextColor(-1);
        this.rarity.setTextColorUneditable(-1);
        this.rarity.setBordered(false);
        this.rarity.setMaxLength(2);
        this.rarity.setResponder(this::onRarityChanged);
        this.rarity.setValue(String.valueOf(this.getMenu().getRarity()));
        this.rarity.setTooltip(Tooltip.create(Component.literal("-1 uses the tower's floor to calculate rarity")));
        this.addRenderableWidget(this.rarity);

        ListManipulationButton addLootItem = new ListManipulationButton(this.leftPos + 82, this.topPos + SCROLLABLES_TOP_POS_Y - 1, true, this::editLootTypeAddedButtons);
        this.addRenderableWidget(addLootItem);

        ListManipulationButton removeLootItem = new ListManipulationButton(this.leftPos + 82, this.topPos + SCROLLABLES_TOP_POS_Y + 59, false, this::editLootTypeAddedButtons);
        this.addRenderableWidget(removeLootItem);


        Checkbox saveMeta = new DataCheckBox(this.leftPos + 80, this.topPos + 52);
        addRenderableWidget(saveMeta);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {

    }

    @Override
    protected void containerTick() {
        super.containerTick();

        this.rarity.tick();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // BABTMain.LOGGER.debug("in screen render: " + this.leftPos + "  " + x);

        // Texture (BaseTexture), x (x Placement), Y (y placement), texture to paint start x, texture to paint start y, x size, y size
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight);

        // guiGraphics.blit(TEXTURE, )
    }

    private void renderScrollers(GuiGraphics guiGraphics) {
        int scrollableBarSection = SCROLL_BAR_HEIGHT - SCROLLER_HEIGHT;
        int pixelsPerScroll =  scrollableBarSection / 9;
        int yPos = this.topPos + SCROLLABLES_TOP_POS_Y + (this.buttons1Scroll * pixelsPerScroll);

        // Texture (BaseTexture), x (x Placement), Y (y placement), z layer, section start x, section start y, section width, section height, image width, image height
        guiGraphics.blit(TEXTURE, this.leftPos + BUTTONS1_START_X + SCROLL_BAR_HEIGHT - 6, yPos, 0, 0.0F, 166.0F, SCROLLER_WIDTH, SCROLLER_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);

        if (this.lootTypeAddedButtons.size() > 7) {
            int addedButtonsExtras = this.lootTypeAddedButtons.size() - 7;
            int pixelsPerScroll2 =  scrollableBarSection / addedButtonsExtras;
            int yPos2 = this.topPos + SCROLLABLES_TOP_POS_Y + (this.buttons2Scroll * pixelsPerScroll2);
            guiGraphics.blit(TEXTURE, this.leftPos + BUTTONS2_START_X + SCROLL_BAR_HEIGHT - 6, yPos2, 0, 0.0F, 166.0F, SCROLLER_WIDTH, SCROLLER_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        } else {
            guiGraphics.blit(TEXTURE, this.leftPos + BUTTONS2_START_X + SCROLL_BAR_HEIGHT - 6, this.topPos + SCROLLABLES_TOP_POS_Y, 0, 0.0F, 181.0F, SCROLLER_WIDTH, SCROLLER_HEIGHT, TEXTURE_SIZE, TEXTURE_SIZE);
        }

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        for (LootTypeButton lootTypeButton: this.lootTypeButtons) {
            lootTypeButton.visible = lootTypeButton.getIndex() >= this.buttons1Scroll && lootTypeButton.getIndex() < this.buttons1Scroll + 7;
        }

        for (LootTypeAddedButton lootTypeAddedButton: this.lootTypeAddedButtons) {
            lootTypeAddedButton.visible = lootTypeAddedButton.getIndex() - 100 >= this.buttons2Scroll && lootTypeAddedButton.getIndex() - 100 < this.buttons2Scroll + 7;
        }
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        renderScrollers(guiGraphics);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int keyPressedValue) {
        this.isDragging = false;
        this.isDragging2 = false;

        // if on scrollbar
        if (mouseWithin(mouseX, this.leftPos + SCROLL_BAR_START_X, leftPos + SCROLL_BAR_START_X + SCROLLER_WIDTH)
                && mouseWithin(mouseY, this.topPos + SCROLLABLES_TOP_POS_Y,this.topPos + SCROLLABLES_TOP_POS_Y + SCROLL_BAR_HEIGHT)) {
            this.isDragging = true;
        }

        if (mouseWithin(mouseX, leftPos + SCROLL_BAR2_START_X, leftPos + SCROLL_BAR2_START_X + SCROLLER_WIDTH)
                && mouseWithin(mouseY, this.topPos + SCROLLABLES_TOP_POS_Y,this.topPos + SCROLLABLES_TOP_POS_Y + SCROLL_BAR_HEIGHT)) {
            this.isDragging2 = true;
        }

        return super.mouseClicked(mouseX, mouseY, keyPressedValue);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double scrolledAmount) {

        if (mouseWithin(mouseX, this.leftPos + BUTTONS1_START_X, this.leftPos + BUTTONS1_START_X + SCROLL_BAR_HEIGHT)
                && mouseWithin(mouseY, this.topPos + SCROLLABLES_TOP_POS_Y, this.topPos + SCROLLABLES_TOP_POS_Y + SCROLL_BAR_HEIGHT)) {
            this.buttons1Scroll = Mth.clamp(this.buttons1Scroll - (int) scrolledAmount, 0, 9);
        }

        int addedButtonsExtras = Math.max(this.lootTypeAddedButtons.size() - 7, 0);

        if (addedButtonsExtras > 0) {
            if (mouseWithin(mouseX, this.leftPos + BUTTONS2_START_X, this.leftPos + BUTTONS2_START_X + SCROLL_BAR_HEIGHT)
                && mouseWithin(mouseY, this.topPos + SCROLLABLES_TOP_POS_Y, this.topPos + SCROLLABLES_TOP_POS_Y + SCROLL_BAR_HEIGHT)) {
                this.buttons2Scroll = Mth.clamp(this.buttons2Scroll - (int) scrolledAmount, 0, addedButtonsExtras);
            }
        }

        return true;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int activeButton, double xDistanceMoved, double yDistanceMoved) {

        if (this.isDragging) {
            this.buttons1Scroll = Mth.clamp(this.buttons1Scroll + (int)yDistanceMoved/(32/9), 0, 9);
            return true;
        }

        if (this.isDragging2) {
            int addedButtonsExtras = Math.max(this.lootTypeAddedButtons.size() - 7, 0);
            if (addedButtonsExtras > 0) {
                this.buttons2Scroll = Mth.clamp(this.buttons2Scroll + (int)yDistanceMoved/(32/addedButtonsExtras), 0, addedButtonsExtras);
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, activeButton, xDistanceMoved, yDistanceMoved);
    }

    public void lootTypeButtonSelected(Button button) {
        if (button instanceof LootTypeButton lootTypeButton) {
            if (lootTypeButton.getIndex() < 100) {
                this.lootTypeButtons[this.lootIndex].unselect();
                this.lootIndex = lootTypeButton.getIndex();

            } else {
                this.lootTypeAddedButtons.get(this.lootIndex2).unselect();
                this.lootIndex2 = lootTypeButton.getIndex() - 100;
            }
        }
    }

    public void editLootTypeAddedButtons(Button button) {
        if (button instanceof ListManipulationButton listManipulationButton) {
            if (listManipulationButton.isAdd() && this.lootTypeAddedButtons.size() < 16) {
                LootTypeAddedButton lootTypeAddedButton = new LootTypeAddedButton(
                        this.leftPos + BUTTONS2_START_X, this.topPos + SCROLLABLES_TOP_POS_Y, this.lootTypeAddedButtons.size(),
                        this.lootIndex, this::lootTypeButtonSelected, lootNames.get(lootIndex));
                this.getMenu().setLootAt(this.lootTypeAddedButtons.size(), this.lootIndex);
                this.lootTypeAddedButtons.add(lootTypeAddedButton);
                this.addRenderableWidget(lootTypeAddedButton);

            } else if (!this.lootTypeAddedButtons.isEmpty()){
                LootTypeAddedButton lootTypeAddedButton =  this.lootTypeAddedButtons.remove(this.lootIndex2);
                this.getMenu().setLootAt(this.lootIndex2, -1);
                this.removeWidget(lootTypeAddedButton);
                this.lootIndex2 = 0;
                this.buttons2Scroll = 0;
                for (int i = 0; i < this.lootTypeAddedButtons.size(); i++) {
                    this.lootTypeAddedButtons.get(i).setIndex(i);
                }

            }
        }
    }

    private void onRarityChanged(String rarity) {
      try {
        this.menu.setRarity(Integer.parseInt(rarity));
      } catch (NumberFormatException e) {
          this.menu.setRarity(-1);
      }
    }


    @Override
    public void onClose() {
        super.onClose();
    }

    @OnlyIn(Dist.CLIENT)
    class LootTypeButton extends Button {
        int index;
        float texturePosX;
        float texturePosY = 166F;
        int baseY;
        int indexY;
        boolean selected;

        public LootTypeButton(int x, int y, int index, Button.OnPress onPress, String name) {
            super(x, y + (index * 10), SCROLL_BAR_HEIGHT - 7, 10, Component.literal(name), onPress, DEFAULT_NARRATION);
            this.index = index;
            this.visible = false;
            this.baseY = y;
            this.indexY = this.baseY + (index * 10);
            this.texturePosX = this instanceof LootTypeAddedButton ? 98 : 8;
            this.selected = false;
        }

        public int getIndex() {
            return this.index;
        }

        @Override
        public void onPress() {
            super.onPress();
            this.selected = true;
        }

        public void unselect() {
            this.selected = false;
        }

        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
            int scrollAmount = this instanceof LootTypeAddedButton ? buttons2Scroll : buttons1Scroll;
            this.setY(this.indexY - (scrollAmount * 10));
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 0, texturePosX, texturePosY + (this.selected ? 10F : 0F), this.getWidth(), this.getHeight(), TEXTURE_SIZE, TEXTURE_SIZE);
            // guiGraphics, font, component, x, y, width, height, alpha
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = getFGColor();
            this.renderString(guiGraphics, Minecraft.getInstance().font, i | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class LootTypeAddedButton extends LootTypeButton {
        final int lootTypeIndex;

        public LootTypeAddedButton(int x, int y, int index, int lootTypeIndex, OnPress onPress, String name) {
            super(x, y, index, onPress, name);
            this.lootTypeIndex = lootTypeIndex;
        }

        public int getLootTypeIndex() {
            return lootTypeIndex;
        }

        public void setIndex(int index) {
            this.index = index;
            this.indexY = this.baseY + (index * 10);
        }

        @Override
        public int getIndex() {
            return super.getIndex() + 100;
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ListManipulationButton extends Button {
        final boolean isAdd;
        boolean clicked;
        protected ListManipulationButton(int x, int y, boolean isAdd, OnPress onPress) {
            super(x, y, 12, 12, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.isAdd = isAdd;
            this.clicked = false;
        }

        public boolean isAdd() {
            return isAdd;
        }

        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
            guiGraphics.blit(
                    TEXTURE, this.getX(), this.getY(), 0,
                    178.0F, 34.0F + (this.clicked ? 16F : 0F) + (this.isAdd() ? 0 : 32),
                    this.getWidth(), this.getHeight(), TEXTURE_SIZE, TEXTURE_SIZE
            );
        }

        @Override
        public void onPress() {
            super.onPress();
            this.clicked = true;
        }

        @Override
        public void onRelease(double p_93669_, double p_93670_) {
            this.clicked = false;
            super.onRelease(p_93669_, p_93670_);
        }
    }

    class DataCheckBox extends Checkbox {

        public DataCheckBox(int x, int y) {
            super(x, y, 16, 16, Component.literal(""), true);
        }

        public void renderWidget(GuiGraphics p_283124_, int p_282925_, int p_282705_, float p_282612_) {
            RenderSystem.enableDepthTest();
            p_283124_.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            p_283124_.blit(TEXTURE, this.getX(), this.getY(), 176.0F, this.selected() ? 0.0F : 16.0F, this.getWidth(), this.getHeight(), TEXTURE_SIZE, TEXTURE_SIZE);
            p_283124_.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        }

        @Override
        public void onPress() {
            super.onPress();
            getMenu().blockEntity.setSaveMetaData(this.selected());
        }
    }

    private boolean mouseWithin(double value, int lowBound, int upperBound) {
        return value >= lowBound && value <= upperBound;
    }
}
