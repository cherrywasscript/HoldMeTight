package com.ricardthegreat.holdmetight.client.screens.preferences;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSize;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//TODO change background
public class PreferencesScreen extends Screen{
    //TODO make these translatable
    private static final Component TITLE = Component.literal("Preferences Screen");

    protected static final Component SAVE_BUTTON = Component.literal("Save");
    protected static final Component RESET_BUTTON = Component.literal("Reset");

    protected static final Component SAVE_BUTTON_TOOLTIP = Component.literal("Save all changed preferences");
    protected static final Component RESET_BUTTON_TOOLTIP = Component.literal("Reset all preferences to default(you must manually click save after this to apply the reset)");

    //String components related to scale preferences
    protected static final Component MAX_BUTTON = Component.literal("set max");
    protected static final Component MIN_BUTTON = Component.literal("set min");
    protected static final Component DEFAULT_BUTTON = Component.literal("set default");

    protected static final Component MAX_BUTTON_TOOLTIP = Component.literal("set your maximum scale");
    protected static final Component MIN_BUTTON_TOOLTIP = Component.literal("set minimum scale");
    protected static final Component DEFAULT_BUTTON_TOOLTIP = Component.literal("set default scale");
    protected static final Component OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP = Component.literal("Allow or disallow others from changing your size");
    protected static final Component YOU_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP = Component.literal("Allow or disallow yourself from changing your size");


    protected static final Component MAX_SCALE_FIELD = Component.literal("max scale");
    protected static final Component MIN_SCALE_FIELD = Component.literal("min scale");
    protected static final Component DEFAULT_SCALE_FIELD = Component.literal("default scale");

    protected static final Component MAX_SCALE_FIELD_TOOLTIP = Component.literal("input max scale");
    protected static final Component MIN_SCALE_FIELD_TOOLTIP = Component.literal("input min scale");
    protected static final Component DEFAULT_SCALE_FIELD_TOOLTIP = Component.literal("input default scale");

    //String components related to carry preferences
    //protected static final Component ACCESS_INVENTORY_BUTTON = Component.literal("True/False");
    //protected static final Component TRAP_WHEN_CARRYING_BUTTON = Component.literal("True/False");
    //protected static final Component TRAP_WHEN_CARRIED_BUTTON = Component.literal("True/False");
    protected static final Component TRUE_BUTTON = Component.literal("True");
    protected static final Component FALSE_BUTTON = Component.literal("False");

    protected static final Component ACCESS_INVENTORY_BUTTON_TOOLTIP = Component.literal("Allow or disallow players carrying you from accessing and editing your inventory");
    protected static final Component TRAP_WHEN_CARRYING_BUTTON_TOOLTIP = Component.literal("Allow or disallow players you are carrying from dismounting");
    protected static final Component TRAP_WHEN_CARRIED_BUTTON_TOOLTIP = Component.literal("Allow or disallow players who are carrying you from preventing you from dismounting");
    protected static final Component CAN_BE_PICKED_UP_BUTTON_TOOLTIP = Component.literal("Allow or disallow other players picking you up");
    protected static final Component CAN_PICKUP_OTHERS_BUTTON_TOOLTIP = Component.literal("Allow or disallow yourself from picking other players up");



    private final int imageWidth;
    private final int imageHeight;

    //the image background not final
    private ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");

    private Player player;
    private PlayerPreferences playerPreferences;

    //positions on the image background
    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    protected Tab currentTab;

    //Tabs
    protected Button scalePreferences;
    protected Button CarryPreferences;

    //Buttons and fields related to scale preferences
    protected Button maxButton;
    protected Button minButton;
    protected Button defaultButton;
    protected Button othersCanChangeYourSize;
    protected Button youCanChangeYourSize;

    protected EditBox maxScaleField;
    protected EditBox minScaleField;
    protected EditBox defaultScaleField;

    //Buttons and fields related to carry preferences
    protected Button inventoryCanBeAccessed; // true/false allow players carrying you to access and mess with your inventory
    protected Button trapCarriedPlayer; // true/false for stopping players from "shifting" off when you are carrying them
    protected Button canBeTrappedWhileCarried; // true/false for allowing yourself to not be able to "shift" off if the person carrying you has the above option enabled
    protected Button canBePickedup;
    protected Button canPickupOthers;

    //preference variables
    protected boolean othersCanChangeYourSizeVar;
    protected boolean youCanChangeYourSizeVar;
    protected boolean inventoryCanBeAccessedVar;
    protected boolean trapCarriedPlayerVar;
    protected boolean canBeTrappedWhileCarriedVar;
    protected boolean canBePickedupVar;
    protected boolean canPickupOthersVar;


