package com.ricardthegreat.holdmetight.mixins.carry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.utils.carry.carryPreferencesChecker;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Entity.class)
public class PickupEntityMixin {
    //allows for picking up entities when clicking on them
    @Inject(at = @At("HEAD"), method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void unnamedsizemod$interact(Player vehicle, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        Entity rider = (Entity) (Object) this;
        
        if(rider instanceof Player && vehicle.getMainHandItem().isEmpty() && carryPreferencesChecker.canPickup(vehicle, rider) && HMTConfig.SERVER_CONFIG.canPickupPlayers.get()){
            rider.startRiding(vehicle);
            
            

            ItemStack item = PlayerStandinItem.createEntityItem(vehicle, (Player) rider);
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
            info.setReturnValue(InteractionResult.sidedSuccess(vehicle.level().isClientSide));
            
        }else if (!(rider instanceof Player) && vehicle.getMainHandItem().isEmpty() && carryPreferencesChecker.canPickup(vehicle, rider) && HMTConfig.SERVER_CONFIG.canPickupEntities.get()) {
            rider.startRiding(vehicle);

            ItemStack item = EntityStandinItem.createEntityItem(vehicle, rider);
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
            info.setReturnValue(InteractionResult.sidedSuccess(vehicle.level().isClientSide));
        }
    }

    //dismounting players is desynced so this sends a packet from the server to all clients which should sync it up
    //it works in my testing but idk about at scale 
    @Inject(at = @At("HEAD"), method = "stopRiding()V")
    public void unnamedsizemod$dismount(CallbackInfo info){
        Entity ent = (Entity) (Object) this;
        Entity vehicle = ent.getVehicle();
        if(ent instanceof Player && ent.isPassenger() && vehicle != null && vehicle instanceof Player){
            if(!ent.level().isClientSide()) {
                PacketHandler.sendToAllClients(new CPlayerDismountPlayerPacket(ent.getUUID()));
            }
        }
    }   
}
