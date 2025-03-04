package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;

public class PlayerStandinItem extends Item {

    public PlayerStandinItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(@Nonnull UseOnContext context) {
        Vec3 bp = context.getClickLocation();

        return InteractionResult.PASS;
    }
    
}
