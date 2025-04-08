package net.steampn.createhorsepower.utils;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.steampn.createhorsepower.CreateHorsePower;

public class CHPBlockPartials {
    public static final PartialModel HORSE_CRANK_COG = get("horse_crank/inner");
    public static PartialModel HORSE_CRANK_BASE = get("horse_crank/block");

    public static PartialModel get(String path){
        return PartialModel.of(CreateHorsePower.asResource("block/" + path));
    }


    public static void load(){}
}
