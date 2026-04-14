package com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.inventory.HeldEntityInventoryProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SOpenStandInItemMenuPacket {

    private final UUID accessedPlayer;

    public SOpenStandInItemMenuPacket(UUID accessedPlayer){
        this.accessedPlayer = accessedPlayer;
    }
    
    public SOpenStandInItemMenuPacket(FriendlyByteBuf buffer){
        this(buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeUUID(accessedPlayer);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        Player sender = context.get().getSender();
        Level level = sender.level();
        Player accessed = level.getPlayerByUUID(accessedPlayer);

        sender.openMenu(new HeldEntityInventoryProvider(accessed));
    }
}
