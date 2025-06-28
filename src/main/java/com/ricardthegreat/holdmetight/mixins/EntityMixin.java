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
import com.ricardthegreat.holdmetight.network.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;


//TODO make not pushed by fluids when big (IFORGEENTITY)
@Mixin(Entity.class)
public abstract class EntityMixin {

    private double vertOffset = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    //allows for picking up entities when clicking on them
    //currently only works on players
    @Inject(at = @At("HEAD"), method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void unnamedsizemod$interact(Player vehicle, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        Entity rider = (Entity) (Object) this;
        PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);
        
        if (vehicleCarry.getIsCarrying()) {
            if (vehicle.getPassengers().size() == 0) {
                vehicleCarry.setCarrying(false);
            }
        }else if(rider instanceof Player && vehicle.getMainHandItem() == ItemStack.EMPTY && EntitySizeUtils.getSize(rider) <= EntitySizeUtils.getSize(vehicle)/4){

            PlayerCarry riderCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) rider);

            rider.startRiding(vehicle);
            
            riderCarry.setCarried(true);
            vehicleCarry.setCarrying(true);

            if(!rider.level().isClientSide()) {
                riderCarry.setShouldSyncSimple(true);
                vehicleCarry.setShouldSyncSimple(true);

                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, rider.getUUID(), (byte) 0));
                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, vehicle.getUUID(), (byte) 1));
            }
            

            //when i get round to doing item holding this works im pretty sure
            /* 
            ItemStack item = new ItemStack(ItemInit.PLAYER_ITEM.get());
            item.setHoverName(Component.literal("hello"));
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
            */
            
        }else if (!(rider instanceof Player) && vehicle.getMainHandItem() == ItemStack.EMPTY && EntitySizeUtils.getSize(rider) <= EntitySizeUtils.getSize(vehicle)/4) {

            rider.startRiding(vehicle);

            vehicleCarry.setCarrying(true);

            if(!rider.level().isClientSide()) {
                vehicleCarry.setShouldSyncSimple(true);
            }
        }
    }


    //dismounting players is desynced so this sends a packet from the server to all clients which should sync it up
    //it works in my testing but idk about at scale
    @Inject(at = @At("HEAD"), method = "stopRiding()V")
    public void unnamedsizemod$dismount(CallbackInfo info){
        Entity ent = (Entity) (Object) this;
        Entity vehicle = ent.getVehicle();
        if(ent instanceof Player && ent.isPassenger() && vehicle != null && vehicle instanceof Player){
            PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) vehicle);
            PlayerCarry riderCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) ent);


            vehicleCarry.setCarrying(false);
            riderCarry.setCarried(false);

            if(!ent.level().isClientSide()) {
                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(false, ent.getUUID(), (byte) 0));
                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(false, vehicle.getUUID(), (byte) 1));

                vehicleCarry.setShouldSyncSimple(true);
                riderCarry.setShouldSyncSimple(true);
                PacketHandler.sendToAllClients(new CPlayerDismountPlayerPacket(ent.getUUID()));
            }
        }else if (!(ent instanceof Player) && ent.isPassenger() && vehicle != null && vehicle instanceof Player) {
            PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability((Player) vehicle);
            vehicleCarry.setCarrying(false);
            if(!ent.level().isClientSide()) {
                vehicleCarry.setShouldSyncSimple(true);
                //PacketHandler.sendToAllClients(new CPlayerDismountPlayerPacket(ent.getUUID()));
            }
        }
    }

    //@Inject(at = @At("RETURN"), method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity/MoveFunction;)V")
    @Overwrite
    protected void positionRider(Entity rider, Entity.MoveFunction func) {
        Entity vehicle = (Entity) (Object) this;
        if (vehicle.hasPassenger(rider)) {
            if(vehicle instanceof Player player){
                double scaleDif =  EntitySizeUtils.getSize(vehicle)/EntitySizeUtils.getSize(rider);
                
                PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(player);

                //find the riders position
                if(scaleDif<4){
                    rider.stopRiding();
                // && pExt.getCustomCarry()
                }else if (vehicleCarry.getCarryPosition().headLink) {
                    calcHeadPosition(player, rider);
                    func.accept(rider, vehicle.getX()+xOffset, vertOffset, vehicle.getZ()+yOffset);
                }else{
                    calcBodyPosition(player, rider);
                    func.accept(rider, vehicle.getX()+xOffset, vertOffset, vehicle.getZ()+yOffset);
                }
            }else{

                vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset();
                func.accept(rider, vehicle.getX(), vertOffset, vehicle.getZ());

            }
         }
    }

    //need to check the scale of the rider and the vehicle and move accordingly
    // if rider is above 0.25 set them to just being on head like normal probably
    //on shoulder rider should get lower e.g. 0.125 should have a larger vertical offset tho im not sure by how much yet

    private void calcBodyPosition(Player vehicle, Entity rider){
        
        PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);
        CarryPosition carryPos = vehicleCarry.getCarryPosition();

        if(vehicle.getMainHandItem() != ItemStack.EMPTY && carryPos.posName == "hand"){
            vehicleCarry.setCarryPosition(false, 1);

            if(!vehicle.level().isClientSide){
                vehicleCarry.setShouldSyncSimple(true);
                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, vehicle.getUUID(), (byte) 2));
            }
        }

        vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset() - (carryPos.vertOffset*EntitySizeUtils.getSize(vehicle));

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

    private void calcHeadPosition(Player vehicle, Entity rider){
        PlayerCarry vehicleCarry = PlayerCarryProvider.getPlayerCarryCapability(vehicle);
        CarryPosition carryPos = vehicleCarry.getCarryPosition();

        if(vehicle.getMainHandItem() != ItemStack.EMPTY && carryPos.posName == "hand"){
            vehicleCarry.setCarryPosition(false, 1);

            if(!vehicle.level().isClientSide){
                vehicleCarry.setShouldSyncSimple(true);
                //PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, vehicle.getUUID(), (byte) 2));
            }
        }

        Vec3 vec = vehicle.getLookAngle();

        double offsetAddition = carryPos.vertOffset*EntitySizeUtils.getSize(vehicle)*Math.abs(vec.y - 1);
        vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset() - offsetAddition;

        
        //this doesnt really function how i'd like

        double degrees = vehicle.yHeadRotO + carryPos.RotationOffset;

        double leftRightOffset = Math.toRadians((degrees+90)%360);

        double x = Math.cos(leftRightOffset) * carryPos.leftRightMove;
        double y = Math.sin(leftRightOffset) * carryPos.leftRightMove;

        xOffset = vec.x*carryPos.xymult+x;
        yOffset = vec.z*carryPos.xymult+y;

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

    @Overwrite
    public boolean fireImmune() {
        if (EntitySizeUtils.getSize(((Entity) (Object) this)) >= 4) {
            return true;
        }
        return ((Entity) (Object) this).getType().fireImmune();
    }

}
