package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

@Mixin(BrushableBlock.class)
public abstract class TinyPlayersBrushBlocks extends BaseEntityBlock{
    protected TinyPlayersBrushBlocks(Properties p_49224_) {super(p_49224_);}

    private int useTime = 0;

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (EntitySizeUtils.getSize(player) <= 0.25) {
            if (useTime >= 0) {
                int i = 200 - useTime + 1;
                boolean flag = i % 20 == 5;
                if (flag) {
                    level.playSound(player, pos, this.getBrushSound(), SoundSource.BLOCKS);
                    if (!level.isClientSide()) {
                            BlockEntity blockentity = level.getBlockEntity(pos);
                            if (blockentity instanceof BrushableBlockEntity) {
                                BrushableBlockEntity brushableblockentity = (BrushableBlockEntity)blockentity;
                                boolean flag1 = brushableblockentity.brush(level.getGameTime(), player, result.getDirection());
                            }
                    }
                }

                useTime--;
            }else{
                useTime = 200;
            }
        }

        return super.use(state, level, pos, player, hand, result);
    }

    @Shadow
    abstract public SoundEvent getBrushSound();
}
