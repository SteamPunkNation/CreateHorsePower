package com.steampunknation.createhorsepower.utils;

import com.jozufozu.flywheel.core.PartialModel;
import com.steampunknation.createhorsepower.CreateHorsePower;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CHPBlockPartials {

    public static final PartialModel HORSE_CRANK_COG = get("horse_crank/inner");
    public static final PartialModel HORSE_CRANK_BASE = get("horse_crank/block");

    public static PartialModel get(String path){
        return new PartialModel(CreateHorsePower.asResource("block/" + path));
    }
    public static void load(){
    }
}
