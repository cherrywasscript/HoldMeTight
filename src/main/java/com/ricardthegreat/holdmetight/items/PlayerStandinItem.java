package com.ricardthegreat.holdmetight.items;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.plugins.tiff.TIFFDirectory;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.init.BlockInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CUsePlayerItemPacket;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinTextureCallback;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import virtuoel.pehkui.mixin.ItemEntityMixin;

public class PlayerStandinItem extends Item {

    public static String PLAYER_UUID = "LINKED_PLAYER_UUID";
    public static String INV_ID = "INVENTORY_ID";
    public static String SELECTED = "IS_SELECTED";

    private int prevSlot = -1;

    public PlayerStandinItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(@Nonnull UseOnContext context) {
        //get the player using the item and fail if it doesnt exist
        Player vehicle = context.getPlayer();
        if (vehicle == null) {
            System.out.println("no vehicle");
            return InteractionResult.FAIL;
        }

        //get the item the player is holding
        ItemStack item = context.getItemInHand(); 

        //get the tag from the item and fail if it has no tag
        if (!item.hasTag()) {
            System.out.println("no tag");
            return InteractionResult.FAIL;
        }

        CompoundTag tag = item.getTag();


        //get all passengers from the player, this should be temporary as ill probably at some point have "passengers" that are not in the same dimension
        List<Entity> passengers = vehicle.getPassengers();

        if (passengers.size() == 0) {
            System.out.println("no passengers");
            return InteractionResult.FAIL; // fail if there are no passengers
        }

        //check if item entity is a passenger
        UUID id = tag.getUUID(PLAYER_UUID);
        Entity passenger = null;
        
        for(Entity entity : passengers){
            if (entity.getUUID().equals(id)) {
                passenger = entity;
            }
        }
        
        if (passenger == null) {
            System.out.println("passenger null");
            return InteractionResult.FAIL;
        }

        //send packet to clients as this should be run serverside
        //if (!context.getLevel().isClientSide) {
        //    passenger.dismountTo(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z);
        //}
        
        
        if (!context.getLevel().isClientSide) {
            passenger.dismountTo(context.getClickLocation().x, context.getClickLocation().y, context.getClickLocation().z);
        }else{
            passenger.stopRiding();
        }
        
            
        if (vehicle.getMainHandItem().is(this)) {
            item.shrink(1);
        }else if (vehicle.getOffhandItem().is(this)) {
            item.shrink(1);
        }

        if (!context.getLevel().isClientSide) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);
            playerCarry.removeCarriedPlayer(id);
            PacketHandler.sendToAllClients(new CRemovePlayerCarrySyncPacket(id, vehicle.getUUID()));
        }
        
            
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int index, boolean selected) {
        //get the tag from the item and fail if it has no tag, really shouldnt need this? but like idk better than having accidental null pointer crashes
        if (stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            //get all passengers from the player, this should be temporary as ill probably at some point have "passengers" that are not in the same dimension
            List<Entity> passengers = entity.getPassengers();

            //check if item entity is a passenger
            @SuppressWarnings("null") //ive already checked if it is null or not with if stack.hasTag()
            UUID id = tag.getUUID(PLAYER_UUID);
            Entity passenger = null;
            
            for(Entity pass : passengers){
                if (pass.getUUID().equals(id)) {
                    passenger = pass;
                }
            }
            
            if (passenger == null) {
                stack.shrink(1);
            }else if(passenger instanceof Player carried && entity instanceof Player player){
                checkCorrectCarryPos(carried, player, index, selected);
            }
            
        }
        super.inventoryTick(stack, level, entity, index, selected);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.discard();

        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();

        if (level != null && tag != null) {
            Player player = level.getPlayerByUUID(tag.getUUID(PLAYER_UUID));

            if (player != null) {
                list.add(Component.literal("Size: " + PlayerSizeUtils.getSize(player)));
            }
        }
        super.appendHoverText(stack, level, list, flag);
    }

    private void checkCorrectCarryPos(Player carried, Player player, int index, boolean selected){
        if (!player.level().isClientSide && index != prevSlot) {
            prevSlot = index;
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            CompoundTag tag = new CompoundTag();
            tag.putUUID(PLAYER_UUID, carried.getUUID());
            tag.putInt(INV_ID, index);
            
            playerCarry.addOrUpdateCarriedPlayer(tag);

            PacketHandler.sendToAllClients(new CAddPlayerCarrySyncPacket(tag, player.getUUID()));
        }
    }

    public static ItemStack createPlayerItem(Player player){
        ItemStack item = new ItemStack(ItemInit.PLAYER_ITEM.get());

        item.setHoverName(player.getDisplayName());

        CompoundTag tag = item.getOrCreateTag();

        tag.putUUID(PLAYER_UUID, player.getUUID());

        item.setTag(tag);
        
        return item;
    }
}
