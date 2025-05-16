package com.ricardthegreat.holdmetight.Client.screens;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.joml.Quaternionf;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;



public class CarryPositionScreen extends Screen{

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".carry_position_screen");

    private static final ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/carry_position_item_bg.png");
    private static final ResourceLocation CIRCLE = new ResourceLocation(HoldMeTight.MODID, "textures/gui/carry_screen_circle.png");

    private static final Component ROT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.rotate_button");

    private static final Component CUSTOM_INPUT_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_input_field");
    private static final Component CUSTOM_INPUT_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_input_field_tooltip");

    private final double[] chestPreset = new double[]{0,0,0,0};
    private final double[] mouthPreset = new double[]{0,0,0,0};

    private final int imageWidth;
    private final int imageHeight;

    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    private Player user;
    private PlayerCarry userCarry;

    private int rotation = 0;
    private double forwardsBackwardsMultiplier = 0;
    private double vertical = 0;
    private double leftRight = 0;

    private boolean topDownView = true;
    

    private Button setButton;

    private EditBox customInputField;

    public CarryPositionScreen(Player user) {
        super(TITLE);
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.user = user;
        userCarry = PlayerCarryProvider.getPlayerCarryCapability(user);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.rightPos = (this.width - this.leftPos) ;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.bottomPos = (this.height - this.topPos) ;
        this.centerHorizonalPos = (this.leftPos + this.rightPos) / 2 ;
        this.centerVerticalPos = (this.topPos + this.bottomPos) / 2;

        this.setButton = addRenderableWidget(
            Button.builder(
                ROT_BUTTON, this::handleSetButton)
                .bounds(this.leftPos + 8, this.topPos +200, 76, 20)
                .tooltip(Tooltip.create(ROT_BUTTON))
                .build()
        );

        initCustomInputField();
    }

    //32 pixels is 1 meter in this screen im psure
    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        
        
        PlayerRenderExtension rend = (PlayerRenderExtension) user;
        //possibly use this in place of coloured boxes
        if(rend != null){
            rend.setMenu(true);
            if (topDownView) {
                //top down facing to the top of the screen
                renderEntityInInventoryFollowsAngle(graphics, centerHorizonalPos, centerVerticalPos, 60, 0, -4.5f, (Player) rend);
            }else{
                //from the side facing to the left of the screen
                renderEntityInInventoryFollowsAngle(graphics, centerHorizonalPos, centerVerticalPos+60, 60, 270, 0, (Player) rend);
            }
            rend.setMenu(false);
        }


        renderTopDown(graphics, mouseX, mouseY, partialTicks);

        //graphics.fill(leftPos + 9, topPos + 9, this.leftPos+55, this.topPos+55, 0xFF00FF00);

        //graphics.pose().rotateAround(null, mouseX, mouseY, partialTicks);

    }

    private void renderTopDown(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int xpos = centerHorizonalPos;
        int ypos = centerVerticalPos;
        //graphics.fill(xpos-5, ypos-5, xpos+5, ypos + 5, 0xFFFFFFFF);

        double xOffset = (Math.cos(Math.toRadians((rotation - 90)%360)));
        double yOffset = (Math.sin(Math.toRadians((rotation - 90)%360)));

        forwardsBackwardsMultiplier = 1;
        xpos += xOffset*64*forwardsBackwardsMultiplier;
        ypos += yOffset*64*forwardsBackwardsMultiplier;

        graphics.fill(xpos - 2, ypos - 2, xpos + 2, ypos + 2, 0xFFFF0000);
    }
    
    private void handleSetButton(Button button) {  
        String rot = customInputField.getValue();

        topDownView = !topDownView;

        if (rot != null && !rot.isEmpty()){
            rotation = (Integer.parseInt(rot)%360);
            //userCarryExt.setRotationOffset(rotation);
        }
    }


    //customised so i can have a player positioned like i want
    private void renderEntityInInventoryFollowsAngle(GuiGraphics graphics, int xPos, int yPos, int scale, float angleXComponent, float angleYComponent, LivingEntity entity) {
      //float f = (float)Math.atan((double)(angleXComponent / 40.0F));
      //float f1 = (float)Math.atan((double)(angleYComponent / 40.0F));
      
        float f = angleXComponent;
        float f1 = angleYComponent;
    

        Quaternionf quaternionf = (new Quaternionf()).rotateZ((float)Math.PI);
        Quaternionf quaternionf1 = (new Quaternionf()).rotateX(f1 * 20.0F * ((float)Math.PI / 180F));
        quaternionf.mul(quaternionf1);
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        
        //entity.yBodyRot = 180.0F + f * 20.0F;
        //entity.setXRot(-f1 * 20.0F);
        entity.yBodyRot = f;
        //entity.setYRot(180.0F + f * 40.0F);
        entity.setYRot(f);
        entity.setXRot(0);
        entity.yHeadRot = f;
        entity.yHeadRotO = 0;
        InventoryScreen.renderEntityInInventory(graphics, xPos, yPos, scale, quaternionf, quaternionf1, entity);
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
   }

   //this is straight copied from size remote so need to actually look at once other stuff done
   private void initCustomInputField() {
        customInputField = addRenderableWidget(new EditBox(font, this.leftPos + 48, this.bottomPos - 80, 80, 20, CUSTOM_INPUT_FIELD));

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
                    Integer.parseInt(t);
                    return true;
                }catch (Exception e){ 
                    return false;
                }      
            }
        };

        customInputField.setFilter(filter);

        // get mult from tag, kept in comment
        //Tag tagMul = tag.get("multiplier");



        //grab existing rotation from user
        customInputField.setValue(Integer.toString(userCarry.getCarryPosition().RotationOffset));
        
        Tooltip t = Tooltip.create(CUSTOM_INPUT_FIELD_TOOLTIP, CUSTOM_INPUT_FIELD_TOOLTIP);
        customInputField.setTooltip(t);

    }
}
