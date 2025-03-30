package com.ricardthegreat.holdmetight.items.remotes;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.Client.ClientHooks;
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
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {

        ItemStack item = player.getItemInHand(hand);

        if (!item.hasTag()) {
            setDefaultTags(item, player);
        }

        //open item screen client side only (need to figure out how to not pause in single player)
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openBasicSizeRemoteScreen(player, hand));
        }

        return super.use(level, player, hand);
    }
}
