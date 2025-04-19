package net.steampn.createhorsepower.config;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.steampn.createhorsepower.utils.CHPTags;
import net.steampn.createhorsepower.utils.CHPTags.Entities;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_WORKERS = "workers";
    private static final String CATEGORY_PATHS = "paths";
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.IntValue BASE_CREATURE_RPM, SMALL_CREATURE_STRESS, MEDIUM_CREATURE_STRESS, LARGE_CREATURE_STRESS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> SMALL_CREATURES, MEDIUM_CREATURES, LARGE_CREATURES, POOR_PATH, NORMAL_PATH, GREAT_PATH;

    static {
        //Category Title
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);

        //Creature RPM
        BASE_CREATURE_RPM = COMMON_BUILDER.comment("Base rpm creatures can spin the horse crank.").defineInRange("creatureRPMRange", 16, 1, Integer.MAX_VALUE);

        //Creature Stress
        SMALL_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress small creatures can produce for the horse crank.").defineInRange("smallCreatureStressRange", 64, 1, Integer.MAX_VALUE);
        MEDIUM_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress medium creatures can produce for the horse crank.").defineInRange("mediumCreatureStressRange", 128, 1, Integer.MAX_VALUE);
        LARGE_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress large creatures can produce for the horse crank.").defineInRange("largeCreatureStressRange", 256, 1, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Path Category").push(CATEGORY_PATHS);

        //Paths for creatures to move on
        POOR_PATH = COMMON_BUILDER.comment("Types of blocks valid as \"Poor\" quality,").worldRestart().defineList("poor_path_block", List.of("minecraft:dirt", "minecraft:grass_block"), Config::validateBlockName);
        NORMAL_PATH = COMMON_BUILDER.comment("Types of blocks valid as \"Normal\" quality,").worldRestart().defineList("normal_path_block", List.of("minecraft:dirt_path", "minecraft:gravel"), Config::validateBlockName);
        GREAT_PATH = COMMON_BUILDER.comment("Types of blocks valid as \"Great\" quality,").worldRestart().defineList("great_path_block", List.of("minecraft:ice", "minecraft:packed_ice", "minecraft:blue_ice"), Config::validateBlockName);

        COMMON_BUILDER.pop();

        //Category Title
        COMMON_BUILDER.comment("Creature Category").push(CATEGORY_WORKERS);

        //Valid Creatures
        SMALL_CREATURES = COMMON_BUILDER.comment("Valid \"Small\" creatures").worldRestart().defineList("small_creatures", List.of("minecraft:wolf"), Config::validateMobName);
        MEDIUM_CREATURES = COMMON_BUILDER.comment("Valid \"Medium\" creatures").worldRestart().defineList("medium_creatures", List.of("minecraft:cow"), Config::validateMobName);
        LARGE_CREATURES = COMMON_BUILDER.comment("Valid \"Large\" creatures").worldRestart().defineList("large_creatures", List.of("minecraft:horse"), Config::validateMobName);

    }

    public static final ForgeConfigSpec SPEC = COMMON_BUILDER.build();
    public static int base_creature_rpm, small_creature_stress, medium_creature_stress, large_creature_stress;
    public static Set<ResourceLocation> small_mobs, medium_mobs, large_mobs;
    public static Set<Block> poor_path, normal_path, great_path;
    private static boolean validateMobName(final Object obj){
        return obj instanceof final String mobName && ForgeRegistries.ENTITY_TYPES.containsKey(new ResourceLocation(mobName));
    }

    private static boolean validateBlockName(final Object obj){
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event){
        LOGGER.info("Create Horse Power config has been loaded!");
        base_creature_rpm = BASE_CREATURE_RPM.get();

        small_creature_stress = SMALL_CREATURE_STRESS.get();
        medium_creature_stress = MEDIUM_CREATURE_STRESS.get();
        large_creature_stress = LARGE_CREATURE_STRESS.get();

        small_mobs = SMALL_CREATURES.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());
        medium_mobs = MEDIUM_CREATURES.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());
        large_mobs = LARGE_CREATURES.get().stream()
                .map(ResourceLocation::new)
                .collect(Collectors.toSet());

        poor_path = POOR_PATH.get().stream()
                .map(pathName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(pathName)))
                .collect(Collectors.toSet());
        normal_path = NORMAL_PATH.get().stream()
                .map(pathName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(pathName)))
                .collect(Collectors.toSet());
        great_path = GREAT_PATH.get().stream()
                .map(pathName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(pathName)))
                .collect(Collectors.toSet());
    }
}
