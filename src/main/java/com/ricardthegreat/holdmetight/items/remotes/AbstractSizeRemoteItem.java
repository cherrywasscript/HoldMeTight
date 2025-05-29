package com.ricardthegreat.holdmetight.items.remotes;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public abstract class AbstractSizeRemoteItem extends Item{

    public static final String SCALE_TAG = "multiplier";
    public static final String UUID_TAG = "target";
    public static final String MIN_SCALE_TAG = "minscale";
    public static final String MAX_SCALE_TAG = "maxscale";
    public static final String NUM_TICKS_TAG = "numticks";
    public static final String TARGET_TAG = "has target";

    public static final String ENTITY_ID = "entity id";
    public static final String IS_PLAYER_TAG = "is player";
    

    public static final Float DEFAULT_SCALE = 1.0f;
    public static final int DEFAULT_TICKS = 20;
    public static final float RANDOM_MAX_LIMIT = 100f;
    public static final float RANDOM_MIN_LIMIT = 0;

    public AbstractSizeRemoteItem(Properties properties) {
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
            tag.putInt(ENTITY_ID, player.getId());
            tag.putBoolean(IS_PLAYER_TAG, true);
            item.setTag(tag);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        //open item screen client side only
        if (level.isClientSide()) {
            openScreen(player, hand);
        }

        return super.use(level, player, hand);
    }

    protected abstract void openScreen(Player player, InteractionHand hand);

    protected CompoundTag setDefaultTags(ItemStack stack, Player player){
        CompoundTag tag = stack.getOrCreateTag();

        tag.putFloat(SCALE_TAG, DEFAULT_SCALE);
        tag.putFloat(MIN_SCALE_TAG, 0.5f);
        tag.putFloat(MAX_SCALE_TAG, 2f);
        tag.putInt(NUM_TICKS_TAG, 20);
        
        tag.putUUID(UUID_TAG, player.getUUID());
        stack.setTag(tag);
        
        return tag;
    }

    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int tick, boolean bool) {
    }

    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {
        setDefaultTags(stack, player);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> list, @Nonnull TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        Component target = Component.translatable("tooltip.holdmetight.abstract_size_remote_target.tooltip");
        Component noTarget = Component.translatable("tooltip.holdmetight.abstract_size_remote_no_target.tooltip");
        if (tag != null) {
            String tooltip = target.getString();
            if (level != null) {

                if (tag.contains(TARGET_TAG) && !tag.getBoolean(TARGET_TAG)) {
                    list.add(Component.literal(noTarget.getString()));
                }else if (tag.getBoolean(IS_PLAYER_TAG)) {
                    Player player = level.getPlayerByUUID(tag.getUUID(UUID_TAG));
                    if (player != null) {
                        list.add(Component.literal(tooltip + player.getName().getString()));
                    }
                }else{
                    Entity entity = level.getEntity(tag.getInt(ENTITY_ID));
                    if (entity != null) {
                        list.add(Component.literal(tooltip + entity.getName().getString()));
                    }
                }
            }
            //list.add(Component.literal(tooltip));
        }
        super.appendHoverText(stack, level, list, flag);
    }

    @SuppressWarnings("null")
    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player, @Nonnull LivingEntity entity, @Nonnull InteractionHand hand) {
        //System.out.println("interact"); //TODO remove
        ItemStack item = player.getItemInHand(hand);
        if (!item.hasTag()) {
            setDefaultTags(item, player);
        }
        CompoundTag tag = item.getTag();

        // does nothing if the item is on cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.PASS;
        }

        // if the entity clicked is a player save them to the item
        if (entity instanceof Player) {
            tag.putUUID(UUID_TAG, entity.getUUID());
            tag.putInt(ENTITY_ID, entity.getId());
            tag.putBoolean(IS_PLAYER_TAG, true);
            item.setTag(tag);

            player.getCooldowns().addCooldown(this, 20);
            return InteractionResult.SUCCESS;
        }else{
            tag.putUUID(UUID_TAG, entity.getUUID());
            tag.putInt(ENTITY_ID, entity.getId());
            tag.putBoolean(IS_PLAYER_TAG, false);
            item.setTag(tag);

            player.getCooldowns().addCooldown(this, 20);
            return InteractionResult.SUCCESS;
        }
        //return InteractionResult.FAIL;
    }

    //this is probably how to do the "Size Remote (name)" thing i wanted
    /* 
    @Override
    public Component getName(@Nonnull ItemStack stack) {
        Component.translatable(this.getDescriptionId(stack)).getString();
         + " hello"
        return Component.translatable(this.getDescriptionId(stack));
    }
        */
}
