package net.steampn.createhorsepower.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;

import static net.minecraftforge.versions.forge.ForgeVersion.MOD_ID;

public class EntityRegister {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<EntityType<CHPLeashKnotEntity>> CHP_LEASH_KNOT = ENTITIES.register("chp_leash_knot",
            () -> EntityType.Builder.of(CHPLeashKnotEntity::create, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setShouldReceiveVelocityUpdates(false)
                    .build(new ResourceLocation(MOD_ID, "chp_leash_knot").toString()));

    //TODO
    // Replace creating a extend of LeashKnotEntity and create custom entity with no render model, only hit-box that is
    // 0.2 ^ 3 big. All it needs to do is be summoned inside the CrankBlock, attach a worker to it, and then be killed
    // when the worker is removed (block right clicked with no item and worker connected)
    // Resource: https://www.youtube.com/watch?v=nZHU6hTAI0o



    public static void register(){}

}
