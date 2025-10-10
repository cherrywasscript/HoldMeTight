package com.ricardthegreat.holdmetight.client.screens;

import java.util.List;

import javax.annotation.Nonnull;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.items.remotes.AbstractSizeRemoteItem;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CollarScreen extends Screen{

    private static final Component TITLE = Component.translatable("gui." + HoldMeTight.MODID + ".collar_screen_title");

    private static final ResourceLocation BACKGROUND = new ResourceLocation(HoldMeTight.MODID, "textures/gui/collar_screen_bg.png");

    private final int imageWidth;
    private final int imageHeight;

    private Player user;

    private ItemStack stack;


    //positions on the image background
    protected int leftPos;
    protected int rightPos;
    protected int topPos;
    protected int bottomPos;
    protected int centerHorizonalPos;
    protected int centerVerticalPos;

    public CollarScreen(Player user, InteractionHand hand) {
        this(TITLE, user, hand, 176, 256);
    }

    private CollarScreen(Component title, Player user, InteractionHand hand, int width, int height) {
        super(title);

        this.imageWidth = width;
        this.imageHeight = height;

        this.user = user;
        stack = user.getItemInHand(hand);
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.rightPos = (this.width - this.leftPos) ;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.bottomPos = (this.height - this.topPos) ;
        this.centerHorizonalPos = (this.leftPos + this.rightPos) / 2 ;
        this.centerVerticalPos = (this.topPos + this.bottomPos) / 2;

        // im not really sure what this does but im also afraid to change/remove it
        if(this.minecraft == null) return;
        @SuppressWarnings("null")
        Level level = this.minecraft.level;
        if(level == null) return;
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        graphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);

        List<Player> nearbyPlayers = user.level().getNearbyPlayers(TargetingConditions.DEFAULT, user, user.getBoundingBox().inflate(10));

        int boxWidth = 50;
        int boxHeight = 50;

        for(int i = 0; i < nearbyPlayers.size(); i++){
            graphics.drawString(font, nearbyPlayers.get(i).getName(), leftPos + boxWidth*i, topPos, 0xadd8e6,false);
        }
    }
    
}
