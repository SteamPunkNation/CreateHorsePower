package com.steampunknation.createhorsepower.utils;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class CHPTags {
    public static class Entities {

        public static final Tags.IOptionalNamedTag<EntityType<?>> WORKER_ENTITIES = tag("worker");

        private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name){
            return EntityTypeTags.createOptional(new ResourceLocation("createhorsepower:" + name));
        }
    }
}
