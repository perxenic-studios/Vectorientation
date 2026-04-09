package dev.perxenic.vectorientation;

import net.minecraft.IdentifierException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue MIN_STRETCH = BUILDER.comment("Defines the vertical stretch at 0 velocity").defineInRange("minStretch", 0.75, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue MIN_SQUISH = BUILDER.comment("Defines the horizontal squish at 0 velocity").defineInRange("minSquish", 0.75, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue STRETCH_FACTOR = BUILDER.comment("Defines the amount vertical stretch increases with velocity").defineInRange("stretchFactor", 1.0, 0, Double.MAX_VALUE);
    private static final ModConfigSpec.DoubleValue SQUISH_FACTOR = BUILDER.comment("Defines the amount horizontal squish increases with velocity").defineInRange("squishFactor", 1.0, 0, Double.MAX_VALUE);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST = BUILDER.comment("A list of blocks that should not be squished.").defineListAllowEmpty("blacklist", List.of("minecraft:anvil", "minecraft:chipped_anvil", "minecraft:damaged_anvil", "minecraft:pointed_dripstone"), () -> "", Config::validateBlockName);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static double minStretch;
    public static double minSquish;
    public static double stretchFactor;
    public static double squishFactor;
    public static Set<Block> blacklist;

    private static boolean validateBlockName(final Object obj) {
        try {
            return obj instanceof String blockName && BuiltInRegistries.BLOCK.containsKey(Identifier.parse(blockName));
        }
        catch(IdentifierException exception) {
            return false;
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        minStretch = MIN_STRETCH.get();
        minSquish = MIN_SQUISH.get();
        stretchFactor = STRETCH_FACTOR.get();
        squishFactor = SQUISH_FACTOR.get();

        // convert the list of strings into a set of items
        blacklist = BLACKLIST.get().stream().map(blockName -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(blockName))).collect(Collectors.toSet());
    }
}
