package com.steampunknation.createhorsepower.utils;

import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import com.steampunknation.createhorsepower.CreateHorsePower;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankCogInstance;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankRenderer;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankTileEntity;

public class TileEntityRegister {

    public static final TileEntityEntry<HorseCrankTileEntity> HORSE_CRANK = CreateHorsePower.registrate()
            .tileEntity("horse_crank", HorseCrankTileEntity::new)
            .instance(() -> HorseCrankCogInstance::new)
            .validBlocks(BlockRegister.HORSE_CRANK)
            .renderer(() -> HorseCrankRenderer::new)
            .register();

    public static void register(){}

}
