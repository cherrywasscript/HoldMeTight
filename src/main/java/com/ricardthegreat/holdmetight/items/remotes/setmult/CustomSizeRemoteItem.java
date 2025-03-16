package com.ricardthegreat.holdmetight.items.remotes.setmult;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.Client.ClientHooks;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;


public class CustomSizeRemoteItem extends AbstractSizeRemoteItem {

    public CustomSizeRemoteItem(Item.Properties properties) {
        super(properties);
    }

    //suppressing warnings because tag should never be null as if item as no tags i create them before continuing
    @SuppressWarnings("null")
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {

        ItemStack item = player.getItemInHand(hand);

        if (!item.hasTag()) {
            setDefaultTags(item, player);
        }

        CompoundTag tag = item.getTag();

        if (player.isShiftKeyDown()){
            tag.putUUID(UUID_TAG, player.getUUID());
            item.setTag(tag);
            
            //this.selectedPlayer = null;
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        //open item screen client side only
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizeRemoteScreen(player, hand));
        }

        return super.use(level, player, hand);
    }

    //suppressing warnings because tag should never be null as if item as no tags i create them before continuing
    @SuppressWarnings("null")
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity entity, @Nonnull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (!item.hasTag()) {
            setDefaultTags(item, player);
        }
        CompoundTag tag = item.getTag();

        // does nothing if the item is on cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.FAIL;
        }

        // if the entity clicked is a player save them to the item
        if (entity instanceof Player) {
            tag.putUUID(UUID_TAG, entity.getUUID());
            item.setTag(tag);

            player.getCooldowns().addCooldown(this, 20);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

}
