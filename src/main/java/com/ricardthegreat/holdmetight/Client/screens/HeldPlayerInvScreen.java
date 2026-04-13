package com.ricardthegreat.holdmetight.client.screens;

import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class HeldPlayerInvScreen extends AbstractContainerScreen<HeldEntityInventoryMenu>{

    private float xMouse;
    private float yMouse;

    public HeldPlayerInvScreen(HeldEntityInventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public void render(GuiGraphics graphics, int x, int y, float partial) {
        this.renderBackground(graphics);

        this.renderTooltip(graphics, x, y);
        this.xMouse = (float)x;
        this.yMouse = (float)y;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, i + 51, j + 75, 30, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.minecraft.player);
    }
    
}
