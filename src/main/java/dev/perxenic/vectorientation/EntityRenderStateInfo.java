package dev.perxenic.vectorientation;

import net.minecraft.world.phys.Vec3;

public interface EntityRenderStateInfo {
    void vectorientation$setOnGround(boolean value);
    boolean vectorientation$onGround();

    void vectorientation$setGravity(double value);
    double vectorientation$getGravity();

    void vectorientation$setDeltaMovement(Vec3 deltaMovement);
    Vec3 vectorientation$getDeltaMovement();
}
