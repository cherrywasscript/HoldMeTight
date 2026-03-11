package com.ricardthegreat.holdmetight;

import com.ricardthegreat.holdmetight.HMTConfig.CommonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HMTConfig {

        private static final ForgeConfigSpec.Builder clientConfigSpecBuilder = new ForgeConfigSpec.Builder();
        private static final ForgeConfigSpec.Builder commonConfigSpecBuilder = new ForgeConfigSpec.Builder();
        private static final ForgeConfigSpec.Builder serverConfigSpecBuilder = new ForgeConfigSpec.Builder();

        public static final ClientConfig CLIENT_CONFIG = new ClientConfig(clientConfigSpecBuilder);
        public static final CommonConfig COMMON_CONFIG = new CommonConfig(commonConfigSpecBuilder);
        public static final ServerConfig SERVER_CONFIG = new ServerConfig(serverConfigSpecBuilder);

        public static final ForgeConfigSpec clientSpec = clientConfigSpecBuilder.build();
        public static final ForgeConfigSpec commonSpec = commonConfigSpecBuilder.build();
        public static final ForgeConfigSpec serverSpec = serverConfigSpecBuilder.build();

        @SubscribeEvent
        public static void onLoad(ModConfigEvent event){
                HoldMeTight.LOGGER.debug("loaded config: " + event.getConfig().getFileName());
        }

        public static class ClientConfig {
                ClientConfig(ForgeConfigSpec.Builder builder){

                }
        }

        public static class CommonConfig {
                CommonConfig(ForgeConfigSpec.Builder builder){

                }
        }

        public static class ServerConfig {

                public final ForgeConfigSpec.ConfigValue<Float> maxHitboxScale;
                private final ForgeConfigSpec.ConfigValue<Float> maxEntityScale;
                public final ForgeConfigSpec.ConfigValue<Float> maxWingsScale;
                public final ForgeConfigSpec.ConfigValue<Float> minParticleScale;
                public final ForgeConfigSpec.BooleanValue miningSpeedScaleLink;
                public final ForgeConfigSpec.BooleanValue damageTakenScaleLink;
                public final ForgeConfigSpec.BooleanValue canPickupEntities;
                public final ForgeConfigSpec.BooleanValue canPickupPlayers;
                public final ForgeConfigSpec.BooleanValue playerChatScale;


                ServerConfig(ForgeConfigSpec.Builder builder){
                        builder.comment("Serverside Config Settings").push("Scale Limits");

                        //TODO make these translations
                        this.maxHitboxScale = builder.comment("The maximum scale a hitbox can be (8 or lower recommended for performance sake when larger)")
                                .define("maxHitboxScale", 8f);
                        this.maxEntityScale = builder.comment("The maximum scale an entity can become, set to 0 for no cap (cannot be below 1)")
                                .define("maxEntityScale", 0f);
                        this.maxWingsScale = builder.comment("the largest someone can be while wearing the paper wings item (an elytra in all ways that matter)")
                                .define("maxWingsScale", 0.05f);
                        this.minParticleScale = builder.comment("the scale an entity should be before ambient particles are disabled on them")
                                .define("minParticleScale", 0.5f);

                        builder.pop();

                        builder.push("Optional Features");

                        this.miningSpeedScaleLink = builder.comment("should a players mining speed be linked to their scale (faster for larger folk slower for smaller folk)")
                                .define("miningSpeedScaleLink", true);
                        this.damageTakenScaleLink = builder.comment("should the damage a player takes be linked to their scale (less for larger folk more for smaller folk)")
                                .define("damageTakenScaleLink", true);
                        this.canPickupEntities = builder.comment("enable or disable the ability to pickup non player mobs")
                                .define("canPickupMobs", true);
                        this.canPickupPlayers = builder.comment("enable or disable the ability to pickup players")
                                .define("canPickupPlayers", true);
                        
                        this.playerChatScale = builder//.comment("should player messages be scaled based on their size (this is not properly tested and could cause many issues use at your own risk)")
                                .comment("this isnt used currently")
                                .define("playerChatScale", false);

                        builder.pop();
                }

                public float getMaxEntityScale(){
                        if (this.maxEntityScale.get() <= 0) {
                                return Float.POSITIVE_INFINITY;
                        }else if (this.maxEntityScale.get() < 1) {
                                maxEntityScale.set(1f);;
                        }
                        return maxEntityScale.get();
                }
        }
}
