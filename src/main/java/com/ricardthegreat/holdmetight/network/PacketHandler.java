package com.ricardthegreat.holdmetight.network;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;

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

        INSTANCE.registerMessage(id++, CPlayerSizeMixinSyncPacket.class, 
        CPlayerSizeMixinSyncPacket::encode, 
        CPlayerSizeMixinSyncPacket::new, 
        CPlayerSizeMixinSyncPacket::handle);

        INSTANCE.registerMessage(id++, SPlayerCarrySimplePacket.class, 
        SPlayerCarrySimplePacket::encode, 
        SPlayerCarrySimplePacket::new, 
        SPlayerCarrySimplePacket::handle);
    }

    public static void sendToServer(Object msg){
        INSTANCE.sendToServer(msg);
    }

    //send directly to player
    public static void sendToPlayer(Object msg, Supplier<ServerPlayer> player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(player), msg);
    }

    public static void sendToAllClients(Object msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

}
