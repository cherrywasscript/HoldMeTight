package com.ricardthegreat.holdmetight.client.handlers;

import com.mojang.brigadier.CommandDispatcher;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Commands.CustomCarryCommand;
import com.ricardthegreat.holdmetight.Commands.ResetCarriedCommand;
import com.ricardthegreat.holdmetight.Commands.ResetCarryingCommand;
import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.client.ClientHooks;
import com.ricardthegreat.holdmetight.client.Keybindings;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.items.CollarItem;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    private static final Component SHOULDER_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".shoulder_key_pressed");
    private static final Component CUSTOM_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".custom_key_pressed");
    private static final Component DEFAULT_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".default_key_pressed");

    @SubscribeEvent
    public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        CustomCarryCommand.register(dispatcher);

        ResetCarriedCommand.register(dispatcher);
        ResetCarryingCommand.register(dispatcher);
        //ChatScaleCommand.register(dispatcher);
    } 

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer mcPlayer = minecraft.player;
        if(Keybindings.INSTANCE.shoulderCarryKey.consumeClick() && mcPlayer != null) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(mcPlayer);

            if (playerCarry.getCarryPosition().posName != "shoulder") {
                playerCarry.setCarryPosition(false, 1);
                mcPlayer.displayClientMessage(SHOULDER_KEY_PRESSED, true);
            }else{
                playerCarry.setCarryPosition(false, 0);
                mcPlayer.displayClientMessage(DEFAULT_KEY_PRESSED, true);
            }

            playerCarry.setShouldSyncSimple(true);
        }

        if(Keybindings.INSTANCE.customCarryKey.consumeClick() && mcPlayer != null) {
            PlayerCarry playerCarry = PlayerCarryProvider.getPlayerCarryCapability(mcPlayer);

            if (playerCarry.getCarryPosition().posName == "shoulder" || playerCarry.getCarryPosition().posName == "hand" ) {
                playerCarry.setCarryPosition(true, 0);
                mcPlayer.displayClientMessage(CUSTOM_KEY_PRESSED, true);
            }else{
                playerCarry.setCarryPosition(false, 0);
                mcPlayer.displayClientMessage(DEFAULT_KEY_PRESSED, true);
            }

            playerCarry.setShouldSyncSimple(true);
        }

        if(Keybindings.INSTANCE.carryWheelKey.consumeClick() && mcPlayer != null){
            if (mcPlayer.level().isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openCarryPositionWheel(mcPlayer));
            }
        }

        //key to open size prefs screen
        if(Keybindings.INSTANCE.sizePrefsKey.consumeClick() && mcPlayer != null) {     
            if (mcPlayer.level().isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openSizePrefsScreen(mcPlayer));
            }    
        }

        //key to open carry positioning screen
        if(Keybindings.INSTANCE.carryScreenKey.consumeClick() && mcPlayer != null) { 
            if (mcPlayer.level().isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openCarryPositionScreen(mcPlayer));
            }
        }
    }

    @SubscribeEvent
    public static void interactLivingEntityEvent(PlayerInteractEvent.EntityInteract event){
        Item item = event.getEntity().getItemInHand(event.getHand()).getItem();

        Entity target = event.getTarget();

        if (item instanceof AbstractSizeRemoteItem) {
            AbstractSizeRemoteItem sizeRemote = (AbstractSizeRemoteItem) item;
            
            if (target instanceof LivingEntity && !event.getEntity().getCooldowns().isOnCooldown(item)) {
                sizeRemote.interactLivingEntity(event.getEntity().getItemInHand(event.getHand()), event.getEntity(), (LivingEntity) target, event.getHand());
                if (event.isCancelable()) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }
    }

    //TODO figure out if this should actually be done on rendernametag even or not? it seems like i could probably change a players name or something idk
    @SubscribeEvent
    public static void renderNameTag(RenderNameTagEvent event){
        if (event.getEntity() instanceof Pig) {
            event.setResult(Result.ALLOW);
        }
    }

}
