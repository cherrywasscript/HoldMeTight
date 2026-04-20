package com.ricardthegreat.holdmetight.network.serverbound.itempackets.standinitem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SApplyPlayerEffectPacket {

    private final List<MobEffectInstance> effects; 
    private final UUID targetPlayer;

    public SApplyPlayerEffectPacket(List<MobEffectInstance> effects, UUID targetPlayer){
        this.effects = effects;
        this.targetPlayer = targetPlayer;
    }
    
    public SApplyPlayerEffectPacket(FriendlyByteBuf buffer){
        int listSize = buffer.readInt();
        if (listSize >= 1) {
            this.effects = new ArrayList<>();
        }else{
            this.effects = List.of();
        }
        
        for(int i = 0; i < listSize; i++){
            MobEffectInstance temp = MobEffectInstance.load(buffer.readNbt());
            effects.add(temp);
        }
        
        this.targetPlayer = buffer.readUUID();
    }

    public void encode(FriendlyByteBuf buffer){
        buffer.writeInt(effects.size());

        for(int i = 0; i < effects.size(); i++){
            CompoundTag tag = new CompoundTag();
            effects.get(i).save(tag);

            buffer.writeNbt(tag);
        }

        buffer.writeUUID(targetPlayer);
    }

    public void handle(Supplier<NetworkEvent.Context> context){
        Player sender = context.get().getSender();
        Level level = sender.level();
        Player target = level.getPlayerByUUID(targetPlayer);

        if (target != null) {
            target.playSound(SoundEvents.AMBIENT_UNDERWATER_EXIT);
            if (effects.size() >= 1) {
                for(MobEffectInstance mobeffectinstance : effects) {
                    if (mobeffectinstance.getEffect().isInstantenous()) {
                        mobeffectinstance.getEffect().applyInstantenousEffect(sender, sender, target, mobeffectinstance.getAmplifier(), 1.0D);
                    } else {
                        target.addEffect(new MobEffectInstance(mobeffectinstance));
                    }
                }
            }else{
                target.removeAllEffects();
            }
        }
    }
}
