package com.ricardthegreat.holdmetight.mixins.customchatsizes;

import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.ricardthegreat.holdmetight.Config;
import com.ricardthegreat.holdmetight.utils.IChatComponentPlayerSent;
import com.ricardthegreat.holdmetight.utils.SizeUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent.Chat;


/*
 * Wanna add something to this class where it does nothing unless the player opts into it because i can see the
 * stuff im doing here causing issues
 */
@Mixin(ChatComponent.class)
public abstract class ServerPlayerMixin implements IChatComponentPlayerSent{

   private final List<GameProfile> profiles = Lists.newArrayList();
   private final List<Float> scales = Lists.newArrayList();
   private final List<Component> nonFormattedMessages = Lists.newArrayList();
   private final List<GuiMessage.Line> formattedMessagesNoName = Lists.newArrayList();
   private final List<Component> names = Lists.newArrayList();

    @Shadow
    List<GuiMessage.Line> trimmedMessages;
    @Shadow
    Minecraft minecraft;
    @Shadow
    int chatScrollbarPos;
    @Shadow
    boolean newMessageSinceScroll;

    @Shadow
    abstract boolean isChatHidden();
    @Shadow
    abstract boolean isChatFocused();
    @Shadow
    abstract int getMessageEndIndexAt(double p_249245_, double p_252282_);
    @Shadow
    abstract double screenToChatX(double p_240580_);
    @Shadow
    abstract double screenToChatY(double p_240580_);
    @Shadow
    abstract int getLineHeight();
    @Shadow
    private static double getTimeFactor(int p_93776_) {return 0;}
    @Shadow
    abstract int getTagIconLeft(GuiMessage.Line p_240622_);
    @Shadow
    abstract void drawTagIcon(GuiGraphics p_283206_, int p_281677_, int p_281878_, GuiMessageTag.Icon p_282783_);