    //"universal" buttons
    protected Button saveButton;
    protected Button resetButton;

    private final TabManager tabManager = new TabManager(this::addRenderableWidget, (widget) -> {
        this.removeWidget(widget);
    });

    private TabNavigationBar tabNavigationBar;

    public PreferencesScreen(Player player) {
        super(TITLE);
        
        this.imageWidth = 256;
        this.imageHeight = 256;

        this.player = player;
        this.playerPreferences = PlayerPreferencesProvider.getPlayerPreferencesCapability(player);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.rightPos = (this.width - this.leftPos) ;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.bottomPos = (this.height - this.topPos) ;
        this.centerHorizonalPos = (this.leftPos + this.rightPos) / 2 ;
        this.centerVerticalPos = (this.topPos + this.bottomPos) / 2;

        // im not really sure what this does but im also afraid to change/remove it
        if(this.minecraft == null) return;
        @SuppressWarnings("null")
        Level level = this.minecraft.level;
        if(level == null) return;

        this.currentTab = Tab.SIZE;

        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new CreateWorldScreen.GameTab(), new CreateWorldScreen.WorldTab(), new CreateWorldScreen.MoreTab()).build();
        this.addRenderableWidget(this.tabNavigationBar);

        initPreferenceVariables();

        initButtons();

        initEditBoxes();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.fill(leftPos, topPos, rightPos, bottomPos, 0xFF000000);
        //graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        //graphics.fill(centerHorizonalPos-50, centerVerticalPos-20, centerHorizonalPos+50, centerVerticalPos+20, 0x88FFFFFF);

