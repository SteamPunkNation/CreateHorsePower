package net.steampn.createhorsepower.utils;

import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CHPShapes {

    public static final VoxelShape HORSE_CRANK = Stream.of(
            Block.box(0, 0, 0, 16, 6, 16),
            Block.box(2, 6, 2, 14, 14, 14),
            Block.box(6, 12, 6, 10, 20, 10)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
}
