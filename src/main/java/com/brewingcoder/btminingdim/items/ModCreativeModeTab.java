package com.brewingcoder.btminingdim.items;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.blocks.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab MAIN = new CreativeModeTab(BTMiningDim.MODID) {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.MINING_PORTAL.get().asItem());
        }
    };
}
