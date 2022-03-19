package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.jozufozu.flywheel.backend.instancing.IDynamicInstance;
import com.jozufozu.flywheel.backend.instancing.Instancer;
import com.jozufozu.flywheel.backend.material.MaterialManager;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.RotatingData;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.steampunknation.createhorsepower.CHPBlockPartials;
import com.steampunknation.createhorsepower.CHPShapes;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

public class HorseCrankCogInstance extends SingleRotatingInstance {


    public HorseCrankCogInstance(MaterialManager<?> modelManager, KineticTileEntity tile) {
        super(modelManager, tile);
    }

    @Override
    protected Instancer<RotatingData> getModel() {
        return getRotatingMaterial().getModel(CHPBlockPartials.HORSE_CRANK_COG, tile.getBlockState());
    }
}
