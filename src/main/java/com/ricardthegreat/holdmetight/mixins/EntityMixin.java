package com.ricardthegreat.holdmetight.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.utils.CheckNonInvSlotUtil;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


//TODO make not pushed by fluids when big (IFORGEENTITY)
@Mixin(Entity.class)
public abstract class EntityMixin {

    private double vertOffset = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    private boolean hidden = false;

    //allows for picking up entities when clicking on them
    //currently only works on players
    @Inject(at = @At("HEAD"), method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void unnamedsizemod$interact(Player vehicle, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        Entity rider = (Entity) (Object) this;
        
        if(rider instanceof Player && vehicle.getMainHandItem().isEmpty() && EntitySizeUtils.getSize(rider) <= EntitySizeUtils.getSize(vehicle)/4){
            rider.startRiding(vehicle);
            
            

            ItemStack item = PlayerStandinItem.createEntityItem((Player) rider);
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
            
        }else if (!(rider instanceof Player) && vehicle.getMainHandItem().isEmpty() && EntitySizeUtils.getSize(rider) <= EntitySizeUtils.getSize(vehicle)/4) {
            rider.startRiding(vehicle);

            ItemStack item = EntityStandinItem.createEntityItem(rider);
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
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

    
    //@Overwrite
    @Inject(at = @At("HEAD"), method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", cancellable = true)
    protected void positionRider(Entity rider, Entity.MoveFunction func, CallbackInfo info) {
        Entity vehicle = (Entity) (Object) this;
        if (vehicle.hasPassenger(rider)) {
            if(vehicle instanceof Player playerV){
                double scaleDif =  EntitySizeUtils.getSize(vehicle)/EntitySizeUtils.getSize(rider);
                if(scaleDif<4){
                    rider.stopRiding();
                }
                
                calcBodyPosition(playerV, rider);
                func.accept(rider, vehicle.getX()+xOffset, vertOffset, vehicle.getZ()+yOffset);

                info.cancel();
            }
         }
    }

    //need to check the scale of the rider and the vehicle and move accordingly
    // if rider is above 0.25 set them to just being on head like normal probably
    //on shoulder rider should get lower e.g. 0.125 should have a larger vertical offset tho im not sure by how much yet

    private void calcBodyPosition(Player vehicle, Entity rider){
        //this is rider

        PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);

        CarryPosition carryPos = vehicleCarry.getCarryPosition(rider, CheckNonInvSlotUtil.checkIfNonInvSlot(vehicle, rider));
            

        vertOffset = vehicle.getY() + ((vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset())/PlayerSizeUtils.getHitboxScalingFactor(vehicle)) - (carryPos.vertOffset*EntitySizeUtils.getSize(vehicle));
        
        
        double degrees = vehicle.yBodyRotO + carryPos.RotationOffset;
        

        double rotation = Math.toRadians(degrees%360);

        double leftRightOffset = Math.toRadians((degrees+90)%360);

        //i have it as y bc im thinking like graph coords but really it should be z
        double x = Math.cos(leftRightOffset) * carryPos.leftRightMove;
        double y = Math.sin(leftRightOffset) * carryPos.leftRightMove;

        xOffset = (Math.cos(rotation)*carryPos.xymult)+x;
        yOffset = (Math.sin(rotation)*carryPos.xymult)+y;

        xOffset *= EntitySizeUtils.getSize(vehicle);
        yOffset *= EntitySizeUtils.getSize(vehicle);
    }


    
    







    /*
     * fixing pushing for smaller and larger entities
     */

    @Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    public void push(Entity entity, CallbackInfo info) {
         
        Entity thisEnt = (Entity) (Object) this;
        float scaleDif = EntitySizeUtils.getSize(entity)/EntitySizeUtils.getSize(thisEnt);


        if (scaleDif <= 0.5 || scaleDif >= 2) {
            info.cancel();
        }  
    }

    @Inject(at = @At("HEAD"), method = "canBeHitByProjectile()Z", cancellable = true)
    public void canBeHitByProjectile(CallbackInfoReturnable<Boolean> info){
        if (hidden) {
            //info.setReturnValue(false);
        }
    }

    @Overwrite
    public boolean fireImmune() {
        if (EntitySizeUtils.getSize(((Entity) (Object) this)) >= 4) {
            return true;
        }
        return ((Entity) (Object) this).getType().fireImmune();
    }

}