        graphics.drawString(this.font, String.valueOf(PlayerSizeUtils.getSize(player)), leftPos + 8 + font.width("Current scale: "), topPos +18,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerPreferences.getMaxScale()), leftPos + 8 + font.width("Max scale: "), topPos +30,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerPreferences.getMinScale()), leftPos + 8 + font.width("Min scale: "), topPos +40,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerPreferences.getDefaultScale()), leftPos + 8 + font.width("Default scale: "), topPos +50,0xdddddd,false);

        graphics.drawString(this.font, "Current scale:", leftPos + 8, topPos +18,0xdddddd,false);
        graphics.drawString(this.font, "Max scale:", leftPos + 8, topPos +30,0xdddddd,false);
        graphics.drawString(this.font, "Min scale:", leftPos + 8, topPos +40,0xdddddd,false);
        graphics.drawString(this.font, "Default scale:", leftPos + 8, topPos +50,0xdddddd,false);
    }

    private void saveEditBox(){
        //TODO add logic for saving edit boxes
    }

    protected void initPreferenceVariables(){
        othersCanChangeYourSizeVar = playerPreferences.getOthersCanChange();
        youCanChangeYourSizeVar = playerPreferences.getSelfCanChange();
        inventoryCanBeAccessedVar = playerPreferences.getInventoryCanBeAccessed();
        trapCarriedPlayerVar = playerPreferences.getTrapCarriedPlayer();
        canBeTrappedWhileCarriedVar = playerPreferences.getCanBeTrappedWhileCarried();
        canBePickedupVar = playerPreferences.getCanBePickedup();
        canPickupOthersVar = playerPreferences.getCanPickupOthers();
    }

    protected void initButtons(){
        saveButton = addRenderableWidget(
            Button.builder(
                SAVE_BUTTON, this::handleMaxButton)
                .bounds(leftPos + 8, this.bottomPos, 74, 20)
                .tooltip(Tooltip.create(SAVE_BUTTON_TOOLTIP))
                .build()
        );

        resetButton = addRenderableWidget(
            Button.builder(
                RESET_BUTTON, this::handleMaxButton)
                .bounds(rightPos, this.bottomPos, 74, 20)
                .tooltip(Tooltip.create(RESET_BUTTON_TOOLTIP))
                .build()
        );

        initScalePreferenceButtons();
        initCarryPreferenceButtons();
    }

    protected void initScalePreferenceButtons(){
        maxButton = addRenderableWidget(
            Button.builder(
                MAX_BUTTON, this::handleMaxButton)
                .bounds(leftPos + 8, this.bottomPos -111, 74, 20)
                .tooltip(Tooltip.create(MAX_BUTTON_TOOLTIP))
                .build()
        );

        minButton = addRenderableWidget(
            Button.builder(
                MIN_BUTTON, this::handleMinButton)
                .bounds(leftPos + 91, this.bottomPos -111, 74, 20)
                .tooltip(Tooltip.create(MIN_BUTTON_TOOLTIP))
                .build()
        );

        defaultButton = addRenderableWidget(
            Button.builder(
                DEFAULT_BUTTON, this::handleDefaultButton)
                .bounds(leftPos + 174, this.bottomPos -111, 74, 20)
                .tooltip(Tooltip.create(DEFAULT_BUTTON_TOOLTIP))
                .build()
        );

        othersCanChangeYourSize = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleOtherSizeButton)
                .bounds(leftPos + 8, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP))
                .build()
        );

        youCanChangeYourSize = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleYouSizeButton)
                .bounds(leftPos + 8 + 30, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(YOU_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP))
                .build()
        );
    }

    protected void initCarryPreferenceButtons(){
        inventoryCanBeAccessed = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleInvAccessButton)
                .bounds(leftPos + 8 + 30 + 30 , this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(ACCESS_INVENTORY_BUTTON_TOOLTIP))
                .build()
        );

        trapCarriedPlayer = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleTrapCarriedButton)
                .bounds(leftPos + 8 + 30 + 30 + 30, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(TRAP_WHEN_CARRYING_BUTTON_TOOLTIP))
                .build()
        );

        canBeTrappedWhileCarried = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleCanTrapButton)
                .bounds(leftPos + 8 + 30 + 30 + 30 + 30, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(TRAP_WHEN_CARRIED_BUTTON_TOOLTIP))
                .build()
        );

        canBePickedup = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleCanBePickedButton)
                .bounds(leftPos + 8 + 30 + 30 + 30 + 30 + 30, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(CAN_BE_PICKED_UP_BUTTON_TOOLTIP))
                .build()
        );
        
        canPickupOthers = addRenderableWidget(
            Button.builder(
                TRUE_BUTTON, this::handleCanPickOthersButton)
                .bounds(leftPos + 8 + 30 + 30 + 30 + 30 + 30 + 30, this.bottomPos -90, 30, 20)
                .tooltip(Tooltip.create(CAN_PICKUP_OTHERS_BUTTON_TOOLTIP))
                .build()
        );
    }

    protected void handleMaxButton(Button button){
        String scaleString = maxScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setMaxScale(Float.parseFloat(scaleString));
            playerPreferences.updateShouldSync();
        }
    }
    protected void handleMinButton(Button button){
        String scaleString = minScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setMinScale(Float.parseFloat(scaleString));
            playerPreferences.updateShouldSync();
        }
    }
    protected void handleDefaultButton(Button button){
        String scaleString = defaultScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setDefaultScale(Float.parseFloat(scaleString));
            playerPreferences.updateShouldSync();
        }
    }

    protected void handleOtherSizeButton(Button button){
        othersCanChangeYourSizeVar = !othersCanChangeYourSizeVar;
        flipTrueFalseMessage(button, othersCanChangeYourSizeVar);
    }
    protected void handleYouSizeButton(Button button){
        youCanChangeYourSizeVar = !youCanChangeYourSizeVar;
        flipTrueFalseMessage(button, youCanChangeYourSizeVar);
    }
    protected void handleInvAccessButton(Button button){
        inventoryCanBeAccessedVar = !inventoryCanBeAccessedVar;
        flipTrueFalseMessage(button, inventoryCanBeAccessedVar);
    }
    protected void handleTrapCarriedButton(Button button){
        trapCarriedPlayerVar = !trapCarriedPlayerVar;
        flipTrueFalseMessage(button, trapCarriedPlayerVar);
    }
    protected void handleCanTrapButton(Button button){
        canBeTrappedWhileCarriedVar = !canBeTrappedWhileCarriedVar;
        flipTrueFalseMessage(button, canBeTrappedWhileCarriedVar);
    }
    protected void handleCanBePickedButton(Button button){
        canBePickedupVar = !canBePickedupVar;
        flipTrueFalseMessage(button, canBePickedupVar);
    }
    protected void handleCanPickOthersButton(Button button){
        canPickupOthersVar = !canPickupOthersVar;
        flipTrueFalseMessage(button, canPickupOthersVar);
    }

    protected void handleSaveButton(Button button){
        String scaleString = maxScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setMaxScale(Float.parseFloat(scaleString));
        }

        scaleString = minScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setMinScale(Float.parseFloat(scaleString));
        }

        scaleString = defaultScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerPreferences.setDefaultScale(Float.parseFloat(scaleString));
        }

        playerPreferences.setOthersCanChange(othersCanChangeYourSizeVar);
        playerPreferences.setSelfCanChange(youCanChangeYourSizeVar);
        playerPreferences.setInventoryCanBeAccessed(inventoryCanBeAccessedVar);
        playerPreferences.setTrapCarriedPlayer(trapCarriedPlayerVar);
        playerPreferences.setCanBeTrappedWhileCarried(canBeTrappedWhileCarriedVar);
        playerPreferences.setCanBePickedup(canBePickedupVar);
        playerPreferences.setCanPickupOthers(canPickupOthersVar);

        playerPreferences.updateShouldSync();
    }
    protected void handleResetButton(Button button){
        maxScaleField.setValue(Double.toString(0d));
        minScaleField.setValue(Double.toString(1d));
        defaultScaleField.setValue(Double.toString(Double.MAX_VALUE));

        othersCanChangeYourSizeVar = true;
        flipTrueFalseMessage(othersCanChangeYourSize, othersCanChangeYourSizeVar);

        youCanChangeYourSizeVar = true;
        flipTrueFalseMessage(youCanChangeYourSize, youCanChangeYourSizeVar);

        inventoryCanBeAccessedVar = true;
        flipTrueFalseMessage(inventoryCanBeAccessed, inventoryCanBeAccessedVar);

        trapCarriedPlayerVar = true;
        flipTrueFalseMessage(trapCarriedPlayer, trapCarriedPlayerVar);

        canBeTrappedWhileCarriedVar = true;
        flipTrueFalseMessage(canBeTrappedWhileCarried, canBeTrappedWhileCarriedVar);

        canBePickedupVar = true;
        flipTrueFalseMessage(canBePickedup, canBePickedupVar);

        canPickupOthersVar = true;
        flipTrueFalseMessage(canPickupOthers, canPickupOthersVar);
    }

    protected void flipTrueFalseMessage(Button button, boolean bool){
        if (bool) {
            button.setMessage(TRUE_BUTTON);
        }else{
            button.setMessage(FALSE_BUTTON);
        }
    }

    protected void initEditBoxes() {
        maxScaleField = addRenderableWidget(new EditBox(font, leftPos + 8, bottomPos - 80, 74, 20, MAX_SCALE_FIELD));
        minScaleField = addRenderableWidget(new EditBox(font, leftPos + 91, bottomPos - 80, 74, 20, MIN_SCALE_FIELD));
        defaultScaleField = addRenderableWidget(new EditBox(font, leftPos + 174, bottomPos - 80, 74, 20, DEFAULT_SCALE_FIELD));

        //there's probably a better way to do this but setting up a predicate filter for the box you type sizes into
        Predicate<String> filter = new Predicate<String>() {
            @Override
            public boolean test(String t){
                //disallow spaces at the beginning or end
                if (!t.trim().equals(t)){
                    return false;
                }
                //check if the string is empty to allow people to delete everything
                if(t != null && t.isEmpty()){
                    return true;
                }
                //check if the string ends with f or d as these are both fine to parse as floats but i dont want it to be typed
                //again almost certainly a better way to do this but its just a silly little thing
                String checkFinalChar = t.substring(t.length()-1);
                if(checkFinalChar.equals("f") || checkFinalChar.equals("d")){
                    return false;
                }
                //try to parse the string as a float, allow it if it succeeds dont if it doesnt
                try{
                    float f = Float.parseFloat(t);

                    //ensure that the input it not less than 0 as that can cause issues
                    if (f < 0) {
                        return false;
                    }
                    return true;
                }catch (Exception e){ 
                    return false;
                }      
            }
        };

        maxScaleField.setFilter(filter);
        minScaleField.setFilter(filter);
        defaultScaleField.setFilter(filter);

        maxScaleField.setValue(Float.toString(playerPreferences.getMaxScale()));
        minScaleField.setValue(Float.toString(playerPreferences.getMinScale()));
        defaultScaleField.setValue(Float.toString(playerPreferences.getDefaultScale()));

        maxScaleField.setTooltip(Tooltip.create(MAX_SCALE_FIELD_TOOLTIP, MAX_SCALE_FIELD_TOOLTIP));
        minScaleField.setTooltip(Tooltip.create(MIN_SCALE_FIELD_TOOLTIP, MIN_SCALE_FIELD_TOOLTIP));
        defaultScaleField.setTooltip(Tooltip.create(DEFAULT_SCALE_FIELD_TOOLTIP, DEFAULT_SCALE_FIELD_TOOLTIP));
    }

    @Override
    public void onClose() {
        saveEditBox();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    enum Tab {
        SIZE,
        CARRY
    }

    @OnlyIn(Dist.CLIENT)
    class SizeTab extends GridLayoutTab {
        //TODO make translatable
        private static final Component TITLE = Component.literal("Size");

        SizeTab() {
            super(TITLE);
            RowHelper helper = this.layout.columnSpacing(10).rowSpacing(8).createRowHelper(2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class CarryTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Carry");

        CarryTab() {
            super(TITLE);
        }
    }
}
