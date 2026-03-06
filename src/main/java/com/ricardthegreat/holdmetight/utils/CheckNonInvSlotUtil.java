package com.ricardthegreat.holdmetight.utils;

import java.util.UUID;

import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.utils.constants.CarryPosConstants;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class CheckNonInvSlotUtil {

    //this exists as i cant create a wrapper variable within a mixin class, is this the right way to do it? who knows definitely not me but it works and thats what matters

    //TODO figure out if getorcreatetag is the correct method, might be better to use gettag and have a null check to ensure the item actually has a tag instead
    public static String checkIfNonInvSlot(Player vehicle, Entity rider){
        var wrapper = new Object(){ boolean checkPassed = false; String strReturn = null; int count = 0;};

        CuriosApi.getCuriosInventory(vehicle)
        .ifPresent(handler -> handler.findCurios((stack) -> stack.getItem() == ItemInit.PLAYER_ITEM.get() || stack.getItem() == ItemInit.ENTITY_ITEM.get()).forEach((slotResult) -> {
            UUID id = slotResult.stack().getOrCreateTag().getUUID(EntityStandinItem.ENTITY_UUID);
            if (rider.getUUID().equals(id)) {
                wrapper.checkPassed = true;
                System.out.println(slotResult.slotContext().identifier());
                wrapper.strReturn = slotResult.slotContext().identifier();
            }
        }));

        if (wrapper.checkPassed) {
            return wrapper.strReturn;
        }

        

        if (checkCorrectItem(vehicle.getOffhandItem(), rider)) {
            return CarryPosConstants.OFF_HAND;
        }else if (checkCorrectItem(vehicle.getMainHandItem(), rider)) {
            return CarryPosConstants.MAIN_HAND;
        }else {
            return "none";
        }
    }

    public static boolean checkCorrectItem(ItemStack stack, Entity rider){
        if (stack.getItem() instanceof EntityStandinItem) {
            UUID id = stack.getOrCreateTag().getUUID(EntityStandinItem.ENTITY_UUID);
            if (rider.getUUID().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
