package com.fletchery.mod.api;

import com.fletchery.mod.FletcheryExpanded;
import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ArrowComponentRegistry {
    private static final ArrowComponentRegistry INSTANCE = new ArrowComponentRegistry();

    private final Map<ArrowComponentType, List<ArrowComponent>> components = new ConcurrentHashMap<>();
    private final Map<Identifier, ArrowComponent> byId = new ConcurrentHashMap<>();
    private int nextCustomModelData = 10000;

    private ArrowComponentRegistry() {
        for (ArrowComponentType type : ArrowComponentType.values()) {
            components.put(type, new ArrayList<>());
        }
    }

    public static ArrowComponentRegistry get() {
        return INSTANCE;
    }

    public void register(ArrowComponent component) {
        if (byId.containsKey(component.id())) {
            FletcheryExpanded.LOGGER.warn("Component {} already registered, skipping", component.id());
            return;
        }
        byId.put(component.id(), component);
        components.get(component.type()).add(component);
        FletcheryExpanded.LOGGER.info("Registered {}: {}", component.type(), component.id());
    }

    public List<ArrowComponent> getComponents(ArrowComponentType type) {
        return Collections.unmodifiableList(components.getOrDefault(type, Collections.emptyList()));
    }

    @Nullable
    public ArrowComponent findComponent(ArrowComponentType type, ItemStack stack) {
        if (stack.isEmpty()) return null;
        for (ArrowComponent comp : components.get(type)) {
            if (comp.ingredientMatcher().test(stack)) {
                return comp;
            }
        }
        return null;
    }

    @Nullable
    public ArrowComponent getComponent(Identifier id) {
        return byId.get(id);
    }

    public int getBaseIndex(ArrowComponent comp) {
        List<ArrowComponent> list = components.get(comp.type());
        return list.indexOf(comp);
    }

    public int getModelData(ArrowComponent feather, ArrowComponent shaft, ArrowComponent tip, @Nullable ArrowComponent effect) {
        if (feather == null || shaft == null || tip == null) return 0;
        if (feather.isBase() && shaft.isBase() && tip.isBase() && (effect == null || effect.isBase())) {
            int fIdx = getBaseIndex(feather);
            int sIdx = getBaseIndex(shaft);
            int tIdx = getBaseIndex(tip);
            if (fIdx < 0 || sIdx < 0 || tIdx < 0) return 0;
            int totalBases = components.get(ArrowComponentType.FEATHER).size() *
                             components.get(ArrowComponentType.SHAFT).size() *
                             components.get(ArrowComponentType.TIP).size();
            int base = (fIdx * components.get(ArrowComponentType.SHAFT).size() + sIdx) *
                       components.get(ArrowComponentType.TIP).size() + tIdx + 1;
            if (effect != null) {
                int eIdx = getBaseIndex(effect);
                if (eIdx < 0) return 0;
                return base + totalBases * (eIdx + 1);
            }
            return base;
        } else {
            String key = feather.id() + "|" + shaft.id() + "|" + tip.id() + "|" + (effect != null ? effect.id() : "none");
            int hash = key.hashCode() & 0x7FFFFFFF;
            if (hash < 10000) hash += 10000;
            return hash;
        }
    }

    public int nextCustomModelData() {
        return nextCustomModelData++;
    }

    // ==================== РЕГИСТРАЦИЯ БАЗОВЫХ КОМПОНЕНТОВ ====================

    public void registerBaseComponents() {
        // Feathers
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "feather"),
                ArrowComponentType.FEATHER,
                stack -> stack.getItem() == Items.FEATHER,
                "feather",
                (stack, props) -> {},
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "phantom_membrane"),
                ArrowComponentType.FEATHER,
                stack -> stack.getItem() == Items.PHANTOM_MEMBRANE,
                "phantom",
                (stack, props) -> props.speedMultiplier = ModConfig.get().phantomMembraneSpeed,
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "wheat"),
                ArrowComponentType.FEATHER,
                stack -> stack.getItem() == Items.WHEAT,
                "wheat",
                (stack, props) -> {
                    props.speedMultiplier = ModConfig.get().wheatSpeed;
                    props.gravityMultiplier = ModConfig.get().wheatGravity;
                },
                true
        ));

        // Shafts
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "stick"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.STICK,
                "stick",
                (stack, props) -> {},
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "blaze_rod"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.BLAZE_ROD,
                "blaze_rod",
                (stack, props) -> {
                    props.ignite = true;
                    props.igniteTicks = ModConfig.get().blazeRodIgniteTicks;
                    props.blazeIgniteBlocks = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "breeze_rod"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.BREEZE_ROD,
                "breeze_rod",
                (stack, props) -> {
                    props.noGravity = true;
                    props.hasLifetime = true;
                    props.lifetimeTicks = ModConfig.get().breezeRodLifetimeTicks;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "bone"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.BONE,
                "bone",
                (stack, props) -> {
                    props.bonusDamageFloat += ModConfig.get().boneBonusDamage;
                    props.hungerTarget = true;
                    props.hungerDuration = ModConfig.get().boneHungerDuration;
                    props.spawnSkeleton = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "end_rod"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.END_ROD,
                "end_rod",
                (stack, props) -> {
                    props.bonusDamage += ModConfig.get().endRodBonusDamage;
                    props.speedMultiplier = ModConfig.get().endRodSpeedMultiplier;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "fishing_rod"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.FISHING_ROD,
                "fishing_rod",
                (stack, props) -> {
                    props.pullEntity = true;
                    props.fishingRodPullStrength = ModConfig.get().fishingRodPullStrength;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "chain"),
                ArrowComponentType.SHAFT,
                stack -> stack.getItem() == Items.CHAIN,
                "chain",
                (stack, props) -> {
                    props.chainDamage = true;
                    props.chainDamageMultiplier = ModConfig.get().chainDamageMultiplier;
                },
                true
        ));

        // Tips
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "flint"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.FLINT,
                "flint",
                (stack, props) -> {},
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "amethyst_shard"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.AMETHYST_SHARD,
                "amethyst_shard",
                (stack, props) -> {
                    props.passLeaves = ModConfig.get().amethystPassLeaves;
                    props.passGlass = ModConfig.get().amethystPassGlass;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "prismarine_shard"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.PRISMARINE_SHARD,
                "prismarine_shard",
                (stack, props) -> {
                    props.passWater = ModConfig.get().prismarinePassWater;
                    props.bonusDamage += ModConfig.get().prismarineBonusDamage;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "echo_shard"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.ECHO_SHARD,
                "echo_shard",
                (stack, props) -> {
                    props.bonusDamageFloat = ModConfig.get().echoBonusDamage;
                    props.slowTarget = ModConfig.get().echoSlowEnabled;
                    props.slowDuration = ModConfig.get().echoSlowDuration;
                    props.darknessTarget = true;
                    props.darknessDuration = 60;
                    props.weakenTarget = true;
                    props.weakenDuration = 40;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "quartz"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.QUARTZ,
                "quartz",
                (stack, props) -> {
                    props.blindTarget = ModConfig.get().quartzBlindEnabled;
                    props.blindDuration = ModConfig.get().quartzBlindDuration;
                    props.dropQuartz = true;
                    props.nauseaTarget = true;
                    props.nauseaDuration = 60;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "gold_ingot"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.GOLD_INGOT,
                "gold_ingot",
                (stack, props) -> {
                    props.weakenTarget = ModConfig.get().goldWeakenEnabled;
                    props.weakenDuration = ModConfig.get().goldWeakenDuration;
                    props.weakenAmplifier = 1;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "copper_ingot"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.COPPER_INGOT,
                "copper_ingot",
                (stack, props) -> {
                    props.summonLightning = ModConfig.get().copperLightningEnabled;
                    props.bonusDamageFloat -= 1.5f;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "shulker_shell"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.SHULKER_SHELL,
                "shulker_shell",
                (stack, props) -> {
                    props.levitateTarget = ModConfig.get().shulkerLevitateEnabled;
                    props.levitateDuration = ModConfig.get().shulkerLevitateDuration;
                    props.shulkerExplosion = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "iron_ingot"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.IRON_INGOT,
                "iron_ingot",
                (stack, props) -> {
                    props.bonusDamage += ModConfig.get().ironBonusDamage;
                    props.ironTip = true;
                    props.shieldBreak = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "diamond"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.DIAMOND,
                "diamond",
                (stack, props) -> {
                    props.bonusDamage += ModConfig.get().diamondBonusDamage;
                    props.diamondTip = true;
                    props.armorPiercing = true;
                    props.shieldBreak = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "netherite_ingot"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.NETHERITE_INGOT,
                "netherite_ingot",
                (stack, props) -> {
                    props.bonusDamage += ModConfig.get().netheriteBonusDamage;
                    props.netheriteTip = true;
                    props.armorPiercing = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "heavy_core"),
                ArrowComponentType.TIP,
                stack -> stack.getItem() == Items.HEAVY_CORE,
                "heavy_core",
                (stack, props) -> {
                    props.heavyCore = true;
                    props.heavyCoreHeightMultiplier = ModConfig.get().heavyCoreHeightMultiplier;
                    props.heavyCoreMaxBonus = ModConfig.get().heavyCoreMaxBonus;
                },
                true
        ));

        // Effects (без зелий)
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "gunpowder"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.GUNPOWDER,
                "gunpowder",
                (stack, props) -> {
                    props.explosive = true;
                    props.explosionPower = ModConfig.get().gunpowderExplosionPower;
                    props.explosionEntityBonus = ModConfig.get().gunpowderEntityBonus;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "glowstone_dust"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.GLOWSTONE_DUST,
                "glowstone_dust",
                (stack, props) -> {
                    if (ModConfig.get().glowstoneDamageEnabled)
                        props.bonusDamage += ModConfig.get().glowstoneBonusDamage;
                    props.glowTarget = ModConfig.get().glowstoneGlowEnabled;
                    props.glowDuration = ModConfig.get().glowstoneGlowDuration;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "slime_ball"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.SLIME_BALL,
                "slime_ball",
                (stack, props) -> {
                    props.bouncy = ModConfig.get().slimeBounceEnabled;
                    props.bounceCount = ModConfig.get().slimeBounceCount;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "honeycomb"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.HONEYCOMB,
                "honeycomb",
                (stack, props) -> {
                    props.spawnHoney = true;
                    props.slowTargetEffect = true;
                    props.slowTargetEffectDuration = 40;
                    props.slowTargetEffectAmplifier = 0;
                    props.replaceBlockWithHoney = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "ender_pearl"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.ENDER_PEARL,
                "ender_pearl",
                (stack, props) -> props.teleportShooter = ModConfig.get().enderPearlTeleportEnabled,
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "blaze_powder"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.BLAZE_POWDER,
                "blaze_powder",
                (stack, props) -> {
                    props.igniteGround = ModConfig.get().blazePowderIgniteEnabled;
                    props.meltSnow = true;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "turtle_helmet"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.TURTLE_HELMET,
                "turtle_helmet",
                (stack, props) -> {
                    if (ModConfig.get().turtleHelmetSlowEnabled) {
                        props.slowTargetEffect = true;
                        props.slowTargetEffectDuration = ModConfig.get().turtleHelmetSlowDuration;
                        props.slowTargetEffectAmplifier = 2;
                    }
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "dragon_breath"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.DRAGON_BREATH,
                "dragon_breath",
                (stack, props) -> {
                    props.dragonBreath = ModConfig.get().dragonBreathEnabled;
                    props.dragonBreathCloudRadius = ModConfig.get().dragonBreathCloudRadius;
                    props.dragonBreathCloudDuration = ModConfig.get().dragonBreathCloudDuration;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "heart_of_the_sea"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.HEART_OF_THE_SEA,
                "heart_of_the_sea",
                (stack, props) -> props.passWater = true,
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "wind_charge"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.WIND_CHARGE,
                "wind_charge",
                (stack, props) -> {
                    if (ModConfig.get().windChargeEnabled) {
                        props.windCharge = true;
                        props.windChargeDamage = ModConfig.get().windChargeDamage;
                        props.windChargeKnockback = ModConfig.get().windChargeKnockback;
                    }
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "lapis_lazuli"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.LAPIS_LAZULI,
                "lapis_lazuli",
                (stack, props) -> {
                    props.randomPotion = true;
                    props.lapisEffectDuration = ModConfig.get().lapisEffectDuration;
                    props.lapisEffectAmplifier = ModConfig.get().lapisEffectAmplifier;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "torch"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.TORCH,
                "torch",
                (stack, props) -> props.torchPlace = true,
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "fire_charge"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.FIRE_CHARGE,
                "fire_charge",
                (stack, props) -> {
                    if (ModConfig.get().fireChargeExplosionEnabled) {
                        props.fireCharge = true;
                        props.fireChargeExplosionPower = ModConfig.get().fireChargeExplosionPower;
                        props.fireChargeFireTicks = ModConfig.get().fireChargeFireTicks;
                    }
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "firework_star"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.FIREWORK_STAR,
                "firework_star",
                (stack, props) -> {
                    props.fireworkStar = true;
                    props.fireworkStarDamage = ModConfig.get().fireworkStarDamage;
                },
                true
        ));
        register(new ArrowComponent(
                Identifier.of("fletchery_expanded", "firework_rocket"),
                ArrowComponentType.EFFECT,
                stack -> stack.getItem() == Items.FIREWORK_ROCKET,
                "firework_rocket",
                (stack, props) -> {
                    props.fireworkRocket = true;
                    props.fireworkRocketDamage = ModConfig.get().fireworkRocketDamage;
                },
                true
        ));
    }
}
