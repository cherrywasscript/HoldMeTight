package com.ricardthegreat.unnamedsizemod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;
import com.ricardthegreat.unnamedsizemod.utils.PlayerRenderExtension;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.font.providers.UnihexProvider.Dimensions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;



@Mixin(EntityRenderer.class)
public abstract class SmallEntityRenderMixin<T extends Entity> {
	
	// probably shite and unoptimised but allows players to see entities that are smaller than normal
	//not sure if it works with zoom mods yet


	@Inject(at = @At("RETURN"), method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z")
	public boolean shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Dimensions> cir){

		if(FMLEnvironment.dist == Dist.CLIENT){
			double doub = 10D;

			return shouldRenderAtSqrDistance(doub, entity);
		}

		return cir.getReturnValueZ();
	}

	private boolean shouldRenderAtSqrDistance(double doub, Entity entity) {
		double d0 = entity.getBoundingBox().getSize() * 10.0D;
		if (Double.isNaN(d0)) {
		   d0 = 1.0D;
		}
  
		d0 *= 1000.0D * Entity.getViewScale();

		return doub < d0 * d0;
	}

	
	//@Inject(at = @At("HEAD"), method="render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
	@Overwrite
	public void render(T ent, float p_114486_, float p_114487_, PoseStack p_114488_, MultiBufferSource p_114489_, int p_114490_) {

		ClientLevel level = Minecraft.getInstance().level;

		if(level != null && level.isClientSide && ent instanceof PlayerCarryExtension){
			if(((PlayerRenderExtension) ent).getIsMenu()){
				LocalPlayer player = Minecraft.getInstance().player;
				if(player != null){
					ent = (T) player;
				}
			}
		}

		var renderNameTagEvent = new net.minecraftforge.client.event.RenderNameTagEvent(ent, ent.getDisplayName(),(EntityRenderer) (Object) this, p_114488_, p_114489_, p_114490_, p_114487_);
      	net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
      	if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(ent))) {
         	this.renderNameTag(ent, renderNameTagEvent.getContent(), p_114488_, p_114489_, p_114490_);
		}
			
		/* 
		if(ent instanceof Player){
			PlayerCarryExtension p = (PlayerCarryExtension) ent;
			if(p.getIsMenu()){
				ent.
				return;
			}
		}
		*/
   	}
 
	@Shadow
	protected abstract void renderNameTag(T p_114498_, Component p_114499_, PoseStack p_114500_, MultiBufferSource p_114501_, int p_114502_);

	@Shadow
	protected abstract boolean shouldShowName(T p_114504_);

	

}
