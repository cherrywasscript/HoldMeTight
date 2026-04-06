package com.ricardthegreat.holdmetight.events;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.size.PlayerSize;
import com.ricardthegreat.holdmetight.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

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
                    newStore.copy(oldStore);
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

    //when item is thrown check if it is player item, if it is then remove it and put the player it represents there with the same momentum
    @SubscribeEvent
    public static void onItemTossEvent(ItemTossEvent event){
        /*
        ItemEntity entity = event.getEntity();
        
        if (entity.getItem().getItem() instanceof EntityStandinItem) {
            Player thrower = event.getPlayer();

            ItemStack stack = entity.getItem();

            if (stack.hasTag()) {
                CompoundTag tag = stack.getTag(); 
                UUID id = tag.getUUID(EntityStandinItem.ENTITY_UUID);

                Level level = thrower.level();

                Entity thrown;
                if (tag.getBoolean(EntityStandinItem.IS_PLAYER)) {
                    thrown = level.getPlayerByUUID(id);
                }else{
                    thrown = level.getEntity(tag.getInt(EntityStandinItem.ENTITY_ID));
                }
                //Player player = thrower.level().getPlayerByUUID(id);

                if (thrown != null) {
                    thrown.stopRiding();
                    thrown.setDeltaMovement(entity.getDeltaMovement()); 
                    thrown.hurtMarked = true;
                    System.out.println("itemtossevent(server+client)/" + thrown.getVehicle());
                    if (!thrower.level().isClientSide) {
                        System.out.println("itemtosseventserver/" + thrown.getVehicle());
                        PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(thrower);
                        playerCarry.removeCarriedEntity(id);
                        DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> 
                        PacketHandler.sendToAllClients(new CRemovePlayerCarrySyncPacket(id, thrower.getUUID())));
                    }
                }
            }
        }
             */
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
                        PacketHandler.sendToAllClients(orElse.getClientSyncPacket(player));
                    }else{
                        PacketHandler.sendToPlayer(orElse.getClientSyncPacket(player), supplier);
                    }
                }
            }
    }

    @SubscribeEvent
    public static void onLivingKnockbackEvent(LivingKnockBackEvent event){
        float scale = EntitySizeUtils.getSize(event.getEntity());
        float strength = event.getOriginalStrength();
        System.out.println("kb event strength:" + strength);
        scale = (float) Math.pow(scale, 0.6);
        strength /= scale;
        System.out.println("kb event strength mod:" + strength);
        event.setStrength(strength);
    }

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event){
        
    }
}
