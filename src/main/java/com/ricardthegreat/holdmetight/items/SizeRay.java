package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.client.ClientHooks;
import com.ricardthegreat.holdmetight.entities.projectile.RayGunProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class SizeRay extends Item {

    public static final String SCALE_TAG = "multiplier";

    public static final String MULT_TAG = "shouldMultiply";

    private static final Float DEFAULT_SCALE = 1.0f;

    public SizeRay(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!itemstack.hasTag()) {
            System.out.println("notag");
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

            System.out.println("scale: "+ tag.getFloat(SCALE_TAG) + " ismult: " + tag.getBoolean(MULT_TAG));
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

    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int p_41407_, boolean p_41408_) {
    }

    public void onCraftedBy(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Player player) {
        setDefaultTags(stack);
    }
}
