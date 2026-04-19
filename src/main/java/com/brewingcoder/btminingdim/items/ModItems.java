package com.brewingcoder.btminingdim.items;

import com.brewingcoder.btminingdim.BTMiningDim;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(BTMiningDim.MODID);

    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
