package dev.perxenic.vectorientation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public interface EntityRenderStateInfo {
    void vectorientation$setOnGround(boolean value);
    boolean vectorientation$onGround();

    void vectorientation$setGravity(double value);
    double vectorientation$getGravity();

    void vectorientation$setDeltaMovement(Vec3 deltaMovement);
    Vec3 vectorientation$getDeltaMovement();

    void vectorientation$setBlock(Block block);
    Block vectorientation$getBlock();

    default void vectorientation$setDeltaMovementInterpolated(Entity entity, float partialTick) {
        var originalDelta = entity.getDeltaMovement();
        vectorientation$setDeltaMovement(new Vec3(
                originalDelta.x,
                originalDelta.y - vectorientation$getGravity() * partialTick,
                originalDelta.z
        ));
    }
}
