package com.example.fletcheryaddon;

import com.fletchery.mod.api.ArrowComponent;
import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class RubyArrowTip implements ArrowComponent {

    @Override
    public Identifier id() {
        return Identifier.of(ExampleFletcheryAddon.MOD_ID, "ruby");
    }

    @Override
    public Item item() {
        return AddonItems.RUBY;
    }

    @Override
    public String modelFile() {
        return "ruby_tip";
    }

    @Override
    public void apply(ArrowProperties props, ModConfig cfg) {
        props.bonusDamage += 3;
        props.armorPiercing = true;
    }
}
