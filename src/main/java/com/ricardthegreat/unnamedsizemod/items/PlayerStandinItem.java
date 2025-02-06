package com.ricardthegreat.unnamedsizemod.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class PlayerStandinItem extends Item {

    public PlayerStandinItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Vec3 bp = context.getClickLocation();

        System.out.println(bp.x + "," + bp.y + "," + bp.z);
        return InteractionResult.PASS;
    }
    
}
