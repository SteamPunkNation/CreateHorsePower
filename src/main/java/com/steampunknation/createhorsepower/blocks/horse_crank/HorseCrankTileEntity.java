package com.steampunknation.createhorsepower.blocks.horse_crank;

import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import com.simibubi.create.foundation.gui.widgets.InterpolatedChasingValue;
import com.steampunknation.createhorsepower.BlockRegister;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;


public class HorseCrankTileEntity extends GeneratingKineticTileEntity {
    //Create power variables
    private float generatedCapacity;
    private float generatedSpeed = 16f;

    //Client
    InterpolatedChasingValue visualSpeed = new InterpolatedChasingValue();
    float angle;

    public HorseCrankTileEntity(TileEntityType<? extends HorseCrankTileEntity> type) {
        super(type);
    }

    /*TODO
    1. Move preset stress unit values and rotation speed to a config file.

    2. Speed should be based on entity attached (Ex: Wolf [4 rpm] -> Cow [8 rpm] -> Horse [16 rpm])
    3. Stress should be based on entity size (Ex: Wolf [16 stress] -> Cow [32 stress] -> Horse [64 stress])
    */

    //Rotation Speed NOT STRESS UNIT
    @Override
    public float getGeneratedSpeed() {
        if (!BlockRegister.HORSE_CRANK.has(getBlockState())) {
            return 0;
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
