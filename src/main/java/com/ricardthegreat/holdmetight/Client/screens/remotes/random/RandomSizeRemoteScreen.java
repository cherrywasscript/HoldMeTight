package com.ricardthegreat.holdmetight.Client.screens.remotes.random;



import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

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
public class RandomSizeRemoteScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote");

    private static final Component RANDOM_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.random_button");
    private static final Component RESET_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.reset_button");

    private static final Component MIN_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.min_scale_field");
    private static final Component MAX_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.max_scale_field");
    private static final Component MIN_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.min_scale_field_tooltip");
    private static final Component MAX_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.max_scale_field_tooltip");



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
    private Button randomButton;
    private Button resetButton;

    //the custom fields
    private EditBox minScaleField;
    private EditBox maxScaleField;

    public RandomSizeRemoteScreen(Player user, InteractionHand hand){
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

        
        


        this.randomButton = addRenderableWidget(
            Button.builder(
                RANDOM_BUTTON, this::handlerandomButton)
                .bounds(this.leftPos + 8, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(RANDOM_BUTTON))
                .build()
        );

        this.resetButton = addRenderableWidget(
            Button.builder(
                RESET_BUTTON, this::handleResetButton)
                .bounds(this.leftPos + 91, this.bottomPos -40, 76, 20)
                .tooltip(Tooltip.create(RESET_BUTTON))
                .build()
        );

        if (selectedPlayer == null) {
            randomButton.active = false;
            resetButton.active = false;
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
            String minScaleString = minScaleField.getValue();
            String maxScaleString = maxScaleField.getValue();

            if (minScaleString != null && !minScaleString.isEmpty()){
                Float scale = Float.parseFloat(minScaleString);  
                tag.putFloat(AbstractSizeRemoteItem.MIN_SCALE_TAG, scale);
            }
            
            if (maxScaleString != null && !maxScaleString.isEmpty()){
                Float scale = Float.parseFloat(maxScaleString);  
                tag.putFloat(AbstractSizeRemoteItem.MAX_SCALE_TAG, scale);
            }
            stack.setTag(tag);
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }


    // setup the text box that allows players to type in a height
    private void initScaleFields() {
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
    private void handlerandomButton(Button button) {

        String minString = minScaleField.getValue();
        String maxString = maxScaleField.getValue();

        Float minScale = tag.getFloat(AbstractSizeRemoteItem.MAX_SCALE_TAG); 
        Float maxScale = tag.getFloat(AbstractSizeRemoteItem.MIN_SCALE_TAG);  

        if (minString != null && !minString.isEmpty() && maxString != null && !maxString.isEmpty()){
            minScale = Float.parseFloat(minString); 
            maxScale = Float.parseFloat(maxString); 
            if (maxScale > RandomSizeRemoteItem.RANDOM_MAX_LIMIT || maxScale < RandomSizeRemoteItem.RANDOM_MIN_LIMIT) {
                maxScale = RandomSizeRemoteItem.RANDOM_MAX_LIMIT;
                maxScaleField.setValue(Float.toString(maxScale));
            }
            if (minScale < RandomSizeRemoteItem.RANDOM_MIN_LIMIT || minScale > RandomSizeRemoteItem.RANDOM_MAX_LIMIT) {
                minScale = RandomSizeRemoteItem.RANDOM_MIN_LIMIT;
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
            if (inRange()) {
                Random rand = new Random();
                float randScale = rand.nextFloat(minScale, maxScale);
                //send the random scale and playeruuid to the server packet handler
                PacketHandler.sendToServer(new SEntitySetTargetScalePacket(randScale, selectedPlayer.getUUID()));
            }
        }
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
