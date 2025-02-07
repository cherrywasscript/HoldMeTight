package com.ricardthegreat.unnamedsizemod.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ricardthegreat.unnamedsizemod.network.CPlayerCarryPositionPacket;
import com.ricardthegreat.unnamedsizemod.network.CPlayerDismountPlayerPacket;
import com.ricardthegreat.unnamedsizemod.network.PacketHandler;
import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;
import com.ricardthegreat.unnamedsizemod.utils.SizeUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


@Mixin(Entity.class)
public abstract class EntityMixin {

    private double vertOffset = 0;
    private double xOffset = 0;
    private double yOffset = 0;

    //allows for picking up entities when clicking on them
    //currently only works on players
    @Inject(at = @At("HEAD"), method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;")
    public void unnamedsizemod$interact(Player vehicle, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
        Entity rider = (Entity) (Object) this;
        
        if(rider instanceof Player && vehicle.getMainHandItem() == ItemStack.EMPTY && SizeUtils.getSize(rider) <= SizeUtils.getSize(vehicle)/4){

            PlayerCarryExtension vehicleExt = (PlayerCarryExtension) vehicle;
            PlayerCarryExtension riderExt = (PlayerCarryExtension) rider;

            rider.startRiding(vehicle);
            
            riderExt.setCarried(true);

            vehicleExt.setCarrying(true);

            if(!rider.level().isClientSide()) {
                //riderExt.setShouldSync(true);
                //vehicleExt.setShouldSync(true);  

                PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, rider.getUUID(), (byte) 0));
                PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, vehicle.getUUID(), (byte) 1));
            }
            
            //when i get round to doing item holding this works im pretty sure


            /* 
            ItemStack item = new ItemStack(ItemInit.PLAYER_ITEM.get());
            item.setHoverName(Component.literal("hello"));
            vehicle.getInventory().add(vehicle.getInventory().selected, item);
            */
            
        }
        
        //e.stopRiding();;
        //System.out.println("Clicked on entity");
    }



    
    /* 
    @Inject(at = @At("HEAD"), method = "saveWithoutId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;")
    private void saveWithoutId(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info) {
        Entity ent = (Entity) (Object) this;

        if(ent instanceof Player){
            PlayerCarryExtension player = (PlayerCarryExtension) ent;
            player.writeCarryNbt(tag);
        }
    }

    @Inject(at = @At("HEAD"), method = "load(Lnet/minecraft/nbt/CompoundTag;)V")
    public void load(CompoundTag tag, CallbackInfo info) {
        Entity ent = (Entity) (Object) this;

        if(ent instanceof Player){
            PlayerCarryExtension player = (PlayerCarryExtension) ent;
            player.readCarryNbt(tag);
        }
    }
    */




    //dismounting players is desynced so this sends a packet from the server to all clients which should sync it up
    //it works in my testing but idk about at scale
    @Inject(at = @At("HEAD"), method = "stopRiding()V")
    public void unnamedsizemod$dismount(CallbackInfo info){
        Entity ent = (Entity) (Object) this;
        Entity vehicle = ent.getVehicle();
        if(ent instanceof Player && ent.isPassenger() && vehicle != null && vehicle instanceof Player){

            PlayerCarryExtension player = (PlayerCarryExtension) vehicle;
            PlayerCarryExtension entity = (PlayerCarryExtension) ent;
            
            entity.setCarried(false);
            player.setCarrying(false);

            if(!ent.level().isClientSide()) {
                //entity.setShouldSync(true);
                //player.setShouldSync(true);
                System.out.println("stop riding");

                PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(false, ent.getUUID(), (byte) 0));
                PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(false, vehicle.getUUID(), (byte) 1));
                PacketHandler.sendToAllClients(new CPlayerDismountPlayerPacket(ent.getUUID()));
            }
        }
    }

    //@Inject(at = @At("RETURN"), method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity/MoveFunction;)V")
    @Overwrite
    protected void positionRider(Entity rider, Entity.MoveFunction p_19958_) {
        Entity vehicle = (Entity) (Object) this;
        if (vehicle.hasPassenger(rider)) {
            if(vehicle instanceof Player){
                double scaleDif =  SizeUtils.getSize(vehicle)/SizeUtils.getSize(rider);
                //scaleDif = scaleDif/4;
                if(scaleDif<4){
                    rider.stopRiding();
                    //vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset();
                    //p_19958_.accept(rider, vehicle.getX(), vertOffset, vehicle.getZ());
                }else{
                    calcPosition((Player)vehicle, rider);
                    p_19958_.accept(rider, vehicle.getX()+xOffset, vertOffset, vehicle.getZ()+yOffset);
                }
            }else{

                vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset();
                p_19958_.accept(rider, vehicle.getX(), vertOffset, vehicle.getZ());

            }
         }
    }

    //need to check the scale of the rider and the vehicle and move accordingly
    // if rider is above 0.25 set them to just being on head like normal probably
    //on shoulder rider should get lower e.g. 0.125 should have a larger vertical offset tho im not sure by how much yet

    private void calcPosition(Player vehicle, Entity rider){

        PlayerCarryExtension vehiclePlayer = (PlayerCarryExtension)vehicle;

        if(vehicle.getMainHandItem() != ItemStack.EMPTY && !vehiclePlayer.getShoulderCarry() && !vehiclePlayer.getCustomCarry()){
            vehiclePlayer.setShoulderCarry(true);

            if(!vehicle.level().isClientSide){
                PacketHandler.sendToAllClients(new CPlayerCarryPositionPacket(true, vehicle.getUUID(), (byte) 2));
                //vechicleExt.setShouldSync(true);
            }
        }

        SizeUtils.getSize(rider);

        vertOffset = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset() - (vehiclePlayer.getVertOffset()*SizeUtils.getSize(vehicle));

        double degrees = vehicle.yBodyRotO + vehiclePlayer.getRotationOffset();

        double rotation = Math.toRadians(degrees%360);

        double leftRightOffset = Math.toRadians((degrees+90)%360);

        double x = Math.cos(leftRightOffset) * vehiclePlayer.getLeftRightMove();
        double y = Math.sin(leftRightOffset) * vehiclePlayer.getLeftRightMove();

        xOffset = (Math.cos(rotation)*vehiclePlayer.getXYMult())+x;
        yOffset = (Math.sin(rotation)*vehiclePlayer.getXYMult())+y;

        xOffset *= SizeUtils.getSize(vehicle);
        yOffset *= SizeUtils.getSize(vehicle);
    }

    
}
