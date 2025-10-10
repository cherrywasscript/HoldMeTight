package com.ricardthegreat.holdmetight.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;
import com.ricardthegreat.holdmetight.client.ClientHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeableLeatherItem;
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
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.DistExecutor;
import top.theillusivec4.curios.api.SlotContext;
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
        List<MobEffectInstance> effects = getEffect(stack);
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
        Pair<UUID, String> owner = getFirstOwner(stack);
        if (owner != null) {
            components.add(Component.translatable("item.holdmetight.collar_item.owner", owner.getSecond()));
        }
        boolean locked = getIsLocked(stack);
        if (locked) {
            components.add(Component.translatable("item.holdmetight.collar_item.locked").withStyle(ChatFormatting.YELLOW));
        }else{
            components.add(Component.translatable("item.holdmetight.collar_item.unlocked").withStyle(ChatFormatting.BLUE));
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
            if (!(slot instanceof CurioSlot)) {

                if (!(PotionUtils.getPotion(stack2) == Potions.EMPTY)) {
                    setEffect(stack, PotionUtils.getPotion(stack2));
                    return true;
                }

                setLocked(stack);
                if (player.level().isClientSide) {
                    playSound(stack);
                }
                return true;
            }else if (stack2.getItem() instanceof CollarKeyItem key) {
                Pair<UUID, String> pair = key.getOwner(stack2);

                if (pair != null && pair.getFirst().compareTo(getFirstOwner(stack).getFirst()) == 0) {
                    setLocked(stack);
                    if (player.level().isClientSide) {
                       playSound(stack);
                    }
                    return true;
                }
            }
        }
        return super.overrideOtherStackedOnMe(stack, stack, slot, action, player, access);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openCollarScreen(player, hand));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
    
    //TODO get more than 1 owner
    public Pair<UUID, String> getFirstOwner(ItemStack stack){
        CompoundTag tag = stack.getTagElement("owners");
        if (tag != null && tag.contains("uuid"+0) && tag.contains("name"+0)) {
            return new Pair<UUID,String>(tag.getUUID("uuid"+0), tag.getString("name"+0));
        }
        return null;
    }

    public List<Pair<UUID, String>> getOwners(ItemStack stack){
        CompoundTag tag = stack.getTagElement("owners");

        List<Pair<UUID, String>> owners = new ArrayList<>();

        if (tag != null && tag.contains("numOwners")) {
            
            for(int i = 0; i < tag.getInt("numOwners"); i++){
                owners.add(new Pair<UUID,String>(tag.getUUID("uuid"+i), tag.getString("name"+i)));
            }

            return owners;
        }
        return null;
    }

    public void addOwner(ItemStack stack, @NotNull UUID uuid, @NotNull String name){
        CompoundTag tag = stack.getOrCreateTagElement("owners");

        tag.putUUID("uuid" + tag.getInt("numOwners"), uuid);
        tag.putString("name" + tag.getInt("numOwners"), name);
        tag.putInt("numOwners", tag.getInt("numOwners") + 1);
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
        stack.removeTagKey("potion");
        CompoundTag tag = stack.getOrCreateTagElement("potion");

        List<MobEffectInstance> effects = potion.getEffects();

        tag.putInt("numEffects", effects.size());

        for(int i = 0; i < effects.size(); i++){
            tag.putInt("effectId" + i, MobEffect.getId(effects.get(i).getEffect()));
        }
    }

    public List<MobEffectInstance> getEffect(ItemStack stack){
        CompoundTag tag = stack.getOrCreateTagElement("potion");
        List<MobEffectInstance> effects = new ArrayList<>();
        for(int i = 0; i < tag.getInt("numEffects"); i++){
            effects.add(new MobEffectInstance(MobEffect.byId(tag.getInt("effectId" + i)), 20));
        }
        return effects;
    }

    private void setupNbt(ItemStack stack, Player player){
        CompoundTag tag = stack.getOrCreateTagElement("locked");
        tag.putBoolean("isLocked", false);

        addOwner(stack, player.getUUID(), player.getName().getString());
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(ItemStack stack){
        if (getIsLocked(stack)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, 1.0F));
        }else{
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }
    }
}
