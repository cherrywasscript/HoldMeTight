package com.ricardthegreat.holdmetight.client.screens;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class HeldPlayerInvScreen extends AbstractContainerScreen<HeldEntityInventoryMenu>{

    public static final ResourceLocation CONTAINER_TEXTURE = new ResourceLocation(HoldMeTight.MODID,"textures/gui/held_player_inventory.png");

    private float xMouse;
    private float yMouse;

    public HeldPlayerInvScreen(HeldEntityInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageHeight = 256;

        this.titleLabelX = 8;
        this.titleLabelY = 2;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 92;

        //TODO Auto-generated constructor stub
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float partial) {
        this.renderBackground(graphics);

        super.render(graphics, x, y, partial);
        
        this.renderTooltip(graphics, x, y);
        this.xMouse = (float)x;
        this.yMouse = (float)y;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int x, int y) {
        int i = this.leftPos;
        int j = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (this.minecraft.level.getEntity(this.menu.getOwnerId().get()) instanceof Player render) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, i + 51, j + 75, 30, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, render);
        }
    }
    
}
