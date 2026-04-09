package dev.perxenic.vectorientation.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.vectorientation.Config;
import dev.perxenic.vectorientation.EntityRenderStateInfo;
import dev.perxenic.vectorientation.Vectorientation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TntRenderer.class, priority = 1100)
public class TntRendererMixin {
    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/TntMinecartRenderer;submitWhiteSolidBlock(Lnet/minecraft/client/renderer/block/BlockModelRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IZI)V"
            ),
            method = "Lnet/minecraft/client/renderer/entity/TntRenderer;submit(Lnet/minecraft/client/renderer/entity/state/TntRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"
    )
    public void addRotation(TntRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (Config.blacklist.contains(Blocks.TNT)) return;
        Vectorientation.addRotation(state, poseStack);
    }

    @Inject(
            at = @At("HEAD"),
            method = "Lnet/minecraft/client/renderer/entity/TntRenderer;extractRenderState(Lnet/minecraft/world/entity/item/PrimedTnt;Lnet/minecraft/client/renderer/entity/state/TntRenderState;F)V"
    )
    public void addRenderStateInfo(PrimedTnt tntEntity, TntRenderState renderState, float f, CallbackInfo ci)
    {
        ((EntityRenderStateInfo)renderState).vectorientation$setOnGround(tntEntity.onGround());
        ((EntityRenderStateInfo)renderState).vectorientation$setDeltaMovement(tntEntity.getDeltaMovement());
        ((EntityRenderStateInfo)renderState).vectorientation$setGravity(tntEntity.getGravity());
        ((EntityRenderStateInfo)renderState).vectorientation$setBlock(tntEntity.getBlockState().getBlock());
    }
}