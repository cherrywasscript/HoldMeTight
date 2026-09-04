package com.ricardthegreat.holdmetight.events;

import java.util.UUID;
import java.util.function.Supplier;

import com.ricardthegreat.holdmetight.HMTConfig;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.capabilities.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferences;
import com.ricardthegreat.holdmetight.capabilities.preferences.PlayerPreferencesProvider;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSize;
import com.ricardthegreat.holdmetight.capabilities.size.PlayerSizeProvider;
import com.ricardthegreat.holdmetight.enchantments.GrowingCurseEnchantment;
import com.ricardthegreat.holdmetight.enchantments.ShrinkingCurseEnchantment;
import com.ricardthegreat.holdmetight.enchantments.ShrinkingEnchantment;
import com.ricardthegreat.holdmetight.enchantments.SizeStealEnchantment;
import com.ricardthegreat.holdmetight.init.EnchantmentInit;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.items.EntityStandinItem;
import com.ricardthegreat.holdmetight.items.PlayerStandinItem;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.capabilitySync.carry.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.sizeutils.EntitySizeUtils;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

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

            if(!event.getObject().getCapability(PlayerPreferencesProvider.PLAYER_PREFERENCES).isPresent()) {
                event.addCapability(new ResourceLocation(HoldMeTight.MODID, "preferences"), new PlayerPreferencesProvider());
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
            event.getOriginal().getCapability(PlayerPreferencesProvider.PLAYER_PREFERENCES).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerPreferencesProvider.PLAYER_PREFERENCES).ifPresent(newStore -> {
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
        event.register(PlayerPreferences.class);
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
            //TODO something here if server doesnt exist maybe
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
                    if (!thrower.level().isClientSide) {
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

                LazyOptional<PlayerPreferences> PreferencesOptional = player.getCapability(PlayerPreferencesProvider.PLAYER_PREFERENCES);
                if (PreferencesOptional.isPresent()) {
                    PlayerPreferences orElse = PreferencesOptional.orElse(new PlayerPreferences());

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
        //HoldMeTight.LOGGER.debug("kb event strength:" + strength);
        scale = (float) Math.pow(scale, 0.6);
        strength /= scale;
        //HoldMeTight.LOGGER.debug("kb event strength mod:" + strength);
        event.setStrength(strength);
    }

    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent event){
        DamageSource source = event.getSource();
        if (!source.isIndirect()) {
            Entity ent = source.getEntity();
            if (ent instanceof LivingEntity living) {
                int level = living.getMainHandItem().getEnchantmentLevel(EnchantmentInit.SHRINKING_ENCHANTMENT.get());
                if (level > 0) {
                    ShrinkingEnchantment.doShrink(event.getEntity(), living, event.getAmount(), level);
                    event.setAmount(0.0F);
                }else{
                    level = living.getMainHandItem().getEnchantmentLevel(EnchantmentInit.SIZE_STEALING_ENCHANTMENT.get());
                    if (level > 0) {
                        SizeStealEnchantment.doSteal(event.getEntity(), living, event.getAmount(), level);
                        event.setAmount(0.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickEvent(PlayerTickEvent event){
        if(event.phase.equals(TickEvent.Phase.END)){
            Player player = event.player;
            if (!player.level().isClientSide) {
                int level = 0;
                ICuriosItemHandler curios = CuriosApi.getCuriosInventory(player).orElse(null);
                if (curios != null) {
                    if (curios.isEquipped(ItemInit.COLLAR_ITEM.get())) {
                        ItemStack stack = curios.findCurios(ItemInit.COLLAR_ITEM.get()).get(0).stack();

                        level += getCurseLevel(stack);
                    }
                }

                level += getCurseLevel(player.getItemBySlot(EquipmentSlot.HEAD));
                level += getCurseLevel(player.getItemBySlot(EquipmentSlot.CHEST));
                level += getCurseLevel(player.getItemBySlot(EquipmentSlot.LEGS));
                level += getCurseLevel(player.getItemBySlot(EquipmentSlot.FEET));
                
                if (level != 0) {
                    doSizeCurse(player, level);
                }
            }
        }
    }

    private static int getCurseLevel(ItemStack stack){
        if (stack.getEnchantmentLevel(EnchantmentInit.SHRINKING_CURSE_ENCHANTMENT.get()) > 0) {
            return -1;
        }else if (stack.getEnchantmentLevel(EnchantmentInit.GROWTH_CURSE_ENCHANTMENT.get()) > 0){
            return 1;
        }

        return 0;
    }

    private static void doSizeCurse(Player player, int level){
        float mult = 0.99994f;
        int ticks = 0;
        boolean cap = false;
        if (level > 0) {
            mult = 1.00006f;
            ticks = 1;
            cap = true;
        }else{
            level = -level;
        }

        mult = (float) Math.pow(mult, level);

        if (cap) {
            if (PlayerSizeUtils.getTargetSize(player) < HMTConfig.SERVER_CONFIG.growthCurseMaxScale.get() && PlayerSizeUtils.getSize(player) < HMTConfig.SERVER_CONFIG.growthCurseMaxScale.get()) {
                PlayerSizeUtils.multSize(null, player, mult, ticks);
            }
        }else{
            PlayerSizeUtils.multSize(null, player, mult, ticks);
        }
    }
}
