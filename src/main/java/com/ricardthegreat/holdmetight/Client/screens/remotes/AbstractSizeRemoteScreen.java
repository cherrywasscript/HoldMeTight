package com.ricardthegreat.holdmetight.Client.screens.remotes;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
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

    //these strings need to be translatable at some point
    //which will also involve figuring out spacing as they wont remain constant lengths if they are
    //simplest way will probably just be making it smaller? not sure yet
    private static final String TARGET = "Target:";
    private static final String CURRENT_SCALE = "Current Scale:";
    private static final String NOT_APPLICABLE = "N/A";
    private static final String OUT_OF_RANGE = "Out of range";
    private static final String NO_TARGET = "No Target";
    

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
        graphics.drawString(this.font,TARGET, this.leftPos + 28, topPos +10,0xdddddd,false);
        graphics.drawString(this.font,CURRENT_SCALE, centerHorizonalPos + 5, topPos +10,0xdddddd,false);
        
        if (selectedPlayer != null) {
            if (inRange()) {
                graphics.drawCenteredString(font, Float.toString(EntitySizeUtils.getSize(selectedPlayer)), (rightPos + centerHorizonalPos)/2, topPos +19, 0xdddddd);
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
