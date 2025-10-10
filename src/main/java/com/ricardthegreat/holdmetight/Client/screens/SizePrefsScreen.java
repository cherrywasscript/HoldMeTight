package com.ricardthegreat.holdmetight.client.screens;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
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

public class SizePrefsScreen extends Screen{

    private static final Component TITLE = Component.literal("Size Prefs Screen");


    private final int imageWidth;
    private final int imageHeight;


    //the image background not final
    private ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_remote_bg.png");

    private Player player;

    //positions on the image background
    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;


    public SizePrefsScreen(Player player) {
        super(TITLE);
        
        this.imageWidth = 176;
        this.imageHeight = 256;

        this.player = player;
    }

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
        
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);


        drawSelectionHemisphere(graphics);

        //graphics.fill(centerHorizonalPos-50, centerVerticalPos-20, centerHorizonalPos+50, centerVerticalPos+20, 0x88FFFFFF);

        graphics.drawString(this.font, PlayerSizeProvider.getPlayerSizeCapability(player).getMaxScale().toString(), this.leftPos + 65, topPos +18,0xdddddd,false);
    }

    private void drawSelectionHemisphere(GuiGraphics graphics){
        fillCircleSection(50, 100, graphics, 0xFFFF0000);
        renderHemisphere(50, graphics, 0xFFFFFFFF);
        renderHemisphere(100, graphics, 0xFFFFFFFF);

        

        graphics.fill(centerHorizonalPos+50, centerVerticalPos, centerHorizonalPos+100, centerVerticalPos+1, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos-50, centerVerticalPos, centerHorizonalPos-100, centerVerticalPos+1, 0xFFFFFFFF);
        //midPointLineGeneration(centerHorizonalPos+50, centerVerticalPos, centerHorizonalPos+100, centerVerticalPos, graphics); 
        //midPointLineGeneration(centerHorizonalPos-50, centerVerticalPos, centerHorizonalPos-100, centerVerticalPos, graphics); 
    }

    private void midPointLineGeneration(int y, int x, int y2, int x2, GuiGraphics graphics){
        
    }

    private void renderHemisphere(int radius, GuiGraphics graphics, int colour){
        int x = 0;
        int y = radius;

        fillTopSemicirclePixels(y, x, graphics, colour);
        fillTopSemicirclePixels(x, y, graphics, colour);
        while (y > x) {
            if (!inCirclePerimeter(y-0.5, x+1, radius)) {
                y--;
            }
            x++;
            //System.out.println("y:" + y +"/x:" + x);
            fillTopSemicirclePixels(y, x, graphics, colour);
            fillTopSemicirclePixels(x, y, graphics, colour);
        }
    }

    private void fillCircleSection(int radiusInternal, int radiusExternal, GuiGraphics graphics, int colour){
        for(int x = -radiusExternal; x < radiusExternal; x++){
            int y = 1;
            int min = 0;
            while (inCirclePerimeter(y, x, radiusInternal)) {
                min++;
                y++;
            }
            int max = min;
            while (inCirclePerimeter(y, x, radiusExternal)) {
                max++;
                y++;
            }
            
            //System.out.println(centerHorizonalPos+radius);
            graphics.fill(centerHorizonalPos+x, centerVerticalPos-min, centerHorizonalPos+x+1, centerVerticalPos-max, colour);
        }
    }

    private void renderCircle(int radius, GuiGraphics graphics){
        int x = 0;
        int y = radius;

        fillCirclePixels(y, x, graphics);
        fillCirclePixels(x, y, graphics);
        while (y > x) {
            if (!inCirclePerimeter(y-0.5, x+1, radius)) {
                y--;
            }
            x++;
            //System.out.println("y:" + y +"/x:" + x);
            fillCirclePixels(y, x, graphics);
            fillCirclePixels(x, y, graphics);
        }
    }

    private void fillTopSemicirclePixels(int y, int x, GuiGraphics graphics, int colour){
        graphics.fill(centerHorizonalPos+x, centerVerticalPos-y, centerHorizonalPos+x+1, centerVerticalPos-y+1, colour);
        graphics.fill(centerHorizonalPos-x, centerVerticalPos-y, centerHorizonalPos-x+1, centerVerticalPos-y+1, colour);
    }

    private void fillCirclePixels(int y, int x, GuiGraphics graphics){
        graphics.fill(centerHorizonalPos+x, centerVerticalPos+y, centerHorizonalPos+x+1, centerVerticalPos+y+1, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos-x, centerVerticalPos+y, centerHorizonalPos-x+1, centerVerticalPos+y+1, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos+x, centerVerticalPos-y, centerHorizonalPos+x+1, centerVerticalPos-y+1, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos-x, centerVerticalPos-y, centerHorizonalPos-x+1, centerVerticalPos-y+1, 0xFFFFFFFF);
    }

    private boolean inCirclePerimeter(double y, double x, int radius){
        double i = y*y + x*x - radius*radius;
        if (i > 0) {
            return false;
        }
        return true;
    }

    private void saveEditBox(){

    }

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
