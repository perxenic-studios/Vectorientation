package dev.perxenic.vectorientation.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.vectorientation.Config;
import dev.perxenic.vectorientation.EntityRenderStateInfo;
import dev.perxenic.vectorientation.Vectorientation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.world.entity.item.FallingBlockEntity;

@Mixin(value = FallingBlockRenderer.class, priority = 1100)
public class FallingBlockRendererMixin {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"
            ),
            method = "Lnet/minecraft/client/renderer/entity/FallingBlockRenderer;render(Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    public void addRotation(FallingBlockRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        if (Config.blacklist.contains(renderState.blockState.getBlock())) return;
        Vectorientation.addRotation(renderState, poseStack);
    }

    @Inject(
            at = @At("HEAD"),
            method = "Lnet/minecraft/client/renderer/entity/FallingBlockRenderer;extractRenderState(Lnet/minecraft/world/entity/item/FallingBlockEntity;Lnet/minecraft/client/renderer/entity/state/FallingBlockRenderState;F)V"
    )
    public void addRenderStateInfo(FallingBlockEntity fallingBlockEntity, FallingBlockRenderState renderState, float f, CallbackInfo ci)
    {
        ((EntityRenderStateInfo)renderState).vectorientation$setOnGround(fallingBlockEntity.onGround());
        ((EntityRenderStateInfo)renderState).vectorientation$setDeltaMovement(fallingBlockEntity.getDeltaMovement());
        ((EntityRenderStateInfo)renderState).vectorientation$setGravity(fallingBlockEntity.getGravity());
    }
}