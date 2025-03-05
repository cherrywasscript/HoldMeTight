package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionsInit {
    

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, HoldMeTight.MODID);

    public static final RegistryObject<Potion> MASSIVE_SHRINK_POTION = POTIONS.register("massive_shrink_potion", 
        () -> new Potion(new MobEffectInstance(EffectsInit.MASSIVE_SHRINK_EFFECT.get(), 200, 0)));
    public static final RegistryObject<Potion> MASSIVE_SHRINK_POTION_LONG = POTIONS.register("massive_shrink_potion_long", 
        () -> new Potion("massive_shrink_potion", new MobEffectInstance(EffectsInit.MASSIVE_SHRINK_EFFECT.get(), 600, 0)));

    //shrinking potions, 5 strength levels with 2 time levels for each this could be way too much but yeah
    public static final RegistryObject<Potion> SHRINK_POTION = POTIONS.register("shrink_potion", 
        () -> new Potion(new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 0)));
    public static final RegistryObject<Potion> SHRINK_POTION_LONG = POTIONS.register("shrink_potion_long", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 6000, 0)));
    
    public static final RegistryObject<Potion> SHRINK_POTION_1 = POTIONS.register("shrink_potion_1", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 1)));
    public static final RegistryObject<Potion> SHRINK_POTION_1_LONG = POTIONS.register("shrink_potion_1_long", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 6000, 1)));
    
    public static final RegistryObject<Potion> SHRINK_POTION_2 = POTIONS.register("shrink_potion_2", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 2)));
    public static final RegistryObject<Potion> SHRINK_POTION_2_LONG = POTIONS.register("shrink_potion_2_long", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 6000, 2)));

    public static final RegistryObject<Potion> SHRINK_POTION_3 = POTIONS.register("shrink_potion_3", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 3)));
    public static final RegistryObject<Potion> SHRINK_POTION_3_LONG = POTIONS.register("shrink_potion_3_long", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 6000, 3)));

    public static final RegistryObject<Potion> SHRINK_POTION_4 = POTIONS.register("shrink_potion_4", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 4)));
    public static final RegistryObject<Potion> SHRINK_POTION_4_LONG = POTIONS.register("shrink_potion_4_long", 
        () -> new Potion("shrink_potion", new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 6000, 4)));



    //growth potions, 5 strength levels with 2 time levels for each this could be way too much but yeah
    public static final RegistryObject<Potion> GROW_POTION = POTIONS.register("grow_potion", 
        () -> new Potion(new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 1200, 0)));
    public static final RegistryObject<Potion> GROW_POTION_LONG = POTIONS.register("grow_potion_long", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 6000, 0)));

    public static final RegistryObject<Potion> GROW_POTION_1 = POTIONS.register("grow_potion_1", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 1200, 1)));
    public static final RegistryObject<Potion> GROW_POTION_1_LONG = POTIONS.register("grow_potion_1_long", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 6000, 1)));

    public static final RegistryObject<Potion> GROW_POTION_2 = POTIONS.register("grow_potion_2", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 1200, 2)));
    public static final RegistryObject<Potion> GROW_POTION_2_LONG = POTIONS.register("grow_potion_2_long", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 6000, 2)));

    public static final RegistryObject<Potion> GROW_POTION_3 = POTIONS.register("grow_potion_3", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 1200, 3)));
    public static final RegistryObject<Potion> GROW_POTION_3_LONG = POTIONS.register("grow_potion_3_long", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 6000, 3)));

    public static final RegistryObject<Potion> GROW_POTION_4 = POTIONS.register("grow_potion_4", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 1200, 4)));
    public static final RegistryObject<Potion> GROW_POTION_4_LONG = POTIONS.register("grow_potion_4_long", 
        () -> new Potion("grow_potion", new MobEffectInstance(EffectsInit.GROW_EFFECT.get(), 6000, 4)));


    public static void register(IEventBus bus){
        POTIONS.register(bus);
    }
}
