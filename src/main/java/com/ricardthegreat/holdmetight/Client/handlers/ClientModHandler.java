package com.ricardthegreat.holdmetight.Client.handlers;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.Keybindings;
import com.ricardthegreat.holdmetight.Client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.Client.models.RayGunProjectileModel;
import com.ricardthegreat.holdmetight.Client.renderers.RayGunProjectileRenderer;
import com.ricardthegreat.holdmetight.Client.renderers.WandProjectileRenderer;
import com.ricardthegreat.holdmetight.entities.projectile.WandProjectile;
import com.ricardthegreat.holdmetight.init.EntityInit;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.LlamaSpitRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.INSTANCE.shoulderCarryKey);
        event.register(Keybindings.INSTANCE.customCarryKey);
    } 

    @SubscribeEvent
    public static void RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RAY_LAYER, RayGunProjectileModel::createBodyLayer);
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        
        EntityRenderers.register(EntityInit.RAY_GUN_PROJECTILE.get(), RayGunProjectileRenderer::new);
        EntityRenderers.register(EntityInit.WAND_PROJECTILE.get(), WandProjectileRenderer::new);
    }
    
}
