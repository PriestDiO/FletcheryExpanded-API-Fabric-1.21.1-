package com.fletchery.mod.api;

import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public interface ArrowComponent {
    Identifier id();
    Item item();
    String modelFile();
    void apply(ArrowProperties props, ModConfig cfg);
}
