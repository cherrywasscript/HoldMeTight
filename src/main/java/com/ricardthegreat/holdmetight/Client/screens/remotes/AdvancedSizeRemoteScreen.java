package com.ricardthegreat.holdmetight.Client.screens.remotes;

import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.guielements.checkboxes.CustomCheckbox;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class AdvancedSizeRemoteScreen extends BasicSizeRemoteScreen{

    protected static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote_title");

    protected static final Component SET_MULT_CHOICE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.set_mult_choice_button");
    protected static final Component RANDOMISE_CHOICE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.randomise_choice_button");

    protected static final Component RANDOM_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.random_button");
    protected static final Component RANDOM_BUTTON_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.random_button_tooltip");

    protected static final Component MIN_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.min_scale_field");
    protected static final Component MAX_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.max_scale_field");
    protected static final Component SECONDS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field");
    protected static final Component MINUTES_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field");
    protected static final Component HOURS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field");

    protected static final Component MIN_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.min_scale_field_tooltip");
    protected static final Component MAX_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.max_scale_field_tooltip");
    protected static final Component SECONDS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field_tooltip");
    protected static final Component MINUTES_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field_tooltip");
    protected static final Component HOURS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field_tooltip");

    protected static final Component CUSTOM_DURATION = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.checkbox.custom_duration");
    

    protected static ResourceLocation POPOUT = new ResourceLocation(HoldMeTight.MODID, "textures/gui/master_size_remote_popout.png");


    protected Button setMultChoiceButton;
    protected Button randomiseChoiceButton;
    protected Button randomButton;

    protected CustomCheckbox customDuration;

    protected EditBox minScaleField;
    protected EditBox maxScaleField;
    protected EditBox secondsField;
    protected EditBox minutesField;
    protected EditBox hoursField;

    protected boolean popoutShown;

    public AdvancedSizeRemoteScreen(Player user, InteractionHand hand) {
        this(TITLE, user, hand, 176, 256);
    }

    public AdvancedSizeRemoteScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title, user, hand, width, height);
        
        BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
        range = 1000;
    }

    @Override
    protected void init() {
        super.init();

        //need this here bc sometimes it recalls init while the screen is still up
        popoutShown = false;

        initTimeFields();

        //customDuration = addRenderableWidget(new Checkbox(leftPos - 92, topPos, 20, 20, CUSTOM_DURATION, false));
        customDuration = addRenderableWidget(new CustomCheckbox(leftPos - 92, topPos, 20, 20, CUSTOM_DURATION, false));

        setMultChoiceButton.active = false;

        setMultChoiceButton.visible = false;
        randomiseChoiceButton.visible = false;
        customDuration.visible = false;


        randomButton.visible = false; 
        minScaleField.visible = false; 
        maxScaleField.visible = false; 
        secondsField.visible = false; 
        minutesField.visible = false; 
        hoursField.visible = false;

        if (tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG) != AbstractSizeRemoteItem.DEFAULT_TICKS) {
            customDuration.onPress();
            moveElements();
        }
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
        if (selectingPopoutEdge((int) mouseX, (int) mouseY)) {
            popoutShown = !popoutShown;
            setMultChoiceButton.visible = !setMultChoiceButton.visible;
            randomiseChoiceButton.visible = !randomiseChoiceButton.visible;
            customDuration.visible = !customDuration.visible;
            //play a clicking sound
            setMultChoiceButton.playDownSound(Minecraft.getInstance().getSoundManager());
        }
        saveEditBox();

        boolean changed = customDuration.selected();

        boolean clicked = super.mouseClicked(mouseX, mouseY, p_94697_);
        
        if (customDuration.selected() != changed) {
            moveElements();
        }

        return clicked;
    }

    protected boolean selectingPopoutEdge(int mouseX, int mouseY){
        int xRight = leftPos;
        int xLeft = leftPos-8;
        if (popoutShown) {
            xRight -= 93;
            xLeft -= 93;
        }
        if (mouseX < xRight && mouseX > xLeft && mouseY >= topPos + 13 && mouseY <= bottomPos - 13) {
            return true;
        }
        return false;
    }

    protected void renderPopout(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int popoutLeftPos = leftPos-100;
        int popoutWidth = 100;
        if (selectingPopoutEdge(mouseX, mouseY)) {
            popoutLeftPos += 3;
            popoutWidth -= 3;
        }
        graphics.blit(POPOUT, popoutLeftPos, topPos, 0, 0, popoutWidth, imageHeight);

        customDuration.setPosition(popoutLeftPos + 8, topPos + 20);
        setMultChoiceButton.setPosition(popoutLeftPos + 8, topPos + 60);
        randomiseChoiceButton.setPosition(popoutLeftPos + 8, topPos + 100);
    }

    protected void handleChoiceButton(Button button){
        setMultChoiceButton.active = !setMultChoiceButton.active;
        randomiseChoiceButton.active = !randomiseChoiceButton.active;

        setVisibility();
    }
    
    @Override
    protected void initButtons(){
        super.initButtons();

        setMultChoiceButton = addRenderableWidget(
            Button.builder(
                SET_MULT_CHOICE_BUTTON, this::handleChoiceButton)
                .bounds(leftPos - 92, topPos + 60, 88, 20)
                .tooltip(Tooltip.create(SET_MULT_CHOICE_BUTTON))
                .build()
        );

        randomiseChoiceButton = addRenderableWidget(
            Button.builder(
                RANDOMISE_CHOICE_BUTTON, this::handleChoiceButton)
                .bounds(leftPos - 92, topPos + 100, 88, 20)
                .tooltip(Tooltip.create(RANDOMISE_CHOICE_BUTTON))
                .build()
        );

        this.randomButton = addRenderableWidget(
            Button.builder(
                RANDOM_BUTTON, this::handleRandomButton)
                .bounds(this.leftPos + 8, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(RANDOM_BUTTON_TOOLTIP))
                .build()
        );
    }

    // wthat to do when the mult button is clicked
    @Override
    protected void handleMultButton(Button button) {
        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }

        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            if (inRange() && customDuration.selected()) {
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntityMultTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)));
            }else if (inRange()) {
                PacketHandler.sendToServer(new SEntityMultTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), AbstractSizeRemoteItem.DEFAULT_TICKS));
            }
        }
    }
    
    @Override
    protected void handleSetButton(Button button) {
        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }
        
        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            if (inRange() && customDuration.selected()) {
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)));
            }else if (inRange()) {
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), AbstractSizeRemoteItem.DEFAULT_TICKS));
            }
        }
    }

    protected void handleRandomButton(Button button) {

        String minString = minScaleField.getValue();
        String maxString = maxScaleField.getValue();

        Float minScale = tag.getFloat(AbstractSizeRemoteItem.MAX_SCALE_TAG); 
        Float maxScale = tag.getFloat(AbstractSizeRemoteItem.MIN_SCALE_TAG);  

        if (minString != null && !minString.isEmpty() && maxString != null && !maxString.isEmpty()){
            minScale = Float.parseFloat(minString); 
            maxScale = Float.parseFloat(maxString); 
            if (maxScale > AbstractSizeRemoteItem.RANDOM_MAX_LIMIT || maxScale < AbstractSizeRemoteItem.RANDOM_MIN_LIMIT) {
                maxScale = AbstractSizeRemoteItem.RANDOM_MAX_LIMIT;
                maxScaleField.setValue(Float.toString(maxScale));
            }
            if (minScale < AbstractSizeRemoteItem.RANDOM_MIN_LIMIT || minScale > AbstractSizeRemoteItem.RANDOM_MAX_LIMIT) {
                minScale = AbstractSizeRemoteItem.RANDOM_MIN_LIMIT;
                minScaleField.setValue(Float.toString(minScale));
            }
            if (minScale*2 >= maxScale) {
                minScale = maxScale/2;
                minScaleField.setValue(Float.toString(minScale));
            }
            
            tag.putFloat(AbstractSizeRemoteItem.MAX_SCALE_TAG, maxScale);
            tag.putFloat(AbstractSizeRemoteItem.MIN_SCALE_TAG, minScale);
            stack.setTag(tag);
        }

        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            Random rand = new Random();
            float randScale = rand.nextFloat(minScale, maxScale);
            if (inRange() && customDuration.selected()) {
                //send the random scale and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(randScale, selectedPlayer.getUUID(), tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)));
            }else if (inRange()) {
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(randScale, selectedPlayer.getUUID(), AbstractSizeRemoteItem.DEFAULT_TICKS));
            }
        }
    }

    // setup the text box that allows players to type in a height
    @Override
    protected void initEditBoxes() {
        super.initEditBoxes();
        minScaleField = addRenderableWidget(new EditBox(font, this.leftPos + 8, this.bottomPos - 80, 76, 20, MIN_SCALE_FIELD));
        maxScaleField = addRenderableWidget(new EditBox(font, this.leftPos + 91, this.bottomPos - 80, 76, 20, MAX_SCALE_FIELD));

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

        minScaleField.setFilter(filter);
        maxScaleField.setFilter(filter);

        Float min = tag.getFloat(AbstractSizeRemoteItem.MIN_SCALE_TAG);
        Float max = tag.getFloat(AbstractSizeRemoteItem.MAX_SCALE_TAG);

        String minFloatString = Float.toString(min);
        String maxFloatString = Float.toString(max);

        minScaleField.setValue(minFloatString);
        maxScaleField.setValue(maxFloatString);
        
        Tooltip minTooltip = Tooltip.create(MIN_SCALE_FIELD_TOOLTIP, MIN_SCALE_FIELD_TOOLTIP);
        Tooltip maxTooltip = Tooltip.create(MAX_SCALE_FIELD_TOOLTIP, MAX_SCALE_FIELD_TOOLTIP);

        minScaleField.setTooltip(minTooltip);
        maxScaleField.setTooltip(maxTooltip);
    }

    protected void initTimeFields() {
        hoursField = addRenderableWidget(new EditBox(font, this.leftPos + 8, this.bottomPos - 111, 44, 20, SECONDS_FIELD));
        minutesField = addRenderableWidget(new EditBox(font, this.leftPos + 64, this.bottomPos - 111, 44, 20, MINUTES_FIELD));
        secondsField = addRenderableWidget(new EditBox(font, this.leftPos + 120, this.bottomPos - 111, 44, 20, HOURS_FIELD));


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
                    Float f = Float.parseFloat(t);
                    if (f%1 == 0) {
                        return true;
                    }else if ((f*60)%1 == 0) {
                        return true;
                    }else if ((f*60*60)%1 == 0) {
                        return true;
                    }
                    return false;
                }catch (Exception e){ 
                    return false;
                }      
            }
        };

        secondsField.setFilter(filter);
        minutesField.setFilter(filter);
        hoursField.setFilter(filter);

        setTimes();
    }

    protected void setTimes(){
        int duration = tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)/20;

        int seconds = 0;
        int minutes = 0;
        int hours = 0;

        seconds += duration%60;
        duration -= seconds;

        minutes += duration%3600;
        duration -= minutes;
        minutes = minutes/60;

        hours = duration /3600;

        String secondsString = Integer.toString(seconds);
        String minutesString = Integer.toString(minutes);
        String hoursString = Integer.toString(hours);

        secondsField.setValue(secondsString);
        minutesField.setValue(minutesString);
        hoursField.setValue(hoursString);

        Tooltip secondsTooltip = Tooltip.create(SECONDS_FIELD_TOOLTIP, SECONDS_FIELD_TOOLTIP);
        Tooltip minutesTooltip = Tooltip.create(MINUTES_FIELD_TOOLTIP, MINUTES_FIELD_TOOLTIP);
        Tooltip hoursTooltip = Tooltip.create(HOURS_FIELD_TOOLTIP, HOURS_FIELD_TOOLTIP);

        secondsField.setTooltip(secondsTooltip);
        minutesField.setTooltip(minutesTooltip);
        hoursField.setTooltip(hoursTooltip);
    }

    protected void setVisibility(){
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
    }

    protected void moveElements(){
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
    }
    
    @Override
    protected void saveEditBox(){
        super.saveEditBox();
        System.out.println("advanced save box");

        String secondsString = secondsField.getValue();
        String minutesString = minutesField.getValue();
        String hoursString = hoursField.getValue();

        if (checkNotNull(secondsString, minutesString, hoursString)){
            Float seconds = Float.parseFloat(secondsString); 
            Float minutes = Float.parseFloat(minutesString);
            Float hours = Float.parseFloat(hoursString);
            hours *= 3600;
            minutes *= 60;
            float ticks = seconds + minutes + hours;
            ticks *= 20;

            tag.putFloat(AbstractSizeRemoteItem.NUM_TICKS_TAG, ticks);

            stack.setTag(tag);
        }
    }

    protected boolean checkNotNull(String secs, String mins, String hours){
        System.out.println("advanced check null");
        if (secs == null || secs.isEmpty()){
            return false;
        }
        if (mins == null || mins.isEmpty()){
            return false;
        }
        if (hours == null || hours.isEmpty()){
            return false;
        }
        return true;
    }
}
