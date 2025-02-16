package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.ClientHooks;
import com.ricardthegreat.holdmetight.Client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.entities.projectile.RayGunProjectile;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.EnderpearlItem;

public class SizeRay extends Item {

    public static final String SCALE_TAG = "multiplier";

    public static final String MULT_TAG = "shouldMultiply";

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

        if (!itemstack.hasTag()) {
            setDefaultTags(itemstack);
        }

        CompoundTag tag = itemstack.getTag();

        //play sound taken from enderpearl
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 20);

        //open menu if shifted otherwise fire the ray
        if (player.isShiftKeyDown() && level.isClientSide()){

            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizeRayScreen(player, hand));
            return InteractionResultHolder.success(player.getItemInHand(hand));

        }else if (!player.isShiftKeyDown() && !level.isClientSide()) {

            RayGunProjectile rayGunProjectile = new RayGunProjectile(player, level, tag.getFloat(SCALE_TAG), tag.getBoolean(MULT_TAG));
            rayGunProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            rayGunProjectile.setNoGravity(true);
            level.addFreshEntity(rayGunProjectile);
            
        }

        return super.use(level, player, hand);
    }

    private void setDefaultTags(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(SizeRay.MULT_TAG, false);
        tag.putFloat(SizeRay.SCALE_TAG, DEFAULT_SCALE);
        stack.setTag(tag);
    }

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
    }

    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        System.out.println("this had been crafted hopefully");
        setDefaultTags(stack);
    }
}
