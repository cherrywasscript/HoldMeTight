package com.ricardthegreat.holdmetight.items;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class PaperWingsItemBackup extends Item implements Equipable {

    public PaperWingsItemBackup(Properties properties) {
            super(properties);
    }
    
    public InteractionResultHolder<ItemStack> swapWithEquipmentSlot(@Nonnull Item item, @Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (EntitySizeUtils.getSize(player) > 0.5) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return swapWithEquipmentSlot(item, level, player, hand);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        if (!checkCorrectScale(entity)) {
            return false;
        }
        return ElytraItem.isFlyEnabled(stack);
    }

    private boolean checkCorrectScale(Entity ent){
        if (EntitySizeUtils.getSize(ent) >= Config.maxWingsScale) {
            return false;
        }
        return true;
    }


    public static boolean isFlyEnabled(ItemStack p_41141_) {
      return p_41141_.getDamageValue() < p_41141_.getMaxDamage() - 1;
   }

   public boolean isValidRepairItem(ItemStack p_41134_, ItemStack p_41135_) {
      return p_41135_.is(Items.PHANTOM_MEMBRANE);
   }

   public InteractionResultHolder<ItemStack> use(Level p_41137_, Player p_41138_, InteractionHand p_41139_) {
      return this.swapWithEquipmentSlot(this, p_41137_, p_41138_, p_41139_);
   }

   @Override
   public boolean elytraFlightTick(ItemStack stack, net.minecraft.world.entity.LivingEntity entity, int flightTicks) {
      if (!entity.level().isClientSide) {
         int nextFlightTick = flightTicks + 1;
         if (nextFlightTick % 10 == 0) {
            if (nextFlightTick % 20 == 0) {
               stack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.CHEST));
            }
            entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
         }
      }
      return true;
   }

   public SoundEvent getEquipSound() {
      return SoundEvents.ARMOR_EQUIP_ELYTRA;
   }

   public EquipmentSlot getEquipmentSlot() {
      return EquipmentSlot.CHEST;
   }
}
