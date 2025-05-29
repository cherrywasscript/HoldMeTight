package com.ricardthegreat.holdmetight.items.remotes;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.Client.ClientHooks;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;


public class BasicSizeRemoteItem extends AbstractSizeRemoteItem {

    public BasicSizeRemoteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected void openScreen(Player player, InteractionHand hand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openBasicSizeRemoteScreen(player, hand));
    }
}
