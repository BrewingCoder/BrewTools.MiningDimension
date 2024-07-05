package com.brewingcoder.btminingdim.items;

import com.brewingcoder.btminingdim.BTMiningDim;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BTMiningDim.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
