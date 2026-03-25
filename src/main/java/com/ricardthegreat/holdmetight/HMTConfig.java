package com.ricardthegreat.holdmetight;

import org.apache.commons.lang3.tuple.Pair;

import com.ricardthegreat.holdmetight.HMTConfig.CommonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = HoldMeTight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HMTConfig {
        public static final ClientConfig CLIENT_CONFIG;
        public static final CommonConfig COMMON_CONFIG;
        public static final ServerConfig SERVER_CONFIG;

        public static final ForgeConfigSpec clientSpec;
        public static final ForgeConfigSpec commonSpec;
        public static final ForgeConfigSpec serverSpec;

        @SubscribeEvent
        public static void onLoad(ModConfigEvent event){
                HoldMeTight.LOGGER.debug("loaded config: " + event.getConfig().getFileName());
        }

        static {
                final Pair<ServerConfig, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
                serverSpec = serverPair.getRight();
                SERVER_CONFIG = serverPair.getLeft();

                final Pair<CommonConfig, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
                commonSpec = commonPair.getRight();
                COMMON_CONFIG = commonPair.getLeft();

                final Pair<ClientConfig, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
                clientSpec = clientPair.getRight();
                CLIENT_CONFIG = clientPair.getLeft();
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

                public final ForgeConfigSpec.DoubleValue maxHitboxScale;
                private final ForgeConfigSpec.DoubleValue maxEntityScale;
                public final ForgeConfigSpec.DoubleValue maxWingsScale;
                public final ForgeConfigSpec.DoubleValue minParticleScale;
                public final ForgeConfigSpec.DoubleValue pickupRatioScale;
                public final ForgeConfigSpec.BooleanValue miningSpeedScaleLink;
                public final ForgeConfigSpec.BooleanValue damageTakenScaleLink;
                public final ForgeConfigSpec.BooleanValue canPickupEntities;
                public final ForgeConfigSpec.BooleanValue canPickupPlayers;
                public final ForgeConfigSpec.BooleanValue playerChatScale;
                public final ForgeConfigSpec.DoubleValue maximumMovespeed;
                public final ForgeConfigSpec.DoubleValue maximumElytraspeed;


                ServerConfig(ForgeConfigSpec.Builder builder){
                        builder.comment("Serverside Config Settings");
                        
                        builder.push("Scale Limits");
                        //TODO make these translations
                        this.maxHitboxScale = builder.comment("The maximum scale a hitbox can be (8 or lower recommended for performance sake when larger)")
                                .defineInRange("maxHitboxScale", 8.0, 1, 256);
                        this.maxEntityScale = builder.comment("The maximum scale an entity can become, set to 0 for no cap (if it is any other number below 1 it will default to 1)")
                                .defineInRange("maxEntityScale", 0, 0, Double.MAX_VALUE);
                        this.maxWingsScale = builder.comment("the largest someone can be while wearing the paper wings item (an elytra in all ways that matter)")
                                .defineInRange("maxWingsScale", 0.05, 0, 1);
                        this.minParticleScale = builder.comment("the scale an entity should be before ambient particles are disabled on them")
                                .defineInRange("minParticleScale", 0.5, 0, 1);
                        this.pickupRatioScale = builder.comment("how much smaller an entity needs to be before it can be picked up (e.g. at the default of 0.25 someone at 4x can pickup someone at 1x but not any larger than that)")
                                .defineInRange("pickupRatio", 0.25, Double.MIN_VALUE, Double.MAX_VALUE);

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

                        builder.push("Maximum movespeed");

                        this.maximumMovespeed = builder.comment("the maximum movement speed before it starts giving the 'moved too fast' error. i hightly recommend not increasing this unless you are okay with people potentially moving way too quickly while big (default 100)")
                                .defineInRange("maximumMovespeed", 100, 100, Double.MAX_VALUE);

                        this.maximumElytraspeed = builder.comment("the maximum movement speed when using an elytra before it starts giving the 'moved too fast' error. i hightly recommend not increasing this unless you are okay with people potentially moving way too quickly while big (default 100)")
                                .defineInRange("maximumElytraspeed", 300, 300, Double.MAX_VALUE);

                        //builder.pop();
                        builder.build();
                }

                public float getMaxEntityScale(){
                        if (this.maxEntityScale.get() <= 0) {
                                return Float.POSITIVE_INFINITY;
                        }else if (this.maxEntityScale.get() < 1) {
                                maxEntityScale.set(1d);;
                        }
                        return maxEntityScale.get().floatValue();
                }
        }
}
