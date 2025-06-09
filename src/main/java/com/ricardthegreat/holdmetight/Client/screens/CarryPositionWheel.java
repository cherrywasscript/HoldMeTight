package com.ricardthegreat.holdmetight.Client.screens;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CarryPositionWheel extends Screen{
    private static final Component TITLE = Component.literal("Carry selection screen");


    private final int imageWidth;
    private final int imageHeight;
    
    private Player player;

    //positions on the image background
    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    public CarryPositionWheel(Player player) {
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
        drawRadialSelector(mouseX, mouseY, graphics);
    }

    private void drawRadialSelector(int mouseX, int mouseY, GuiGraphics graphics){
        int internalRadius = 50;
        int externalRadius = 100;
        int verticalOffset = 30;

        if (inCirclePerimeter(mouseY-centerVerticalPos-verticalOffset, mouseX-centerHorizonalPos, externalRadius) && !inCirclePerimeter(mouseY-centerVerticalPos-verticalOffset, mouseX-centerHorizonalPos, internalRadius)) {
            System.out.println((mouseY-centerVerticalPos-verticalOffset) + "/" + (mouseX-centerHorizonalPos));
            fillCircleSection(internalRadius, externalRadius, verticalOffset, graphics, 0x55FF0000);
        }else{
            System.out.println((mouseY-centerVerticalPos-verticalOffset) + "/" + (mouseX-centerHorizonalPos));
            fillCircleSection(internalRadius, externalRadius, verticalOffset, graphics, 0xFFFF0000);
        }
        
        
        renderHemisphere(internalRadius, verticalOffset, graphics, 0xFFFFFFFF);
        renderHemisphere(externalRadius, verticalOffset, graphics, 0xFFFFFFFF);

        //generate the seperation lines
        genLineSeperations(internalRadius, externalRadius, verticalOffset, graphics);

        graphics.fill(centerHorizonalPos+internalRadius, centerVerticalPos+verticalOffset, centerHorizonalPos+externalRadius, centerVerticalPos+1+verticalOffset, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos-internalRadius, centerVerticalPos+verticalOffset, centerHorizonalPos-externalRadius, centerVerticalPos+1+verticalOffset, 0xFFFFFFFF);
        //midPointLineGeneration(centerHorizonalPos+50, centerVerticalPos, centerHorizonalPos+100, centerVerticalPos, graphics); 
        //midPointLineGeneration(centerHorizonalPos-50, centerVerticalPos, centerHorizonalPos-100, centerVerticalPos, graphics); 
    }

    private void genLineSeperations(int internalRadius, int externalRadius, int verticalOffset, GuiGraphics graphics){
        int number = 3;
        for(int i = 1; i < number; i++){
            double angle = 180/number;
            angle *= i;
            int[] firstCoords = genAngles(externalRadius, angle);
            midPointLineGeneration(0, 0, -firstCoords[1], firstCoords[0], internalRadius, externalRadius, verticalOffset, graphics);
        }

        

        //midPointLineGeneration(0, 0, firstCoords[1], -firstCoords[0], verticalOffset, graphics);
        //midPointLineGeneration(0, 0, -externalRadius, externalRadius, verticalOffset, graphics);
    }

    private int[] genAngles(int radius, double angle){
        double x = radius*Math.cos(Math.toRadians(angle));
        double y = radius*Math.sin(Math.toRadians(angle));

        //System.out.println(x + "/" + y);

        return new int[]{(int)x, (int)y};
    }

    private void midPointLineGeneration(int y1, int x1, int y2, int x2, int internalRadius, int externalRadius, int verticalOffset, GuiGraphics graphics){
        boolean negX = false;
        boolean negY = false;
        boolean swapped = false;

        //check if target x is less than starting, swap if so and mark for swapping back
        if (x2 < x1) {
            x2 = x1 + (x1 - x2);
            negX = true;
        }

        //check if target y is less than starting, swap if so and mark for swapping back
        if (y2 < y1) {
            y2 = y1 + (y1 - y2);
            negY = true;
        }

        // calculate dx & dy
        int dx = x2 - x1;
        int dy = y2 - y1;

        //check if change in y is greater than change in x, swap if so and mark for swapping back
        if (y2 > x2) {
            int temp = y1;
            y1 = x1;
            x1 = temp;
            temp = y2;
            y2 = x2;
            x2 = temp;

            dx = x2 - x1;
            dy = y2 - y1;

            swapped = true;
        }

        // initial value of decision
        // parameter d
        int d = dy - (dx/2);
        int x = x1;
        int y = y1;

        // iterate through value of X
        while (x < x2) {
            x++;

            if (d < 0){ // E or East is chosen
                d = d + dy;
            } else { // NE or North East is chosen
                d += (dy - dx);
                y++;
            }

            // Plot intermediate points
            // putpixel(x,y) is used to print
            // pixel of line in graphics

            int tempX = x;
            int tempY = y;

            if (swapped) {
                int temp = tempX;
                tempX = tempY;
                tempY = temp;
            }

            if (negX) {
                tempX = x1 - (tempX - x1);
            }

            if (negY) {
                tempY = y1 - (tempY - y1);
            }

            if (inCirclePerimeter(tempY, tempX, externalRadius) && !inCirclePerimeter(tempY, tempX, internalRadius)) {
                graphics.fill(centerHorizonalPos+tempX, centerVerticalPos+tempY+verticalOffset, centerHorizonalPos+1+tempX, centerVerticalPos+1+tempY+verticalOffset, 0xFFFFFFFF);
            }
            //System.out.print(x +"," + y + "\n");
        }
    }

    private void renderHemisphere(int radius, int verticalOffset, GuiGraphics graphics, int colour){
        int x = 0;
        int y = radius;

        fillTopSemicirclePixels(y, x, verticalOffset, graphics, colour);
        fillTopSemicirclePixels(x, y, verticalOffset, graphics, colour);
        while (y > x) {
            if (!inCirclePerimeter(y-0.5, x+1, radius)) {
                y--;
            }
            x++;
            //System.out.println("y:" + y +"/x:" + x);
            fillTopSemicirclePixels(y, x, verticalOffset, graphics, colour);
            fillTopSemicirclePixels(x, y, verticalOffset, graphics, colour);
        }
    }

    private void fillCircleSection(int radiusInternal, int radiusExternal, int verticalOffset, GuiGraphics graphics, int colour){
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
            graphics.fill(centerHorizonalPos+x, centerVerticalPos-min+verticalOffset, centerHorizonalPos+x+1, centerVerticalPos-max+verticalOffset, colour);
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

    private void fillTopSemicirclePixels(int y, int x, int verticalOffset, GuiGraphics graphics, int colour){
        graphics.fill(centerHorizonalPos+x, centerVerticalPos-y+verticalOffset, centerHorizonalPos+x+1, centerVerticalPos-y+1+verticalOffset, colour);
        graphics.fill(centerHorizonalPos-x, centerVerticalPos-y+verticalOffset, centerHorizonalPos-x+1, centerVerticalPos-y+1+verticalOffset, colour);
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
}
