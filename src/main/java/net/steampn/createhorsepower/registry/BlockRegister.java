package net.steampn.createhorsepower.registry;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.material.MapColor;
import net.steampn.createhorsepower.CreateHorsePower;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class BlockRegister {

    public static final CreateRegistrate REGISTRATE = CreateHorsePower.CREATE_REGISTRATE.defaultCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS);

    public static final BlockEntry<HorseCrankBlock> HORSE_CRANK = REGISTRATE.block("horse_crank", HorseCrankBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.STONE))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static void register(){}
}
