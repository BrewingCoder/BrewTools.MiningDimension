package com.brewingcoder.btminingdim.world;

import com.brewingcoder.btminingdim.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Helpers for picking a destination position when entering / leaving the
 * mining dimension. Replaces the old Forge `ITeleporter` interface (removed
 * in NeoForge 1.21.x) — callers now do the search here, then hand the result
 * to MC's `Entity.teleport(TeleportTransition)` directly.
 *
 * Search rules:
 *   1. Find an existing portal block in the destination chunk → land on top of it.
 *   2. If none, place a new portal on the highest non-air column in that chunk
 *      that has 3 vertical air blocks above it → land on the new portal.
 *   3. If neither succeeds (no buildable space at all), return null and the
 *      caller should leave the player put.
 */
public final class MiningWorldTeleporter {
    private MiningWorldTeleporter() {}

    /**
     * Returns the world-space position the player should arrive at in
     * [destWorld], near the chunk at [originPos]. Null = couldn't find or
     * place a portal (rare).
     */
    @Nullable
    public static Vec3 findOrPlaceArrival(ServerLevel destWorld, BlockPos originPos) {
        ChunkAccess chunk = destWorld.getChunk(originPos);
        BlockPos portal = findExistingPortal(chunk);
        if (portal == null) portal = placeNewPortal(destWorld, chunk);
        if (portal == null) return null;
        return new Vec3(portal.getX() + 0.5, portal.getY() + 1.0, portal.getZ() + 0.5);
    }

    @Nullable
    private static BlockPos findExistingPortal(ChunkAccess chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y >= 1; y--) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).is(ModBlocks.MINING_PORTAL.get())) {
                        return chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private static BlockPos placeNewPortal(ServerLevel world, ChunkAccess chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y >= 1; y--) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).is(Blocks.AIR)) continue;
                    if (chunk.getBlockState(pos.above(1)).is(Blocks.AIR)
                            && chunk.getBlockState(pos.above(2)).is(Blocks.AIR)
                            && chunk.getBlockState(pos.above(3)).is(Blocks.AIR)) {
                        BlockPos absolute = chunk.getPos().getWorldPosition()
                                .offset(pos.getX(), pos.getY() + 1, pos.getZ());
                        world.setBlockAndUpdate(absolute, ModBlocks.MINING_PORTAL.get().defaultBlockState());
                        return absolute;
                    }
                }
            }
        }
        return null;
    }
}
