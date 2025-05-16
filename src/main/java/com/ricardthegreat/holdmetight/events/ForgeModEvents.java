package com.ricardthegreat.holdmetight.events;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import virtuoel.pehkui.api.PehkuiConfig;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeModEvents {
    
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerSizeProvider.PLAYER_SIZE).isPresent()) {
                event.addCapability(new ResourceLocation(HoldMeTight.MODID, "size"), new PlayerSizeProvider());
            }

            if(!event.getObject().getCapability(PlayerCarryProvider.PLAYER_CARRY).isPresent()) {
                event.addCapability(new ResourceLocation(HoldMeTight.MODID, "carry"), new PlayerCarryProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps(); //need this as death removes caps from what i've read
            event.getOriginal().getCapability(PlayerSizeProvider.PLAYER_SIZE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerSizeProvider.PLAYER_SIZE).ifPresent(newStore -> {
                    if (PehkuiConfig.COMMON.keepAllScalesOnRespawn.get()) {
                        newStore.copyAll(oldStore);
                    }else{
                        newStore.copyBasic(oldStore);
                    }
                });
            });
            event.getOriginal().getCapability(PlayerCarryProvider.PLAYER_CARRY).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerCarryProvider.PLAYER_CARRY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
            event.getOriginal().invalidateCaps();//reinvalidating the caps after doing what i need
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSize.class);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event){
        event.getEntity().getCapability(PlayerSizeProvider.PLAYER_SIZE).ifPresent(capability -> {
            if (!event.getEntity().level().isClientSide) {
                PacketHandler.sendToAllClients(capability.getSyncPacket(event.getEntity()));
            }
        });
    }

}