   //@Overwrite
   @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/gui/GuiGraphics;III)V", cancellable = true)
   public void render(GuiGraphics graphics, int p_283491_, int p_282406_, int p_283111_, CallbackInfo info) {
      //have this so i can put an if statement checking if it should use this or the original method
      if (Config.playerChatScale) {
         customRender(graphics, p_283491_, p_282406_, p_283111_);
         info.cancel();
      }
   }

   private void customRender(GuiGraphics graphics, int p_283491_, int p_282406_, int p_283111_){
      if (!this.isChatHidden()) {
         int i = ((ChatComponent) (Object) this).getLinesPerPage();
         int j = this.trimmedMessages.size();
         if (j > 0) {
            boolean flag = this.isChatFocused();
            float f = (float)((ChatComponent) (Object) this).getScale();
            int k = Mth.ceil((float)((ChatComponent) (Object) this).getWidth() / f);
            int l = graphics.guiHeight();
            graphics.pose().pushPose();
            graphics.pose().scale(f, f, 1.0F);
            graphics.pose().translate(4.0F, 0.0F, 0.0F);
            int i1 = Mth.floor((float)(l - 40) / f);
            int j1 = this.getMessageEndIndexAt(this.screenToChatX((double)p_282406_), this.screenToChatY((double)p_283111_));
            double d0 = this.minecraft.options.chatOpacity().get() * (double)0.9F + (double)0.1F;
            double d1 = this.minecraft.options.textBackgroundOpacity().get();
            double d2 = this.minecraft.options.chatLineSpacing().get();
            int k1 = this.getLineHeight();
            int l1 = (int)Math.round(-8.0D * (d2 + 1.0D) + 4.0D * d2);
            int i2 = 0;

            for(int j2 = 0; j2 + this.chatScrollbarPos < this.trimmedMessages.size() && j2 < i; ++j2) {
               int k2 = j2 + this.chatScrollbarPos;
               GuiMessage.Line guimessage$line = this.trimmedMessages.get(k2);
               if (guimessage$line != null) {
                  int l2 = p_283491_ - guimessage$line.addedTime();
                  if (l2 < 200 || flag) {
                     double d3 = flag ? 1.0D : getTimeFactor(l2);
                     int j3 = (int)(255.0D * d3 * d0);
                     int k3 = (int)(255.0D * d3 * d1);
                     ++i2;
                     if (j3 > 3) {
                        int l3 = 0;
                        int i4 = i1 - j2 * k1;
                        int j4 = i4 + l1;
                        graphics.pose().pushPose();
                        graphics.pose().translate(0.0F, 0.0F, 50.0F);
                        graphics.fill(-4, i4 - k1, 0 + k + 4 + 4, i4, k3 << 24);
                        GuiMessageTag guimessagetag = guimessage$line.tag();
                        if (guimessagetag != null) {
                           int k4 = guimessagetag.indicatorColor() | j3 << 24;
                           graphics.fill(-4, i4 - k1, -2, i4, k4);
                           if (k2 == j1 && guimessagetag.icon() != null) {
                              int l4 = this.getTagIconLeft(guimessage$line);
                              int i5 = j4 + 9;
                              this.drawTagIcon(graphics, l4, i5, guimessagetag.icon());
                           }
                        }

                        
                        
                        graphics.pose().translate(0.0F, 0.0F, 50.0F);
                        

                        if (scales.get(k2) < 1) {
                           calcChatSize(graphics, k2, j4, j3);
                        }else{
                           graphics.drawString(this.minecraft.font, guimessage$line.content(), 0, j4, 16777215 + (j3 << 24));
                        }
                        
                        


                        graphics.pose().popPose();
                     }
                  }
               }
            }

            long j5 = this.minecraft.getChatListener().queueSize();
            if (j5 > 0L) {
               int k5 = (int)(128.0D * d0);
               int i6 = (int)(255.0D * d1);
               graphics.pose().pushPose();
               graphics.pose().translate(0.0F, (float)i1, 50.0F);
               graphics.fill(-2, 0, k + 4, 9, i6 << 24);
               graphics.pose().translate(0.0F, 0.0F, 50.0F);
               graphics.drawString(this.minecraft.font, Component.translatable("chat.queue", j5), 0, 1, 16777215 + (k5 << 24));
               graphics.pose().popPose();
            }

            if (flag) {
               int l5 = this.getLineHeight();
               int j6 = j * l5;
               int k6 = i2 * l5;
               int i3 = this.chatScrollbarPos * k6 / j - i1;
               int l6 = k6 * k6 / j6;
               if (j6 != k6) {
                  int i7 = i3 > 0 ? 170 : 96;
                  int j7 = this.newMessageSinceScroll ? 13382451 : 3355562;
                  int k7 = k + 4;
                  graphics.fill(k7, -i3, k7 + 2, -i3 - l6, j7 + (i7 << 24));
                  graphics.fill(k7 + 2, -i3, k7 + 1, -i3 - l6, 13421772 + (i7 << 24));
               }
            }

            graphics.pose().popPose();
         }
      }
   }


   //this needs like lots and lots of testing on actual servers with lots of messages because i can see many many issues that might and 
   //almost certainly will crop up, but i have no idea how to address them currently
   
   //add if statement checking if i should do this based on config or command or something
   public void addMessage(Component component, @Nullable MessageSignature signature, @Nullable GuiMessageTag tag, GameProfile profile){
      ClientLevel level = minecraft.level;
      Player player = null;
      float scale = 1;

      if (level != null) {
         player = level.getPlayerByUUID(profile.getId());
      }
      if (player != null) {
         scale = SizeUtils.getSize(player) > 1 ? 1 : SizeUtils.getSize(player);
      }


      //turns the component into two components, one with the name and one with the message body
      Component[] comps = takeNameFromComponent(component);

      //find how many lines the message has with name
      List<FormattedCharSequence> nameList = getCompCharSequence(component, tag);
      int nameSize = nameList.size();
      //find how many lines the message has without name
      List<FormattedCharSequence> noNameList = getCompCharSequence(comps[1], tag);
      int noNameSize = noNameList.size();

      //get char sequence that is just the name
      List<FormattedCharSequence> name = getCompCharSequence(comps[0], tag);

      
      
      /*find how many lines the message with no name should have
       * should probably do namesize-nonamesize to see if it should be 1 line larger to fit the name on maybe?
       */
      //int size = (int) Math.ceil(noNameSize*scale);

      //add the scale, component and if it includes name for each line
      for(int j = 0; j < nameSize; ++j) {
         FormattedCharSequence formattedcharsequence = noNameList.get(j);
         boolean flag1 = j == noNameList.size() - 1;
         formattedMessagesNoName.add(0, new GuiMessage.Line(this.minecraft.gui.getGuiTicks(), formattedcharsequence, tag, flag1));

         profiles.add(0, profile);
         scales.add(0, scale);
         //nonFormattedMessages.add(0, comps[1]);
         if (j == 0) {
            names.add(0, comps[0]);
         }else{
            names.add(0, Component.empty());
         }
      }

      clearExcess();

      //continue to normal adding message
      ((ChatComponent) (Object) this).addMessage(component, signature, tag);

      //need to remove excess message lines once im get around to making small messages take up less space
   }

   private void clearExcess(){
      //remove the oldest things in the list
      while(this.profiles.size() > 100) {
         profiles.remove(profiles.size() - 1);
      }
      //remove the oldest things in the list
      while(this.scales.size() > 100) {
         scales.remove(scales.size() - 1);
      }
      //remove the oldest things in the list
      while(this.nonFormattedMessages.size() > 100) {
         nonFormattedMessages.remove(nonFormattedMessages.size() - 1);
      }
   }


   
   private void calcChatSize(GuiGraphics graphics, int pos, int j4, int j3){
      graphics.pose().pushPose();
      if (names.get(pos) != Component.empty()) {
         GuiMessage.Line name = new GuiMessage.Line(this.minecraft.gui.getGuiTicks(), getCompCharSequence(names.get(pos), null).get(0), null, true);
         graphics.drawString(this.minecraft.font, name.content(), 0, j4, 16777215 + (j3 << 24));
         graphics.pose().translate(minecraft.font.width(name.content())+3,0,0);
      }

      float scale = scales.get(pos);

      //getting log0.1 of scale to make the speed at which the text gets smaller slower at larger sizes
      //can mess with the number but the idea is to make text still readable even when at around 0.05
      float logScale = (float) (Math.log(scale)/Math.log(0.1));
      logScale++;
      logScale = 1/logScale;

      FormattedCharSequence message = formattedMessagesNoName.get(pos).content();

      if (logScale < 0.25 && names.get(pos) != Component.empty()) {
         logScale = 0.5f;
         String s = "*indecernable*";
         message = getCompCharSequence(Component.literal(s), null).get(0);
      }else if (logScale < 0.25) {
         logScale = 0;
      }

      graphics.pose().translate(0, ((295-(9*pos))*(1-logScale)) / (float)((ChatComponent) (Object) this).getScale(), 0);
      graphics.pose().scale(logScale, logScale, 1);
      
      graphics.drawString(this.minecraft.font, message, 0, j4, 16777215 + (j3 << 24));

      graphics.pose().popPose();

   }

   private Component[] takeNameFromComponent(Component component){
      String message = component.getString();
      String[] messageParts = message.split(">");

      for(int i = 0; i < messageParts.length - 1; i ++){
      	messageParts[i] = messageParts[i] + ">";
      }
      
      String messageName = messageParts[0];
      String messageBody = "";
      
      for(int i = 1; i < messageParts.length; i ++){
      	messageBody = messageBody + messageParts[i];
      }

      //i dont know how components work so this is probably terrible
      Component[] comps = new Component[]{Component.literal(messageName),Component.literal(messageBody)};
      return comps;
   }

   private List<FormattedCharSequence> getCompCharSequence(Component component, @Nullable GuiMessageTag tag){
      //this is taken from the original addmessage and as far as i can tell just gets how many lines a message should take up
      int i = Mth.floor((double) ((ChatComponent) (Object)this).getWidth() / ((ChatComponent) (Object)this).getScale());
      if (tag != null && tag.icon() != null) {
         i -= tag.icon().width + 4 + 2;
      }
      List<FormattedCharSequence> list = ComponentRenderUtils.wrapComponents(component, i, this.minecraft.font);

      return list;
   }
    
}
