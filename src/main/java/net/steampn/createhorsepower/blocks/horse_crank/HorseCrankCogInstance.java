package net.steampn.createhorsepower.blocks.horse_crank;

import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.steampn.createhorsepower.utils.CHPBlockPartials;

public class HorseCrankCogInstance extends SingleAxisRotatingVisual<HorseCrankTileEntity> {


    public HorseCrankCogInstance(
        VisualizationContext context,
        HorseCrankTileEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(CHPBlockPartials.HORSE_CRANK_COG));
    }
}
