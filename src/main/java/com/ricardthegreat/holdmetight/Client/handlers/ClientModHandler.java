package com.ricardthegreat.holdmetight.client.handlers;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;

import java.util.Set;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.Keybindings;
import com.ricardthegreat.holdmetight.client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.client.models.RayGunProjectileModel;
import com.ricardthegreat.holdmetight.client.renderers.RayGunProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.WandProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.layers.PaperWingsLayer;
import com.ricardthegreat.holdmetight.init.EntityInit;

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
        event.register(Keybindings.INSTANCE.sizePrefsKey);
        event.register(Keybindings.INSTANCE.carryScreenKey);
    } 

    @SubscribeEvent
    public static void RegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RAY_LAYER, RayGunProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterModelLayers(EntityRenderersEvent.AddLayers event) {
        Set<String> skins = event.getSkins();
        for (String skin : skins) {
            try {
                LivingEntityRenderer<Player, EntityModel<Player>> renderer = event.getSkin(skin);
                if (renderer != null) {
                    renderer.addLayer(new PaperWingsLayer<>(renderer, event.getEntityModels()));
                }
            } catch (Exception e) {
            }
        }
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){ 
        EntityRenderers.register(EntityInit.RAY_GUN_PROJECTILE.get(), RayGunProjectileRenderer::new);
        EntityRenderers.register(EntityInit.WAND_PROJECTILE.get(), WandProjectileRenderer::new);
    }
}
