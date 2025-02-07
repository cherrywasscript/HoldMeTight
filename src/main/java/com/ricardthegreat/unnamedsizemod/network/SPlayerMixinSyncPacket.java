package com.ricardthegreat.unnamedsizemod.network;

import java.util.function.Supplier;


import com.ricardthegreat.unnamedsizemod.utils.PlayerCarryExtension;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class SPlayerMixinSyncPacket {

    private final int rotation;
    private final double distance;
    private final double height;
    private final double leftRight;
    private final boolean carried;
    private final boolean carrying;
    private final boolean shoulder;
    private final boolean custom;

    public SPlayerMixinSyncPacket(int rotation, double distance, double height, double leftRight, boolean carried, boolean carrying, boolean shoulder, boolean custom){
        this.rotation = rotation;
        this.distance = distance;
        this.height = height;
        this.leftRight = leftRight;
        this.carried = carried;
        this.carrying = carrying;
        this.shoulder = shoulder;
        this.custom = custom;

    }
    
    public SPlayerMixinSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(rotation);
        buffer.writeDouble(distance);
        buffer.writeDouble(height);
        buffer.writeDouble(leftRight);
        buffer.writeBoolean(carried);
        buffer.writeBoolean(carrying);
        buffer.writeBoolean(shoulder);
        buffer.writeBoolean(custom);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        
        ServerPlayer player = context.get().getSender();
        
        PlayerCarryExtension carry = (PlayerCarryExtension) player;

        if(carry != null){
            carry.updateSyncables(rotation, distance, height, leftRight, 
                carried, carrying, shoulder, custom);
            //carry.setRotationOffset(rotation);
            //carry.setXYMult(distance);
            PacketHandler.sendToAllClients(new CPlayerMixinSyncPacket(rotation, distance, height, leftRight, 
                carried, carrying, shoulder, custom, player.getUUID()));
        }

    }
    
}
