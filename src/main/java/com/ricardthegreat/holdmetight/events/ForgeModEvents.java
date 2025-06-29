package com.ricardthegreat.holdmetight.events;

import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
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
        event.register(PlayerCarry.class);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerRespawnEvent event){
        Player respawnPlayer = event.getEntity();
        Level level = respawnPlayer.level();
        MinecraftServer server = level.getServer();
        
        if (server != null) {
            ServerPlayer serverJoiner = server.getPlayerList().getPlayer(respawnPlayer.getUUID());
            syncPlayerCapabilities(serverJoiner, server);
        }else{
            for(Player player : level.players()){
                System.out.println(player.getName());
            }
        }
    }

    public static void syncPlayerCapabilities(ServerPlayer serverJoiner, MinecraftServer server){
            Supplier<ServerPlayer> supplier = () -> serverJoiner;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                LazyOptional<PlayerSize> optional = player.getCapability(PlayerSizeProvider.PLAYER_SIZE);
                if (optional.isPresent()) {
                    PlayerSize orElse = optional.orElse(new PlayerSize());

                    if (player == serverJoiner) {
                        PacketHandler.sendToAllClients(orElse.getSyncPacket(player));
                    }else{
                        PacketHandler.sendToPlayer(orElse.getSyncPacket(player), supplier);
                    }
                }

                LazyOptional<PlayerCarry> CarryOptional = player.getCapability(PlayerCarryProvider.PLAYER_CARRY);
                if (CarryOptional.isPresent()) {
                    PlayerCarry orElse = CarryOptional.orElse(new PlayerCarry());

                    if (player == serverJoiner) {
                        PacketHandler.sendToAllClients(orElse.getSyncPacket(player));
                    }else{
                        PacketHandler.sendToPlayer(orElse.getSyncPacket(player), supplier);
                    }
                }
            }
    }

}
