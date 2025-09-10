package com.ricardthegreat.holdmetight.items;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.Client.renderers.ArmorRenderer;
import com.ricardthegreat.holdmetight.Client.renderers.layers.CollarModelLayers;

import java.util.function.Consumer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CollarItem extends Item implements Equipable{

    public CollarItem(Properties p_41383_) {
        super(p_41383_);
    }
    
    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        //TODO enable at some point in the future + readd the collar curio slot
        /* 
        if (HoldMeTight.curiosInstalled) {
            try {
                Object c = Class.forName("ricardthegreat.holdmetight.items.curiosCompat.CollarItemCurio").getDeclaredConstructor(Integer.class).newInstance(1);
                Method getCap = c.getClass().getMethod("getCuriosCapability", ItemStack.class, CompoundTag.class);
                
                return (ICapabilityProvider) getCap.invoke(stack, nbt);
            } catch (Exception e) {

            }
        }
        */

        return super.initCapabilities(stack, nbt);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return ArmorItem.Type.HELMET.getSlot();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                    EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                HumanoidModel<?> armourModel = new HumanoidModel<>(new ArmorRenderer<>(CollarModelLayers::createBodyLayer, CollarModelLayers::new).makeArmorParts(equipmentSlot));
                armourModel.crouching = original.crouching;
                armourModel.riding = original.riding;
                armourModel.young = original.young;
                return armourModel;
            }
        });
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(HoldMeTight.MODID, "/textures/entity/collar.png").toString();
    }
}
