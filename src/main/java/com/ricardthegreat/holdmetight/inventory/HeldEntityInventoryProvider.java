package com.ricardthegreat.holdmetight.inventory;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class HeldEntityInventoryProvider implements MenuProvider{

    private final Entity accessedEnt;

    public HeldEntityInventoryProvider(Entity ent){
        super();
        this.accessedEnt = ent;
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory targetInv, Player player) {
        //TODO make it so this returns a menu using a provided players inventory and not just the player who is opening
        if (this.accessedEnt instanceof Player accessed) {
            return new HeldEntityInventoryMenu(i, accessed.getInventory(), player.getInventory());
        }else{
            return new HeldEntityInventoryMenu(i, player.getInventory());
        }
        
    }

    @Override
    public Component getDisplayName() {
        //TODO make this translatable and also like better
        return Component.literal("Held Entity Inventory Menu");
    }
    
}
