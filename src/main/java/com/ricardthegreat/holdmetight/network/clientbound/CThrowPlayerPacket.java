package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.UUID;
import java.util.function.Supplier;

import org.joml.Vector3f;

import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CThrowPlayerPacket {

    private final UUID thrown;
    private final Vector3f speed;

    public CThrowPlayerPacket(UUID thrown, Vector3f speed){
        this.thrown = thrown;
        this.speed = speed;
    }

    public CThrowPlayerPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID(), buffer.readVector3f());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(thrown);
        buffer.writeVector3f(speed);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        

        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleThrowPlayerPacket(this, context))
        );
        
        context.get().setPacketHandled(true);

    }

    public Vector3f getMovement(){
        return this.speed;
    }

    public UUID getThrownId(){
        return this.thrown;
    }
    
}
