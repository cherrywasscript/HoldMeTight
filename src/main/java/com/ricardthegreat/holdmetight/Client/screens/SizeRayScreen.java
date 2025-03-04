package com.ricardthegreat.holdmetight.Client.screens;



import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.SSizeRaySync;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// this file honestly does far more than it probably should but yeah
public class SizeRayScreen extends Screen {

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".size_ray");

    private static final Component SET_OR_MULT_BUTTON = Component.translatable("gui." + HoldMeTight.MODID + ".size_ray.button.set_or_mult_button");

    private static final Component CUSTOM_SCALE_FIELD = Component.translatable("gui." + HoldMeTight.MODID + ".size_ray.field.custom_scale_field");

    private static final Component CUSTOM_SCALE_FIELD_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_ray.field.custom_scale_field_tooltip");
    private static final Component SET_OR_MULT_BUTTON_TOOLTIP = Component.translatable("gui." + HoldMeTight.MODID + ".size_ray.button.set_or_mult_button_tooltip");

    private static final ResourceLocation TEXTURE = new ResourceLocation(HoldMeTight.MODID, "textures/gui/size_ray_bg.png");

    private final int imageWidth;
    private final int imageHeight;

    //the item user
    private Player user;

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
    private Button setOrMultButton;

    private EditBox customScaleField;

    public SizeRayScreen(Player user, InteractionHand hand){
        super(TITLE);
        this.imageWidth = 256;
        //132
        this.imageHeight = 132;
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
        //this gives warning that minecraft can be null but it literally returns if thats the case so im not sure why
        //@SuppressWarnings("null")
        Level level = this.minecraft.level;
        if(level == null) return;

        this.setOrMultButton = addRenderableWidget(
            Button.builder(
                SET_OR_MULT_BUTTON, this::handleSetOrMultiplyButton)
                .bounds(this.leftPos + 173, this.topPos +64, 76, 20)
                .tooltip(Tooltip.create(SET_OR_MULT_BUTTON_TOOLTIP))
                .build()
        );

        initCustomScaleField();
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        if (tag.getBoolean(SizeRay.MULT_TAG)) {
            graphics.drawString(this.font,"Multiply scale by:", this.leftPos + 65, topPos +18,0xdddddd,false);
        }else{
            graphics.drawString(this.font,"Set scale to:", this.leftPos + 65, topPos +18,0xdddddd,false);
        }
        graphics.drawString(this.font, ""+ tag.getFloat(SizeRay.SCALE_TAG), this.leftPos + 154, topPos +18,0xdddddd,false);
             
    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        if (key == 257) {
            String scaleString = customScaleField.getValue();
            if (scaleString != null && !scaleString.isEmpty()){
                Float scale = Float.parseFloat(scaleString);  
                tag.putFloat(SizeRay.SCALE_TAG, scale);
                stack.setTag(tag);
            }
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }


    // setup the text box that allows players to type in a height
    private void initCustomScaleField() {
        customScaleField = addRenderableWidget(new EditBox(font, this.leftPos + 56, this.topPos +64, 97, 20, CUSTOM_SCALE_FIELD));

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


        Float mul = tag.getFloat(SizeRay.SCALE_TAG);

        String floatString = Float.toString(mul);
        //String floatStringNoEndingF = floatString.substring(0, floatString.length()-1);

        customScaleField.setValue(floatString);
        
        Tooltip t = Tooltip.create(CUSTOM_SCALE_FIELD_TOOLTIP, CUSTOM_SCALE_FIELD_TOOLTIP);
        customScaleField.setTooltip(t);

    }

    // wthat to do when the scale button is clicked
    private void handleSetOrMultiplyButton(Button button) {
        String scaleString = customScaleField.getValue();
        if (scaleString != null && !scaleString.isEmpty()){
            Float scale = Float.parseFloat(scaleString);  
            tag.putBoolean(SizeRay.MULT_TAG, !tag.getBoolean(SizeRay.MULT_TAG));
            tag.putFloat(SizeRay.SCALE_TAG, scale);
            stack.setTag(tag);
        }
    }

    @Override
    public void onClose() {
        PacketHandler.sendToServer(new SSizeRaySync(tag));
        super.onClose();
    }
}
