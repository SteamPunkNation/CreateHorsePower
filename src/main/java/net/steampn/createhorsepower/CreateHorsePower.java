package net.steampn.createhorsepower;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.steampn.createhorsepower.config.Config;
import net.steampn.createhorsepower.registry.BlockRegister;
import net.steampn.createhorsepower.registry.TileEntityRegister;
import net.steampn.createhorsepower.utils.CHPBlockPartials;
import org.slf4j.Logger;


@Mod(CreateHorsePower.MODID)
public class CreateHorsePower {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "createhorsepower";
    public static final CreateRegistrate CREATE_REGISTRATE = CreateRegistrate.create(MODID);

    public CreateHorsePower(){
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CREATE_REGISTRATE.registerEventListeners(modEventBus);

        BlockRegister.register();
        TileEntityRegister.register();
        modEventBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event){
        LOGGER.debug("{} is registered!", BlockRegister.HORSE_CRANK.get());
    }

    @SubscribeEvent
    public void serverSetup(final ServerStartingEvent event){
        configFileDebug();
    }

    private void configFileDebug(){
        LOGGER.info("Base RPM for all creatures is {}", Config.base_creature_rpm);
        LOGGER.info("Stress for Small is {}", Config.small_creature_stress);
        LOGGER.info("Stress for Medium is {}", Config.medium_creature_stress);
        LOGGER.info("Stress for Large is {}", Config.large_creature_stress);

        Config.small_mobs.forEach((mob) -> LOGGER.info("Selected Small mob: {}", mob));
        Config.medium_mobs.forEach((mob) -> LOGGER.info("Selected Medium mob: {}", mob));
        Config.large_mobs.forEach((mob) -> LOGGER.info("Selected Large mob: {}", mob));

        Config.poor_path.forEach((block) -> LOGGER.info("Selected Poor Path Block: {}", block));
        Config.normal_path.forEach((block) -> LOGGER.info("Selected Normal Path Block: {}", block));
        Config.great_path.forEach((block) -> LOGGER.info("Selected Great Path Block: {}", block));
    }

    public static ResourceLocation asResource(String path){
        return new ResourceLocation(MODID, path);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents{

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event){
            CHPBlockPartials.load();
        }
    }
}
