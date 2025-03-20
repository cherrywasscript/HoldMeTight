package com.ricardthegreat.holdmetight.Client.screens.remotes.overtime;



import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.checkerframework.checker.units.qual.min;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.screens.remotes.AbstractSizeRemoteScreen;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.random.RandomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.setmult.OtherCustomSizeRemoteItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SEntityMultTargetScalePacket;
import com.ricardthegreat.holdmetight.network.SEntitySetTargetScalePacket;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// this file honestly does far more than it probably should but yeah
// works for the 3 remotes in the "setmult" category, honestly the only reason it does is because i was too lazy to make 2 more files like this
public class TimedSizeRemoteScreen extends AbstractSizeRemoteScreen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    private static final Component MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.mult10_button");
    private static final Component DIVIDE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.divide_button");

    private static final Component SECONDS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field");
    private static final Component MINUTES_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field");
    private static final Component HOURS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field");

    private static final Component SECONDS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field_tooltip");
    private static final Component MINUTES_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field_tooltip");
    private static final Component HOURS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field_tooltip");

    //the buttons used
    private Button multButton;
    private Button divideButton;

    //the custom fields
    private EditBox secondsField;
    private EditBox minutesField;
    private EditBox hoursField;


    public TimedSizeRemoteScreen(Player user, InteractionHand hand){
        super(TITLE, user, hand, 176, 256);
        
        this.BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
    }

    // "this." is used alot im not sure why but im scared to change it
    @Override
    protected void init() {
        super.init();

        this.multButton = addRenderableWidget(
            Button.builder(
                MULT_BUTTON, this::handleMultButton)
                .bounds(this.leftPos + 8, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(MULT_BUTTON))
                .build()
        );

        this.divideButton = addRenderableWidget(
            Button.builder(
                DIVIDE_BUTTON, this::handleDivideButton)
                .bounds(this.leftPos + 91, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(DIVIDE_BUTTON))
                .build()
        );

        if (selectedPlayer == null) {
            multButton.active = false;
            divideButton.active = false;
        }
        
        initScaleFields();
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if (key == 257) {
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
        return super.keyPressed(key, p_96553_, p_96554_);
    }

    private boolean checkNotNull(String secs, String mins, String hours){
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


    // setup the text box that allows players to type in a height
    private void initScaleFields() {
        hoursField = addRenderableWidget(new EditBox(font, this.leftPos + 8, this.bottomPos - 80, 49, 20, SECONDS_FIELD));
        minutesField = addRenderableWidget(new EditBox(font, this.leftPos + 64, this.bottomPos - 80, 49, 20, MINUTES_FIELD));
        secondsField = addRenderableWidget(new EditBox(font, this.leftPos + 120, this.bottomPos - 80, 49, 20, HOURS_FIELD));


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

    private void setTimes(){
        int duration = tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)/20;

        int seconds = 0;
        int minutes = 0;
        int hours = 0;

        seconds += duration%60;
        duration -= seconds;

        minutes += duration%3600;
        duration -= minutes;

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

    // what to do when the reset button is clicked
    private void handleResetButton(Button button) {
        //this check shouldnt be needed but just in case 
        if (selectedPlayer != null) {
            if (inRange()) {
                //send the multiplier and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(DEFAULT_SCALE, selectedPlayer.getUUID(), 0));
            }
        }
    }

    // wthat to do when the mult button is clicked
    private void handleMultButton(Button button){

    }

    private void handleDivideButton(Button button){

    }
}
