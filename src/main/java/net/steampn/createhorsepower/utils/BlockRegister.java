package net.steampn.createhorsepower.utils;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTabs;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class BlockRegister {

    private static final CreateRegistrate REGISTRATE = CreateHorsePower.CREATEREGISTRATE
            .defaultCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);

    public static final BlockEntry<HorseCrankBlock> HORSE_CRANK = REGISTRATE.block("horse_crank", HorseCrankBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .item()
            .transform(customItemModel())
            .register();

    public static void register(){}
}
