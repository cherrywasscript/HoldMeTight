package com.ricardthegreat.holdmetight.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class RecipeInit {
    
    public static void register(){
        Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
        Ingredient.of(new ItemStack(Items.FLOWERING_AZALEA));
        PotionUtils.setPotion(new ItemStack(Items.POTION), PotionsInit.SHRINK_POTION.get());

        
        //shrinking pots
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(Potions.AWKWARD, Items.FLOWERING_AZALEA, PotionsInit.SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4_LONG.get()));

        //growth pots
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD, PotionsInit.GROW_POTION.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION.get(), Items.REDSTONE, PotionsInit.GROW_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_1.get(), Items.REDSTONE, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_2.get(), Items.REDSTONE, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_3.get(), Items.REDSTONE, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_4.get(), Items.REDSTONE, PotionsInit.GROW_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.GROW_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4_LONG.get()));

        
        //super shrink pots
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(setBrewRecipe(PotionsInit.SHRINK_POTION_4_LONG.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION_LONG.get()));







        //this big fuck off thing is from when i used the BetterBrewingRecipe class, 
        //keeping it out of not wanting to bother typing it all out again if i switch back

        /* 
        //shrinking pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.FLOWERING_AZALEA, PotionsInit.SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.REDSTONE, PotionsInit.SHRINK_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.SHRINK_POTION_4_LONG.get()));

        //growth pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.AMETHYST_SHARD, PotionsInit.GROW_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION.get(), Items.REDSTONE, PotionsInit.GROW_POTION_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1.get(), Items.REDSTONE, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2.get(), Items.REDSTONE, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3.get(), Items.REDSTONE, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_4.get(), Items.REDSTONE, PotionsInit.GROW_POTION_4_LONG.get()));

        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_1_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_1_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_2_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_2_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_3_LONG.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.GROW_POTION_3_LONG.get(), Items.GLOWSTONE_DUST, PotionsInit.GROW_POTION_4_LONG.get()));

        
        //super shrink pots
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION.get()));
        BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(PotionsInit.SHRINK_POTION_4_LONG.get(), Items.CHERRY_SAPLING, PotionsInit.MASSIVE_SHRINK_POTION_LONG.get()));
        */
    }

    private static BrewingRecipe setBrewRecipe(Potion potion, Item item, Potion output){
        return new BrewingRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potion)), Ingredient.of(new ItemStack(item)), PotionUtils.setPotion(new ItemStack(Items.POTION), output));
    }
}
