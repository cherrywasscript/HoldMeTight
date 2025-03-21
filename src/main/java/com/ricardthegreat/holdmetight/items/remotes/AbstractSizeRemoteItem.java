package com.ricardthegreat.holdmetight.items.remotes;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ricardthegreat.holdmetight.items.remotes.setmult.CustomSizeRemoteItem;
import com.ricardthegreat.holdmetight.items.remotes.setmult.OtherCustomSizeRemoteItem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
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

    public static final Float DEFAULT_SCALE = 1.0f;
    public static final int DEFAULT_TICKS = 20;

    public AbstractSizeRemoteItem(Properties properties) {
        super(properties);
    }

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
                Player player = level.getPlayerByUUID(tag.getUUID(UUID_TAG));
                if (player != null) {
                    if (tag.contains(OtherCustomSizeRemoteItem.TARGET_TAG) && !tag.getBoolean(OtherCustomSizeRemoteItem.TARGET_TAG)) {
                        list.add(Component.literal(noTarget.getString()));
                    }else{
                        list.add(Component.literal(tooltip + player.getName().getString()));
                    }
                }
            }
            //list.add(Component.literal(tooltip));
        }
        super.appendHoverText(stack, level, list, flag);
    }
}
