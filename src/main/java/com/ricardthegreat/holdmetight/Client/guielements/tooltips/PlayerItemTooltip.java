package com.ricardthegreat.holdmetight.client.guielements.tooltips;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class PlayerItemTooltip implements TooltipComponent{
    private final NonNullList<ItemStack> items;
    private final NonNullList<ItemStack> armour;
    private final NonNullList<ItemStack> offhand;
    private final Player player;

    public PlayerItemTooltip(NonNullList<ItemStack> items, NonNullList<ItemStack> armour, NonNullList<ItemStack> offhand, Player player) {
        this.items = items;
        this.armour = armour;
        this.offhand = offhand;
        this.player = player;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public NonNullList<ItemStack> getArmour() {
        return this.armour;
    }

    public NonNullList<ItemStack> getOffhand() {
        return this.offhand;
    }

    public Player getPlayer(){
        return this.player;
    }
}
