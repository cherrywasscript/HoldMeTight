package com.ricardthegreat.holdmetight.items;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class CollarKeyItem extends Item{

    private InteractionResult interactResult;

    public CollarKeyItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity instanceof Player target) {
            
            interactResult = InteractionResult.FAIL;

            CuriosApi.getCuriosInventory(target).ifPresent(handler -> handler.getStacksHandler("collar").ifPresent(stacksHandler -> {
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        for (int i = 0; i < stackHandler.getSlots(); i++) {
                            ItemStack collarStack = stackHandler.getStackInSlot(i);
                            if (collarStack.getItem() instanceof CollarItem item) {
                                interactResult = unlockCollar(stack, collarStack);
                            }
                        }

                    }));

            return interactResult;
        }
        return super.interactLivingEntity(stack, player, entity, hand);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        addOwner(stack, player.getUUID(), player.getName().getString());
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        CompoundTag tag = stack.getTagElement("owner");
        if (tag == null) {
            if (entity instanceof Player player) {
                addOwner(stack, player.getUUID(), player.getName().getString());
            }
        }
        
        super.inventoryTick(stack, level, entity, slot, selected);
    }
    
    //TODO get more than 1 owner
    public Pair<UUID, String> getOwner(ItemStack stack){
        CompoundTag tag = stack.getTagElement("owner");
        if (tag != null && tag.contains("uuid") && tag.contains("name")) {
            return new Pair<UUID,String>(tag.getUUID("uuid"), tag.getString("name"));
        }
        return null;
    }

    public void addOwner(ItemStack stack, @NotNull UUID uuid, @NotNull String name){
        CompoundTag tag = stack.getOrCreateTagElement("owner");

        tag.putUUID("uuid", uuid);
        tag.putString("name", name);
    }

    private InteractionResult unlockCollar(ItemStack key, ItemStack collarStack){
        CollarItem collar = (CollarItem) collarStack.getItem();

        Pair<UUID, String> owner = collar.getOwner(collarStack);
        if (owner != null && owner.getFirst().compareTo(getOwner(key).getFirst()) == 0) {
            collar.setLocked(collarStack);

            if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.isClientSide) {
                 playSound(collarStack);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(ItemStack stack){
        CollarItem collar = (CollarItem) stack.getItem();
        if (collar.getIsLocked(stack)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, 1.0F));
        }else{
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}
