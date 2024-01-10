package net.steampn.createhorsepower.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.steampn.createhorsepower.entities.CHPLeashKnotEntity;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;


public class EntityRegister {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<CHPLeashKnotEntity>> CHP_LEASH_KNOT = ENTITIES.register("chp_leash_knot",
            () -> EntityType.Builder.of(CHPLeashKnotEntity::create, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setShouldReceiveVelocityUpdates(false)
                    .build(new ResourceLocation(MODID, "chp_leash_knot").toString()));



    public static void register(){}

}
