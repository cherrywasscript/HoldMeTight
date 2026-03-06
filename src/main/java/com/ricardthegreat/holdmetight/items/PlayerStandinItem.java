package com.ricardthegreat.holdmetight.items;

import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.ricardthegreat.holdmetight.carry.PlayerCarry;
import com.ricardthegreat.holdmetight.carry.PlayerCarryProvider;
import com.ricardthegreat.holdmetight.init.ItemInit;
import com.ricardthegreat.holdmetight.network.PacketHandler;
import com.ricardthegreat.holdmetight.network.clientbound.CAddPlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.network.clientbound.CRemovePlayerCarrySyncPacket;
import com.ricardthegreat.holdmetight.utils.sizeutils.PlayerSizeUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PlayerStandinItem extends EntityStandinItem{
    
    //lmao this class is now empty
    //keeping it as i do eventually want to do things with player items that are seperate from what i want from entity items

    public PlayerStandinItem(Properties properties) {
        super(properties);
    }

}
