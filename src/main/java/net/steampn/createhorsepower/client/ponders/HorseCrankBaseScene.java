package net.steampn.createhorsepower.client.ponders;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.utility.Pointing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock;
import net.steampn.createhorsepower.registry.BlockRegister;

public class HorseCrankBaseScene {
    public static void intro(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("horse_crank", "Using Horse Crank");

        Selection horseCrank = util.select.position(3,2,3);
        Selection validBlocks = util.select.fromTo(0,1,0,6,1,6);
        Selection rods = util.select.fromTo(3,0,3,7,0,3);
        Selection base = util.select.layer(0).substract(rods);

        BlockPos horseCrankPos = util.grid.at(3,4,3);

        scene.overlay.showText(20).text("This is a Horse Crank");
        scene.world.modifyBlock(horseCrankPos, state -> state.setValue(HorseCrankBlock.HAS_WORKER, false), false);
        scene.world.showSection(horseCrank, Direction.DOWN);
        scene.world.showSection(base, Direction.DOWN);
        scene.addKeyframe();
        scene.idleSeconds(2);

        scene.overlay.showText(20).text("To use a horse crank you first need to have valid blocks underneath it");
        scene.world.showSection(validBlocks, Direction.NORTH);
        scene.addKeyframe();
        scene.idleSeconds(2);

        scene.overlay.showText(20).text("Valid blocks are listed in the config");
        scene.addKeyframe();
        scene.idleSeconds(2);

        scene.overlay.showText(20).text("When a valid worker is attached the crank will produce stress");
        ItemStack lead = Items.LEAD.getDefaultInstance();
        scene.world.showSection(util.select.layersFrom(1).substract(horseCrank).substract(validBlocks), Direction.DOWN);
        scene.addKeyframe();
        scene.idleSeconds(2);

        scene.overlay.showText(20).text("You can attach a any mob in the config to turn it into a worker");
        scene.overlay.showControls(
                new InputWindowElement(util.vector.blockSurface(horseCrankPos, Direction.NORTH), Pointing.RIGHT).rightClick()
                        .withItem(lead),
                60);
        scene.world.destroyBlock(horseCrankPos);
        scene.world.setBlock(horseCrankPos,
                BlockRegister.HORSE_CRANK.getDefaultState().setValue(HorseCrankBlock.HAS_WORKER, true).setValue(HorseCrankBlock.SMALL_WORKER_STATE, true),
                false
                );

//        scene.world.modifyBlock(
//                horseCrankPos,
//                blockState -> blockState.setValue(HorseCrankBlock.HAS_WORKER, true).setValue(HorseCrankBlock.SMALL_WORKER_STATE, true),
//                false
//        );
        scene.addKeyframe();
        scene.idleSeconds(2);



//        scene.markAsFinished();
    }
}
