package com.ricardthegreat.holdmetight.items;

import java.util.List;
import java.util.Optional;

import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.client.guielements.tooltips.PlayerItemTooltip;
import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem.SApplyPlayerEffectPacket;
import com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem.SFeedPlayerPacket;
import com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem.SOpenStandInItemMenuPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class PlayerStandinItem extends EntityStandinItem{

    public static String INVENTORY = "Inventory";
    public static String CURIOS = "Curios";

    public PlayerStandinItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int index, boolean selected) {
        super.inventoryTick(stack, level, entity, index, selected);

        if (!entity.level().isClientSide) {
            CompoundTag tag = stack.getTag();
            if (tag != null) {
                Player player = entity.level().getPlayerByUUID(tag.getUUID(ENTITY_UUID));

                if (player != null) {
                    ListTag savedInv = tag.getList(INVENTORY, Tag.TAG_COMPOUND);
                    ListTag playerInv = player.getInventory().save(new ListTag());

                    if (!savedInv.equals(playerInv)) {
                        tag.put(INVENTORY, playerInv);
                    }

                    ListTag savedCurios = tag.getList(CURIOS, Tag.TAG_COMPOUND);
                    ICuriosItemHandler curioHandler = CuriosApi.getCuriosInventory(player).orElse(null);
                    if (curioHandler != null ) {
                        ListTag playerCurios = curioHandler.saveInventory(false);
                        if (!savedCurios.equals(playerCurios)) {
                            tag.put(CURIOS, playerCurios);
                        }
                    }
                }
            }
            
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe (ItemStack stackThis, ItemStack stackOther, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY) {
            CompoundTag tag = stackThis.getTag();
            Level level = player.level();
            Player representation = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));
            if (representation != null) {
                if (stackOther.isEmpty()) {
                    boolean canViewInv = PlayerPreferencesProvider.getPlayerPreferencesCapability(representation).getInventoryCanBeAccessed();
                    if (canViewInv) {
                        if (!level.isClientSide) {
                            if (tag != null) {
                                player.openMenu(new HeldEntityInventoryProvider(representation));
                            }
                        }else{
                            PacketHandler.sendToServer(new SOpenStandInItemMenuPacket(representation.getUUID()));
                        }
                    }
                    return true;
                }else if(stackOther.getItem() instanceof PotionItem){
                    if (player.level().isClientSide) {
                        if (!(PotionUtils.getPotion(stackOther) == Potions.EMPTY)) {
                            Potion potion = PotionUtils.getPotion(stackOther);
                            List<MobEffectInstance> effects = potion.getEffects();
                            PacketHandler.sendToServer(new SApplyPlayerEffectPacket(effects, representation.getUUID()));
                        }
                    }
                    //playSound();
                    return true;
                }else if (stackOther.getItem() instanceof MilkBucketItem) {
                    if (player.level().isClientSide) {
                        PacketHandler.sendToServer(new SApplyPlayerEffectPacket(List.of(), representation.getUUID()));
                    }
                    //playSound();
                    return true;
                }else if (stackOther.isEdible()) {
                    if (player.level().isClientSide) {
                        PacketHandler.sendToServer(new SFeedPlayerPacket(stackOther.copy(), representation.getUUID()));
                    }
                    return true;
                }
            }
        }
        return super.overrideOtherStackedOnMe(stackThis, stackOther, slot, action, player, access);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stackThis, Slot slot, ClickAction action, Player player) {
        if (action == ClickAction.SECONDARY) {
            ItemStack stackOther = slot.getItem();
            CompoundTag tag = stackThis.getTag();
            Level level = player.level();
            Player representation = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));
            if (representation != null && !stackOther.isEmpty()) {
                if (stackOther.getItem() instanceof PotionItem) {
                    if (!player.level().isClientSide) {
                        //this will never be called
                        //i have no idea why
                        //from what i can tell it **should** be called on both the server and the client
                        //but it is only ever called on the client
                        //its really annoying
                        //i dont want to have to keep making packets just to perform on stack events
                    }else{
                        if (!(PotionUtils.getPotion(stackOther) == Potions.EMPTY)) {
                            Potion potion = PotionUtils.getPotion(stackOther);
                            List<MobEffectInstance> effects = potion.getEffects();
                            PacketHandler.sendToServer(new SApplyPlayerEffectPacket(effects, representation.getUUID()));
                        }
                    }
                    //playSound();
                    return true;
                }else if (stackOther.getItem() instanceof MilkBucketItem) {
                    if (player.level().isClientSide) {
                        PacketHandler.sendToServer(new SApplyPlayerEffectPacket(List.of(), representation.getUUID()));
                    }
                    //playSound();
                    return true;
                }else if (stackOther.isEdible()) {
                    if (player.level().isClientSide) {
                        PacketHandler.sendToServer(new SFeedPlayerPacket(stackOther.copy(), representation.getUUID()));
                    }
                    return true;
                }
            }
        }
        return super.overrideStackedOnOther(stackThis, slot, action, player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        Level level = Minecraft.getInstance().level;

        if (level == null) {
            return super.getTooltipImage(stack);
        }

        if (tag != null && Minecraft.getInstance().level.isClientSide) {
            ListTag invList = tag.getList(INVENTORY, Tag.TAG_COMPOUND);
            ListTag curioList = tag.getList(CURIOS, Tag.TAG_COMPOUND);

            
            Player player = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));

            if (player != null) {
                Inventory inv = new Inventory(player);
                inv.load(invList);
                
                ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElse(null);
                curios.loadInventory(curioList);

                
                return Optional.of(new PlayerItemTooltip(inv.items, inv.armor, inv.offhand, player, curios));
            }
            
        }
        
        return super.getTooltipImage(stack);
    }

    @OnlyIn(Dist.CLIENT)
    private void playSound(){
        //Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.AMBIENT_UNDERWATER_EXIT, 1.0F));
    }
}
