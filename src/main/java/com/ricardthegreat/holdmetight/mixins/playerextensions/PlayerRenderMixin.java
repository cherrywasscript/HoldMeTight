package com.ricardthegreat.holdmetight.mixins.playerextensions;

import org.spongepowered.asm.mixin.Mixin;
import com.ricardthegreat.holdmetight.utils.PlayerRenderExtension;
import net.minecraft.world.entity.player.Player;

@Mixin(Player.class)
public abstract class PlayerRenderMixin implements PlayerRenderExtension{

    //for if they are being rendered in the size remote so that i can disable the nametag
    private boolean isMenuGraphic = false;

    //getter and setter for if its a menu object
    @Override
    public boolean getIsMenu() {
        return isMenuGraphic;
    }

    @Override
    public void setMenu(boolean menu) {
        isMenuGraphic = menu;
    }
}
