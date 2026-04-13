package com.ricardthegreat.holdmetight.inventory;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class HeldEntityInventoryProvider implements MenuProvider{



    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new HeldEntityInventoryMenu(i, inventory, player);
    }

    @Override
    public Component getDisplayName() {
        //TODO make this translatable and also like better
        return Component.literal("Held Entity Inventory Menu");
    }
    
}
