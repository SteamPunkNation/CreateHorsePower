package net.steampn.createhorsepower.registry;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.steampn.createhorsepower.client.ponders.HorseCrankBaseScene;
import net.steampn.createhorsepower.client.ponders.HorseCrankPonderTags;

public class PonderRegister {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper){
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(
            RegistryEntry::getId);

        HELPER.forComponents(BlockRegister.HORSE_CRANK)
                .addStoryBoard("horse_crank/horse_crank_intro", HorseCrankBaseScene::intro);
    }
}
