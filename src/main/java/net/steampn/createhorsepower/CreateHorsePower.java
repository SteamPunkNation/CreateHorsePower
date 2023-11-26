package net.steampn.createhorsepower;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.utils.BlockRegister;
import net.steampn.createhorsepower.utils.CHPBlockPartials;
import net.steampn.createhorsepower.utils.EntityRegister;
import net.steampn.createhorsepower.utils.TileEntityRegister;
import org.slf4j.Logger;

@Mod(CreateHorsePower.MODID)
public class CreateHorsePower
{
    public static final String MODID = "createhorsepower";

    public static final CreateRegistrate CREATEREGISTRATE = CreateRegistrate.create(MODID);

    private static final Logger LOGGER = LogUtils.getLogger();
    public CreateHorsePower()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Config.registerConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("createhorsepower-common.toml"));

        CREATEREGISTRATE.registerEventListeners(modEventBus);
        BlockRegister.register();
        TileEntityRegister.register();
        EntityRegister.ENTITIES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> CHPBlockPartials::load);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("Loading CreateHorsePower Blocks, Items, Etc.");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    public static ResourceLocation asResource(String path){
        return new ResourceLocation(MODID, path);
    }
}
