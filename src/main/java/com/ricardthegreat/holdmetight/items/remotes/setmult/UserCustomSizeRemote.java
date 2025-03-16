package com.ricardthegreat.holdmetight.items.remotes.setmult;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.Client.ClientHooks;
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


public class UserCustomSizeRemote extends Item {

    public static final String SCALE_TAG = "multiplier";

    public static final String UUID_TAG = "target";

    private static final Float DEFAULT_SCALE = 1.0f;

    public UserCustomSizeRemote(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {

        ItemStack item = player.getItemInHand(hand);

        if (!item.hasTag()) {
            setDefaultTags(item, player);
        }

        CompoundTag tag = item.getTag();

        //open item screen client side only (need to figure out how to not pause in single player)
        
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizeRemoteScreen(player, hand));
        }

        return super.use(level, player, hand);
    }

    private CompoundTag setDefaultTags(ItemStack stack, Player player){
        CompoundTag tag = stack.getOrCreateTag();

        tag.putFloat(CustomSizeRemote.SCALE_TAG, DEFAULT_SCALE);
        tag.putUUID(CustomSizeRemote.UUID_TAG, player.getUUID());
        stack.setTag(tag);
        
        return tag;
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        setDefaultTags(stack, player);
    }
}
