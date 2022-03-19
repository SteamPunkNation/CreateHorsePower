package com.steampunknation.createhorsepower.utils;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

public class CHPUtils {
    public static void register(){}

    public static Entity getEntityWithinArea(World world, AxisAlignedBB alignedBB, Predicate<CreatureEntity> predicate) {
        List<CreatureEntity> entities = world.getEntitiesOfClass(CreatureEntity.class, alignedBB,
                e -> CHPTags.Entities.WORKER_ENTITIES.contains(e.getType()) && predicate.test(e));
        if (entities.size() > 0) {
            return entities.get(0);
        }
        return null;
    }
}
