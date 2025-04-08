package net.steampn.createhorsepower.client.ponders;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import java.util.Optional;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Variant;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.steampn.createhorsepower.blocks.horse_crank.HorseCrankBlock;

public class HorseCrankBaseScene {
    public static void intro(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("horse_crank", "Using Horse Crank");
        scene.configureBasePlate(1, 0, 8);
        scene.scaleSceneView(.95f);
        scene.removeShadow();

        Selection horseCrank = util.select().position(3,2,3);
        Selection exampleCog = util.select().position(3,2,2);
        Selection validBlocks = util.select().fromTo(0,1,0,6,1,6).substract(util.select().position(3,1,3));
        Selection betterBlocks = util.select().layer(3);
        Selection shafts = util.select().fromTo(3, 4, 3, 7, 4, 3);
        Selection rods = util.select()
            .fromTo(7,1,0, 7,1,6)
            .add(util.select().position(3,1,3))
            .add(util.select().position(7,2,4))
            .add(util.select().position(7,2,3))
            .add(util.select().position(7,2,2))
            .add(util.select().fromTo(7,1,7, 0,1,7));
        Selection rodCutout = util.select().fromTo(3,0,3,7,0,3);
        Selection base = util.select().layer(0);

        BlockPos horseCrankPos = util.grid().at(3,2,3);
        BlockPos speedometer = util.grid().at(7,2,2);
        BlockPos stressometer = util.grid().at(7,2,4);

        scene.world().modifyBlock(horseCrankPos, state -> state.setValue(HorseCrankBlock.HAS_WORKER, false), false);
        scene.world().showSection(horseCrank, Direction.DOWN);
        scene.world().showSection(base, Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(60)
            .placeNearTarget()
            .pointAt(util.vector().blockSurface(util.grid().at(3,2,3), Direction.WEST))
            .attachKeyFrame()
            .text("This is a Horse Crank");
        scene.idle(60);

        scene.world().showSection(validBlocks, Direction.NORTH);
        scene.overlay().showText(60)
            .placeNearTarget()
            .attachKeyFrame()
            .pointAt(util.vector().blockSurface(util.grid().at(6,1,6), Direction.UP))
            .text("To use a horse crank you first need to have valid blocks underneath it");
        scene.idle(70);

        scene.world().hideSection(validBlocks, Direction.NORTH);
        scene.idle(15);
        ElementLink<WorldSectionElement> fromBetterBlocks = scene.world().showIndependentSection(betterBlocks, Direction.NORTH);
        scene.world().moveSection(fromBetterBlocks, util.vector().of(0, -2, 0), 0);
        scene.idle(3);
        scene.overlay().showText(40)
            .pointAt(util.vector().blockSurface(util.grid().at(6,1,6), Direction.UP))
            .text("Some materials might be better than others");
        scene.idle(40);

        scene.world().hideIndependentSection(fromBetterBlocks, Direction.NORTH);
        scene.idle(15);
        scene.world().showSection(validBlocks, Direction.NORTH);
        scene.idle(3);
        scene.overlay().showText(60)
            .attachKeyFrame()
            .placeNearTarget()
            .pointAt(util.vector().blockSurface(util.grid().at(3,2,3), Direction.WEST))
            .text("When a valid worker is attached the crank will produce stress");
        scene.idle(40);
        ItemStack lead = Items.LEAD.getDefaultInstance();

        scene.world().hideSection(rodCutout, Direction.UP);
        scene.world().showSection(rods, Direction.UP);

        ElementLink<WorldSectionElement> shaftSection = scene.world().showIndependentSection(shafts, Direction.UP);
        scene.world().moveSection(shaftSection, util.vector().of(0, -4, 0), 4);
        scene.idle(4);
        ElementLink<EntityElement> horseElement = scene.world().createEntity(l-> {
            Horse entity = EntityType.HORSE.create(l);
            entity.setVariant(Variant.CHESTNUT);
            Vec3 p = util.vector().topOf(util.grid().at(1, 1, 4));
            entity.setPos(p);
            entity.xo = p.x;
            entity.yo = p.y;
            entity.zo = p.z;
            WalkAnimationState animation = entity.walkAnimation;
            animation.update(-animation.position(), 1);
            animation.setSpeed(1);
            entity.yRotO = 210;
            entity.setYRot(210);
            entity.yHeadRotO = 210;
            entity.yHeadRot = 210;
            return entity;
        });
        scene.idle(5);
        scene.overlay().showControls(
            util.vector().blockSurface(horseCrankPos, Direction.NORTH),
            Pointing.RIGHT,
            60)
            .withItem(lead)
            .rightClick();
        scene.idle(15);
        ElementLink<EntityElement> leashFenceKnotHorseElement = scene.world().createEntity(level -> {
            LeashFenceKnotEntity knot = EntityType.LEASH_KNOT.create(level);
            Vec3 p = horseCrankPos.getCenter();
            knot.mainSupportingBlockPos = Optional.of(horseCrankPos);
            knot.setPos(p);
            knot.xo = p.x;
            knot.yo = p.y;
            knot.zo = p.z;
            return knot;
        });

        scene.world().modifyEntity(horseElement, horse -> {
            Horse horse1 = (Horse) horse;
            EntityElement leash = scene.getScene().resolve(leashFenceKnotHorseElement);
            if (leash != null) {
                leash.ifPresent(entity -> {
                    LeashFenceKnotEntity knot = (LeashFenceKnotEntity) entity;
                    horse1.setLeashedTo(knot, false);
                });
            }
        });
        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.HAS_WORKER);
        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.LARGE_WORKER_STATE);
        scene.effects().indicateSuccess(horseCrankPos);
        scene.world().setKineticSpeed(util.select().everywhere(), 4);
        scene.effects().rotationSpeedIndicator(horseCrankPos);
        scene.effects().rotationSpeedIndicator(speedometer);
        scene.effects().rotationSpeedIndicator(stressometer);
        scene.idle(60);

