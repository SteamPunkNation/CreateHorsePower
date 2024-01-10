package net.steampn.createhorsepower.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.steampn.createhorsepower.utils.BlockRegister;
import net.steampn.createhorsepower.utils.EntityRegister;
import org.jetbrains.annotations.Nullable;

public class CHPLeashKnotEntity extends HangingEntity {

    public CHPLeashKnotEntity(EntityType<? extends HangingEntity> type, Level level) {
        super(type, level);
    }

    public CHPLeashKnotEntity(Level level, BlockPos pos) {
        this(EntityRegister.CHP_LEASH_KNOT.get(), level);
        setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public int getWidth() {
        return 9;
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public void dropItem(@Nullable Entity p_31717_) {
        this.playSound(SoundEvents.LEASH_KNOT_BREAK, 1.0F, 1.0F);
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.LEASH_KNOT_PLACE, 1.0F, 1.0F);
    }

    public static CHPLeashKnotEntity create(EntityType<? extends HangingEntity> type, Level level){
        return new CHPLeashKnotEntity(type, level);
    }

    @Override
    public boolean survives() {
        BlockState blockState = this.level().getBlockState(this.pos);
        return blockState.is(BlockRegister.HORSE_CRANK.get());
    }

    protected void recalculateBoundingBox() {
        this.setPosRaw((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.375D, (double)this.pos.getZ() + 0.5D);
        double d0 = (double)this.getType().getWidth() / 2.0D;
        double d1 = (double)this.getType().getHeight();
        this.setBoundingBox(new AABB(this.getX() - d0, this.getY(), this.getZ() - d0, this.getX() + d0, this.getY() + d1, this.getZ() + d0));
    }

    protected float getEyeHeight(Pose p_31839_, EntityDimensions p_31840_) {
        return 0.0625F;
    }

    public boolean shouldRenderAtSqrDistance(double p_31835_) {
        return p_31835_ < 1024.0D;
    }

    public ItemStack getPickResult() {
        return new ItemStack(Items.LEAD);
    }
}
