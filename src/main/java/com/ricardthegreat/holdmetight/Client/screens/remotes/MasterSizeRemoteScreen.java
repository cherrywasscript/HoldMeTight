package com.ricardthegreat.holdmetight.Client.screens.remotes;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class MasterSizeRemoteScreen extends AdvancedSizeRemoteScreen{

    protected static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    public MasterSizeRemoteScreen(Player user, InteractionHand hand) {
        super(TITLE, user, hand, 176, 256);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 10000;
    }

    public MasterSizeRemoteScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title, user, hand, width, height);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 10000;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        
        if (popoutShown) {
            renderPopout(graphics, mouseX, mouseY, partialTicks);
        }else if (selectingPopoutEdge(mouseX, mouseY)) {
            graphics.blit(POPOUT, leftPos-7, topPos, 0, 0, 7, imageHeight);
        }else {
            graphics.blit(POPOUT, leftPos-4, topPos, 0, 0, 4, imageHeight);
        }

        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        if (customDuration.selected()) {
            graphics.drawString(this.font,"Hours", (leftPos + 30) - (font.width("Hours"))/2, bottomPos - 121,0x222222, false);
            graphics.drawString(this.font,"Minutes", (leftPos + 86) - (font.width("Minutes"))/2, bottomPos - 121,0x222222, false);
            graphics.drawString(this.font,"Seconds", (leftPos + 144) - (font.width("Seconds"))/2, bottomPos - 121,0x222222, false);
        }

        renderPlayerDisplay(graphics, mouseX, mouseY, partialTicks);
        //have this because i cant just call screen render method
        for(Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }

        //transparent black bar that emulates a small shadow over the popout
        graphics.fill(leftPos-2, topPos+14, leftPos, bottomPos-15, 0x88000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int p_94697_) {
        //extra on click stuff goes here
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

        System.out.println(button == setMultChoiceButton);

        setMultChoiceButton.active = !setMultChoiceButton.active;
        randomiseChoiceButton.active = !randomiseChoiceButton.active;

        setVisibility();
    }

    @Override
    protected void initButtons(){
        super.initButtons();

        //add extra buttons here
    }

    // setup the text box that allows players to type in a height
    @Override
    protected void initEditBoxes() {
        super.initEditBoxes();
        
        //add extra edit boxes here
    }

    @Override
    protected void setVisibility(){
        //possibly could just use super call here but i need to test
        multButton.visible = !multButton.visible;
        setButton.visible = !setButton.visible;
        randomButton.visible = !randomButton.visible;
        customScaleField.visible = !customScaleField.visible;
        minScaleField.visible = !minScaleField.visible;
        maxScaleField.visible = !maxScaleField.visible;

        if (multButton.visible) {
            resetButton.setPosition(this.centerHorizonalPos - 38, this.bottomPos -40);
        }else{
            resetButton.setPosition(this.leftPos + 91, this.bottomPos -40);
        }

        //blabla extra stuff here etc
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
