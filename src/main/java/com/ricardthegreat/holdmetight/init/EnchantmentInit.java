package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.effects.MassiveShrinkEffect;
import com.ricardthegreat.holdmetight.enchantments.ShrinkingEnchantment;
import com.ricardthegreat.holdmetight.enchantments.SizeStealEnchantment;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final DeferredRegister<Enchantment> EFFECTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, HoldMeTight.MODID);

    public static final RegistryObject<Enchantment> SHRINKING_ENCHANTMENT = EFFECTS.register("shrink", 
        () -> new ShrinkingEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static final RegistryObject<Enchantment> SIZE_STEALING_ENCHANTMENT = EFFECTS.register("sizesteal", 
        () -> new SizeStealEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    public static void register(IEventBus bus){
        EFFECTS.register(bus);
    }
}
