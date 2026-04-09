package com.ricardthegreat.holdmetight.network.clientbound;

import java.util.function.Supplier;

import org.joml.Vector3f;

import com.ricardthegreat.holdmetight.client.handlers.ClientPacketHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class CThrowEntityPacket {

    private final int thrown;
    private final Vector3f speed;

    public CThrowEntityPacket(int thrown, Vector3f speed){
        this.thrown = thrown;
        this.speed = speed;
    }

    public CThrowEntityPacket(FriendlyByteBuf buffer){
        this(buffer.readInt(), buffer.readVector3f());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(thrown);
        buffer.writeVector3f(speed);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        

        context.get().enqueueWork(() -> 
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handleThrowEntityPacket(this, context))
        );
        
        context.get().setPacketHandled(true);

    }

    public Vector3f getMovement(){
        return this.speed;
    }

    public int getThrownId(){
        return this.thrown;
    }
    
}
