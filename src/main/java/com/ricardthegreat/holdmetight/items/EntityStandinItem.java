package com.ricardthegreat.holdmetight.items;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.client.renderers.HeldEntityItemRenderer;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EntityStandinItem extends Item implements ICurioItem{

    public static String ENTITY_UUID = "LINKED_ENTITY_UUID";
    public static String ENTITY_ID = "LINKED_ENTITY_ID";
    public static String INV_ID = "INVENTORY_ID";
    public static String SELECTED = "IS_SELECTED";
    public static String IS_PLAYER = "IS_PLAYER";

    private int prevSlot = -1;

    public EntityStandinItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(@Nonnull UseOnContext context) {
        //get the player using the item and fail if it doesnt exist
        Player vehicle = context.getPlayer();
        if (vehicle == null) {
            HoldMeTight.LOGGER.debug("Ent stand in item no vehicle");
            return InteractionResult.FAIL;
        }
        //get the item the player is holding
        ItemStack item = context.getItemInHand(); 

        //get the tag from the item and fail if it has no tag
        if (!item.hasTag()) {
            HoldMeTight.LOGGER.debug("Ent stand in item no tag");
            return InteractionResult.FAIL;
        }

        CompoundTag tag = item.getTag();


        //get all passengers from the player, this should be temporary as ill probably at some point have "passengers" that are not in the same dimension
        List<Entity> passengers = vehicle.getPassengers();

        if (passengers.size() == 0) {
            HoldMeTight.LOGGER.debug("Ent stand in item no passengers");
            return InteractionResult.FAIL; // fail if there are no passengers
        }

        //check if item entity is a passenger
        UUID id = tag.getUUID(ENTITY_UUID);
        Entity passenger = null;
        
        for(Entity entity : passengers){
            if (entity.getUUID().equals(id)) {
                passenger = entity;
            }
        }
        
        if (passenger == null) {
            HoldMeTight.LOGGER.debug("Ent stand in item passenger null");
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
            playerCarry.removeCarriedEntity(id);
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
            UUID id = tag.getUUID(ENTITY_UUID);
            Entity passenger = null;
            
            for(Entity pass : passengers){
                if (pass.getUUID().equals(id)) {
                    passenger = pass;
                }
            }
            
            if (passenger == null) {
                stack.shrink(1);
            }else if(entity instanceof Player vehicle){
                if (passenger instanceof Player carried) {
                    checkCorrectCarryPos(carried, vehicle, index, selected);
                }else{

                }
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

            Entity entity;

            //i have this in its current state for future proofing, maybe its not the best but im not too worried about non player entities getting desynced 
            //via something like id changing when im not expecting it while i do not want that to happen with actual player, also at some point i intend to
            //have players that are "carried" while being in different dimensions and therefore not actually riding anything
            if (tag.getBoolean(IS_PLAYER)) {
                entity = level.getPlayerByUUID(tag.getUUID(ENTITY_UUID));
            }else{
                entity = level.getEntity(tag.getInt(ENTITY_ID));
            }

            if (entity != null) {
                list.add(Component.literal("Size: " + EntitySizeUtils.getSize(entity)));
            }
        }
        super.appendHoverText(stack, level, list, flag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return HeldEntityItemRenderer.INSTANCE;
            }
        });
    }

    
    private void checkCorrectCarryPos(Player carried, Player player, int index, boolean selected){
        if (!player.level().isClientSide && index != prevSlot) {
            prevSlot = index;
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

            CompoundTag tag = new CompoundTag();
            tag.putUUID(ENTITY_UUID, carried.getUUID());
            tag.putInt(INV_ID, index);
            
            playerCarry.addOrUpdateCarriedEntity(tag);

            PacketHandler.sendToAllClients(new CAddPlayerCarrySyncPacket(tag, player.getUUID()));
        }
    }

    //I dont think this is proper form i should probably have a seperate static createPlayerItem in PlayerStandInItem
    //TODO ^that
    public static ItemStack createEntityItem(Entity entity){
        ItemStack item;
        CompoundTag tag;
        if (entity instanceof Player) {
            item = new ItemStack(ItemInit.PLAYER_ITEM.get());
            tag = item.getOrCreateTag();
            tag.putBoolean(IS_PLAYER, true);
        }else{
            item = new ItemStack(ItemInit.ENTITY_ITEM.get());
            tag = item.getOrCreateTag();
            tag.putBoolean(IS_PLAYER, false);
        }

        item.setHoverName(entity.getDisplayName());

        tag.putUUID(ENTITY_UUID, entity.getUUID());
        tag.putInt(ENTITY_ID, entity.getId());

        item.setTag(tag);
        
        return item;
    }
}
