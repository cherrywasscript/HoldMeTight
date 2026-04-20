package com.ricardthegreat.holdmetight.client.handlers;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Commands.CustomCarryCommand;
import com.ricardthegreat.holdmetight.client.ClientHooks;
import com.ricardthegreat.holdmetight.client.Keybindings;
import com.ricardthegreat.holdmetight.items.CollarItem;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    private static final Component SHOULDER_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".shoulder_key_pressed");
    private static final Component CUSTOM_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".custom_key_pressed");
    private static final Component DEFAULT_KEY_PRESSED = Component.translatable("message." + HoldMeTight.MODID + ".default_key_pressed");

    private static final String PICKED_UP_PLAYER = "message." + HoldMeTight.MODID + ".picked_up_player";

    @SubscribeEvent
    public static void RegisterClientCommandsEvent(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        CustomCarryCommand.register(dispatcher);

        //ChatScaleCommand.register(dispatcher);
    } 

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer mcPlayer = minecraft.player;

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

        if (Keybindings.INSTANCE.stickyFingerKey.consumeClick() && mcPlayer != null) {
            if (mcPlayer.level().isClientSide && mcPlayer.getMainHandItem().isEmpty()) {
                Level level = mcPlayer.level();
                List<Entity> ents = level.getEntities(mcPlayer, mcPlayer.getBoundingBox().expandTowards(mcPlayer.getLookAngle()));
                boolean pickedUp = false;
                for (Entity entity : ents) {
                    if (entity instanceof Player) {
                        if (!pickedUp) {
                            entity.interact(mcPlayer, InteractionHand.MAIN_HAND);
                            mcPlayer.connection.send(ServerboundInteractPacket.createInteractionPacket(entity, false, InteractionHand.MAIN_HAND));
                            mcPlayer.displayClientMessage(Component.translatable(PICKED_UP_PLAYER, entity.getName().getString()), true);
                            pickedUp = true;
                        }
                    }
                }
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

    //TODO figure out if this should actually be done on rendernametag event or not? it seems like i could probably change a players name or something idk
    @SubscribeEvent
    public static void renderNameTag(RenderNameTagEvent event){
        if (event.getEntity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> handler.getStacksHandler("collar").ifPresent(stacksHandler -> {
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();

                        for (int i = 0; i < stackHandler.getSlots(); i++) {
                            ItemStack stack = stackHandler.getStackInSlot(i);
                            if (stack.getItem() instanceof CollarItem) {
                                //TODO implement 
                                //event.setContent(Component.literal("test"));
                            }
                        }

                    }));
        }
    }

}
