package com.ricardthegreat.holdmetight.Client.screens.remotes;

import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.checkerframework.checker.units.qual.min;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SEntityMultTargetScalePacket;
import com.ricardthegreat.holdmetight.network.SEntitySetTargetScalePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class BasicSizeRemoteScreen extends AbstractSizeRemoteScreen{

    protected static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    protected static final Component MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.mult_button");
    protected static final Component SET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.set_button");
    protected static final Component RESET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.reset_button");

    protected static final Component CUSTOM_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field");

    protected static final Component CUSTOM_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field_tooltip");

    protected Button multButton;
    protected Button setButton;
    protected Button resetButton;

    protected EditBox customScaleField;

    public BasicSizeRemoteScreen(Player user, InteractionHand hand) {
        super(TITLE, user, hand, 176, 256);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 100;
    }

    public BasicSizeRemoteScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title, user, hand, width, height);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 100;
    }

    @Override
    protected void init() {
        super.init();

        initButtons();

        initEditBoxes();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);

        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        renderPlayerDisplay(graphics, mouseX, mouseY, partialTicks);

        //have this because i cant just call screen render method
        for(Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        saveEditBox();
        return super.mouseClicked(mouseX, mouseY, p_94697_);
    }

    protected void initButtons(){
        this.multButton = addRenderableWidget(
            Button.builder(
                MULT_BUTTON, this::handleMultButton)
                .bounds(this.leftPos + 8, this.bottomPos -111, 76, 20)
                .tooltip(Tooltip.create(MULT_BUTTON))
                .build()
        );

        this.setButton = addRenderableWidget(
            Button.builder(
                SET_BUTTON, this::handleSetButton)
                .bounds(this.leftPos + 91, this.bottomPos -111, 76, 20)
                .tooltip(Tooltip.create(SET_BUTTON))
                .build()
        );

        this.resetButton = addRenderableWidget(
            Button.builder(
                RESET_BUTTON, this::handleResetButton)
                .bounds(this.centerHorizonalPos - 38, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(RESET_BUTTON))
                .build()
        );
    }

    // what to do when the reset button is clicked
    protected void handleResetButton(Button button) {
        //this check shouldnt be needed but just in case 
        if (selectedPlayer != null) {
            if (inRange()) {
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(DEFAULT_SCALE, selectedPlayer.getUUID(), 0));
            }
        }
    }

    // wthat to do when the mult button is clicked
    protected void handleMultButton(Button button) {

        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }

        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            if (inRange()) {
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntityMultTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), AbstractSizeRemoteItem.DEFAULT_TICKS));
            }
        }
    }
    
    protected void handleSetButton(Button button) {

        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }
        
        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            if (inRange()) {
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), AbstractSizeRemoteItem.DEFAULT_TICKS));
            }
        }
    }

    // setup the text box that allows players to type in a height
    protected void initEditBoxes() {
        customScaleField = addRenderableWidget(new EditBox(font, this.leftPos + 48, this.bottomPos - 80, 80, 20, CUSTOM_SCALE_FIELD));

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
                    Float.parseFloat(t);
                    return true;
                }catch (Exception e){ 
                    return false;
                }      
            }
        };

        customScaleField.setFilter(filter);

        Float mul = tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG);

        String floatString = Float.toString(mul);

        customScaleField.setValue(floatString);
        
        Tooltip t = Tooltip.create(CUSTOM_SCALE_FIELD_TOOLTIP, CUSTOM_SCALE_FIELD_TOOLTIP);

        customScaleField.setTooltip(t);
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if (key == 257) {
            saveEditBox();
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }

    @Override
    protected void saveEditBox(){
        String customScale = customScaleField.getValue();

        if (checkNotNull(customScale)){
            Float scale = Float.parseFloat(customScale); 
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }
    }

    protected boolean checkNotNull(String scale){
        System.out.println("basic check null");
        if (scale == null || scale.isEmpty()){
            return false;
        }
        return true;
    }
}
