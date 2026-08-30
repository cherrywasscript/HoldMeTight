package com.ricardthegreat.holdmetight.enchantments;

import com.ricardthegreat.holdmetight.init.ItemInit;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ShrinkingCurseEnchantment extends Enchantment{

    public ShrinkingCurseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slot) {
        super(rarity, category, slot);
    }

    public int getMinCost(int p_44616_) {
        return 25;
    }

    public int getMaxCost(int p_44619_) {
        return 50;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return true;
    }

    public boolean canEnchant(ItemStack stack) {
        return stack.is(ItemInit.COLLAR_ITEM.get());
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchant) {
        return enchant instanceof GrowingCurseEnchantment ? false : super.checkCompatibility(enchant);
    }
}
