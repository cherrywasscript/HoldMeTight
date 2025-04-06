package com.ricardthegreat.holdmetight.Client.screens.remotes;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;



public abstract class AbstractSizeRemoteScreen extends Screen{

    protected static final float DEFAULT_SCALE = 1.0f;

    //these are translateable tho im still not certain on spacing, it should function but depending on stuff it might not fit properly
    protected static final String TARGET = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.target_string").getString() + ":";
    protected static final String CURRENT_SCALE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.current_scale_string").getString() + ":";
    protected static final String TARGET_SCALE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.target_scale_string").getString() + ":";
    protected static final String SCALE_TIME = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.scale_time_string").getString() + ":";
    protected static final String NOT_APPLICABLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.not_applicable_string").getString() + ":";
    protected static final String OUT_OF_RANGE = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.out_of_range_string").getString() + ":";
    protected static final String NO_TARGET = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.strings.no_target_string").getString() + ":";
    

    protected final int imageWidth;
    protected final int imageHeight;

    //the item user and the player the item has selected
    protected Player user;
    protected Player selectedPlayer;

    // the held item and its tags to perform stuff with
    protected ItemStack stack;
    protected CompoundTag tag;

    //the range of the device
    protected int range = 100;

    //the image background should be initialised in constructors when this file is extended
    protected ResourceLocation BACKGROUND;

    //positions on the image background
    protected int leftPos;
    protected int rightPos;
    protected int topPos;
    protected int bottomPos;
    protected int centerHorizonalPos;
    protected int centerVerticalPos;

    protected AbstractSizeRemoteScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title);

        this.imageWidth = width;
        this.imageHeight = height;

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
        
        if (tag.contains(AbstractSizeRemoteItem.TARGET_TAG) && !tag.getBoolean(AbstractSizeRemoteItem.TARGET_TAG)) {
            selectedPlayer = null;
        }else {
            selectedPlayer = level.getPlayerByUUID(tag.getUUID(AbstractSizeRemoteItem.UUID_TAG));
            if(selectedPlayer == null){
                selectedPlayer = user;
                tag.putUUID(AbstractSizeRemoteItem.UUID_TAG, selectedPlayer.getUUID());
                stack.setTag(tag);
            }
        }
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        renderPlayerDisplay(graphics, mouseX, mouseY, partialTicks);
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
                graphics.drawString(font, selectedPlayer.getName().getString(), screenLeft + font.width(TARGET) + 5, screenTop + 2, 0xadd8e6,false);
                graphics.drawString(font, Float.toString(PlayerSizeUtils.getSize(selectedPlayer)), screenLeft + font.width(CURRENT_SCALE) + 5, screenTop + verTextSep, 0xadd8e6,false);
                graphics.drawString(font, Float.toString(PlayerSizeUtils.getTargetSize(selectedPlayer)), screenLeft + font.width(TARGET_SCALE) + 5, screenTop + verTextSep*2, 0xadd8e6,false);
                graphics.drawString(font, ticksToTime(PlayerSizeUtils.getRemainingTicks(selectedPlayer)), screenLeft + font.width(SCALE_TIME) + 5, screenTop + verTextSep*3, 0xadd8e6,false);
            }else{
                graphics.drawString(font, OUT_OF_RANGE, screenLeft + font.width(TARGET) + 2, screenTop + 2, 0xffff00, false);
                graphics.drawString(font, NOT_APPLICABLE, screenLeft + font.width(CURRENT_SCALE) + 2, screenTop + verTextSep, 0xffff00, false);
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

    protected String ticksToTime(int ticks){

        int duration = (int) Math.ceil(((double) ticks)/20);

        int seconds = 0;
        int minutes = 0;
        int hours = 0;

        seconds += duration%60;
        duration -= seconds;

        minutes += duration%3600;
        duration -= minutes;
        minutes = minutes/60;

        hours = duration/3600;

        String output = "";

        if (hours < 10) {
            output = output + "0" + hours + ":";
        }else{
            output = output + hours + ":";
        }

        if (minutes < 10) {
            output = output + "0" + minutes + ":";
        }else{
            output = output + minutes + ":";
        }

        if (seconds < 10) {
            output = output + "0" + seconds;
        }else{
            output = output + seconds;
        }

        return output;
    }

    protected boolean inRange(){
        double distance =  user.position().distanceTo(selectedPlayer.position());
        if (distance <= range) {
            return true;
        }
        return false;
    }

    protected abstract void saveEditBox();

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
