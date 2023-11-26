package net.steampn.createhorsepower.utils;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.steampn.createhorsepower.client.model.CHPLeashKnotModel;
import net.steampn.createhorsepower.client.renderer.CHPLeashKnotRenderer;

import static net.steampn.createhorsepower.CreateHorsePower.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(EntityRegister.CHP_LEASH_KNOT.get(), CHPLeashKnotRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(CHPLeashKnotModel.LAYER_LOCATION, CHPLeashKnotModel::createBodyLayer);
    }
}
