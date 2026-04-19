package com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SFeedPlayerPacket {
    private final ItemStack item; 
    private final UUID targetPlayer;

    public SFeedPlayerPacket(ItemStack item, UUID targetPlayer){
        this.item = item;
        this.targetPlayer = targetPlayer;
    }
    
    public SFeedPlayerPacket(FriendlyByteBuf buffer){
        this(buffer.readItem(), buffer.readUUID());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeItem(item);
        buffer.writeUUID(targetPlayer);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        Player sender = context.get().getSender();
        Level level = sender.level();
        Player target = level.getPlayerByUUID(targetPlayer);

        if (target != null) {
            item.finishUsingItem(level, target);
        }
    }
}
