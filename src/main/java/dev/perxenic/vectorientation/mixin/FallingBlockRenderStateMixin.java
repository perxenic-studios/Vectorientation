package dev.perxenic.vectorientation.mixin;

import dev.perxenic.vectorientation.EntityRenderStateInfo;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FallingBlockRenderState.class)
public class FallingBlockRenderStateMixin implements EntityRenderStateInfo {
    @Unique
    boolean vectorientation$onGround = false;

    @Unique
    double vectorientation$gravity = 0.4905f;

    @Unique
    Vec3 vectorientation$deltaMovement = Vec3.ZERO;

    @Unique
    Block vectorientation$block;

    @Override
    public void vectorientation$setOnGround(boolean value) {
        vectorientation$onGround = value;
    }

    @Override
    public boolean vectorientation$onGround() {
        return vectorientation$onGround;
    }

    @Override
    public void vectorientation$setBlock(Block block) {
        this.vectorientation$block = block;
    }

    @Override
    public void vectorientation$setDeltaMovement(Vec3 value) {
        vectorientation$deltaMovement = value;
    }

    @Override
    public Vec3 vectorientation$getDeltaMovement() {
        return vectorientation$deltaMovement;
    }

    @Override
    public Block vectorientation$getBlock() {
        return vectorientation$block;
    }
}
