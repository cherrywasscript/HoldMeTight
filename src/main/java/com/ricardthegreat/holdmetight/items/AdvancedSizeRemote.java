package com.ricardthegreat.holdmetight.items;

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


public class AdvancedSizeRemote extends Item {

    public static final String SCALE_TAG = "multiplier";

    public static final String UUID_TAG = "target";

    private static final Float DEFAULT_SCALE = 1.0f;

    public AdvancedSizeRemote(Item.Properties properties) {
        super(properties);
    }

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

        //open item screen client side only (need to figure out how to not pause in single player)
        
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizeRemoteScreen(player, hand));
        }

        return super.use(level, player, hand);
    }

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
        player.getCooldowns().addCooldown(this, 20);

        // if the entity clicked is a player save them to the item else change their
        // scale
        if (entity instanceof Player) {
            tag.putUUID(UUID_TAG, entity.getUUID());
            item.setTag(tag);
            //setSelectedPlayer((Player) entity);
        } else {
            SizeUtils.setSizeOverTimeDefault(entity, tag.getFloat(SCALE_TAG));
        }

        return InteractionResult.SUCCESS;
    }

    /* 
    public void setScaleFactor(Float scale) {
        this.scale = scale;
    }

    public Float getScaleFactor(){
        return scale;
    }

    public void setSelectedPlayer(Player player) {
        this.selectedPlayer = player;
    }

    public Player getSelectedPlayer() {
        return selectedPlayer;
    }
        */

    private CompoundTag setDefaultTags(ItemStack stack, Player player){
        CompoundTag tag = stack.getOrCreateTag();

        tag.putFloat(AdvancedSizeRemote.SCALE_TAG, DEFAULT_SCALE);
        tag.putUUID(AdvancedSizeRemote.UUID_TAG, player.getUUID());
        stack.setTag(tag);
        
        return tag;
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        setDefaultTags(stack, player);
    }
}
