package com.ricardthegreat.holdmetight;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        private static final ForgeConfigSpec.ConfigValue<Double> MAX_HITBOX_SIZE = BUILDER
                .comment("The maximum scale a hitbox can be (8 or lower recommended for performance sake when larger)")
                .define("maxHitboxScale", 8d);

        private static final ForgeConfigSpec.ConfigValue<Double> PAPER_WINGS_MAX_SCALE = BUILDER
                .comment("the largest someone can be while wearing the paper wings item (an elytra in all ways that matter)")
                .define("maxWingsScale", 0.05d);

        private static final ForgeConfigSpec.ConfigValue<Double> MIN_PARTICLE_SCALE = BUILDER
                .comment("the scale an entity should be before ambient particles are disabled on them")
                .define("minParticleScale", 0.5d);

        private static final ForgeConfigSpec.ConfigValue<Boolean> MINING_SPEED_SCALE_LINK = BUILDER
                .comment("should a players mining speed be linked to their scale (faster for larger folk slower for smaller folk)")
                .define("miningSpeedScaleLink", true);

        private static final ForgeConfigSpec.ConfigValue<Boolean> DAMAGE_TAKEN_SCALE_LINK = BUILDER
                .comment("should the damage a player takes be linked to their scale (less for larger folk more for smaller folk)")
                .define("damageTakenScaleLink", true);

        private static final ForgeConfigSpec.ConfigValue<Boolean> PLAYER_CHAT_SCALE = BUILDER
                //.comment("should player messages be scaled based on their size (this is not properly tested and could cause many issues use at your own risk)")
                .comment("this isnt used currently")
                .define("playerChatScale", false);

        /* 
        private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
                .comment("A magic number")
                .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

        public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
                .comment("What you want the introduction message to be for the magic number")
                .define("magicNumberIntroduction", "The magic number is... ");

        // a list of strings that are treated as resource locations for items
        private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
                .comment("A list of items to log on common setup.")
                .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
        */

        static final ForgeConfigSpec SPEC = BUILDER.build();

        public static double maxHitboxScale;
        public static double maxWingsScale;
        public static double minParticleScale;
        public static boolean playerChatScale;
        public static boolean miningSpeedScaleLink;
        public static boolean damageTakenScaleLink;
        
        private static boolean validateItemName(final Object obj)
        {
                return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
        }

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event)
        {
                maxHitboxScale = MAX_HITBOX_SIZE.get();
                maxWingsScale = PAPER_WINGS_MAX_SCALE.get();
                minParticleScale = MIN_PARTICLE_SCALE.get();
                playerChatScale = PLAYER_CHAT_SCALE.get();
                miningSpeedScaleLink = MINING_SPEED_SCALE_LINK.get();
                damageTakenScaleLink = DAMAGE_TAKEN_SCALE_LINK.get();
        }
}
