package com.steampunknation.createhorsepower.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class CHPTags {
    public static class Entities {

        public static final Tags.IOptionalNamedTag<EntityType<?>> SMALL_WORKER = tag("worker_small");
        public static final Tags.IOptionalNamedTag<EntityType<?>> MEDIUM_WORKER = tag("worker_medium");
        public static final Tags.IOptionalNamedTag<EntityType<?>> LARGE_WORKER = tag("worker_large");

        private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name) {
            return EntityTypeTags.createOptional(new ResourceLocation("createhorsepower:" + name));
        }
    }
}
