package com.ricardthegreat.holdmetight.items;

import java.util.function.Consumer;

import com.ricardthegreat.holdmetight.HoldMeTight;
import com.ricardthegreat.holdmetight.client.renderers.HeldEntityItemRenderer;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class EntityStandinItem extends Item implements ICurioItem{

    // leaving this class empty so its obvious to me i need to actually implement it
    // should be a more generic version of the player item class with compatability with all entities instead of just players and be something that the player item class can pull from

    public EntityStandinItem(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return HeldEntityItemRenderer.INSTANCE;
            }
        });
    }
}
