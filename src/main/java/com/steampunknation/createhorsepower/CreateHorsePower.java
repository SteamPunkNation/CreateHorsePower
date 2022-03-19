package com.steampunknation.createhorsepower;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.providers.ProviderType;
import com.simibubi.create.repack.registrate.util.NonNullLazyValue;
import com.steampunknation.createhorsepower.utils.CHPTags;
import com.steampunknation.createhorsepower.utils.CHPUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;




@Mod(CreateHorsePower.MOD_ID)
public class CreateHorsePower {

    public static final String MOD_ID = "createhorsepower";
    private static final NonNullLazyValue<CreateRegistrate> CREATEREGISTRATE = CreateRegistrate.lazy(MOD_ID);

    public CreateHorsePower() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        BlockRegister.register();
        TileEntityRegister.register();
        CHPUtils.register();

        //Registration Continue
        eventBus.addListener(this::setup);
        eventBus.addListener(this::enqueueIMC);
        eventBus.addListener(this::processIMC);
        eventBus.addListener(this::postInit);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CHPBlockPartials::load);
    }

    private void setup(final FMLCommonSetupEvent event){

    }

    private void enqueueIMC(final InterModEnqueueEvent event){

    }

    private void processIMC(final InterModProcessEvent event){

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event){

    }

    public void postInit(FMLLoadCompleteEvent event){

        System.out.println("Create Horse Power Initialized!");

    }

    public static CreateRegistrate registrate(){
        return CREATEREGISTRATE.get();
    }

    public static ResourceLocation asResource(String path){
        return new ResourceLocation(MOD_ID, path);
    }
}
