package dev.perxenic.vectorientation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

public interface EntityRenderStateInfo {
    void vectorientation$setOnGround(boolean value);
    boolean vectorientation$onGround();

    void vectorientation$setDeltaMovement(Vec3 deltaMovement);
    Vec3 vectorientation$getDeltaMovement();

    void vectorientation$setBlock(Block block);
    Block vectorientation$getBlock();

    default void vectorientation$setDeltaMovementInterpolated(Entity entity, float partialTick) {
        var originalDelta = entity.getDeltaMovement();
        vectorientation$setDeltaMovement(new Vec3(
                originalDelta.x,
                originalDelta.y * (1 - partialTick) + (originalDelta.y - entity.getGravity())  * 0.98 * partialTick,
                originalDelta.z
        ));
    }
}
