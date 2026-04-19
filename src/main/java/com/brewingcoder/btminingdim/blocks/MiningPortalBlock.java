package com.brewingcoder.btminingdim.blocks;

import com.brewingcoder.btminingdim.BTMiningDim;
import com.brewingcoder.btminingdim.world.MiningWorldTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class MiningPortalBlock extends Block {

    public MiningPortalBlock(Properties props) {
        super(props);
    }

    /**
     * Right-click (no item, not sneaking) toggles between the overworld and
     * the mining dimension. Server-only. Server picks (or builds) a portal in
     * the destination chunk via [MiningWorldTeleporter] and teleports the
     * player there using the 1.21.x [TeleportTransition] API.
     */
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                            Player player, BlockHitResult hit) {
        if (player.isCrouching() || level.isClientSide) return InteractionResult.PASS;

        boolean inMining = level.dimension().equals(BTMiningDim.MINING_WORLD);
        ServerLevel destLevel = level.getServer().getLevel(
                inMining ? BTMiningDim.OVERWORLD : BTMiningDim.MINING_WORLD
        );
        if (destLevel == null) return InteractionResult.PASS;

        Vec3 arrival = MiningWorldTeleporter.findOrPlaceArrival(destLevel, pos);
        if (arrival == null) return InteractionResult.PASS;

        // Preserve facing + zero-out velocity so the player doesn't fly off
        // the new portal block on arrival. DO_NOTHING = no portal sound,
        // no portal-cooldown ticket — feels like a doorway, not a vanilla portal.
        DimensionTransition transition = new DimensionTransition(
                destLevel, arrival, Vec3.ZERO,
                player.getYRot(), player.getXRot(),
                DimensionTransition.DO_NOTHING
        );
        player.changeDimension(transition);
        return InteractionResult.SUCCESS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, RandomSource random) {
        random.nextFloat();
        BlockState here = level.getBlockState(pos);
        if (here.is(ModBlocks.MINING_PORTAL.get())) {
            level.addParticle(ParticleTypes.PORTAL,
                    pos.getX() + (random.nextDouble() - 0.5) * 1.5,
                    pos.getY() + 2,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0, 0, 0);
            level.addParticle(ParticleTypes.ENCHANT,
                    pos.getX() + (random.nextDouble() - 0.5) * 1.5,
                    pos.getY() + 2,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 1.5, 0, 0, 0);
        }
    }
}
