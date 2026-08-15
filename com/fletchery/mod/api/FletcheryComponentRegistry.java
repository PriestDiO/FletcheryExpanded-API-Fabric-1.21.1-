package com.fletchery.mod.api;

import com.fletchery.mod.FletcheryExpanded;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.*;

public final class FletcheryComponentRegistry {

    public enum Slot { FEATHER, SHAFT, TIP, EFFECT }

    private static final Map<Slot, Map<Identifier, ArrowComponent>> BY_ID = new EnumMap<>(Slot.class);
    private static final Map<Slot, Map<Item, ArrowComponent>> BY_ITEM = new EnumMap<>(Slot.class);
    private static final Map<Slot, Set<Identifier>> LEGACY = new EnumMap<>(Slot.class);
    private static final Map<Slot, List<Identifier>> LEGACY_ORDER = new EnumMap<>(Slot.class);

    private static boolean bootstrapped = false;
    private static boolean entrypointsInvoked = false;

    private FletcheryComponentRegistry() {}

    public static synchronized void ensureReady() {
        if (bootstrapped && entrypointsInvoked) return;
        if (!bootstrapped) {
            for (Slot s : Slot.values()) {
                BY_ID.put(s, new LinkedHashMap<>());
                BY_ITEM.put(s, new HashMap<>());
                LEGACY.put(s, new LinkedHashSet<>());
                LEGACY_ORDER.put(s, new ArrayList<>());
            }
            LegacyArrowComponents.registerAll();
            bootstrapped = true;
        }
        if (!entrypointsInvoked) {
            entrypointsInvoked = true; // set before invoking — in case of a reentrant ensureReady() call from an addon
            int beforeCount = countAll();
            FabricLoader.getInstance().invokeEntrypoints(
                    "fletchery_component",
                    FletcheryComponentInitializer.class,
                    FletcheryComponentInitializer::onRegisterArrowComponents
            );
            int afterCount = countAll();
            FletcheryExpanded.LOGGER.info(
                    "[Fletchery API] Registered components: feathers={}, shafts={}, tips={}, effects={} (third-party added: {})",
                    BY_ID.get(Slot.FEATHER).size(), BY_ID.get(Slot.SHAFT).size(),
                    BY_ID.get(Slot.TIP).size(), BY_ID.get(Slot.EFFECT).size(),
                    afterCount - beforeCount
            );
        }
    }

    private static int countAll() {
        int total = 0;
        for (Slot s : Slot.values()) total += BY_ID.get(s).size();
        return total;
    }

    public static synchronized void register(Slot slot, ArrowComponent component) {
        Identifier id = component.id();
        if (BY_ID.get(slot).putIfAbsent(id, component) != null) {
            throw new IllegalStateException("Component already registered: " + id + " (" + slot + ")");
        }
        BY_ITEM.get(slot).put(component.item(), component);
        FletcheryExpanded.LOGGER.debug("[Fletchery API] Registered component {} in slot {}", id, slot);
    }

    static synchronized void registerLegacy(Slot slot, ArrowComponent component) {
        register(slot, component);
        LEGACY.get(slot).add(component.id());
        LEGACY_ORDER.get(slot).add(component.id());
    }

    public static Optional<ArrowComponent> byItem(Slot slot, Item item) {
        ensureReady();
        return Optional.ofNullable(BY_ITEM.get(slot).get(item));
    }

    public static Optional<ArrowComponent> byKey(Slot slot, String key) {
        ensureReady();
        if (key == null || key.isEmpty()) return Optional.empty();
        return Optional.ofNullable(BY_ID.get(slot).get(normalize(key)));
    }

    public static List<ArrowComponent> all(Slot slot) {
        ensureReady();
        return List.copyOf(BY_ID.get(slot).values());
    }

    public static boolean isLegacy(Slot slot, String key) {
        ensureReady();
        if (key == null || key.isEmpty()) return true; // empty effect = "no effect" = legacy
        return LEGACY.get(slot).contains(normalize(key));
    }
    public static boolean allLegacy(String feather, String shaft, String tip, String effect) {
        return isLegacy(Slot.FEATHER, feather) && isLegacy(Slot.SHAFT, shaft)
                && isLegacy(Slot.TIP, tip) && isLegacy(Slot.EFFECT, effect);
    }
    public static int legacyIndexOf(Slot slot, String key) {
        ensureReady();
        if (key == null || key.isEmpty()) return -1;
        return LEGACY_ORDER.get(slot).indexOf(normalize(key));
    }

    public static int legacySize(Slot slot) {
        ensureReady();
        return LEGACY_ORDER.get(slot).size();
    }

    private static Identifier normalize(String key) {
        String k = key.contains(":") ? key : "minecraft:" + key;
        Identifier id = Identifier.tryParse(k);
        if (id == null) {
            FletcheryExpanded.LOGGER.warn("Invalid arrow component key: {}", key);
        }
        return id;
    }
}
