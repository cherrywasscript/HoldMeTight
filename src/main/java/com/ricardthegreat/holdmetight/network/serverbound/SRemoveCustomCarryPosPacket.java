package com.ricardthegreat.holdmetight.network.serverbound;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.carry.CarryPosition;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CAddCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemoveCustomCarryPosPacket;
import com.ricardthegreat.holdmetight.utils.constants.CurioUUIDConstants;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

public class SRemoveCustomCarryPosPacket {
    private final String pos;

    public SRemoveCustomCarryPosPacket(String pos){
        this.pos = pos;
    }
    
    public SRemoveCustomCarryPosPacket(FriendlyByteBuf buffer){
        this.pos = buffer.readUtf();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUtf(pos);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ServerPlayer player = context.get().getSender();

        PlayerCarry carry = PlayerCarryProvider.getPlayerCarryCapability(player);

        ICuriosItemHandler curiosInventory = CuriosApi.getCuriosInventory(player).resolve().get();
        curiosInventory.removeSlotModifier("custom", UUID.nameUUIDFromBytes(pos.getBytes()));
        //curiosInventory.addTransientSlotModifier("custom", UUID.nameUUIDFromBytes(CurioUUIDConstants.CUSTOM_UUID.getBytes()), CurioUUIDConstants.CUSTOM_UUID, -1, AttributeModifier.Operation.ADDITION);

        if(carry != null){
            carry.removeCustomCarryPos(pos);

            PacketHandler.sendToAllClients(new CRemoveCustomCarryPosPacket(pos, player.getUUID()));
        }
    }
}
