package com.brewingcoder.btminingdim.world;

import com.brewingcoder.btminingdim.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class MiningWorldTeleporter implements ITeleporter {
    private final BlockPos pos;

    public MiningWorldTeleporter(BlockPos pos){
        this.pos=pos;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity e = repositionEntity.apply(false);
        if(!(e instanceof ServerPlayer player)) return e;

        ChunkAccess chunk = destWorld.getChunk(pos);
        BlockPos portalBlockPos = findDestinationPortal(chunk);
        if (portalBlockPos == null){
            portalBlockPos = PlacePortalBlock(destWorld,chunk);
        }
        if(portalBlockPos == null) return e;

        player.giveExperienceLevels(0);
        player.teleportTo(portalBlockPos.getX() + 0.5D, portalBlockPos.getY()+1D, portalBlockPos.getZ()+0.5D);
        return e;
    }

    private BlockPos findDestinationPortal(ChunkAccess chunk){
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 255; y >= 1; y--) {
                    pos.set(x, y, z);
                    if (chunk.getBlockState(pos).is(ModBlocks.MINING_PORTAL.get())){
                        return chunk.getPos().getWorldPosition().offset(pos.getX(),pos.getY(),pos.getZ());
                    }
                }
            }
        }
        return null;
    }

    private BlockPos PlacePortalBlock(ServerLevel world, ChunkAccess chunk){
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for(int y=255; y >= 1; y--){
                    pos.set(x,y,z);
                    if(chunk.getBlockState(pos).is(Blocks.AIR)){
                        continue;
                    }
                    if (chunk.getBlockState(pos.above(1)).is(Blocks.AIR) && chunk.getBlockState(pos.above(2)).is(Blocks.AIR) && chunk.getBlockState(pos.above(3)).is(Blocks.AIR)) {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY()+1, pos.getZ());
                        world.setBlockAndUpdate(absolutePos, ModBlocks.MINING_PORTAL.get().defaultBlockState());
                        return absolutePos;
                    }
                }
            }
        }
        return null;
    }

}
