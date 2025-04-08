package com.ricardthegreat.holdmetight.items.curiosCompat;

import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.HoldMeTight;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

/*
 * this class is only called if curios is loaded
 * might make it an interface at somepoint? not sure yet
 */
public class CollarItemCurio {
    
    public CollarItemCurio(int test){
        HoldMeTight.LOGGER.debug("collaritemcurio");
    }

    public @Nullable ICapabilityProvider getCuriosCapability(ItemStack stack, @Nullable CompoundTag nbt){
        return CuriosApi.createCurioProvider(new ICurio() {
                    @Override
                    public ItemStack getStack() {
                        return stack;
                    }
        
                    @Override
                    public void curioTick(SlotContext slotContext) {
                        ICurio.super.curioTick(slotContext);
                    }
                });
    }
}
