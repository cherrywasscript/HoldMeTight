package com.ricardthegreat.holdmetight.client.handlers;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;

import java.util.Set;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.Keybindings;
import com.ricardthegreat.holdmetight.client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.client.models.RayGunProjectileModel;
import com.ricardthegreat.holdmetight.client.renderers.CollarRenderer;
import com.ricardthegreat.holdmetight.client.renderers.RayGunProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.WandProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.layers.PaperWingsLayer;
import com.ricardthegreat.holdmetight.init.EntityInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.items.CollarItem;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybindings.INSTANCE.shoulderCarryKey);
        event.register(Keybindings.INSTANCE.customCarryKey);
        event.register(Keybindings.INSTANCE.carryWheelKey);
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
    public static void onModelBakeEvent(ModelEvent.BakingCompleted event) {
        if (HoldMeTight.curiosInstalled) {
            ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(HoldMeTight.MODID, "collar_item"), "inventory");

            Object renderer = Class.forName("ricardthegreat.holdmetight.Client.renderers.CollarRenderer").getDeclaredConstructor(BakedModel.class).newInstance(event.getModels().get(location));
            //CollarRenderer renderer = new CollarRenderer(event.getModels().get(location));

            CuriosRendererRegistry.register(ItemInit.COLLAR_ITEM.get(), () -> (CollarRenderer) renderer);
        }
    }

    @SubscribeEvent
    public void registerItemColors(RegisterColorHandlersEvent.Item event){
        CollarItem collar = (CollarItem) ItemInit.COLLAR_ITEM.get();
        
        event.register(((itemStack, i) -> i == 0 ? collar.getColor(itemStack) : -1), collar);
        
    }

    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){ 
        EntityRenderers.register(EntityInit.RAY_GUN_PROJECTILE.get(), RayGunProjectileRenderer::new);
        EntityRenderers.register(EntityInit.WAND_PROJECTILE.get(), WandProjectileRenderer::new);
    }
}
