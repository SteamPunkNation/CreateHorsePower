package net.steampn.createhorsepower.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

public class CHPTags {
    public static class Entities {
        public static final TagKey<EntityType<?>> SMALL_WORKER_TAG = tag("worker_small");
        public static final TagKey<EntityType<?>> MEDIUM_WORKER_TAG = tag("worker_medium");
        public static final TagKey<EntityType<?>> LARGE_WORKER_TAG = tag("worker_large");

        private static TagKey<EntityType<?>> tag(String name){
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, name));
        }
    }
}
