package com.ricardthegreat.holdmetight.Client.screens.remotes.setmult;



import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.screens.remotes.AbstractSizeRemoteScreen;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
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
public class CustomSizeRemoteScreen extends AbstractSizeRemoteScreen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    private static final Component MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.mult_button");
    private static final Component SET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.set_button");
    private static final Component RESET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.reset_button");

    private static final Component CUSTOM_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field");
    private static final Component CUSTOM_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field_tooltip");
    


    //the buttons
    private Button multButton;
    private Button setButton;
    private Button resetButton;

    //edit box
    private EditBox customScaleField;

    public CustomSizeRemoteScreen(Player user, InteractionHand hand){
        super(TITLE, user, hand, 176, 256);
        
        this.BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");
    }

    @Override
    protected void init() {
        super.init();

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

        if (selectedPlayer == null) {
            multButton.active = false;
            setButton.active = false;
            resetButton.active = false;
        }
        
        initCustomScaleField();
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if (key == 257) {
            String scaleString = customScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                Float scale = Float.parseFloat(scaleString);  
                tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
                stack.setTag(tag);
            }
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }


    // setup the text box that allows players to type in a height
    private void initCustomScaleField() {
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
    private void handleMultButton(Button button) {

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
                PacketHandler.sendToServer(new SEntityMultTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)));
            }
        }
    }
    
    private void handleSetButton(Button button) {

        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AbstractSizeRemoteItem.SCALE_TAG, scale);
            stack.setTag(tag);
        }
        
        //this check shouldnt be needed but just in case
        if (selectedPlayer != null) {
            if (inRange()) {
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(tag.getFloat(AbstractSizeRemoteItem.SCALE_TAG), selectedPlayer.getUUID(), tag.getInt(AbstractSizeRemoteItem.NUM_TICKS_TAG)));
            }
        }
    }
}
