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

public class SPlayerDropItemPacket {

    CompoundTag stack;

    public SPlayerDropItemPacket(CompoundTag stack){
        this.stack = stack;
    }
    
    public SPlayerDropItemPacket(FriendlyByteBuf buffer){
        this.stack = buffer.readNbt();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeNbt(stack);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        Player sender = context.get().getSender();

        ItemStack item = ItemStack.of(stack);

        item.getItem().onDroppedByPlayer(item, sender);
    }
}
