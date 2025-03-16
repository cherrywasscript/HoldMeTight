package com.ricardthegreat.holdmetight.Client.screens;

import java.util.Vector;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.setmult.CustomSizeRemoteItem;
import com.ricardthegreat.holdmetight.utils.PlayerCarryExtension;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;



public class CarryPositionScreen extends Screen{

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".carry_position_screen");

    private static final ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/carry_position_item_bg.png");
    private static final ResourceLocation CIRCLE = new ResourceLocation(HoldMeTight.MODID, "textures/gui/carry_screen_circle.png");

    private static final Component ROT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.button.rotate_button");

    private static final Component CUSTOM_INPUT_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_input_field");
    private static final Component CUSTOM_INPUT_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_remote.field.custom_input_field_tooltip");


    private final int imageWidth;
    private final int imageHeight;

    private int leftPos;
    private int rightPos;
    private int topPos;
    private int bottomPos;
    private int centerHorizonalPos;
    private int centerVerticalPos;

    private Player user;
    private PlayerCarryExtension userCarryExt;

    private int rotation = 0;

    private Button rotButton;

    private EditBox customInputField;

    public CarryPositionScreen(Player user) {
        super(TITLE);
        this.imageWidth = 256;
        this.imageHeight = 256;
        this.user = user;
        userCarryExt = (PlayerCarryExtension) user;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.rightPos = (this.width - this.leftPos) ;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.bottomPos = (this.height - this.topPos) ;
        this.centerHorizonalPos = (this.leftPos + this.rightPos) / 2 ;
        this.centerVerticalPos = (this.topPos + this.bottomPos) / 2;

        this.rotButton = addRenderableWidget(
            Button.builder(
                ROT_BUTTON, this::handleRotButton)
                .bounds(this.leftPos + 8, this.topPos +200, 76, 20)
                .tooltip(Tooltip.create(ROT_BUTTON))
                .build()
        );

        initCustomInputField();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        //graphics.blit(CIRCLE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int xpos = centerHorizonalPos;
        int ypos = centerVerticalPos;
        graphics.fill(xpos-5, ypos-5, xpos+5, ypos + 5, 0xFFFFFFFF);


        double xOffset = (Math.cos(Math.toRadians(rotation%360)));
        double yOffset = (Math.sin(Math.toRadians(rotation%360)));

        xpos += xOffset*10;
        ypos += yOffset*10;

        //graphics.fill(leftPos + 10, topPos + 10, leftPos+30, topPos+30, 0xFFFF0000);
        graphics.fill(xpos - 2, ypos - 2, xpos + 2, ypos + 2, 0xFFFF0000);

        

        PlayerRenderExtension rend = (PlayerRenderExtension) user;
        //possibly use this in place of coloured boxes
        if(rend != null){
            rend.setMenu(true);
            //renderEntityInInventoryFollowsAngle(graphics, centerHorizonalPos, centerVerticalPos, 30, 0, -4.5f, (Player) rend);
            rend.setMenu(false);
        }

        //graphics.fill(leftPos + 9, topPos + 9, this.leftPos+55, this.topPos+55, 0xFF00FF00);

        //graphics.pose().rotateAround(null, mouseX, mouseY, partialTicks);

    }
    
    private void handleRotButton(Button button) {  
        String rot = customInputField.getValue();
        if (rot != null && !rot.isEmpty()){
            rotation = Integer.parseInt(rot)%360;
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
        entity.yBodyRot = 0;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(0);
        entity.yHeadRot = 0;
        entity.yHeadRotO = 0;
        InventoryScreen.renderEntityInInventory(graphics, xPos, yPos, scale, quaternionf, quaternionf1, entity);
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
   }

   //this is straight copied from advanced size remote so need to actually look at once other stuff done
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
        customInputField.setValue(Integer.toString(userCarryExt.getCustomRotOffset()));
        
        Tooltip t = Tooltip.create(CUSTOM_INPUT_FIELD_TOOLTIP, CUSTOM_INPUT_FIELD_TOOLTIP);
        customInputField.setTooltip(t);

    }
}
