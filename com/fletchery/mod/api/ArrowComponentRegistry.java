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

    // НОВЫЙ МЕТОД
    public int getBaseIndex(ArrowComponent comp) {
        List<ArrowComponent> list = components.get(comp.type());
        return list.indexOf(comp);
    }

    public int getModelData(ArrowComponent feather, ArrowComponent shaft, ArrowComponent tip, @Nullable ArrowComponent effect) {
        if (feather == null || shaft == null || tip == null) return 0;
        // Если все базовые – вычисляем как раньше
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
                // Эффекты идут после всех базовых комбинаций
                return base + totalBases * (eIdx + 1);
            }
            return base;
        } else {
            // Кастомные – генерируем уникальный ID
            String key = feather.id() + "|" + shaft.id() + "|" + tip.id() + "|" + (effect != null ? effect.id() : "none");
            int hash = key.hashCode() & 0x7FFFFFFF;
            if (hash < 10000) hash += 10000;
            return hash;
        }
    }

    public int nextCustomModelData() {
        return nextCustomModelData++;
    }

    // ... остальной код (registerBaseComponents и т.д.) ...
}