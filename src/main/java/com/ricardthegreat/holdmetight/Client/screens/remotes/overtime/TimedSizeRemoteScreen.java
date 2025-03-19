package com.ricardthegreat.holdmetight.Client.screens.remotes.overtime;



import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.checkerframework.checker.units.qual.min;

import com.ricardthegreat.holdmetight.HoldMeTight;
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
public class TimedSizeRemoteScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    private static final Component MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.mult10_button");
    private static final Component DIVIDE_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.divide_button");

    private static final Component SECONDS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field");
    private static final Component MINUTES_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field");
    private static final Component HOURS_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field");

    private static final Component SECONDS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.seconds_field_tooltip");
    private static final Component MINUTES_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.minutes_field_tooltip");
    private static final Component HOURS_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.hours_field_tooltip");



    //these strings need to be translatable at some point
    //which will also involve figuring out spacing as they wont remain constant lengths if they are
    //simplest way will probably just be making it smaller? not sure yet
    private static final String TARGET = "Target:";
    private static final String CURRENT_SCALE = "Current Scale:";
    private static final String NOT_APPLICABLE = "N/A";
    private static final String OUT_OF_RANGE = "Out of range";
    private static final String NO_TARGET = "No Target";


    private static final ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");

    private static final float DEFAULT_SCALE = 1.0f;

    private final int imageWidth;
    private final int imageHeight;

    //the item user and the player the item has selected
    private Player user;
    private Player selectedPlayer;

    // the held item and its tags to perform stuff with
    private ItemStack stack;
    private CompoundTag tag;

    //positions on the gui
    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    //the buttons used
    private Button multButton;
    private Button divideButton;

    //the custom fields
    private EditBox secondsField;
    private EditBox minutesField;
    private EditBox hoursField;


    public TimedSizeRemoteScreen(Player user, InteractionHand hand){
        super(TITLE);
        this.imageWidth = 176;
        this.imageHeight = 256;
        this.user = user;

        stack = user.getItemInHand(hand);
        tag = stack.getTag();
        
    }

    // "this." is used alot im not sure why but im scared to change it
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
        
        if (tag.contains(OtherCustomSizeRemoteItem.TARGET_TAG) && !tag.getBoolean(OtherCustomSizeRemoteItem.TARGET_TAG)) {
            selectedPlayer = null;
        }else {
            selectedPlayer = level.getPlayerByUUID(tag.getUUID(AbstractSizeRemoteItem.UUID_TAG));
            if(selectedPlayer == null){
                selectedPlayer = user;
                tag.putUUID(AbstractSizeRemoteItem.UUID_TAG, selectedPlayer.getUUID());
                stack.setTag(tag);
            }
        }


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
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawString(this.font,TARGET, this.leftPos + 28, topPos +10,0xdddddd,false);
        graphics.drawString(this.font,CURRENT_SCALE, centerHorizonalPos + 5, topPos +10,0xdddddd,false);
        
        if (selectedPlayer != null) {
            if (inRange()) {
                graphics.drawCenteredString(font, Float.toString(SizeUtils.getSize(selectedPlayer)), (rightPos + centerHorizonalPos)/2, topPos +19, 0xdddddd);
                graphics.drawCenteredString(font, selectedPlayer.getName().getString(), (leftPos + centerHorizonalPos)/2, topPos +19, 0xdddddd);
            }else{
                graphics.drawCenteredString(font, NOT_APPLICABLE, (rightPos + centerHorizonalPos)/2, topPos +19, 0xffff00);
                graphics.drawCenteredString(font, OUT_OF_RANGE, (leftPos + centerHorizonalPos)/2, topPos +19, 0xffff00);
            }

            PlayerRenderExtension rend = (PlayerRenderExtension) selectedPlayer;

            if(rend != null){
                rend.setMenu(true);
                InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, centerHorizonalPos, centerVerticalPos, 30, (float)centerHorizonalPos - mouseX, (float)(centerVerticalPos - 80) -mouseY, (Player) rend);
                rend.setMenu(false);
            }
        }else{
            graphics.drawCenteredString(font, NOT_APPLICABLE, (rightPos + centerHorizonalPos)/2, topPos +19, 0xff0000);
            graphics.drawCenteredString(font, NO_TARGET, (leftPos + centerHorizonalPos)/2, topPos +19, 0xff0000);
        }
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
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(DEFAULT_SCALE, selectedPlayer.getUUID()));
            }
        }
    }

    // wthat to do when the mult button is clicked
    private void handleMultButton(Button button){

    }

    private void handleDivideButton(Button button){

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private boolean inRange(){
        double distance =  user.position().distanceTo(selectedPlayer.position());
        if (distance <= 100) {
            return true;
        }
        return false;
    }
}
