package com.ricardthegreat.holdmetight.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HeldEntityItemRenderer extends BlockEntityWithoutLevelRenderer{

    private final SkullModel model;

    public static final HeldEntityItemRenderer INSTANCE = new HeldEntityItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    public HeldEntityItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
        model = new SkullModel(set.bakeLayer(ModelLayers.PLAYER_HEAD));
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource source, int int0, int int1) {
        //run through checks ensuring that the item exists, is the correct item and that the entity attached to the item exists
        if (itemStack.getCount() <= 0) {
            return;
        }
        
        if (!(itemStack.getItem() instanceof EntityStandinItem)) {
            return;
        }

        CompoundTag tag = itemStack.getTag();
        if (tag == null) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        if (itemStack.getItem() instanceof PlayerStandinItem) {
            if (Minecraft.getInstance().getConnection() == null) {
                return;
            }
            PlayerInfo info = Minecraft.getInstance().getConnection().getPlayerInfo(tag.getUUID(EntityStandinItem.ENTITY_UUID));
            if (info == null) {
                return;
            }

            switch (context) {
                case GUI:
                    poseStack.pushPose();
                    poseStack.translate(0.5, 0, 0);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                    poseStack.scale(2, 2, 2);
                    VertexConsumer consumer = source.getBuffer(model.renderType(info.getSkinLocation()));
                    model.renderToBuffer(poseStack, consumer, int0, int1, 1, 1, 1, 1);
                    poseStack.popPose();
                    break;

                default:
                    break;
            }
            
        }else{
            Entity entity = player.level().getEntity(tag.getInt(EntityStandinItem.ENTITY_ID));

            if (entity == null) {
                return;
            }
            
            EntityRenderer<? super Entity> render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
            
            switch (context) {
                case GUI:
                    poseStack.pushPose();
                    poseStack.translate(0.5, 0, 0);
                    //rotate on the x and z axis so it is actually facing towards the screen
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(180));

                    if (render instanceof LivingEntityRenderer livingRender) {
                        Model entModel = livingRender.getModel();
                        //check if it is a humanoid mob because if so we can just use the skull model
                        if (entModel instanceof HumanoidModel) {
                            poseStack.scale(2, 2, 2);
                            entModel = model;
                            //check if it is a skeleton because they are already rotated the correct way and therefore need to be rotated back
                            if (entModel instanceof SkeletonModel) {
                                poseStack.mulPose(Axis.YP.rotationDegrees(180));
                                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                            }
                        }else{
                            poseStack.scale(0.75f, 0.75f, 0.75f);
                            poseStack.translate(0.5, -1, 0);
                        }
                        //render using existing models so i dont have to implement anything myself
                        VertexConsumer consumer = source.getBuffer(entModel.renderType(render.getTextureLocation(entity)));
                        entModel.renderToBuffer(poseStack, consumer, int0, int1, 1, 1, 1, 1);
                        poseStack.popPose();
                    }
                    break;

                default:
                    break;
            }
        }

        
        // TODO Auto-generated method stub
        super.renderByItem(itemStack, context, poseStack, source, int0, int1);
    }
    
}
