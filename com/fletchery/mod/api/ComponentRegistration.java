package com.fletchery.mod.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public class ComponentRegistration {
    private final ArrowComponentRegistry registry = ArrowComponentRegistry.get();

    public ArrowComponent registerFeather(Identifier id, Predicate<ItemStack> matcher, String textureName, IArrowPropertyApplier applier) {
        ArrowComponent comp = new ArrowComponent(id, ArrowComponentType.FEATHER, matcher, textureName, applier, false);
        registry.register(comp);
        return comp;
    }

    public ArrowComponent registerShaft(Identifier id, Predicate<ItemStack> matcher, String textureName, IArrowPropertyApplier applier) {
        ArrowComponent comp = new ArrowComponent(id, ArrowComponentType.SHAFT, matcher, textureName, applier, false);
        registry.register(comp);
        return comp;
    }

    public ArrowComponent registerTip(Identifier id, Predicate<ItemStack> matcher, String textureName, IArrowPropertyApplier applier) {
        ArrowComponent comp = new ArrowComponent(id, ArrowComponentType.TIP, matcher, textureName, applier, false);
        registry.register(comp);
        return comp;
    }

    public ArrowComponent registerEffect(Identifier id, Predicate<ItemStack> matcher, String textureName, IArrowPropertyApplier applier) {
        ArrowComponent comp = new ArrowComponent(id, ArrowComponentType.EFFECT, matcher, textureName, applier, false);
        registry.register(comp);
        return comp;
    }
}