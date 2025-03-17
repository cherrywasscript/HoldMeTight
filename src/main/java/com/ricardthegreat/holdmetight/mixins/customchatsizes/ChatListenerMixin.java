package com.ricardthegreat.holdmetight.mixins.customchatsizes;

import java.time.Instant;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;
import com.ricardthegreat.holdmetight.utils.IChatComponentPlayerSent;

import net.minecraft.Util;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FilterMask;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;

@Mixin(ChatListener.class)
public abstract class ChatListenerMixin {

    @Shadow
    @Final
    Minecraft minecraft;
    @Shadow
    long previousMessageTime;

    @Shadow
    abstract ChatTrustLevel evaluateTrustLevel(PlayerChatMessage p_251246_, Component p_250576_, Instant p_249995_);
    @Shadow
    abstract void narrateChatMessage(ChatType.Bound p_241352_, Component p_243262_);
    @Shadow
    abstract void logPlayerMessage(PlayerChatMessage p_252155_, ChatType.Bound p_249730_, GameProfile p_248589_, ChatTrustLevel p_248881_);

    @Overwrite
    private boolean showMessageToPlayer(ChatType.Bound p_251766_, PlayerChatMessage p_249430_, Component p_249231_, GameProfile profile, boolean p_251638_, Instant p_249665_) {
      ChatTrustLevel chattrustlevel = this.evaluateTrustLevel(p_249430_, p_249231_, p_249665_);
      if (p_251638_ && chattrustlevel.isNotSecure()) {
         return false;
      } else if (!this.minecraft.isBlocked(p_249430_.sender()) && !p_249430_.isFullyFiltered()) {
         GuiMessageTag guimessagetag = chattrustlevel.createTag(p_249430_);
         MessageSignature messagesignature = p_249430_.signature();
         FilterMask filtermask = p_249430_.filterMask();
         if (filtermask.isEmpty()) {
            Component forgeComponent = net.minecraftforge.client.ForgeHooksClient.onClientPlayerChat(p_251766_, p_249231_, p_249430_, p_249430_.sender());
            if (forgeComponent == null) return false;

            //passing player data along with message data
            IChatComponentPlayerSent t = (IChatComponentPlayerSent) this.minecraft.gui.getChat();
            t.addMessage(forgeComponent, messagesignature, guimessagetag, profile);
            //this.minecraft.gui.getChat().addMessage(forgeComponent, messagesignature, guimessagetag);

            this.narrateChatMessage(p_251766_, p_249430_.decoratedContent());
         } else {
            Component component = filtermask.applyWithFormatting(p_249430_.signedContent());
            if (component != null) {
               Component forgeComponent = net.minecraftforge.client.ForgeHooksClient.onClientPlayerChat(p_251766_, p_251766_.decorate(component), p_249430_, p_249430_.sender());
               if (forgeComponent == null) return false;

               //passing player data along with message data
               IChatComponentPlayerSent t = (IChatComponentPlayerSent) this.minecraft.gui.getChat();
               t.addMessage(forgeComponent, messagesignature, guimessagetag, profile);
               //this.minecraft.gui.getChat().addMessage(forgeComponent, messagesignature, guimessagetag);


               this.narrateChatMessage(p_251766_, component);
            }
         }

         this.logPlayerMessage(p_249430_, p_251766_, profile, chattrustlevel);
         this.previousMessageTime = Util.getMillis();
         return true;
      } else {
         return false;
      }
   }
    
}
