package net.steampn.createhorsepower.blocks.horse_crank;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import net.steampn.createhorsepower.utils.CHPBlockPartials;

public class HorseCrankCogInstance extends SingleRotatingInstance<HorseCrankTileEntity> {


    public HorseCrankCogInstance(MaterialManager materialManager, HorseCrankTileEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return this.getRotatingMaterial().getModel(CHPBlockPartials.HORSE_CRANK_COG, this.getRenderedBlockState());
    }
}
