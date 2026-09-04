package com.ricardthegreat.holdmetight.mixins.rendering;

import java.util.List;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.items.CollarItem;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

@Mixin(EntityRenderer.class)
public abstract class CollarNametagCustomMixin<T extends Entity> {
    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Shadow
    public abstract Font getFont();

     
    @ModifyVariable(at = @At("HEAD"), method = "renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    public Component renderNameTag(Component component, @Local Entity ent) {
        if (ent instanceof Player player) {
            ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElse(null);
            if (curios != null) {
                if (curios.isEquipped(ItemInit.COLLAR_ITEM.get())) {
                    MutableComponent comp = component.copy();
                    comp.append(" | Owned by: ");

                    ItemStack stack = curios.findCurios(ItemInit.COLLAR_ITEM.get()).get(0).stack();

                    List<Pair<UUID, String>> owners = CollarItem.getOwners(stack);

                    for (Pair<UUID,String> pair : owners) {
                        if (pair.getFirst().compareTo(player.getUUID()) == 0) {
                            return component;
                        }  
                    }

                    if (owners != null) {
                        comp.append(owners.get(0).getSecond());

                        for(int i = 1; i < owners.size(); i++){
                            comp.append(" and " + owners.get(i).getSecond());
                        }

                        component = comp;
                    }
                }
            }
            
            
            
        }

        return component;
    }
        


    //TODO try and make the owner stuff show up on a seperate line

    /* 
    @ModifyVariable(method = "renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("STORE"), cancellable = true)
    protected int adjustHeight(int i){
        EntityRendererMixin
        return i;
    }

    @Inject(method = "renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"), cancellable = true)
    protected void newText(T ent, Component component, PoseStack stack, MultiBufferSource source, int integer, CallbackInfo info){
    }
    */

    /* 
    @Overwrite
    protected void renderNameTag(T ent, Component component, PoseStack stack, MultiBufferSource source, int integer) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(ent);
        if (net.minecraftforge.client.ForgeHooksClient.isNameplateInRenderDistance(ent, d0)) {
            boolean flag = !ent.isDiscrete();
            float f = ent.getNameTagOffsetY();
            int i = "deadmau5".equals(component.getString()) ? -10 : 0;
            stack.pushPose();
            stack.translate(0.0F, f, 0.0F);
            stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            stack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = stack.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            Font font = this.getFont();
            float f2 = (float)(-font.width(component) / 2);

            ItemStack collar = hasCollar(ent);

            if (collar != null) {
                Component ownerComp = getOwnerComp(component, collar);

                font.drawInBatch(component, f2, (float)i-10, 553648127, false, matrix4f, source, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, integer);
                font.drawInBatch(ownerComp, f2, (float)i, 553648127, false, matrix4f, source, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, integer);
                if (flag) {
                    font.drawInBatch(component, f2, (float)i-10, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, integer);
                    font.drawInBatch(ownerComp, f2, (float)i, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, integer);
                }
            }else{
                font.drawInBatch(component, f2, (float)i, 553648127, false, matrix4f, source, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, integer);
                if (flag) {
                    font.drawInBatch(component, f2, (float)i, -1, false, matrix4f, source, Font.DisplayMode.NORMAL, 0, integer);
                }
            }

            stack.popPose();
        }
    }

    private ItemStack hasCollar(Entity ent){
        if (ent instanceof Player player) {
            ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElse(null);
            if (curios != null) {
                if (curios.isEquipped(ItemInit.COLLAR_ITEM.get())) {
                    return curios.findCurios(ItemInit.COLLAR_ITEM.get()).get(0).stack();
                }
            }
        }
        return null;
    }

    private Component getOwnerComp(Component component, ItemStack stack){
        MutableComponent comp = component.copy();
        comp.append(CommonComponents.NEW_LINE);
        comp.append("Owned by: ");

        List<Pair<UUID, String>> owners = CollarItem.getOwners(stack);

        if (owners != null) {
            comp.append(owners.get(0).getSecond());

            for(int i = 1; i < owners.size(); i++){
                comp.append(" and " + owners.get(i).getSecond());
            }

            component = comp;
        }

        return component;
    }
        */
}
