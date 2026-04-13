package com.ricardthegreat.holdmetight.client.handlers;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import java.util.Set;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.Keybindings;
import com.ricardthegreat.holdmetight.client.guielements.tooltips.ClientPlayerItemTooltipComponent;
import com.ricardthegreat.holdmetight.client.guielements.tooltips.PlayerItemTooltip;
import com.ricardthegreat.holdmetight.client.models.ModModelLayers;
import com.ricardthegreat.holdmetight.client.models.RayGunProjectileModel;
import com.ricardthegreat.holdmetight.client.renderers.CollarRenderer;
import com.ricardthegreat.holdmetight.client.renderers.RayGunProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.WandProjectileRenderer;
import com.ricardthegreat.holdmetight.client.renderers.layers.PaperWingsLayer;
import com.ricardthegreat.holdmetight.client.screens.HeldPlayerInvScreen;
import com.ricardthegreat.holdmetight.init.EntityInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.init.MenuInit;
import com.ricardthegreat.holdmetight.items.CollarItem;
import com.ricardthegreat.holdmetight.items.PaperWingsItem;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.client.ClientEventHandler;
import top.theillusivec4.curios.client.IconHelper;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.common.CuriosRegistry;

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
        HoldMeTight.LOGGER.info("MODEL_BAKE_EVENT");

        ModelResourceLocation location = new ModelResourceLocation(new ResourceLocation(HoldMeTight.MODID, "collar_item"), "inventory");

        CollarRenderer renderer = new CollarRenderer(event.getModels().get(location));

        CuriosRendererRegistry.register(ItemInit.COLLAR_ITEM.get(), () -> renderer);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        CollarItem collar = (CollarItem) ItemInit.COLLAR_ITEM.get();
        
        event.register(((itemStack, i) -> i == 0 ? collar.getColor(itemStack) : -1), collar);
    }

    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){ 
        EntityRenderers.register(EntityInit.RAY_GUN_PROJECTILE.get(), RayGunProjectileRenderer::new);
        EntityRenderers.register(EntityInit.WAND_PROJECTILE.get(), WandProjectileRenderer::new);

        MenuScreens.register(MenuInit.HELD_PLAYER_MENU.get(), HeldPlayerInvScreen::new);

        event.enqueueWork(() -> {
            ItemProperties.register(ItemInit.PAPER_WINGS_ITEM.get(), new ResourceLocation("broken"), (stack, lebel, entity, seed) -> PaperWingsItem.isFlyEnabled(stack) ? 0 : 1);
        });
    }

        
    @SubscribeEvent
    public static void registerToolTip(RegisterClientTooltipComponentFactoriesEvent event){
        event.register(PlayerItemTooltip.class, t -> new ClientPlayerItemTooltipComponent(t));
    }

}
