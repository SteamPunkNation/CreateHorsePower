package com.steampunknation.createhorsepower.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_WORKERS = "workers";

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    //Actual values
    public static ForgeConfigSpec.IntValue SMALL_CREATURE_SPEED;
    public static ForgeConfigSpec.IntValue MEDIUM_CREATURE_SPEED;
    public static ForgeConfigSpec.IntValue LARGE_CREATURE_SPEED;

    public static ForgeConfigSpec.IntValue SMALL_CREATURE_STRESS;
    public static ForgeConfigSpec.IntValue MEDIUM_CREATURE_STRESS;
    public static ForgeConfigSpec.IntValue LARGE_CREATURE_STRESS;

    static {
        //Category Title
        COMMON_BUILDER.comment("General Settings").push(CATEGORY_GENERAL);
        //Category Values
        //Small Creatures
        SMALL_CREATURE_SPEED = COMMON_BUILDER.comment("How fast small creatures can spin the horse crank for.")
                        .defineInRange("small_creature_speed_range", 4, 1, Integer.MAX_VALUE);
        SMALL_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress small creatures can produce for the horse crank.")
                        .defineInRange("small_creature_stress_range", 16, 1, Integer.MAX_VALUE);

        //Medium Creatures
        MEDIUM_CREATURE_SPEED = COMMON_BUILDER.comment("How fast medium creatures can spin the horse crank for.")
                .defineInRange("medium_creature_speed_range", 8, 1, Integer.MAX_VALUE);
        MEDIUM_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress medium creatures can produce for the horse crank.")
                .defineInRange("medium_creature_stress_range", 32, 1, Integer.MAX_VALUE);

        //Large Creatures
        LARGE_CREATURE_SPEED = COMMON_BUILDER.comment("How fast large creatures can spin the horse crank for.")
                .defineInRange("large_creature_speed_range", 16, 1, Integer.MAX_VALUE);
        LARGE_CREATURE_STRESS = COMMON_BUILDER.comment("How much stress large creatures can produce for the horse crank.")
                .defineInRange("large_creature_stress_range", 64, 1, Integer.MAX_VALUE);

        //Create new category space after this line
        COMMON_BUILDER.pop();

        //Category Title
        COMMON_BUILDER.comment("Creature Categories").push(CATEGORY_WORKERS);

        //Create config if it does not exist yet
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void registerConfig(ForgeConfigSpec configSpec, java.nio.file.Path path){
        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        configData.load();
        configSpec.setConfig(configData);
    }

}
