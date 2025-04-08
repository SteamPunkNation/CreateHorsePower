package net.steampn.createhorsepower.client.ponders;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.steampn.createhorsepower.registry.BlockRegister;

public class HorseCrankPonderTags {

  public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {

    PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(
        RegistryEntry::getId);

    HELPER.addToTag(AllCreatePonderTags.KINETIC_SOURCES)
        .add(BlockRegister.HORSE_CRANK);
  }

}