        scene.overlay().showText(60)
            .attachKeyFrame()
            .placeNearTarget()
            .pointAt(util.vector().blockSurface(util.grid().at(3,2,3), Direction.WEST))
            .text("You can attach any mob in the config to turn it into a worker");
        scene.world().modifyEntities(LeashFenceKnotEntity.class, Entity::discard);
        scene.world().modifyEntities(Horse.class, Entity::discard);
        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.HAS_WORKER);
        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.LARGE_WORKER_STATE);
        scene.world().setKineticSpeed(util.select().everywhere(), 0);
        scene.idle(15);
        ElementLink<EntityElement> cowElement = scene.world().createEntity(l-> {
            LivingEntity entity = EntityType.COW.create(l);
            Vec3 p = util.vector().topOf(util.grid().at(1, 1, 4));
            entity.setPos(p);
            entity.xo = p.x;
            entity.yo = p.y;
            entity.zo = p.z;
            WalkAnimationState animation = entity.walkAnimation;
            animation.update(-animation.position(), 1);
            animation.setSpeed(1);
            entity.yRotO = 210;
            entity.setYRot(210);
            entity.yHeadRotO = 210;
            entity.yHeadRot = 210;
            return entity;
        });
        scene.idle(5);
        scene.overlay().showControls(
                util.vector().blockSurface(horseCrankPos, Direction.NORTH),
                Pointing.RIGHT,
                60)
            .withItem(lead)
            .rightClick();
        scene.idle(1);

        ElementLink<EntityElement> leashFenceKnotCowElement = scene.world().createEntity(level -> {
            LeashFenceKnotEntity knot = EntityType.LEASH_KNOT.create(level);
            Vec3 p = horseCrankPos.getCenter();
            knot.mainSupportingBlockPos = Optional.of(horseCrankPos);
            knot.setPos(p);
            knot.xo = p.x;
            knot.yo = p.y;
            knot.zo = p.z;
            return knot;
        });

        scene.world().modifyEntity(cowElement, cow -> {
            Cow cow1 = (Cow) cow;
            EntityElement leash = scene.getScene().resolve(leashFenceKnotCowElement);
            if (leash != null) {
                leash.ifPresent(entity -> {
                    LeashFenceKnotEntity knot = (LeashFenceKnotEntity) entity;
                    cow1.setLeashedTo(knot, false);
                });
            }
        });

        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.HAS_WORKER);
        scene.world().cycleBlockProperty(horseCrankPos, HorseCrankBlock.MEDIUM_WORKER_STATE);
        scene.effects().indicateSuccess(horseCrankPos);
        scene.world().setKineticSpeed(util.select().everywhere(), 4);
        scene.effects().rotationSpeedIndicator(horseCrankPos);
        scene.effects().rotationSpeedIndicator(speedometer);
        scene.effects().rotationSpeedIndicator(stressometer);
        scene.idle(60);

        scene.world().setBlock(BlockPos.containing(util.vector().of(3, 2, 2)), AllBlocks.COGWHEEL.getDefaultState(), false);
        scene.world().showSection(exampleCog, Direction.DOWN);
        scene.world().setKineticSpeed(exampleCog, -4);

        scene.idle(2);
        scene.overlay().showText(40)
            .attachKeyFrame()
            .placeNearTarget()
            .pointAt(util.vector().of(3,3,2))
            .text("You can get rotation out with a cog from the side...");
        scene.idle(40);
        scene.world().hideSection(exampleCog, Direction.NORTH);

        scene.world().hideSection(validBlocks, Direction.UP);
        scene.idle(2);
        scene.overlay().showText(40)
            .placeNearTarget()
            .pointAt(util.vector().of(3,2,3))
            .text("...or a shaft, from underneath...");
        scene.idle(50);

        scene.world().showSection(validBlocks, Direction.NORTH);
        scene.idle(1);
        scene.overlay().showText(40)
            .placeNearTarget()
            .pointAt(util.vector().of(3, 4, 3))
            .text("...but not from on top")
            .colored(PonderPalette.RED);
        scene.idle( 40);

        scene.markAsFinished();
    }
}
