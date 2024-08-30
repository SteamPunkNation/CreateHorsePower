package net.steampn.createhorsepower.utils;

import com.jozufozu.flywheel.core.PartialModel;
import net.steampn.createhorsepower.CreateHorsePower;

public class CHPBlockPartials {
    public static PartialModel HORSE_CRANK_COG = get("horse_crank/inner");
    public static PartialModel HORSE_CRANK_BASE = get("horse_crank/block");

    public static PartialModel get(String path){
        return new PartialModel(CreateHorsePower.asResource("block/" + path));
    }

    public static void load(){}
}
