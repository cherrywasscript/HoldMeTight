package com.ricardthegreat.holdmetight.items;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.client.guielements.tooltips.PlayerItemTooltip;
import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem.SOpenStandInItemMenuPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlayerStandinItem extends EntityStandinItem{

    public static String INVENTORY = "Inventory";

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
                    ListTag savedInv = tag.getList(INVENTORY, 10);
                    ListTag playerInv = player.getInventory().save(new ListTag());

                    if (!savedInv.equals(playerInv)) {
                        tag.put(INVENTORY, playerInv);
                    }
                }
            }
            
        }
    }

    @Override
    public boolean overrideOtherStackedOnMe (ItemStack stackThis, ItemStack stackOther, Slot slot, ClickAction action, Player player, SlotAccess access) {
        if (action == ClickAction.SECONDARY) {

            if (stackOther.isEmpty()) {
                CompoundTag tag = stackThis.getTag();
                Level level = player.level();
                Player representation = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));
                if (representation != null) {
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
                }
            
                return true;
            }
        }
        return super.overrideOtherStackedOnMe(stackThis, stackOther, slot, action, player, access);
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
            ListTag invList = tag.getList(INVENTORY, 10);

            
            Player player = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));

            if (player != null) {
                Inventory inv = new Inventory(player);

                inv.load(invList);
                

                return Optional.of(new PlayerItemTooltip(inv.items, inv.armor, inv.offhand, player));
            }
            
        }
        
        return super.getTooltipImage(stack);
    }
}
