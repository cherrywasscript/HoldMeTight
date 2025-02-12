package com.ricardthegreat.unnamedsizemod.init;

import com.ricardthegreat.unnamedsizemod.UnnamedSizeMod;
import com.ricardthegreat.unnamedsizemod.entities.projectile.RayGunProjectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UnnamedSizeMod.MODID);

    public static final RegistryObject<EntityType<RayGunProjectile>> RAY_GUN_PROJECTILE =
    ENTITIES.register("ray_gun_projectile", () -> EntityType.Builder.<RayGunProjectile>of(RayGunProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build(new ResourceLocation(UnnamedSizeMod.MODID, "ray_gun_projectile").toString()));
    
}
