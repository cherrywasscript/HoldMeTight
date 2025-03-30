package com.ricardthegreat.holdmetight.Client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.init.ItemInit;

import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

public class PaperWingsLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T,M>{

    private static final ResourceLocation WINGS_LOCATION = new ResourceLocation(HoldMeTight.MODID, "textures/entity/paper_wings_item.png");
    private final ElytraModel<T> paperWingsModel;

    public PaperWingsLayer(RenderLayerParent<T, M> p_174493_, EntityModelSet p_174494_) {
        super(p_174493_, p_174494_);
        //this.paperWingsModel = new PaperWingsModel<>(p_174494_.bakeLayer(ModModelLayers.PAPER_WINGS_LAYER));
        this.paperWingsModel = new ElytraModel<>(p_174494_.bakeLayer(ModelLayers.ELYTRA));
    }

    public void render(PoseStack pose, MultiBufferSource source, int p_116953_, T p_116954_, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
      ItemStack itemstack = p_116954_.getItemBySlot(EquipmentSlot.CHEST);
      if (shouldRender(itemstack, p_116954_)) {
         ResourceLocation resourcelocation;
         if (p_116954_ instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_116954_;
            if (abstractclientplayer.isElytraLoaded() && abstractclientplayer.getElytraTextureLocation() != null) {
               resourcelocation = abstractclientplayer.getElytraTextureLocation();
            } else if (abstractclientplayer.isCapeLoaded() && abstractclientplayer.getCloakTextureLocation() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
               resourcelocation = abstractclientplayer.getCloakTextureLocation();
            } else {
               resourcelocation = getElytraTexture(itemstack, p_116954_);
            }
         } else {
            resourcelocation = getElytraTexture(itemstack, p_116954_);
         }

         pose.pushPose();
         pose.translate(0.0F, 0.0F, 0.125F);
         this.getParentModel().copyPropertiesTo(this.paperWingsModel);
         this.paperWingsModel.setupAnim(p_116954_, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
         VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(resourcelocation), false, itemstack.hasFoil());
         this.paperWingsModel.renderToBuffer(pose, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
         pose.popPose();
      }
   }

   public boolean shouldRender(ItemStack stack, T entity) {
      return stack.getItem() == ItemInit.PAPER_WINGS_ITEM.get();
   }

   public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
      return WINGS_LOCATION;
   }
    
}
