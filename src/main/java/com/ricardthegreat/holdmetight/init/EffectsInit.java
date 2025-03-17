package com.ricardthegreat.holdmetight.init;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.effects.GrowEffect;
import com.ricardthegreat.holdmetight.effects.MassiveShrinkEffect;
import com.ricardthegreat.holdmetight.effects.ShrinkEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectsInit {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HoldMeTight.MODID);

    public static final RegistryObject<MobEffect> MASSIVE_SHRINK_EFFECT = EFFECTS.register("massive_shrink", 
        () -> new MassiveShrinkEffect(MobEffectCategory.NEUTRAL, 0xb2ed00));

    public static final RegistryObject<MobEffect> SHRINK_EFFECT = EFFECTS.register("shrink", 
        () -> new ShrinkEffect(MobEffectCategory.NEUTRAL, 0x36ebab));

    public static final RegistryObject<MobEffect> GROW_EFFECT = EFFECTS.register("grow", 
        () -> new GrowEffect(MobEffectCategory.NEUTRAL, 0xd629a9));

    public static void register(IEventBus bus){
        EFFECTS.register(bus);
    }

}
