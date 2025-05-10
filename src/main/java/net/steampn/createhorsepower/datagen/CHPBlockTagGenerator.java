package net.steampn.createhorsepower.datagen;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.steampn.createhorsepower.registry.BlockRegister;
import org.jetbrains.annotations.Nullable;

public class CHPBlockTagGenerator extends BlockTagsProvider {
    public CHPBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegister.HORSE_CRANK.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockRegister.HORSE_CRANK.get());
    }
}
