package com.steampunknation.createhorsepower;

import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.steampunknation.createhorsepower.CreateHorsePower;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankBlock;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankItem;
import com.steampunknation.createhorsepower.blocks.horse_crank.HorseCrankItemRenderer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class BlockRegister {

    private static final CreateRegistrate REGISTRATE = CreateHorsePower.registrate()
            .itemGroup(() -> ItemGroup.TAB_REDSTONE);

    public static final BlockEntry<HorseCrankBlock> HORSE_CRANK = REGISTRATE.block("horse_crank", HorseCrankBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag) //Dono what this tag means (contraption safe?).
            .item()
            .transform(customItemModel())
            .register();

    public static void register(){}

}
