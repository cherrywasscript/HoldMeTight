package com.ricardthegreat.holdmetight.client.guielements.tooltips;

import org.joml.Quaternionf;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import com.ricardthegreat.holdmetight.utils.rendering.ScreenPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientPlayerItemTooltipComponent implements ClientTooltipComponent{

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(HoldMeTight.MODID,"textures/gui/player_item_tooltip.png");
   private static final int MARGIN_Y = 4;
   private static final int BORDER_WIDTH = 1;
   private static final int TEX_SIZE = 128;
   private static final int SLOT_SIZE_X = 18;
   private static final int SLOT_SIZE_Y = 20;
   private final NonNullList<ItemStack> items;
   private final NonNullList<ItemStack> armour;
   private final NonNullList<ItemStack> offhand;
   private final Player player;

   public ClientPlayerItemTooltipComponent(PlayerItemTooltip tooltip) {
      this.items = tooltip.getItems();
      this.armour = tooltip.getArmour();
      this.offhand = tooltip.getOffhand();
      this.player = tooltip.getPlayer();
   }

   public int getHeight() {
      return 168;
   }

   public int getWidth(Font font) {
      return 176;
   }

   public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
      graphics.blit(TEXTURE_LOCATION, x, y, 0, 0, getWidth(font), getHeight());

      renderInventory(0, font, x, y, graphics);
      renderArmour(0, font, x, y, graphics);
      renderOffhand(0, font, x, y, graphics);
      renderPlayer(x, y, graphics);
      
   }

   private void renderInventory(int itemNumber, Font font, int x, int y, GuiGraphics graphics){
      for(int i = 0; i < 4; i++){
         for(int j = 0; j < 9; j++){
            int itemX = x + j*18 + 7;

            int itemY = 0;
            if (i == 0) {
               itemY = y + 4*18 +     69;
            }else{
               itemY = y + i*18 +     65;
            }
            
            ItemStack item = items.get(itemNumber);
            
            graphics.renderItem(items.get(itemNumber), itemX + 1, itemY + 1, itemNumber);
            graphics.renderItemDecorations(font, items.get(itemNumber), itemX + 1, itemY + 1);

            itemNumber++;
         }
      }
   }

   private void renderArmour(int itemNumber, Font font, int x, int y, GuiGraphics graphics){
      for(int i = armour.size()-1; i >= 0; i--){
         int itemX = x + 7;
         int itemY = y + i*18 + 7;

         graphics.renderItem(armour.get(itemNumber), itemX + 1, itemY + 1, itemNumber);
         graphics.renderItemDecorations(font, items.get(itemNumber), itemX + 1, itemY + 1);

         itemNumber++;
      }
   }

   private void renderOffhand(int itemNumber, Font font, int x, int y, GuiGraphics graphics){
      int itemX = x + 4 + 4*18;
      int itemY = y + 3*18 + 7;
      graphics.renderItem(offhand.get(itemNumber), itemX + 1, itemY + 1, itemNumber);
      graphics.renderItemDecorations(font, items.get(itemNumber), itemX + 1, itemY + 1);
   }
    
   private void renderPlayer(int x, int y, GuiGraphics graphics){
      Player screenPlayer = new ScreenPlayer(Minecraft.getInstance().level, player.getGameProfile(), player.getUUID());
      
      InventoryScreen.renderEntityInInventory(graphics, x+50, y+75, 30, (new Quaternionf()).rotationXYZ(0, (float)Math.PI, (float)Math.PI), null, screenPlayer);
   }
}
