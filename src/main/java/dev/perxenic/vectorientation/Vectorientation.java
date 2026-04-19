package dev.perxenic.vectorientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.joml.*;
import org.slf4j.Logger;

import java.lang.Math;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(value = Vectorientation.MOD_ID)
public class Vectorientation {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "vectorientation";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Vectorientation(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.register(Config.class);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    public static void addRotation(Entity entity, PoseStack poseStack, float partialTicks) {
        if (entity.onGround()) return;
        Vec3 deltaMovement = entity.getDeltaMovement();

        var newY = Config.enableInterpolation
                ? deltaMovement.y * (1 - partialTicks) + (deltaMovement.y - entity.getGravity())  * 0.98 * partialTicks
                : deltaMovement.y;

        Vector3f velocity = new Vector3f(
                (float) deltaMovement.x,
                (float) newY,
                (float) deltaMovement.z
        );

        if (Config.squetch) {
            float speed = (float) (Config.minWarp + Config.warpFactor * velocity.length());
            poseStack.scale(1 / speed, speed, 1 / speed);
        }

        float angle = (float) Math.acos(velocity.normalize().y);
        Vector3f axis = new Vector3f((float) (-1 * velocity.z()), 0, (float) velocity.x());
        Quaternionf rot = new Quaternionf();
        if (axis.length() > .01f) {
            axis.normalize();
            rot.rotateAxis(-angle, axis);
        }

        float spinAngle =  (entity.tickCount % 40 + partialTicks) / 40 * (float) Math.TAU;
        Vector3f spinAxis = velocity.normalize();
        poseStack.mulPose(new Quaternionf().rotateAxis(spinAngle, spinAxis));

        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(rot);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
    }
}


