package com.example.fletcheryaddon;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class AddonItems {

    public static final Item RUBY = new Item(new Item.Settings());

    private AddonItems() {}

    public static void register() {
        Registry.register(
                Registries.ITEM,
                Identifier.of(ExampleFletcheryAddon.MOD_ID, "ruby"),
                RUBY
        );
    }
}
