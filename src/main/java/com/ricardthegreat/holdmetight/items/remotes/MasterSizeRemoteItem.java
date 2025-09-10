package com.ricardthegreat.holdmetight.items.remotes;

import com.ricardthegreat.holdmetight.Client.ClientHooks;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;


public class MasterSizeRemoteItem extends AbstractSizeRemoteItem {

    public MasterSizeRemoteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    protected void openScreen(Player player, InteractionHand hand) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openMasterRemoteScreen(player, hand));
    }
}
