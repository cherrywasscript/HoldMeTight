package com.ricardthegreat.holdmetight.network;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.network.clientbound.CThrowEntityPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CThrowPlayerPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CPlayerDismountPlayerPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.size.CPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CEditCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.carrypositions.CRemoveCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SEntityPutDownPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SPlayerPutDownPacket;
import com.ricardthegreat.holdmetight.network.serverbound.SSizeRaySync;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.carry.SPlayerCarrySimplePacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.carry.SPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.capabilitySync.size.SPlayerSizeMixinSyncPacket;
import com.ricardthegreat.holdmetight.network.serverbound.carrypositions.SAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.carrypositions.SEditCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.carrypositions.SRemoveCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem.SOpenStandInItemMenuPacket;
import com.ricardthegreat.holdmetight.network.serverbound.scalepackets.SEntityAddTargetScalePacket;
import com.ricardthegreat.holdmetight.network.serverbound.scalepackets.SEntityMultTargetScalePacket;
import com.ricardthegreat.holdmetight.network.serverbound.scalepackets.SEntitySetTargetScalePacket;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(HoldMeTight.MODID, "main"),
        () -> PROTOCOL_VERSION, 
        PROTOCOL_VERSION::equals, 
        PROTOCOL_VERSION::equals
        );

    private static int id = 0;
   
    public static void register() {
        INSTANCE.messageBuilder(SEntityMultTargetScalePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(SEntityMultTargetScalePacket::encode)
            .decoder(SEntityMultTargetScalePacket::new)
            .consumerMainThread(SEntityMultTargetScalePacket::handle)
            .add();
        
        INSTANCE.messageBuilder(SEntitySetTargetScalePacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(SEntitySetTargetScalePacket::encode)
            .decoder(SEntitySetTargetScalePacket::new)
            .consumerMainThread(SEntitySetTargetScalePacket::handle)
            .add();

        INSTANCE.registerMessage(id++, SEntityAddTargetScalePacket.class, 
        SEntityAddTargetScalePacket::encode, 
        SEntityAddTargetScalePacket::new, 
        SEntityAddTargetScalePacket::handle);

        
        INSTANCE.registerMessage(id++, CPlayerDismountPlayerPacket.class, 
        CPlayerDismountPlayerPacket::encode, 
        CPlayerDismountPlayerPacket::new, 
        CPlayerDismountPlayerPacket::handle);

        INSTANCE.registerMessage(id++, SPlayerCarrySyncPacket.class, 
        SPlayerCarrySyncPacket::encode, 
        SPlayerCarrySyncPacket::new, 
        SPlayerCarrySyncPacket::handle);

        INSTANCE.registerMessage(id++, CPlayerCarrySyncPacket.class, 
        CPlayerCarrySyncPacket::encode, 
        CPlayerCarrySyncPacket::new, 
        CPlayerCarrySyncPacket::handle);

        INSTANCE.registerMessage(id++, CPlayerCarrySimplePacket.class, 
        CPlayerCarrySimplePacket::encode, 
        CPlayerCarrySimplePacket::new, 
        CPlayerCarrySimplePacket::handle);

        INSTANCE.registerMessage(id++, SEntityPutDownPacket.class, 
        SEntityPutDownPacket::encode, 
        SEntityPutDownPacket::new, 
        SEntityPutDownPacket::handle);

        INSTANCE.registerMessage(id++, SPlayerPutDownPacket.class, 
        SPlayerPutDownPacket::encode, 
        SPlayerPutDownPacket::new, 
        SPlayerPutDownPacket::handle);

        INSTANCE.registerMessage(id++, SSizeRaySync.class, 
        SSizeRaySync::encode, 
        SSizeRaySync::new, 
        SSizeRaySync::handle);

        INSTANCE.registerMessage(id++, SPlayerSizeMixinSyncPacket.class, 
        SPlayerSizeMixinSyncPacket::encode, 
        SPlayerSizeMixinSyncPacket::new, 
        SPlayerSizeMixinSyncPacket::handle);

        INSTANCE.registerMessage(id++, CPlayerSizeMixinSyncPacket.class, 
        CPlayerSizeMixinSyncPacket::encode, 
        CPlayerSizeMixinSyncPacket::new, 
        CPlayerSizeMixinSyncPacket::handle);

        INSTANCE.registerMessage(id++, SPlayerCarrySimplePacket.class, 
        SPlayerCarrySimplePacket::encode, 
        SPlayerCarrySimplePacket::new, 
        SPlayerCarrySimplePacket::handle);

        INSTANCE.registerMessage(id++, CAddPlayerCarrySyncPacket.class, 
        CAddPlayerCarrySyncPacket::encode, 
        CAddPlayerCarrySyncPacket::new, 
        CAddPlayerCarrySyncPacket::handle);

        INSTANCE.registerMessage(id++, CRemovePlayerCarrySyncPacket.class, 
        CRemovePlayerCarrySyncPacket::encode, 
        CRemovePlayerCarrySyncPacket::new, 
        CRemovePlayerCarrySyncPacket::handle);

        INSTANCE.registerMessage(id++, SAddCustomCarryPosPacket.class, 
        SAddCustomCarryPosPacket::encode, 
        SAddCustomCarryPosPacket::new, 
        SAddCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, SRemoveCustomCarryPosPacket.class, 
        SRemoveCustomCarryPosPacket::encode, 
        SRemoveCustomCarryPosPacket::new, 
        SRemoveCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, SEditCustomCarryPosPacket.class, 
        SEditCustomCarryPosPacket::encode, 
        SEditCustomCarryPosPacket::new, 
        SEditCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, CAddCustomCarryPosPacket.class, 
        CAddCustomCarryPosPacket::encode, 
        CAddCustomCarryPosPacket::new, 
        CAddCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, CRemoveCustomCarryPosPacket.class, 
        CRemoveCustomCarryPosPacket::encode, 
        CRemoveCustomCarryPosPacket::new, 
        CRemoveCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, CEditCustomCarryPosPacket.class, 
        CEditCustomCarryPosPacket::encode, 
        CEditCustomCarryPosPacket::new, 
        CEditCustomCarryPosPacket::handle);

        INSTANCE.registerMessage(id++, CThrowPlayerPacket.class, 
        CThrowPlayerPacket::encode, 
        CThrowPlayerPacket::new, 
        CThrowPlayerPacket::handle);

        INSTANCE.registerMessage(id++, CThrowEntityPacket.class, 
        CThrowEntityPacket::encode, 
        CThrowEntityPacket::new, 
        CThrowEntityPacket::handle);

        INSTANCE.registerMessage(id++, SOpenStandInItemMenuPacket.class, 
        SOpenStandInItemMenuPacket::encode, 
        SOpenStandInItemMenuPacket::new, 
        SOpenStandInItemMenuPacket::handle);
    }

    public static void sendToServer(Object msg){
        HoldMeTight.LOGGER.debug("PacketHandler sending packet to server");
        INSTANCE.sendToServer(msg);
    }

    //send directly to player
    public static void sendToPlayer(Object msg, Supplier<ServerPlayer> player) {
        HoldMeTight.LOGGER.debug("PacketHandler sending packet to player:" + player.get().getName());
        INSTANCE.send(PacketDistributor.PLAYER.with(player), msg);
    }

    public static void sendToAllClients(Object msg){
        HoldMeTight.LOGGER.debug("PacketHandler sending packet to all players");
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

}
