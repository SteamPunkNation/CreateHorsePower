package net.steampn.createhorsepower.datagen;

import com.simibubi.create.AllBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.steampn.createhorsepower.registry.BlockRegister;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CHPRecipeProvider extends RecipeProvider implements net.neoforged.neoforge.common.conditions.IConditionBuilder {
    public CHPRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries){
        super(packOutput, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegister.HORSE_CRANK.get())
                .pattern(" F ")
                .pattern(" C ")
                .pattern("SSS")
                .define('F', Blocks.OAK_FENCE)
                .define('C', AllBlocks.COGWHEEL.get())
                .define('S', Blocks.STONE)
                .unlockedBy(getHasName(AllBlocks.COGWHEEL.get()), has(AllBlocks.COGWHEEL.get()))
                .save(recipeOutput);
    }
}
