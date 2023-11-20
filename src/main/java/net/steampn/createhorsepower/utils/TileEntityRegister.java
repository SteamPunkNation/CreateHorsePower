package net.steampn.createhorsepower.utils;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankCogInstance;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankTileEntity;

public class TileEntityRegister {

    public static final BlockEntityEntry<HorseCrankTileEntity> HORSE_CRANK = CreateHorsePower.CREATEREGISTRATE
            .blockEntity("horse_crank", HorseCrankTileEntity::new)
            .instance(() -> HorseCrankCogInstance::new)
            .validBlocks(BlockRegister.HORSE_CRANK)
            .register();

    public static void register(){}
}
