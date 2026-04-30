package com.ricardthegreat.holdmetight.client.screens.preferences;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.checkerframework.checker.units.qual.s;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSize;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.GridLayout.RowHelper;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.CommonComponents;
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

    //preference variables
    protected boolean othersCanChangeYourSizeVar;
    protected boolean youCanChangeYourSizeVar;

    protected boolean canBePickedupVar;
    protected boolean canPickupOthersVar;
    protected boolean inventoryCanBeAccessedVar;
    protected boolean trapCarriedPlayerVar;
    protected boolean canBeTrappedWhileCarriedVar;

    private final TabManager tabManager = new TabManager(this::addRenderableWidget, (widget) -> {
        this.removeWidget(widget);
    });

    private TabNavigationBar tabNavigationBar;
    private SizeTab sizeTab;
    private CarryTab carryTab;

    private GridLayout bottomButtons;

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

        initPreferenceVariables();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.rightPos = (this.width - this.leftPos) ;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.bottomPos = (this.height - this.topPos) ;
        this.centerHorizonalPos = (this.leftPos + this.rightPos) / 2 ;
        this.centerVerticalPos = (this.topPos + this.bottomPos) / 2;

        //initiate the tabs here so that they have access to all variables and dont cause crashes
        this.sizeTab = new PreferencesScreen.SizeTab();
        this.carryTab = new PreferencesScreen.CarryTab();

        this.tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(sizeTab, carryTab).build();
        this.addRenderableWidget(this.tabNavigationBar);

        this.tabNavigationBar.selectTab(0, false);

        initBottomButtons();
        repositionElements();
    }
    
    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        sizeTab.currentScale.setMessage(Component.literal("Current Size: " + PlayerSizeUtils.getSize(player)));
    }

    public void repositionElements() {
        if (this.tabNavigationBar != null && this.bottomButtons != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();
            this.bottomButtons.arrangeElements();
            FrameLayout.centerInRectangle(this.bottomButtons, 0, this.height - 36, this.width, 36);
            int i = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle screenrectangle = new ScreenRectangle(0, i, this.width, this.bottomButtons.getY() - i);
            this.tabManager.setTabArea(screenrectangle);
        }
    }

    private void initBottomButtons(){
        this.bottomButtons = (new GridLayout()).columnSpacing(10);

        RowHelper helper = this.bottomButtons.createRowHelper(2);
        helper.addChild(Button.builder(SAVE_BUTTON, (button) -> {
            this.handleSaveButton();
        }).tooltip(Tooltip.create(SAVE_BUTTON_TOOLTIP)).build());

        helper.addChild(Button.builder(CommonComponents.GUI_CANCEL, (button) -> {
            this.minecraft.setScreen((Screen)null);
        }).build());

        this.bottomButtons.visitWidgets((p_267851_) -> {
            p_267851_.setTabOrderGroup(1);
            this.addRenderableWidget(p_267851_);
        });
    }

    protected void initPreferenceVariables(){
        othersCanChangeYourSizeVar = playerPreferences.getOthersCanChange();
        youCanChangeYourSizeVar = playerPreferences.getSelfCanChange();

        canBePickedupVar = playerPreferences.getCanBePickedup();
        canPickupOthersVar = playerPreferences.getCanPickupOthers();
        inventoryCanBeAccessedVar = playerPreferences.getInventoryCanBeAccessed();
        trapCarriedPlayerVar = playerPreferences.getTrapCarriedPlayer();
        canBeTrappedWhileCarriedVar = playerPreferences.getCanBeTrappedWhileCarried();
    }

    protected void handleSaveButton(){
        sizeTab.save();
        carryTab.save();
        
        playerPreferences.updateShouldSync();
        this.minecraft.setScreen((Screen)null);
    }
    protected void handleResetButton(Button button){

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    class SizeTab extends GridLayoutTab {
        //TODO make translatable
        private static final Component TITLE = Component.literal("Size");

        protected static final Component OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON = Component.literal("Others can change your size");
        protected static final Component OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP = Component.literal("Allow or disallow others from changing your size (using size remotes)");
        protected static final Component YOU_CAN_CHANGE_YOUR_SIZE_BUTTON = Component.literal("You can change your size");
        protected static final Component YOU_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP = Component.literal("Allow or disallow yourself from changing your size (using size remotes)");

        protected static final Component MIN_SCALE_FIELD = Component.literal("min scale");
        protected static final Component MIN_SCALE_FIELD_TOOLTIP = Component.literal("input min scale");

        protected static final Component DEFAULT_SCALE_FIELD = Component.literal("default scale");
        protected static final Component DEFAULT_SCALE_FIELD_TOOLTIP = Component.literal("input default scale");

        protected static final Component MAX_SCALE_FIELD = Component.literal("max scale");
        protected static final Component MAX_SCALE_FIELD_TOOLTIP = Component.literal("input max scale");


        protected CycleButton<Boolean> othersCanChangeYourSize;
        protected CycleButton<Boolean> youCanChangeYourSize;

        protected EditBox minScaleField;
        protected EditBox defaultScaleField;
        protected EditBox maxScaleField;

        protected StringWidget currentScale;

        //should this be here, no idea but it is
        private Predicate<String> filter = new Predicate<String>() {
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

        SizeTab() {
            super(TITLE);
            RowHelper mainGrid = this.layout.rowSpacing(8).createRowHelper(1);
            
            currentScale = mainGrid.addChild(new StringWidget(210, 9, Component.literal("Current Size: " + PlayerSizeUtils.getSize(player)), font)).alignCenter();

            initButtons(mainGrid);

            RowHelper subGrid1 = (new GridLayout()).rowSpacing(8).createRowHelper(1);

            RowHelper subGrid1$sub1 = (new GridLayout()).columnSpacing(10).rowSpacing(4).createRowHelper(2);
            subGrid1$sub1.addChild(new StringWidget(150, 20, Component.literal("Min scale: " + PreferencesScreen.this.playerPreferences.getMinScale()), font)).alignLeft();
            minScaleField = subGrid1$sub1.addChild(new EditBox(PreferencesScreen.this.font, 0, 0, 50, 20, MIN_SCALE_FIELD));
            
            RowHelper subGrid1$sub2 = (new GridLayout()).columnSpacing(10).rowSpacing(4).createRowHelper(2);
            subGrid1$sub1.addChild(new StringWidget(150, 20, Component.literal("Default scale: " + PreferencesScreen.this.playerPreferences.getDefaultScale()), font)).alignLeft();
            defaultScaleField = subGrid1$sub1.addChild(new EditBox(PreferencesScreen.this.font, 0, 0, 50, 20, DEFAULT_SCALE_FIELD));

            RowHelper subGrid1$sub3 = (new GridLayout()).columnSpacing(10).rowSpacing(4).createRowHelper(2);
            subGrid1$sub1.addChild(new StringWidget(150, 20, Component.literal("Max scale: " + PreferencesScreen.this.playerPreferences.getMaxScale()), font)).alignLeft();
            maxScaleField = subGrid1$sub1.addChild(new EditBox(PreferencesScreen.this.font, 0, 0, 50, 20, MAX_SCALE_FIELD));

            initEditBoxesExtra();

            subGrid1.addChild(subGrid1$sub1.getGrid());
            subGrid1.addChild(subGrid1$sub2.getGrid());
            subGrid1.addChild(subGrid1$sub3.getGrid());

            mainGrid.addChild(subGrid1.getGrid());
        }

        private void initButtons(RowHelper helper){


            othersCanChangeYourSize = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, OTHERS_CAN_CHANGE_YOUR_SIZE_BUTTON, (button, bool) -> {
                PreferencesScreen.this.othersCanChangeYourSizeVar = bool;
            }));

            othersCanChangeYourSize.setValue(PreferencesScreen.this.othersCanChangeYourSizeVar);

            youCanChangeYourSize = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(YOU_CAN_CHANGE_YOUR_SIZE_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, YOU_CAN_CHANGE_YOUR_SIZE_BUTTON, (button, bool) -> {
                PreferencesScreen.this.youCanChangeYourSizeVar = bool;
            }));

            youCanChangeYourSize.setValue(PreferencesScreen.this.youCanChangeYourSizeVar);
        }

        private void initEditBoxesExtra(){
            minScaleField.setFilter(filter);
            defaultScaleField.setFilter(filter);
            maxScaleField.setFilter(filter);

            minScaleField.setValue(Float.toString(PreferencesScreen.this.playerPreferences.getMinScale()));
            defaultScaleField.setValue(Float.toString(PreferencesScreen.this.playerPreferences.getDefaultScale()));          
            maxScaleField.setValue(Float.toString(PreferencesScreen.this.playerPreferences.getMaxScale()));

            minScaleField.setTooltip(Tooltip.create(MIN_SCALE_FIELD_TOOLTIP, MIN_SCALE_FIELD_TOOLTIP));
            defaultScaleField.setTooltip(Tooltip.create(DEFAULT_SCALE_FIELD_TOOLTIP, DEFAULT_SCALE_FIELD_TOOLTIP));
            maxScaleField.setTooltip(Tooltip.create(MAX_SCALE_FIELD_TOOLTIP, MAX_SCALE_FIELD_TOOLTIP));
        }

        public void save(){
            saveToggles();
            saveSizeValues();
        }

        private void saveToggles(){
            PreferencesScreen.this.playerPreferences.setOthersCanChange(othersCanChangeYourSizeVar);
            PreferencesScreen.this.playerPreferences.setSelfCanChange(youCanChangeYourSizeVar);
        }

        private void saveSizeValues(){
            String scaleString = minScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                PreferencesScreen.this.playerPreferences.setMinScale(Float.parseFloat(scaleString));
            }

            scaleString = defaultScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                PreferencesScreen.this.playerPreferences.setDefaultScale(Float.parseFloat(scaleString));
            }

            scaleString = maxScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                PreferencesScreen.this.playerPreferences.setMaxScale(Float.parseFloat(scaleString));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    class CarryTab extends GridLayoutTab {
        private static final Component TITLE = Component.literal("Carry");

        protected static final Component CAN_BE_PICKED_UP_BUTTON = Component.literal("You can be picked up");
        protected static final Component CAN_BE_PICKED_UP_BUTTON_TOOLTIP = Component.literal("Allow or disallow other players picking you up");

        protected static final Component CAN_PICKUP_OTHERS_BUTTON = Component.literal("You can pick others up");
        protected static final Component CAN_PICKUP_OTHERS_BUTTON_TOOLTIP = Component.literal("Allow or disallow yourself from picking other players up");

        protected static final Component ACCESS_INVENTORY_BUTTON = Component.literal("Players can access your inventory:");
        protected static final Component ACCESS_INVENTORY_BUTTON_TOOLTIP = Component.literal("Allow or disallow players carrying you from accessing and editing your inventory");

        protected static final Component TRAP_WHEN_CARRYING_BUTTON = Component.literal("You trap players when carrying them");
        protected static final Component TRAP_WHEN_CARRYING_BUTTON_TOOLTIP = Component.literal("Allow or disallow players you are carrying from dismounting");

        protected static final Component TRAP_WHEN_CARRIED_BUTTON = Component.literal("You can be trapped while carried");
        protected static final Component TRAP_WHEN_CARRIED_BUTTON_TOOLTIP = Component.literal("Allow or disallow players who are carrying you from preventing you from dismounting");

        protected CycleButton<Boolean> inventoryCanBeAccessed; // true/false allow players carrying you to access and mess with your inventory
        protected CycleButton<Boolean> trapCarriedPlayer; // true/false for stopping players from "shifting" off when you are carrying them
        protected CycleButton<Boolean> canBeTrappedWhileCarried; // true/false for allowing yourself to not be able to "shift" off if the person carrying you has the above option enabled
        protected CycleButton<Boolean> canBePickedup;
        protected CycleButton<Boolean> canPickupOthers;

        CarryTab() {
            super(TITLE);
            RowHelper mainGrid = this.layout.rowSpacing(8).createRowHelper(1);

            initButtons(mainGrid);
        }

        private void initButtons(RowHelper helper){
            canBePickedup = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(CAN_BE_PICKED_UP_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, CAN_BE_PICKED_UP_BUTTON, (button, bool) -> {
                PreferencesScreen.this.canBePickedupVar = bool;
            }));
            canBePickedup.setValue(PreferencesScreen.this.canBePickedupVar);

            canPickupOthers = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(CAN_PICKUP_OTHERS_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, CAN_PICKUP_OTHERS_BUTTON, (button, bool) -> {
                PreferencesScreen.this.canPickupOthersVar = bool;
            }));
            canPickupOthers.setValue(PreferencesScreen.this.canPickupOthersVar);

            inventoryCanBeAccessed = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(ACCESS_INVENTORY_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, ACCESS_INVENTORY_BUTTON, (button, bool) -> {
                PreferencesScreen.this.inventoryCanBeAccessedVar = bool;
            }));
            inventoryCanBeAccessed.setValue(PreferencesScreen.this.inventoryCanBeAccessedVar);

            trapCarriedPlayer = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(TRAP_WHEN_CARRYING_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, TRAP_WHEN_CARRYING_BUTTON, (button, bool) -> {
                PreferencesScreen.this.trapCarriedPlayerVar = bool;
            }));
            trapCarriedPlayer.setValue(PreferencesScreen.this.trapCarriedPlayerVar);

            canBeTrappedWhileCarried = helper.addChild(CycleButton.onOffBuilder().withTooltip((bool) -> {
                return Tooltip.create(TRAP_WHEN_CARRIED_BUTTON_TOOLTIP);
            }).create(0, 0, 210, 20, TRAP_WHEN_CARRIED_BUTTON, (button, bool) -> {
                PreferencesScreen.this.canBeTrappedWhileCarriedVar = bool;
            }));
            canBeTrappedWhileCarried.setValue(PreferencesScreen.this.canBeTrappedWhileCarriedVar);
        }

        public void save(){
            saveToggles();
        }

        private void saveToggles(){
            PreferencesScreen.this.playerPreferences.setCanBePickedup(canBePickedupVar);
            PreferencesScreen.this.playerPreferences.setCanPickupOthers(canPickupOthersVar);
            PreferencesScreen.this.playerPreferences.setInventoryCanBeAccessed(inventoryCanBeAccessedVar);
            PreferencesScreen.this.playerPreferences.setTrapCarriedPlayer(trapCarriedPlayerVar);
            PreferencesScreen.this.playerPreferences.setCanBeTrappedWhileCarried(canBeTrappedWhileCarriedVar);
        }
    }
}
