package dev.perxenic.vectorientation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.slf4j.Logger;

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

    public static void addRotation(EntityRenderState renderState, PoseStack poseStack) {
        var renderStateInfo = (EntityRenderStateInfo) renderState;
        if (renderStateInfo.vectorientation$onGround()) return;
        Vec3 deltaMovement = renderStateInfo.vectorientation$getDeltaMovement();
        Vector3d velocity = new Vector3d(deltaMovement.x, deltaMovement.y, deltaMovement.z);

        float stretch = (float) (Config.minStretch + Config.stretchFactor * velocity.length());
        float squish = 1 / (float) (Config.minSquish + Config.squishFactor * velocity.length());
        float angle = (float) Math.acos(velocity.normalize().y);
        Vector3f axis = new Vector3f((float) (-1 * velocity.z()), 0, (float) velocity.x());
        Quaternionf rot = new Quaternionf();
        if (axis.length() > .01f) {
            axis.normalize();
            rot = new Quaternionf(new AxisAngle4f(-angle, axis));
        }
        poseStack.scale(squish, stretch, squish);
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(rot);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
    }
}


