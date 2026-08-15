package com.fletchery.mod.api;

import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;


final class LegacyArrowComponents {
    private LegacyArrowComponents() {}

    private static ArrowComponent of(String id, Item item, String modelFile, BiConsumer<ArrowProperties, ModConfig> apply) {
        Identifier ident = Identifier.of("minecraft", id);
        return new ArrowComponent() {
            public Identifier id() { return ident; }
            public Item item() { return item; }
            public String modelFile() { return modelFile; }
            public void apply(ArrowProperties props, ModConfig cfg) { apply.accept(props, cfg); }
        };
    }

    static void registerAll() {
        // ===== FEATHER (порядок = ArrowVisualData.FEATHERS) =====
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.FEATHER, of(
                "feather", Items.FEATHER, "feather", (p, cfg) -> {}));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.FEATHER, of(
                "phantom_membrane", Items.PHANTOM_MEMBRANE, "phantom",
                (p, cfg) -> p.speedMultiplier = cfg.phantomMembraneSpeed));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.FEATHER, of(
                "wheat", Items.WHEAT, "wheat", (p, cfg) -> {
                    p.speedMultiplier = cfg.wheatSpeed;
                    p.gravityMultiplier = cfg.wheatGravity;
                }));

        // ===== SHAFT (порядок = ArrowVisualData.SHAFTS) =====
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "stick", Items.STICK, "stick", (p, cfg) -> {}));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "blaze_rod", Items.BLAZE_ROD, "blaze", (p, cfg) -> {
                    p.ignite = true;
                    p.igniteTicks = cfg.blazeRodIgniteTicks;
                    p.blazeIgniteBlocks = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "breeze_rod", Items.BREEZE_ROD, "breeze", (p, cfg) -> {
                    p.noGravity = true;
                    p.hasLifetime = true;
                    p.lifetimeTicks = cfg.breezeRodLifetimeTicks;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "bone", Items.BONE, "bone", (p, cfg) -> {
                    p.bonusDamageFloat += cfg.boneBonusDamage;
                    p.hungerTarget = true;
                    p.hungerDuration = cfg.boneHungerDuration;
                    p.spawnSkeleton = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "end_rod", Items.END_ROD, "end_rod", (p, cfg) -> {
                    p.bonusDamage += cfg.endRodBonusDamage;
                    p.speedMultiplier = cfg.endRodSpeedMultiplier;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "fishing_rod", Items.FISHING_ROD, "fishing_rod", (p, cfg) -> {
                    p.pullEntity = true;
                    p.fishingRodPullStrength = cfg.fishingRodPullStrength;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.SHAFT, of(
                "chain", Items.CHAIN, "chain", (p, cfg) -> {
                    p.chainDamage = true;
                    p.chainDamageMultiplier = cfg.chainDamageMultiplier;
                }));

        // ===== TIP (порядок = ArrowVisualData.TIPS) =====
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "flint", Items.FLINT, "flint", (p, cfg) -> {}));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "amethyst_shard", Items.AMETHYST_SHARD, "amethyst", (p, cfg) -> {
                    p.passLeaves = cfg.amethystPassLeaves;
                    p.passGlass = cfg.amethystPassGlass;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "prismarine_shard", Items.PRISMARINE_SHARD, "prismarine", (p, cfg) -> {
                    p.passWater = cfg.prismarinePassWater;
                    p.bonusDamage += cfg.prismarineBonusDamage;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "echo_shard", Items.ECHO_SHARD, "echo", (p, cfg) -> {
                    p.bonusDamageFloat = cfg.echoBonusDamage;
                    p.slowTarget = cfg.echoSlowEnabled;
                    p.slowDuration = cfg.echoSlowDuration;
                    p.darknessTarget = true;
                    p.darknessDuration = 60;
                    p.weakenTarget = true;
                    p.weakenDuration = 40;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "quartz", Items.QUARTZ, "quartz", (p, cfg) -> {
                    p.blindTarget = cfg.quartzBlindEnabled;
                    p.blindDuration = cfg.quartzBlindDuration;
                    p.dropQuartz = true;
                    p.nauseaTarget = true;
                    p.nauseaDuration = 60;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "gold_ingot", Items.GOLD_INGOT, "gold", (p, cfg) -> {
                    p.weakenTarget = cfg.goldWeakenEnabled;
                    p.weakenDuration = cfg.goldWeakenDuration;
                    p.weakenAmplifier = 1;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "copper_ingot", Items.COPPER_INGOT, "copper", (p, cfg) -> {
                    p.summonLightning = cfg.copperLightningEnabled;
                    p.bonusDamageFloat -= 1.5f;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "shulker_shell", Items.SHULKER_SHELL, "shulker_shell", (p, cfg) -> {
                    p.levitateTarget = cfg.shulkerLevitateEnabled;
                    p.levitateDuration = cfg.shulkerLevitateDuration;
                    p.shulkerExplosion = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "iron_ingot", Items.IRON_INGOT, "iron", (p, cfg) -> {
                    p.bonusDamage += cfg.ironBonusDamage;
                    p.ironTip = true;
                    p.shieldBreak = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "diamond", Items.DIAMOND, "diamond", (p, cfg) -> {
                    p.bonusDamage += cfg.diamondBonusDamage;
                    p.diamondTip = true;
                    p.armorPiercing = true;
                    p.shieldBreak = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "netherite_ingot", Items.NETHERITE_INGOT, "netherite", (p, cfg) -> {
                    p.bonusDamage += cfg.netheriteBonusDamage;
                    p.netheriteTip = true;
                    p.armorPiercing = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.TIP, of(
                "heavy_core", Items.HEAVY_CORE, "heavy_core", (p, cfg) -> {
                    p.heavyCore = true;
                    p.heavyCoreHeightMultiplier = cfg.heavyCoreHeightMultiplier;
                    p.heavyCoreMaxBonus = cfg.heavyCoreMaxBonus;
                }));

        // ===== EFFECT (порядок = ArrowVisualData.EFFECTS, index 0 "none" не регистрируется) =====
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "gunpowder", Items.GUNPOWDER, "gunpowder", (p, cfg) -> {
                    p.explosive = true;
                    p.explosionPower = cfg.gunpowderExplosionPower;
                    p.explosionEntityBonus = cfg.gunpowderEntityBonus;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "glowstone_dust", Items.GLOWSTONE_DUST, "glowstone", (p, cfg) -> {
                    if (cfg.glowstoneDamageEnabled) p.bonusDamage += cfg.glowstoneBonusDamage;
                    p.glowTarget = cfg.glowstoneGlowEnabled;
                    p.glowDuration = cfg.glowstoneGlowDuration;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "slime_ball", Items.SLIME_BALL, "slime", (p, cfg) -> {
                    p.bouncy = cfg.slimeBounceEnabled;
                    p.bounceCount = cfg.slimeBounceCount;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "honeycomb", Items.HONEYCOMB, "honeycomb", (p, cfg) -> {
                    p.spawnHoney = true;
                    p.slowTargetEffect = true;
                    p.slowTargetEffectDuration = 40;
                    p.slowTargetEffectAmplifier = 0;
                    p.replaceBlockWithHoney = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "ender_pearl", Items.ENDER_PEARL, "ender_pearl",
                (p, cfg) -> p.teleportShooter = cfg.enderPearlTeleportEnabled));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "blaze_powder", Items.BLAZE_POWDER, "blaze_powder", (p, cfg) -> {
                    p.igniteGround = cfg.blazePowderIgniteEnabled;
                    p.meltSnow = true;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "turtle_helmet", Items.TURTLE_HELMET, "turtle_helmet", (p, cfg) -> {
                    if (cfg.turtleHelmetSlowEnabled) {
                        p.slowTargetEffect = true;
                        p.slowTargetEffectDuration = cfg.turtleHelmetSlowDuration;
                        p.slowTargetEffectAmplifier = 2;
                    }
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "dragon_breath", Items.DRAGON_BREATH, "dragon_breath", (p, cfg) -> {
                    p.dragonBreath = cfg.dragonBreathEnabled;
                    p.dragonBreathCloudRadius = cfg.dragonBreathCloudRadius;
                    p.dragonBreathCloudDuration = cfg.dragonBreathCloudDuration;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "heart_of_the_sea", Items.HEART_OF_THE_SEA, "heart_of_sea",
                (p, cfg) -> p.passWater = true));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "wind_charge", Items.WIND_CHARGE, "wind_charge", (p, cfg) -> {
                    if (cfg.windChargeEnabled) {
                        p.windCharge = true;
                        p.windChargeDamage = cfg.windChargeDamage;
                        p.windChargeKnockback = cfg.windChargeKnockback;
                    }
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "lapis_lazuli", Items.LAPIS_LAZULI, "lapis", (p, cfg) -> {
                    p.randomPotion = true;
                    p.lapisEffectDuration = cfg.lapisEffectDuration;
                    p.lapisEffectAmplifier = cfg.lapisEffectAmplifier;
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "torch", Items.TORCH, "torch", (p, cfg) -> p.torchPlace = true));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "fire_charge", Items.FIRE_CHARGE, "fire_charge", (p, cfg) -> {
                    if (cfg.fireChargeExplosionEnabled) {
                        p.fireCharge = true;
                        p.fireChargeExplosionPower = cfg.fireChargeExplosionPower;
                        p.fireChargeFireTicks = cfg.fireChargeFireTicks;
                    }
                }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "firework_star", Items.FIREWORK_STAR, "firework_star",
                (p, cfg) -> { p.fireworkStar = true; p.fireworkStarDamage = cfg.fireworkStarDamage; }));
        FletcheryComponentRegistry.registerLegacy(FletcheryComponentRegistry.Slot.EFFECT, of(
                "firework_rocket", Items.FIREWORK_ROCKET, "firework_rocket",
                (p, cfg) -> { p.fireworkRocket = true; p.fireworkRocketDamage = cfg.fireworkRocketDamage; }));
    }
}
