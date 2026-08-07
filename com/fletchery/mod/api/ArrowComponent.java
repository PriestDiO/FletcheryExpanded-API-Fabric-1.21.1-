package com.fletchery.mod.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public record ArrowComponent(
        Identifier id,
        ArrowComponentType type,
        Predicate<ItemStack> ingredientMatcher,
        String textureName,
        IArrowPropertyApplier propertyApplier,
        boolean isBase // true – встроенный, false – кастомный
) {
    public ArrowComponent {
        if (textureName == null || textureName.isEmpty()) {
            textureName = id.getPath();
        }
    }
}