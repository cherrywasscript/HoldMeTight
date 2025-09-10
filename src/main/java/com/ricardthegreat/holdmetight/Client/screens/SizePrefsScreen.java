package com.ricardthegreat.holdmetight.Client.screens;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

//TODO change background
public class SizePrefsScreen extends Screen{

    private static final Component TITLE = Component.literal("Size Prefs Screen");

    protected static final Component MAX_BUTTON = Component.literal("set max");
    protected static final Component MIN_BUTTON = Component.literal("set min");
    protected static final Component DEFAULT_BUTTON = Component.literal("set default");

    protected static final Component MAX_BUTTON_TOOLTIP = Component.literal("set your maximum scale");
    protected static final Component MIN_BUTTON_TOOLTIP = Component.literal("set minimum scale");
    protected static final Component DEFAULT_BUTTON_TOOLTIP = Component.literal("set default scale");

    protected static final Component MAX_SCALE_FIELD = Component.literal("max scale");
    protected static final Component MIN_SCALE_FIELD = Component.literal("min scale");
    protected static final Component DEFAULT_SCALE_FIELD = Component.literal("default scale");

    protected static final Component MAX_SCALE_FIELD_TOOLTIP = Component.literal("input max scale");
    protected static final Component MIN_SCALE_FIELD_TOOLTIP = Component.literal("input min scale");
    protected static final Component DEFAULT_SCALE_FIELD_TOOLTIP = Component.literal("input default scale");

    private final int imageWidth;
    private final int imageHeight;


    //the image background not final
    private ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");

    private Player player;
    private PlayerSize playerSize;

    //positions on the image background
    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;


    protected Button maxButton;
    protected Button minButton;
    protected Button defaultButton;

    protected EditBox maxScaleField;
    protected EditBox minScaleField;
    protected EditBox defaultScaleField;


    public SizePrefsScreen(Player player) {
        super(TITLE);
        
        this.imageWidth = 256;
        this.imageHeight = 256;

        this.player = player;
        this.playerSize = PlayerSizeProvider.getPlayerSizeCapability(player);
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

        graphics.drawString(this.font, String.valueOf(playerSize.getCurrentScale()), leftPos + 8 + font.width("Current scale: "), topPos +18,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerSize.getMaxScale()), leftPos + 8 + font.width("Max scale: "), topPos +30,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerSize.getMinScale()), leftPos + 8 + font.width("Min scale: "), topPos +40,0xdddddd,false);
        graphics.drawString(this.font, String.valueOf(playerSize.getDefaultScale()), leftPos + 8 + font.width("Default scale: "), topPos +50,0xdddddd,false);

        graphics.drawString(this.font, "Current scale:", leftPos + 8, topPos +18,0xdddddd,false);
        graphics.drawString(this.font, "Max scale:", leftPos + 8, topPos +30,0xdddddd,false);
        graphics.drawString(this.font, "Min scale:", leftPos + 8, topPos +40,0xdddddd,false);
        graphics.drawString(this.font, "Default scale:", leftPos + 8, topPos +50,0xdddddd,false);
    }

    private void saveEditBox(){

    }

    protected void initButtons(){
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
    }

    protected void handleMaxButton(Button button){
        String scaleString = maxScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerSize.setMaxScale(Float.parseFloat(scaleString));
            playerSize.updateShouldSync();
        }
    }
    protected void handleMinButton(Button button){
        String scaleString = minScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerSize.setMinScale(Float.parseFloat(scaleString));
            playerSize.updateShouldSync();
        }
    }
    protected void handleDefaultButton(Button button){
        String scaleString = defaultScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            playerSize.setDefaultScale(Float.parseFloat(scaleString));
            playerSize.updateShouldSync();
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

        maxScaleField.setValue(Float.toString(playerSize.getMaxScale()));
        minScaleField.setValue(Float.toString(playerSize.getMinScale()));
        defaultScaleField.setValue(Float.toString(playerSize.getDefaultScale()));

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
    
}
