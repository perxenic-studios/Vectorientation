package dev.perxenic.vectorientation.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.perxenic.vectorientation.Config;
import dev.perxenic.vectorientation.EntityRenderStateInfo;
import dev.perxenic.vectorientation.Vectorientation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.state.TntRenderState;
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
                    target = "Lnet/minecraft/client/renderer/entity/TntMinecartRenderer;renderWhiteSolidBlock(Lnet/minecraft/client/renderer/block/BlockRenderDispatcher;Lnet/minecraft/world/level/block/state/BlockState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IZ)V"
            ),
            method = "Lnet/minecraft/client/renderer/entity/TntRenderer;render(Lnet/minecraft/client/renderer/entity/state/TntRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    public void addRotation(TntRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (Config.blacklist.contains(Blocks.TNT)) return;
        Vectorientation.addRotation(renderState, poseStack);
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
    }
}