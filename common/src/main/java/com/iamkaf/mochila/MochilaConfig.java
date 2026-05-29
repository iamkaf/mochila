package com.iamkaf.mochila;

import com.iamkaf.konfig.api.v1.ConfigBuilder;
import com.iamkaf.konfig.api.v1.ConfigHandle;
import com.iamkaf.konfig.api.v1.ConfigScope;
import com.iamkaf.konfig.api.v1.ConfigValue;
import com.iamkaf.konfig.api.v1.Konfig;
import com.iamkaf.konfig.api.v1.SyncMode;
import net.minecraft.core.registries.Registries;

import java.util.List;

public final class MochilaConfig {
    private static final List<String> DEFAULT_QUICKSTASH_TARGETS = List.of(
            "minecraft:chest",
            "minecraft:trapped_chest",
            "minecraft:barrel",
            "minecraft:copper_chest",
            "minecraft:exposed_copper_chest",
            "minecraft:weathered_copper_chest",
            "minecraft:oxidized_copper_chest",
            "minecraft:waxed_copper_chest",
            "minecraft:waxed_exposed_copper_chest",
            "minecraft:waxed_weathered_copper_chest",
            "minecraft:waxed_oxidized_copper_chest"
    );

    public static final ConfigHandle HANDLE;
    private static final ConfigValue<Boolean> QUICKSTASH_ENABLED;
    private static final ConfigValue<List<String>> QUICKSTASH_TARGETS;
    private static final ConfigValue<Boolean> QUICKSTASH_FEEDBACK;
    private static final ConfigValue<Boolean> QUICKSTASH_FEEDBACK_NOOP;
    private static final ConfigValue<Boolean> QUICKSTASH_SOUND;
    private static final ConfigValue<Boolean> QUICKSTASH_PARTICLES;
    private static final ConfigValue<Boolean> TOOLTIP_SHOW_MODE;
    private static final ConfigValue<Boolean> TOOLTIP_SHOW_SIZE;

    static {
        ConfigBuilder builder = Konfig.builder(Constants.MOD_ID, "common")
                .scope(ConfigScope.COMMON)
                .syncMode(SyncMode.LOGIN)
                .schemaVersion(1)
                .comment("Gameplay and display settings for Mochila.");

        builder.push("quickstash");
        builder.categoryComment("Controls sneak-use backpack storage behavior.");
        QUICKSTASH_ENABLED = builder.bool("enabled", true)
                .comment("Allow backpacks to quickstash into supported containers.")
                .sync(true)
                .build();
        QUICKSTASH_TARGETS = builder.stringList("targets", DEFAULT_QUICKSTASH_TARGETS)
                .comment("Block registry IDs that backpacks can quickstash into.")
                .registry(Registries.BLOCK)
                .sync(true)
                .build();
        QUICKSTASH_FEEDBACK = builder.bool("feedback_enabled", true)
                .comment("Show actionbar feedback after quickstash attempts.")
                .sync(true)
                .build();
        QUICKSTASH_FEEDBACK_NOOP = builder.bool("feedback_show_noop", true)
                .comment("Show actionbar feedback when quickstash moved no items.")
                .sync(true)
                .build();
        QUICKSTASH_SOUND = builder.bool("sound_enabled", true)
                .comment("Play a sound when quickstash moves items.")
                .sync(true)
                .build();
        QUICKSTASH_PARTICLES = builder.bool("particles_enabled", true)
                .comment("Spawn particles when quickstash moves items.")
                .sync(true)
                .build();
        builder.pop();

        builder.push("tooltip");
        builder.categoryComment("Controls backpack tooltip details.");
        TOOLTIP_SHOW_MODE = builder.bool("show_mode", true)
                .comment("Show the current quickstash mode in backpack tooltips.")
                .clientOnly()
                .build();
        TOOLTIP_SHOW_SIZE = builder.bool("show_size", true)
                .comment("Show backpack size in backpack tooltips.")
                .clientOnly()
                .build();
        builder.pop();

        HANDLE = builder.build();
    }

    private MochilaConfig() {
    }

    public static void init() {
    }

    public static boolean quickstashEnabled() {
        return QUICKSTASH_ENABLED.get();
    }

    public static List<String> quickstashTargets() {
        return QUICKSTASH_TARGETS.get();
    }

    public static boolean quickstashFeedback() {
        return QUICKSTASH_FEEDBACK.get();
    }

    public static boolean quickstashFeedbackNoop() {
        return QUICKSTASH_FEEDBACK_NOOP.get();
    }

    public static boolean quickstashSound() {
        return QUICKSTASH_SOUND.get();
    }

    public static boolean quickstashParticles() {
        return QUICKSTASH_PARTICLES.get();
    }

    public static boolean tooltipShowMode() {
        return TOOLTIP_SHOW_MODE.get();
    }

    public static boolean tooltipShowSize() {
        return TOOLTIP_SHOW_SIZE.get();
    }
}
