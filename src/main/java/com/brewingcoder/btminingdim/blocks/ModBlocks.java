package com.brewingcoder.btminingdim.blocks;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(BTMiningDim.MODID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }

    private static final BlockBehaviour.Properties defaultProps =
            BlockBehaviour.Properties.of().sound(SoundType.STONE);

    public static final DeferredBlock<Block> MINING_PORTAL =
            registerBlock("mining_portal", MiningPortalBlock::new, defaultProps.destroyTime(1f));

    /**
     * Register [block] under [name] AND a matching BlockItem so it appears in
     * the creative tab + can be /given to players.
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(
            String name,
            Function<BlockBehaviour.Properties, T> ctor,
            BlockBehaviour.Properties props
    ) {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, ctor, props);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
}
