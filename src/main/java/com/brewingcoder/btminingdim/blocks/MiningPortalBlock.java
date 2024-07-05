package com.brewingcoder.btminingdim.blocks;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.world.MiningWorldTeleporter;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MiningPortalBlock extends Block {

    public MiningPortalBlock(Properties props){
        super(props);
    }


    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult){
        if( !player.isCrouching()){
            if(player.canChangeDimensions() && !level.isClientSide) {
                if (level.dimension().equals(BTMiningDim.MINING_WORLD)){
                     ServerLevel destination = level.getServer().getLevel(BTMiningDim.OVERWORLD);
                    player.changeDimension(destination,new MiningWorldTeleporter(destination,pos));
                    return InteractionResult.SUCCESS;
                }
                if (level.dimension().equals(BTMiningDim.OVERWORLD)){
                    //ServerLevel destination = level.getServer().getLevel(BTMiningDim.MINING_WORLD);
                    ResourceKey destination = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("btminingdim:mining_world"));
                    ServerPlayer sp = (ServerPlayer)player;
                    ServerLevel destLevel = sp.getServer().getLevel(BTMiningDim.MINING_WORLD);
                    Iterable<ServerLevel> Levels = sp.getServer().getAllLevels();

                    player.changeDimension(destLevel, new MiningWorldTeleporter(destLevel,pos));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    };


    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, RandomSource random) {
        random.nextFloat();
        BlockState te = level.getBlockState(pos);
        if (te.is(ModBlocks.MINING_PORTAL.get())){
            level.addParticle(ParticleTypes.PORTAL, pos.getX() +  (random.nextDouble() - 0.5) * 1.5, pos.getY() + 2, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0, 0, 0);
            level.addParticle(ParticleTypes.ENCHANT, pos.getX() + (random.nextDouble() - 0.5) * 1.5, pos.getY() + 2, pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0, 0, 0);
        }
    }
}
