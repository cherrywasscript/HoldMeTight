package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class PaperWingsItem extends ElytraItem {

    public PaperWingsItem(Properties properties) {
            super(properties);
    }
    
    public InteractionResultHolder<ItemStack> swapWithEquipmentSlot(@Nonnull Item item, @Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (SizeUtils.getSize(player) > 0.5) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return super.swapWithEquipmentSlot(item, level, player, hand);
    }

    public void test(GameEvent e){

    }
}
