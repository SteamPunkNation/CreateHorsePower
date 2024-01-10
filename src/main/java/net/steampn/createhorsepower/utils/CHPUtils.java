package net.steampn.createhorsepower.utils;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;
import org.slf4j.Logger;

import java.util.List;

import static net.steampn.createhorsepower.utils.CHPProperties.*;
import static net.steampn.createhorsepower.utils.CHPTags.Entities.*;

public class CHPUtils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Mob getWorker(Level world, BlockPos pos) {
        List<Mob> mobs = world.getEntitiesOfClass(Mob.class, new AABB(pos).inflate(3.0D));
        for(Mob mob : mobs) {
            if(mob.isLeashed() && mob.getLeashHolder() instanceof CHPLeashKnotEntity && ((CHPLeashKnotEntity) mob.getLeashHolder()).getPos().equals(pos)) {
                LOGGER.info("Worker found: " + mob);
                LOGGER.info("Is leashed?: " + (mob.isLeashed() ? "True" : "False"));
                return mob;
            }
        }
        LOGGER.info("No worker found at pos: " + pos);
        return null;
    }

    public static InteractionResult checkForMobsInVicinity(List<Mob> mobsInVicinity, Player player, Level worldIn, BlockPos pos, BlockState state){
        for(Mob mob : mobsInVicinity) {
            if(mob.isLeashed() && mob.getLeashHolder() == player) {
                if(!(mob.getType().getTags().toList().contains(LARGE_WORKER_TAG)
                        || mob.getType().getTags().toList().contains(MEDIUM_WORKER_TAG)
                        || mob.getType().getTags().toList().contains(SMALL_WORKER_TAG))) {
                    player.displayClientMessage(Component.translatable("tooltip3.createhorsepower.horse_crank"),true);
                    return InteractionResult.FAIL; // mob does not have any of the valid tags, so early return
                }

                CHPLeashKnotEntity leashKnot = new CHPLeashKnotEntity(worldIn, pos);
                worldIn.addFreshEntity(leashKnot);
                mob.setLeashedTo(leashKnot, true);
//                        if(!player.isCreative()) {
//                            stack.shrink(1); // remove lead from player inventory
//                        }
                updateBlockStateBasedOnMob(mob, state, worldIn, pos);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    public static void updateBlockStateBasedOnMob(Mob mob, BlockState state, Level world, BlockPos pos) {
        if(mob.getType().getTags().toList().contains(LARGE_WORKER_TAG)) {
            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_LARGE_STATE, true), 3);
        }
        else if(mob.getType().getTags().toList().contains(MEDIUM_WORKER_TAG)) {
            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_MEDIUM_STATE, true), 3);
        }
        else if(mob.getType().getTags().toList().contains(SMALL_WORKER_TAG)) {
            world.setBlock(pos, state.setValue(HAS_WORKER, true).setValue(WORKER_SMALL_STATE, true), 3);
        }
    }
}
