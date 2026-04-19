package com.brewingcoder.btminingdim;

import com.brewingcoder.btminingdim.blocks.ModBlocks;
import com.brewingcoder.btminingdim.items.ModCreativeModeTab;
import com.brewingcoder.btminingdim.items.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(BTMiningDim.MODID)
public class BTMiningDim {

    public static final String MODID = "btminingdim";
    private static final Logger LOGGER = LogUtils.getLogger();

    /** Set during common setup so peripheral classes can `level.dimension().equals(MINING_WORLD)`. */
    public static ResourceKey<Level> MINING_WORLD;
    public static ResourceKey<Level> OVERWORLD;

    /**
     * NeoForge passes the mod-event bus and container directly to the constructor —
     * no `FMLJavaModLoadingContext.get()` dance required.
     */
    public BTMiningDim(IEventBus modBus, ModContainer container) {
        ModItems.register(modBus);
        ModBlocks.register(modBus);
        ModCreativeModeTab.register(modBus);

        modBus.addListener(this::commonSetup);

        // Game-event bus is the GLOBAL one (NeoForge.EVENT_BUS) — we hand-register
        // so @SubscribeEvent on instance methods like onServerStarting fires.
        NeoForge.EVENT_BUS.register(this);

        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        MINING_WORLD = ResourceKey.create(Registries.DIMENSION,
                ResourceLocation.fromNamespaceAndPath(MODID, "mining_world"));
        OVERWORLD = ResourceKey.create(Registries.DIMENSION,
                ResourceLocation.parse("minecraft:overworld"));
        LOGGER.info("BTMiningDim common setup complete");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("BTMiningDim ready on server '{}'", event.getServer().getMotd());
    }

    /**
     * Static client-side init. Currently a stub — the portal block uses vanilla
     * particle rendering so there's nothing client-specific to register yet.
     */
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("BTMiningDim client setup — running as {}",
                    Minecraft.getInstance().getUser().getName());
        }
    }
}
