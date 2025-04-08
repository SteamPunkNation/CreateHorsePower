package net.steampn.createhorsepower.client.ponders;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.registry.PonderRegister;
import org.jetbrains.annotations.NotNull;

public class HorseCrankPonderPlugin implements PonderPlugin {

  @Override
  public @NotNull String getModId() {
    return CreateHorsePower.MODID;
  }

  @Override
  public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
    PonderRegister.register(helper);
  }

  @Override
  public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
    HorseCrankPonderTags.register(helper);
  }
}
