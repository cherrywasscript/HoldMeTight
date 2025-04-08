package com.ricardthegreat.holdmetight.items;

import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.HoldMeTight;

import java.lang.reflect.*;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CollarItem extends Item {

    public CollarItem(Properties p_41383_) {
        super(p_41383_);
        //TODO Auto-generated constructor stub
    }
    
    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (HoldMeTight.curiosInstalled) {
            try {
                Object c = Class.forName("ricardthegreat.holdmetight.items.curiosCompat.CollarItemCurio").getDeclaredConstructor(Integer.class).newInstance(1);
                Method getCap = c.getClass().getMethod("getCuriosCapability", ItemStack.class, CompoundTag.class);
                
                return (ICapabilityProvider) getCap.invoke(stack, nbt);
            } catch (Exception e) {

            }
        }
        return super.initCapabilities(stack, nbt);
    }
}
