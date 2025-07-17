package com.ricardthegreat.holdmetight.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeInit {
    
    public static void register(){
        //Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        //Ingredient.of(new ItemStack(Items.FLOWERING_AZALEA));
        //PotionUtils.setPotion(new ItemStack(Items.POTION), PotionsInit.SHRINK_POTION.get());
         
        //shrinking pots
        PotionBrewing.addMix(Potions.AWKWARD, Items.FLOWERING_AZALEA, PotionsInit.SHRINK_POTION.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4.get());

        PotionBrewing.addMix(PotionsInit.SHRINK_POTION.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_1.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_1_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_2.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_2_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_3.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_3_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_4.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_4_LONG.get());

        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3_LONG.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4_LONG.get());

        //growth pots
        PotionBrewing.addMix(Potions.AWKWARD, Items.AMETHYST_SHARD, PotionsInit.GROW_POTION.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4.get());

        PotionBrewing.addMix(PotionsInit.GROW_POTION.get(), Items.REDSTONE, PotionsInit.GROW_POTION_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_1.get(), Items.REDSTONE, PotionsInit.GROW_POTION_1_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_2.get(), Items.REDSTONE, PotionsInit.GROW_POTION_2_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_3.get(), Items.REDSTONE, PotionsInit.GROW_POTION_3_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_4.get(), Items.REDSTONE, PotionsInit.GROW_POTION_4_LONG.get());

        PotionBrewing.addMix(PotionsInit.GROW_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3_LONG.get());
        PotionBrewing.addMix(PotionsInit.GROW_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4_LONG.get());

        
        //super shrink pots
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_4.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION.get());
        PotionBrewing.addMix(PotionsInit.SHRINK_POTION_4_LONG.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION_LONG.get());
      
    }
}
