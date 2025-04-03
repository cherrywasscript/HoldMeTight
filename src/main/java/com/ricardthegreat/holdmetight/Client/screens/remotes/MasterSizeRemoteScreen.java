package com.ricardthegreat.holdmetight.Client.screens.remotes;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SEntityAddTargetScalePacket;
import com.ricardthegreat.holdmetight.network.SEntityMultTargetScalePacket;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import com.ricardthegreat.holdmetight.utils.PlayerSizeExtension;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class MasterSizeRemoteScreen extends AdvancedSizeRemoteScreen{

    protected static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    protected static final Component SIZE_STEAL_CHOICE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.size_steal_choice_button");
    protected static final Component PERPETUAL_CHANGE_CHOICE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.perpetual_change_choice_button");

    protected static final Component STEAL_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.steal_button");
    protected static final Component GIVE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.give_button");

    protected static final Component TRANSFER_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.transfer_field");

    Button stealChoiceButton;
    Button perpetualChoiceButton;

    Button stealButton;
    Button giveButton;

    EditBox stealField;

    public MasterSizeRemoteScreen(Player user, InteractionHand hand) {
        this(TITLE, user, hand, 176, 256);
    }

    public MasterSizeRemoteScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title, user, hand, width, height);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 10000;
    }

    @Override
    protected void init() {
        super.init();

        stealChoiceButton.visible = false;
        perpetualChoiceButton.visible = false;

        stealButton.visible = false;
        giveButton.visible = false;
        stealField.visible = false;
    }

    //cant super call render as it ends up drawing things in the wrong order
    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        
        //draw the popout, might wanna move this into its own method?
        if (popoutShown) {
            renderPopout(graphics, mouseX, mouseY, partialTicks);
        }else if (selectingPopoutEdge(mouseX, mouseY)) {
            graphics.blit(POPOUT, leftPos-7, topPos, 0, 0, 7, imageHeight);
        }else {
            graphics.blit(POPOUT, leftPos-4, topPos, 0, 0, 4, imageHeight);
        }

        //draw the main screen image
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (customDuration.selected()) {
            graphics.drawString(this.font,"Hours", (leftPos + 30) - (font.width("Hours"))/2, bottomPos - 121,0x222222, false);
            graphics.drawString(this.font,"Minutes", (leftPos + 86) - (font.width("Minutes"))/2, bottomPos - 121,0x222222, false);
            graphics.drawString(this.font,"Seconds", (leftPos + 144) - (font.width("Seconds"))/2, bottomPos - 121,0x222222, false);
        }

        //the main display with the player and information about them
        renderPlayerDisplay(graphics, mouseX, mouseY, partialTicks);


        //have this because i cant just call screen render method as it draws in the wrong layer
        for(Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }

        //transparent black bar that emulates a small shadow over the popout
        graphics.fill(leftPos-2, topPos+14, leftPos, bottomPos-15, 0x88000000);
    }

    protected void renderPlayerDisplay(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int screenLeft = leftPos+8;
        int screenTop = topPos+8;
        int verTextSep = font.lineHeight + 5;

        graphics.drawString(font, TARGET, screenLeft + 2, screenTop + 2,0xdddddd,false);
        graphics.drawString(font, CURRENT_SCALE, screenLeft + 2, screenTop + verTextSep,0xdddddd,false);
        graphics.drawString(font, TARGET_SCALE, screenLeft + 2, screenTop + verTextSep*2,0xdddddd,false);
        graphics.drawString(font, SCALE_TIME, screenLeft + 2, screenTop + verTextSep*3,0xdddddd,false);
        
        if (selectedPlayer != null) {
            if (inRange()) {
                graphics.drawString(font, Float.toString(PlayerSizeUtils.getSize(selectedPlayer)), screenLeft + font.width(TARGET) + 2, screenTop + 2, 0xadd8e6,false);
                graphics.drawString(font, selectedPlayer.getName().getString(), screenLeft + font.width(CURRENT_SCALE) + 2, screenTop + verTextSep, 0xadd8e6,false);
            }else{
                graphics.drawString(font, NOT_APPLICABLE, screenLeft + font.width(TARGET) + 2, screenTop + 2, 0xffff00, false);
                graphics.drawString(font, OUT_OF_RANGE, screenLeft + font.width(CURRENT_SCALE) + 2, screenTop + verTextSep, 0xffff00, false);
            }

            PlayerRenderExtension rend = (PlayerRenderExtension) selectedPlayer;

            if(rend != null){
                rend.setMenu(true);
                InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, centerHorizonalPos, centerVerticalPos, 30, (float)centerHorizonalPos - mouseX, (float)(centerVerticalPos - 80) -mouseY, (Player) rend);
                rend.setMenu(false);
            }
        }else{
            graphics.drawString(font, NOT_APPLICABLE, screenLeft + font.width(TARGET) + 2, screenTop + 2, 0xff0000, false);
            graphics.drawString(font, NO_TARGET, screenLeft + font.width(CURRENT_SCALE) + 2, screenTop + verTextSep, 0xff0000, false);
            graphics.drawString(font, NOT_APPLICABLE, screenLeft + font.width(TARGET) + 2, screenTop + verTextSep*2, 0xff0000, false);
            graphics.drawString(font, NOT_APPLICABLE, screenLeft + font.width(TARGET) + 2, screenTop + verTextSep*3, 0xff0000, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        if (selectingPopoutEdge((int) mouseX, (int) mouseY)) {
            stealChoiceButton.visible = !stealChoiceButton.visible;
            perpetualChoiceButton.visible = !perpetualChoiceButton.visible;
        }

        return super.mouseClicked(mouseX, mouseY, p_94697_);
    }
    
    @Override
    protected void renderPopout(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        super.renderPopout(graphics, mouseX, mouseY, partialTicks);

        //extra button stuff goes here
    }

    //will need to update this as it is no longer just boolean
    @Override
    protected void handleChoiceButton(Button button){

        if (button == setMultChoiceButton) {
            setMultChoiceButton.active = false;
            randomiseChoiceButton.active = true;
            stealChoiceButton.active = true;
            perpetualChoiceButton.active = true;
        }else if (button == randomiseChoiceButton) {
            setMultChoiceButton.active = true;
            randomiseChoiceButton.active = false;
            stealChoiceButton.active = true;
            perpetualChoiceButton.active = true;
        }else if (button == stealChoiceButton) {
            setMultChoiceButton.active = true;
            randomiseChoiceButton.active = true;
            stealChoiceButton.active = false;
            perpetualChoiceButton.active = true;
        }else{
            setMultChoiceButton.active = true;
            randomiseChoiceButton.active = true;
            stealChoiceButton.active = true;
            perpetualChoiceButton.active = false;
        }

        setVisibility();
    }

    @Override
    protected void initButtons(){
        super.initButtons();

        stealChoiceButton = addRenderableWidget(
            Button.builder(
                SIZE_STEAL_CHOICE_BUTTON, this::handleChoiceButton)
                .bounds(leftPos - 92, topPos + 140, 88, 20)
                .tooltip(Tooltip.create(SIZE_STEAL_CHOICE_BUTTON))
                .build()
        );

        perpetualChoiceButton = addRenderableWidget(
            Button.builder(
                PERPETUAL_CHANGE_CHOICE_BUTTON, this::handleChoiceButton)
                .bounds(leftPos - 92, topPos + 180, 88, 20)
                .tooltip(Tooltip.create(PERPETUAL_CHANGE_CHOICE_BUTTON))
                .build()
        );

        stealButton = addRenderableWidget(
            Button.builder(
                STEAL_BUTTON, this::handleStealButton)
                .bounds(this.leftPos + 91, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(STEAL_BUTTON))
                .build()
        );

        giveButton = addRenderableWidget(
            Button.builder(
                GIVE_BUTTON, this::handleGiveButton)
                .bounds(this.leftPos + 8, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(GIVE_BUTTON))
                .build()
        );
    }

    protected void handleStealButton(Button button){

        String stealString = stealField.getValue();
        Float scale = 0f;

        if (stealString != null && !stealString.isEmpty()){
            scale = Float.parseFloat(stealString);  
            scale = -scale;
        }

        //this check shouldnt be needed but just in case
        if (selectedPlayer != null && selectedPlayer != user) {
            if (inRange()) {
                PlayerSizeExtension pext = (PlayerSizeExtension) selectedPlayer;

                float current = pext.getCurrentScale();
                float target = pext.getTargetScale();
                
                current += scale;
                target += scale;


                if (current < 0) {
                    scale = 0f;
                }
                if (target < 0) {
                    scale = 0f;
                }
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntityAddTargetScalePacket(scale, selectedPlayer.getUUID()));
            }
        }
    }

    protected void handleGiveButton(Button button){
        String stealString = stealField.getValue();
        Float scale = 0f;

        if (stealString != null && !stealString.isEmpty()){
            scale = Float.parseFloat(stealString);  
        }

        //this check shouldnt be needed but just in case
        if (selectedPlayer != null && selectedPlayer != user) {
            if (inRange()) {
                PlayerSizeExtension pext = (PlayerSizeExtension) selectedPlayer;

                float current = pext.getCurrentScale();
                float target = pext.getTargetScale();
                
                current += scale;
                target += scale;


                if (current < 0) {
                    scale = 0f;
                }
                if (target < 0) {
                    scale = 0f;
                }
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntityAddTargetScalePacket(scale, selectedPlayer.getUUID()));
            }
        }
    }

    // setup the text box that allows players to type in a height
    @Override
    protected void initEditBoxes() {
        super.initEditBoxes();
        
        stealField = addRenderableWidget(new EditBox(font, this.leftPos + 48, this.bottomPos - 80, 80, 20, TRANSFER_FIELD));

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

        stealField.setFilter(filter);

        stealField.setValue("0");
        
        Tooltip stealTooltip = Tooltip.create(TRANSFER_FIELD, TRANSFER_FIELD);

        stealField.setTooltip(stealTooltip);

    }

    @Override
    protected void setVisibility(){

        if (!setMultChoiceButton.active) {
            resetButton.visible = true;

            multButton.visible = true;
            setButton.visible = true;
            customScaleField.visible = true;

            randomButton.visible = false;
            minScaleField.visible = false;
            maxScaleField.visible = false;

            stealButton.visible = false;
            giveButton.visible = false;
            stealField.visible = false;
        }else if (!randomiseChoiceButton.active) {
            resetButton.visible = true;

            multButton.visible = false;
            setButton.visible = false;
            customScaleField.visible = false;

            randomButton.visible = true;
            minScaleField.visible = true;
            maxScaleField.visible = true;

            stealButton.visible = false;
            giveButton.visible = false;
            stealField.visible = false;
        }else if (!stealChoiceButton.active) {
            resetButton.visible = false;

            multButton.visible = false;
            setButton.visible = false;
            customScaleField.visible = false;

            randomButton.visible = false;
            minScaleField.visible = false;
            maxScaleField.visible = false;

            stealButton.visible = true;
            giveButton.visible = true;
            stealField.visible = true;
        }else{
            resetButton.visible = false;

            multButton.visible = false;
            setButton.visible = false;
            customScaleField.visible = false;

            randomButton.visible = false;
            minScaleField.visible = false;
            maxScaleField.visible = false;
            
            stealButton.visible = false;
            giveButton.visible = false;
            stealField.visible = false;
        }

        if (multButton.visible) {
            resetButton.setPosition(this.centerHorizonalPos - 38, this.bottomPos -40);
        }else{
            resetButton.setPosition(this.leftPos + 91, this.bottomPos -40);
        }
    }

    @Override
    protected void moveElements(){
        //maybe super call
        secondsField.visible = !secondsField.visible;
        minutesField.visible = !minutesField.visible;
        hoursField.visible = !hoursField.visible;

        if (customDuration.selected()) {
            multButton.setPosition(leftPos + 8, bottomPos - 86);
            setButton.setPosition(leftPos + 91, bottomPos - 86);
            customScaleField.setPosition(leftPos + 48, bottomPos - 64);
        }else{
            multButton.setPosition(leftPos + 8, bottomPos - 111);
            setButton.setPosition(leftPos + 91, bottomPos - 111);
            customScaleField.setPosition(leftPos + 48, bottomPos - 80);
        }

        if (multButton.visible) {
            resetButton.setPosition(this.centerHorizonalPos - 38, this.bottomPos -40);
        }

        //extra stuff
    }

    @Override
    protected void saveEditBox(){
        super.saveEditBox();
        
        //extra stuff
    }
}
