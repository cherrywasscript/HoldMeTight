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

    public static final RegistryObject<Potion> SHRINK_POTION = POTIONS.register("shrink_potion", 
        () -> new Potion(new MobEffectInstance(EffectsInit.SHRINK_EFFECT.get(), 1200, 0)));
    
    public static void register(IEventBus bus){
        POTIONS.register(bus);
    }
}
