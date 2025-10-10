package com.ricardthegreat.holdmetight.client.guielements.checkboxes;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomCheckbox extends AbstractButton{

    //this is temporary
    private static final ResourceLocation TEXTURE = new ResourceLocation(HoldMeTight.MODID, "textures/gui/checkbox.png");
    private boolean selected;
    private final boolean showLabel;
    private int textWidth;

    public CustomCheckbox(int leftPos, int topPos, int width, int height, Component label, boolean selected) {
        this(leftPos, topPos, width, height, width*3, label, selected, true);
    }

    public CustomCheckbox(int leftPos, int topPos, int width, int height, int textWidth, Component label, boolean selected, boolean showLabel) {
        super(leftPos, topPos, width, height, label);
        this.selected = selected;
        this.showLabel = showLabel;
        this.textWidth = textWidth;
    }

    public void onPress() {
        this.selected = !this.selected;
    }

    public boolean selected() {
        return this.selected;
    }

    public void updateWidgetNarration(NarrationElementOutput p_260253_) {
        p_260253_.add(NarratedElementType.TITLE, this.createNarrationMessage());
        if (this.active) {
            if (this.isFocused()) {
                p_260253_.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.focused"));
            } else {
                p_260253_.add(NarratedElementType.USAGE, Component.translatable("narration.checkbox.usage.hovered"));
            }
        }

    }

    @Override
    public void renderWidget(GuiGraphics graphics, int p_282925_, int p_282705_, float p_282612_) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        Font font = minecraft.font;
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        graphics.blit(TEXTURE, this.getX(), this.getY(), this.isFocused() ? 20.0F : 0.0F, this.selected ? 20.0F : 0.0F, 20, this.height, 256, 256);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.showLabel) {
            renderScrollingString(graphics, font, 2, 0x9a9a9a);
        }
    }
   
    @Override
    protected void renderScrollingString(GuiGraphics graphics, Font font, int inset, int fontColour) {
        int i = this.getX() + this.getWidth() + inset;
        int j = this.getX() + this.getWidth() + textWidth - inset;
        renderScrollingString(graphics, font, this.getMessage(), i, this.getY(), j, this.getY() + this.getHeight(), fontColour);
    }    
}
