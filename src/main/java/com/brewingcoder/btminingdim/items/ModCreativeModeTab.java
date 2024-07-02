package com.brewingcoder.btminingdim.items;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BTMiningDim.MODID);

    public static final RegistryObject<CreativeModeTab> BTM_TAB = CREATIVE_MODE_TABS.register("btminingdim_tab",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModBlocks.MINING_PORTAL.get().asItem()))
                    .title(Component.translatable("creativetab.brewtools"))
                    .displayItems((pParameters,pOutput) ->{
                        pOutput.accept(ModBlocks.MINING_PORTAL.get());
                    }).build()
            );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}
