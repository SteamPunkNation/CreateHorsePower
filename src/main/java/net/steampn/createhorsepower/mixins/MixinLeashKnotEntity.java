package net.steampn.createhorsepower.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.Level;
import net.steampn.createhorsepower.registry.BlockRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LeashFenceKnotEntity.class)
public class MixinLeashKnotEntity {
    @Unique
    private LeashFenceKnotEntity forge_1_20_1_47_3_6_mdk$getLeashKnotEntity(){
        return (LeashFenceKnotEntity)(Object)this;
    }

    /**
     * @author SteamPunkNation
     * @reason to have leash knot entity survive on horse crank block
     */
    @Overwrite
    public boolean survives() {
        Level level = forge_1_20_1_47_3_6_mdk$getLeashKnotEntity().level();
        BlockPos pos = forge_1_20_1_47_3_6_mdk$getLeashKnotEntity().getPos();

        return level.getBlockState(pos).is(BlockTags.FENCES) || level.getBlockState(pos).is(BlockRegister.HORSE_CRANK.get());
    }
}
