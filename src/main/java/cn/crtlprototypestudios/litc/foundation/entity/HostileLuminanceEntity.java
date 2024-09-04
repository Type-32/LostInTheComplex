package cn.crtlprototypestudios.litc.foundation.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HostileLuminanceEntity extends HostileEntity implements GeoEntity {
    protected static final RawAnimation IDLE_1_ANIM = RawAnimation.begin().thenLoop("idle.1");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    protected HostileLuminanceEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static boolean spawnPredicate(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
        if (world.isOutOfHeightLimit(pos) || !type.isSpawnableFarFromPlayer()) return false;
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Base", 5, this::baseAnimController));
    }

    protected <E extends HostileLuminanceEntity> PlayState baseAnimController(final AnimationState<E> event){
        if(event.isMoving()){
            return event.setAndContinue(IDLE_1_ANIM);
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}
