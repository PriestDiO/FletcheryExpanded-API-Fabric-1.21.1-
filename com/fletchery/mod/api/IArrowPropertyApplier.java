package com.fletchery.mod.api;

import com.fletchery.mod.arrow.ArrowProperties;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IArrowPropertyApplier {
    void apply(ItemStack ingredient, ArrowProperties properties);
}