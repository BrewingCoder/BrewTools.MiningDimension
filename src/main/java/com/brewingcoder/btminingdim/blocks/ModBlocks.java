package com.brewingcoder.btminingdim.blocks;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.items.ModCreativeModeTab;
import com.brewingcoder.btminingdim.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BTMiningDim.MODID);

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name,block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
        return ModItems.ITEMS.register(name,()->new BlockItem(block.get(),new Item.Properties().tab(ModCreativeModeTab.MAIN)));
    }

    private static final BlockBehaviour.Properties defaultProps = BlockBehaviour.Properties.of(Material.STONE);

    public static final RegistryObject<Block> MINING_PORTAL = registerBlock("mining_portal",()->new MiningPortalBlock(defaultProps.destroyTime(1f)));
}
