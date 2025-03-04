package com.ricardthegreat.holdmetight.Client.screens;



import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.AdvancedSizeRemote;
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
public class AdvancedSizeRemoteScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    private static final Component MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.mult_button");

    private static final Component SET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.set_button");

    private static final Component RESET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.reset_button");

    private static final Component CUSTOM_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field");

    private static final Component CUSTOM_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_scale_field_tooltip");


    private static final ResourceLocation TEXTURE = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");

    private static final float DEFAULT_SCALE = 1.0f;

    private final int imageWidth;
    private final int imageHeight;

    //the item user and the player the item has selected
    private Player user;
    
    private Player selectedPlayer;

    // the held item and its tags to perform stuff with
    private ItemStack stack;
    private CompoundTag tag;

    //private SizeItem item;

    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    private Button multButton;
    private Button setButton;
    private Button resetButton;

    private EditBox customScaleField;

    public AdvancedSizeRemoteScreen(Player user, InteractionHand hand){
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

        //System.out.println("SizeItemScreen line 124: " + SizeUtils.getHitboxScaleData(selectedPlayer).getScale());

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

        selectedPlayer = level.getPlayerByUUID(tag.getUUID(AdvancedSizeRemote.UUID_TAG));
        if(selectedPlayer == null){
            selectedPlayer = user;
            tag.putUUID(AdvancedSizeRemote.UUID_TAG, selectedPlayer.getUUID());
            stack.setTag(tag);
        }
        


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
        
        initCustomScaleField();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawString(this.font,"Target:", this.leftPos + 28, topPos +10,0xdddddd,false);

        graphics.drawString(this.font,"Current Scale:", centerHorizonalPos + 5, topPos +10,0xdddddd,false);
        
        
        graphics.drawCenteredString(font, Float.toString(SizeUtils.getSize(selectedPlayer)), (rightPos + centerHorizonalPos)/2, topPos +19, 0xdddddd);
        graphics.drawCenteredString(font, selectedPlayer.getName().getString(), (leftPos + centerHorizonalPos)/2, topPos +19, 0xdddddd);

        //graphics.fill(leftPos + 8, topPos + 8, rightPos-8, bottomPos-120, 0xFF373737);
        //graphics.fill(leftPos + 9, topPos + 9, rightPos-9, bottomPos-121, 0xFF000000);
        //graphics.hLine(leftPos + 8, rightPos-9, topPos + 8, 0xFFFF0000);

        PlayerRenderExtension rend = (PlayerRenderExtension) selectedPlayer;

        if(rend != null){
            rend.setMenu(true);
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, centerHorizonalPos, centerVerticalPos, 30, (float)centerHorizonalPos - mouseX, (float)(centerVerticalPos - 80) -mouseY, (Player) rend);
            rend.setMenu(false);
        }

        
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if (key == 257) {
            String scaleString = customScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                Float scale = Float.parseFloat(scaleString);  
                tag.putFloat(AdvancedSizeRemote.SCALE_TAG, scale);
                stack.setTag(tag);
                //item.setScaleFactor(scale);
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

        // get mult from tag, kept in comment
        //Tag tagMul = tag.get("multiplier");



        Float mul = tag.getFloat(AdvancedSizeRemote.SCALE_TAG);

        String floatString = Float.toString(mul);
        //String floatStringNoEndingF = floatString.substring(0, floatString.length()-1);

        customScaleField.setValue(floatString);
        
        Tooltip t = Tooltip.create(CUSTOM_SCALE_FIELD_TOOLTIP, CUSTOM_SCALE_FIELD_TOOLTIP);
        customScaleField.setTooltip(t);

    }

    // what to do when the reset button is clicked
    private void handleResetButton(Button button) { 
        //SizeUtils.setTargetSize(selectedPlayer, DEFAULT_SCALE);

        //send the multiplier and playeruuid to the server packet handler
        PacketHandler.sendToServer(new SEntitySetTargetScalePacket(DEFAULT_SCALE, selectedPlayer.getUUID()));
    }

    // wthat to do when the scale button is clicked
    private void handleMultButton(Button button) {

        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AdvancedSizeRemote.SCALE_TAG, scale);
            stack.setTag(tag);
            //item.setScaleFactor(scale);
        }

        //SizeUtils.multTargetSize(selectedPlayer, item.getScaleFactor());

        //send the multiplier and playeruuid to the server packet handler
        PacketHandler.sendToServer(new SEntityMultTargetScalePacket(tag.getFloat(AdvancedSizeRemote.SCALE_TAG), selectedPlayer.getUUID(), 1));

    }
    
    private void handleSetButton(Button button) {

        String scaleString = customScaleField.getValue();

        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putFloat(AdvancedSizeRemote.SCALE_TAG, scale);
            stack.setTag(tag);
            //item.setScaleFactor(scale);
        }

        //SizeUtils.setTargetSize(selectedPlayer, item.getScaleFactor());

        PacketHandler.sendToServer(new SEntitySetTargetScalePacket(tag.getFloat(AdvancedSizeRemote.SCALE_TAG), selectedPlayer.getUUID()));
    }
}
