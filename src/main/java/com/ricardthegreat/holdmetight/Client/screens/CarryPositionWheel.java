package com.ricardthegreat.holdmetight.Client.screens;

import java.util.ArrayList;
import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CarryPositionWheel extends Screen{
    private static final Component TITLE = Component.literal("Carry selection screen");

    private static final Component CARRY_POS_SELECTED = Component.translatable("message." + HoldMeTight.MODID + ".carry_key_pressed");


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


    private int internalRadius = 50;
    private int externalRadius = 100;
    private int verticalOffset = 30;


    private ArrayList<CarryPosition> defaultPositions;
    private ArrayList<CarryPosition> customPositions;
    private ArrayList<CarryPosition> currentListedPositions;
    private int numSlices;
    private int page = 0;
    
    private boolean rightArrowVisible;
    private boolean leftArrowVisible;

    public CarryPositionWheel(Player player) {
        super(TITLE);
        
        this.imageWidth = 176;
        this.imageHeight = 256;

        this.player = player;
        defaultPositions = PlayerCarryProvider.getPlayerCarryCapability(player).getAllCarryPositions().get(0);
        customPositions = PlayerCarryProvider.getPlayerCarryCapability(player).getAllCarryPositions().get(1);
        currentListedPositions = defaultPositions;
        numSlices = currentListedPositions.size();

        leftArrowVisible = false;
        if (customPositions.size() > 0) {
            rightArrowVisible = true;
        }
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
        drawArrows(mouseX, mouseY, graphics);
    }

    private void drawArrows(int mouseX, int mouseY, GuiGraphics graphics){
        if (rightArrowVisible) {
            if (hoveringRightArrow(mouseX, mouseY)) {
                drawArrow(7, verticalOffset-2, 17, 9, 6, 0xFFFFFFFF, false, graphics);
                drawArrow(8, verticalOffset-1, 17, 7, 5, 0xFF000000, false, graphics);
            }else{
                drawArrow(8, verticalOffset-1, 15, 7, 4, 0xFFFFFFFF, false, graphics);
                drawArrow(9, verticalOffset, 15, 5, 3, 0xFF000000, false, graphics);
            }
        }
        
        if (leftArrowVisible) {
            if (hoveringLeftArrow(mouseX, mouseY)) {
                drawArrow(-7, verticalOffset-2, 17, 9, 6, 0xFFFFFFFF, true, graphics);
                drawArrow(-8, verticalOffset-1, 17, 7, 5, 0xFF000000, true, graphics);
            }else{
                drawArrow(-8, verticalOffset-1, 15, 7, 4, 0xFFFFFFFF, true, graphics);
                drawArrow(-9, verticalOffset, 15, 5, 3, 0xFF000000, true, graphics);
            }
        }
    }

    private void drawArrow(int x, int y, int length, int height, int pointHeight, int colour, boolean backwards, GuiGraphics graphics){
        int direction = 1;
        if (backwards) {
            length = -length;
            direction = -1;
        }

        graphics.fill(centerHorizonalPos+x, centerVerticalPos+y, centerHorizonalPos+x+length, centerVerticalPos+height+y, colour);
        int tip = height/2;
        for(int i = -pointHeight; i <= tip; i++){
            graphics.fill(centerHorizonalPos+x+length, centerVerticalPos+y+i, centerHorizonalPos+x+length+1, centerVerticalPos+y+height-i, colour);
            length += direction;
        }
    }

    private void drawRadialSelector(int mouseX, int mouseY, GuiGraphics graphics){
        int relativeMouseY = mouseY-centerVerticalPos-verticalOffset;
        int relativeMouseX = mouseX-centerHorizonalPos;


        if (findSlice(relativeMouseY, relativeMouseX) >= 0) {
            fillCircleSection(graphics, 0xFFFF0000, findSlice(relativeMouseY, relativeMouseX));
        }else{
            fillCircleSection(graphics, 0xFFFF0000);
        }
        
        
        renderHemisphere(internalRadius, graphics, 0xFFFFFFFF);
        renderHemisphere(externalRadius, graphics, 0xFFFFFFFF);

        //generate the seperation lines
        genLineSeperations(graphics);
        findTextPositions(graphics);

        graphics.fill(centerHorizonalPos+internalRadius, centerVerticalPos+verticalOffset, centerHorizonalPos+externalRadius, centerVerticalPos+1+verticalOffset, 0xFFFFFFFF);
        graphics.fill(centerHorizonalPos-internalRadius, centerVerticalPos+verticalOffset, centerHorizonalPos-externalRadius, centerVerticalPos+1+verticalOffset, 0xFFFFFFFF);
        //midPointLineGeneration(centerHorizonalPos+50, centerVerticalPos, centerHorizonalPos+100, centerVerticalPos, graphics); 
        //midPointLineGeneration(centerHorizonalPos-50, centerVerticalPos, centerHorizonalPos-100, centerVerticalPos, graphics); 
    }

    private void findTextPositions(GuiGraphics graphics){
        for(int i = 0; i < numSlices; i++){
            double angle = 180/numSlices;
            angle = (angle/2) + (angle*i);
            int[] firstCoords = genAngles(externalRadius, angle);
            ArrayList<int[]> coords = midPointLineGeneration(0, 0, -firstCoords[1], firstCoords[0]);
            
            int half = coords.size()/2;
            
            graphics.drawCenteredString(font, currentListedPositions.get(i).posName, centerHorizonalPos+coords.get(half)[0], centerVerticalPos+coords.get(half)[1]+verticalOffset, 0xdddddd);
        }
    }

    private void genLineSeperations(GuiGraphics graphics){
        for(int i = 1; i < numSlices; i++){
            double angle = 180/numSlices;
            angle *= i;
            int[] firstCoords = genAngles(externalRadius, angle);
            ArrayList<int[]> coords = midPointLineGeneration(0, 0, -firstCoords[1], firstCoords[0]);

            for(int j = 0; j < coords.size(); j++){
                graphics.fill(centerHorizonalPos+coords.get(j)[0], centerVerticalPos+coords.get(j)[1]+verticalOffset, centerHorizonalPos+1+coords.get(j)[0], centerVerticalPos+1+coords.get(j)[1]+verticalOffset, 0xFFFFFFFF);
            }
        }
    }

    private int[] genAngles(int radius, double angle){
        double x = radius*Math.cos(Math.toRadians(angle));
        double y = radius*Math.sin(Math.toRadians(angle));

        //System.out.println(x + "/" + y);

        return new int[]{(int)x, (int)y};
    }

    private ArrayList<int[]> midPointLineGeneration(int y1, int x1, int y2, int x2){
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
        
        ArrayList<int[]> coords = new ArrayList<int[]>();
        
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
                coords.add(new int[]{tempX, tempY});
            }
        }

        return coords;
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

    private void fillCircleSection(GuiGraphics graphics, int colour){
        for(int x = -externalRadius; x < externalRadius; x++){
            int y = 1;
            int min = 0;
            while (inCirclePerimeter(y, x, internalRadius)) {
                min++;
                y++;
            }
            int max = min;
            while (inCirclePerimeter(y, x, externalRadius)) {
                max++;
                y++;
            }
            
            //System.out.println(centerHorizonalPos+radius);
            graphics.fill(centerHorizonalPos+x, centerVerticalPos-min+verticalOffset, centerHorizonalPos+x+1, centerVerticalPos-max+verticalOffset, 0x55FF0000);
        }
    }

    private void fillCircleSection(GuiGraphics graphics, int colour, int slice){
        for(int x = -externalRadius; x < externalRadius; x++){
            int y = 1;
            int min = 0;
            while (inCirclePerimeter(y, x, internalRadius)) {
                min++;
                y++;
            }

            int max = min;

            int sliceMin = min;
            int sliceMax = max;

            while (inCirclePerimeter(y, x, externalRadius)) {
                
                if (inSlice(-y, x, slice)) {
                    if (sliceMax < max) {
                        sliceMax = max;
                    }
                    if (sliceMin <= min) {
                        min++;
                        max++;
                    }

                    sliceMax++;
                }else{
                    if (max < sliceMax) {
                        max = sliceMax;
                    }

                    if (min <= sliceMin) {
                        sliceMin++;
                        sliceMax++;
                    }

                    max++;   
                } 

                y++;
            }
            
            graphics.fill(centerHorizonalPos+x, centerVerticalPos-min+verticalOffset, centerHorizonalPos+x+1, centerVerticalPos-max+verticalOffset, 0x55FF0000);
            graphics.fill(centerHorizonalPos+x, centerVerticalPos-sliceMin+verticalOffset, centerHorizonalPos+x+1, centerVerticalPos-sliceMax+verticalOffset, 0xFFFF0000);
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

    private int findSlice(double y, double x){
        int slice = -1;
        if (inCirclePerimeter(y, x, externalRadius) && !inCirclePerimeter(y, x, internalRadius)) {
            for(int i = 0; i < numSlices; i++){
                if (inSlice(y, x, i)) {
                    slice = i;
                }
            }
            
        }
        return slice;
    }

    private boolean inSlice(double y, double x, int slice){
        double angle = 180/numSlices;
        angle = Math.toRadians(angle);

        double A = Math.atan2(y-0, x-0);
        double startingAngle = 0 - angle*slice;
        double endingAngle = -angle - angle*slice;

        if (A < startingAngle && A > endingAngle) {
            return true;
        }
        return false;
    }

    private void updateListedPositions(){
        if (page == 0) {
            currentListedPositions = defaultPositions;
        }else{
            ArrayList<CarryPosition> positions = new ArrayList<>();
            for(int i = 0; i<5 && i<customPositions.size(); i++){
                int selectionOffset = i + (page-1)*5;
                positions.add(customPositions.get(selectionOffset));
            }
            currentListedPositions = positions;
        }
        updateNumSlices();
        updateArrows();
    }

    private void updateNumSlices(){
        numSlices = currentListedPositions.size();
    }

    private void updateArrows(){
        if (page == 0) {
            leftArrowVisible = false;
            rightArrowVisible = true;
        }else{
            leftArrowVisible = true;
            if (customPositions.size() > page*5) {
                rightArrowVisible = true;
            }else{
                rightArrowVisible = false;
            }
        }
    }

    private boolean hoveringLeftArrow(double x, double y){
        int y1 = centerVerticalPos + verticalOffset - 2;
        int y2 = centerVerticalPos + verticalOffset - 2 + 9;

        int x1 = centerHorizonalPos-7;
        int x2 = centerHorizonalPos-7-22;

        if (y > y1 && y < y2 && x < x1 && x > x2) {
            return true;
        }
        return false;
    }

    private boolean hoveringRightArrow(double x, double y){
        int y1 = centerVerticalPos + verticalOffset - 2;
        int y2 = centerVerticalPos + verticalOffset - 2 + 9;

        int x1 = centerHorizonalPos+7;
        int x2 = centerHorizonalPos+7+22;

        if (y > y1 && y < y2 && x > x1 && x < x2) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_94697_) {
        int posSelection = findSlice(y-centerVerticalPos-verticalOffset, x-centerHorizonalPos);
        if(posSelection >= 0){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            CarryPosition selectedPos = currentListedPositions.get(posSelection);

            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            if (page == 0) {
                playerCarry.setCarryPosition(false, posSelection);
            }else{
                playerCarry.setCarryPosition(true, posSelection);
            }
            
            player.displayClientMessage(Component.literal(CARRY_POS_SELECTED.getString() + selectedPos.posName), true);

            playerCarry.setShouldSyncSimple(true);
        }

        if (rightArrowVisible && hoveringRightArrow(x, y)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            
            page++;
            updateListedPositions();
        }

        if (leftArrowVisible && hoveringLeftArrow(x, y)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            page--;
            updateListedPositions();
        }

        return super.mouseClicked(x, y, p_94697_);
    }
}
