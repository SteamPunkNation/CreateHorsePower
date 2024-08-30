package net.steampn.createhorsepower.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankCogInstance;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankTileEntity;

public class TileEntityRegister {

    public static final BlockEntityEntry<HorseCrankTileEntity> HORSE_CRANK = CreateHorsePower.CREATE_REGISTRATE
            .blockEntity("horse_crank", HorseCrankTileEntity::new)
            .instance(() -> HorseCrankCogInstance::new)
            .validBlock(BlockRegister.HORSE_CRANK)
            .register();

    public static void register(){}
}
