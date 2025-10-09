package com.ricardthegreat.holdmetight.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.renderers.ArmorRenderer;
import com.ricardthegreat.holdmetight.client.renderers.layers.CollarModelLayers;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.common.inventory.CurioSlot;

public class CollarItem extends Item implements DyeableLeatherItem, ICurioItem {

    public CollarItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return super.initCapabilities(stack, nbt);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag tag = stack.getTagElement("display");

        return tag != null && tag.contains("color", 99) ? tag.getInt("color") : MapColor.COLOR_RED.col;
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        List<MobEffectInstance> effects = getEffect(stack).getEffects();
        for(int i = 0; i < effects.size(); i++){
            slotContext.entity().addEffect(effects.get(i));
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (stack.getTagElement("owners") == null) {
            if (entity instanceof Player player) {
                setupNbt(stack, player);
            }
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player && player.getAbilities().instabuild) {
            return true;
        }
        return !getIsLocked(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @javax.annotation.Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        Pair<UUID, String> owner = getOwner(stack);
        if (owner != null) {
            components.add(Component.translatable("item.holdmetight.collar.owner", owner.getSecond()));
        }
        boolean locked = getIsLocked(stack);
        if (locked) {
            components.add(Component.translatable("item.holdmetight.collar.locked").withStyle(ChatFormatting.YELLOW));
        }else{
            components.add(Component.translatable("item.holdmetight.collar.unlocked").withStyle(ChatFormatting.BLUE));
        }
        
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        setupNbt(stack, player);
        super.onCraftedBy(stack, level, player);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack stack2, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY) {
            if (!(PotionUtils.getPotion(stack2) == Potions.EMPTY)) {
                setEffect(stack, PotionUtils.getPotion(stack2));
                Potion effect = getEffect(stack);
                System.out.println(effect.getName(""));
                return true;
            }

            if (!(slot instanceof CurioSlot)) {
                setLocked(stack);
                if (player.level().isClientSide) {
                    playSound();
                }
                return true;
            }else if (stack2.getItem() instanceof CollarKeyItem key) {
                if (key.getOwner(stack2).getFirst().compareTo(getOwner(stack).getFirst()) == 0) {
                    setLocked(stack);
                    if (player.level().isClientSide) {
                       playSound();
                    }
                    return true;
                }
            }
        }
        return super.overrideOtherStackedOnMe(stack, stack, slot, action, player, access);
    }
    
    //TODO get more than 1 owner
    public Pair<UUID, String> getOwner(ItemStack stack){
        CompoundTag tag = stack.getTagElement("owners");
        if (tag != null && tag.contains("uuid") && tag.contains("name")) {
            return new Pair<UUID,String>(tag.getUUID("uuid"), tag.getString("name"));
        }
        return null;
    }

    public void addOwner(ItemStack stack, @NotNull UUID uuid, @NotNull String name){
        CompoundTag tag = stack.getOrCreateTagElement("owners");

        tag.putInt("numOwners", tag.getInt("numOwners") + 1);
        tag.putUUID("uuid" + tag.getInt("numOwners"), uuid);
        tag.putString("name" + tag.getInt("numOwners"), name);
    }

    public void removeOwner(ItemStack stack, UUID uuid, String name){

    }

    public boolean getIsLocked(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTagElement("locked");
        return tag.getBoolean("isLocked");
    }

    public void setLocked(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTagElement("locked");
        tag.putBoolean("isLocked", !tag.getBoolean("isLocked"));
    }

    public void setEffect(ItemStack stack, Potion potion){
        CompoundTag tag = stack.getOrCreateTagElement("potion");
        tag.putString("effect", potion.getName(""));
    }

    public Potion getEffect(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTagElement("potion");
        Potion potion = Potion.byName(tag.getString("effect"));
        return potion;
    }

    private void setupNbt(ItemStack stack, Player player){
        CompoundTag tag = stack.getOrCreateTagElement("locked");
        tag.putBoolean("isLocked", false);

        addOwner(stack, player.getUUID(), player.getName().getString());
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(){
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
