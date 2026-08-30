package com.ricardthegreat.holdmetight.enchantments;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;

public class SizeStealEnchantment extends Enchantment{
    public SizeStealEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slot) {
        super(rarity, category, slot);
    }
    
    public int getMinCost(int i) {
        return i * 25;
    }

    public int getMaxCost(int i) {
        return this.getMinCost(i) + 50;
    }

    public int getMaxLevel() {
        return 5;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean canEnchant(ItemStack p_44642_) {
        return p_44642_.getItem() instanceof AxeItem ? true : super.canEnchant(p_44642_);
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant) {
        return enchant instanceof ShrinkingEnchantment ? false : super.checkCompatibility(enchant);
    }

    //not sure if i can outright stop an attack from doing damage in enchants so this is called from onlivingdamage event listener which also then sets the damage to 0
    public static void doSteal(LivingEntity attacked, Entity attacker, float damage, int level) {
        //this is like way over complicated i could fully just use 3, 2 or even just 1 variable here
        float shrinkPercentage = 0.5f*level;
        float shrinkDecimal = (100-shrinkPercentage)/100;
        float shrinkRatio = (float) Math.pow(shrinkDecimal, damage);
        float currentSize = EntitySizeUtils.getSize(attacked);
        float shrinkAmount = currentSize * shrinkRatio;
        float heightChange = currentSize - shrinkAmount; 

        if (attacker instanceof Player p) {
            EntitySizeUtils.addSize(p, attacked, -heightChange);

            if (EntitySizeUtils.willChangeBeAllowed(p, attacked)) {
                EntitySizeUtils.addSize(null, attacker, heightChange);
            }
        }else{
            EntitySizeUtils.addSize(null, attacked, -heightChange);

            EntitySizeUtils.addSize(null, attacker, heightChange);
        }
        
    }
}
