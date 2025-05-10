package net.steampn.createhorsepower.registry;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankCogInstance;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankRender;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankTileEntity;

public class TileEntityRegister {

    public static final BlockEntityEntry<HorseCrankTileEntity> HORSE_CRANK = CreateHorsePower.CREATE_REGISTRATE
            .blockEntity("horse_crank", HorseCrankTileEntity::new)
            .visual(() -> HorseCrankCogInstance::new, false)
            .validBlock(BlockRegister.HORSE_CRANK)
            .renderer(() -> HorseCrankRender::new)
            .register();

    public static void register(){}
}
