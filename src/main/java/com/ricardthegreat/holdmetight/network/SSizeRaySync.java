package com.ricardthegreat.holdmetight.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.items.SizeRay;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SSizeRaySync {

    private final CompoundTag tag;

    // for scaleType 0 - sets target,  1- mults target
    //probably gonna add more, not sure on the default yet maybe just setting to 1
    public SSizeRaySync(CompoundTag tag){
        this.tag = tag;
    }
    
    public SSizeRaySync(FriendlyByteBuf buffer){
        this(buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeNbt(tag);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        ItemStack item = context.get().getSender().getItemInHand(InteractionHand.MAIN_HAND);
        if(item.getItem() instanceof SizeRay){
            item.setTag(tag);
        }
    }
}
