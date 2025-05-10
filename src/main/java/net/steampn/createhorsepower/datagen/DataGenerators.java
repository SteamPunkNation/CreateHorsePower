package net.steampn.createhorsepower.datagen;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

import com.tterrag.registrate.providers.ProviderType;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.client.ponders.HorseCrankPonderPlugin;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new CHPRecipeProvider(packOutput));

        CHPBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new CHPBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new CHPItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
        CreateHorsePower.CREATE_REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;
            providePonderLang(langConsumer);
        });
    }

    private static void providePonderLang(BiConsumer<String, String> consumer) {
        PonderIndex.addPlugin(new HorseCrankPonderPlugin());

        PonderIndex.getLangAccess().provideLang(MODID, consumer);
    }
}
