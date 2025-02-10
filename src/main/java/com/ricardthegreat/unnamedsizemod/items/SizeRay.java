package com.ricardthegreat.unnamedsizemod.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.unnamedsizemod.Client.ClientHooks;
import com.ricardthegreat.unnamedsizemod.utils.SizeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.EnderpearlItem;

public class SizeRay extends Item {

    public static final String MULT_TAG = "multiplier";

    public static final String UUID_TAG = "target";

    private static final Float DEFAULT_SCALE = 1.0f;

    //private Float scale = 1.0f;

    //private Player selectedPlayer;

    private MinecraftServer server;

    public SizeRay(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        //play sound taken from enderpearl
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        player.getCooldowns().addCooldown(this, 20);

        //
        if (player.isShiftKeyDown() && level.isClientSide()){

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizeRayScreen(player, hand));
            return InteractionResultHolder.success(player.getItemInHand(hand));

        }else if (!player.isShiftKeyDown() && !level.isClientSide()) {

            ThrownEnderpearl thrownenderpearl = new ThrownEnderpearl(level, player);
            thrownenderpearl.setItem(itemstack);
            thrownenderpearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownenderpearl);
            
        }

        return super.use(level, player, hand);
    }

    private CompoundTag checkDefaultTags(ItemStack stack, Player player){
        CompoundTag tag = stack.getTag();
        if(tag == null){
            tag = stack.getOrCreateTag();
            tag.putFloat(AdvancedSizeRemote.MULT_TAG, DEFAULT_SCALE);
            stack.setTag(tag);
        }
        return tag;
    }

    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
    }

    public void onCraftedBy(ItemStack p_41447_, Level p_41448_, Player p_41449_) {
    }
}
