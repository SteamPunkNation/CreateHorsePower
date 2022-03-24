package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.foundation.gui.widgets.InterpolatedChasingValue;
import com.steampunknation.createhorsepower.config.Config;
import com.steampunknation.createhorsepower.utils.BlockRegister;
import net.minecraft.entity.MobEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;


public class HorseCrankTileEntity extends GeneratingKineticTileEntity {
    //Config variables
    private static final Integer
    SMALL_CREATURE_SPEED = Config.SMALL_CREATURE_SPEED.get(),
    SMALL_CREATURE_STRESS = Config.SMALL_CREATURE_STRESS.get(),
    MEDIUM_CREATURE_SPEED = Config.MEDIUM_CREATURE_SPEED.get(),
    MEDIUM_CREATURE_STRESS = Config.MEDIUM_CREATURE_STRESS.get(),
    LARGE_CREATURE_SPEED = Config.LARGE_CREATURE_SPEED.get(),
    LARGE_CREATURE_STRESS = Config.LARGE_CREATURE_STRESS.get();


    //Create power variables
    private float generatedCapacity;
    private float generatedSpeed;
    private boolean smallWorker = false;
    private boolean mediumWorker = false;
    private boolean largeWorker = false;

    //Client
    InterpolatedChasingValue visualSpeed = new InterpolatedChasingValue();
    float angle;

    public HorseCrankTileEntity(TileEntityType<? extends HorseCrankTileEntity> type) {
        super(type);
    }

    /*
    TODO Move preset stress unit values and rotation speed to a config file.
    TODO Speed should be based on entity attached (Ex: Wolf [4 rpm] -> Cow [8 rpm] -> Horse [16 rpm])
    TODO Stress should be based on entity size (Ex: Wolf [16 stress] -> Cow [32 stress] -> Horse [64 stress])
    */

    //Rotation Speed NOT STRESS UNIT
    @Override
    public float getGeneratedSpeed() {
        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
            return 0;
        }

        if (smallWorker){
            generatedSpeed = SMALL_CREATURE_SPEED;
        }
        else if (mediumWorker){
            generatedSpeed = MEDIUM_CREATURE_SPEED;
        }
        else if (largeWorker){
            generatedSpeed = LARGE_CREATURE_SPEED;
        }
        else{
            generatedSpeed = 0;
        }

        return generatedSpeed;
    }

    //Stress Unit NOT ROTATION SPEED
    @Override
    public float calculateAddedStressCapacity() {
        float capacity = generatedSpeed / 4;
        this.lastCapacityProvided = capacity;
        return capacity;
    }

    @Override
    protected AxisAlignedBB makeRenderBoundingBox() {
        return new AxisAlignedBB(worldPosition).inflate(1);
    }

    @Override
    public void tick() {
        super.tick();

        if (level.isClientSide) {
            float targetSpeed = isVirtual() ? speed : getGeneratedSpeed();
            visualSpeed.target(targetSpeed);
            visualSpeed.tick();
            angle += visualSpeed.value * 3 / 10f;
            angle %= 360;
            return;
        }

        if (getGeneratedSpeed() != 0 && getSpeed() == 0)
            updateGeneratedRotation();
    }
}
