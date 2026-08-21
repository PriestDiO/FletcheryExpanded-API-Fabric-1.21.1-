package com.example.fletcheryaddon;

import com.fletchery.mod.api.FletcheryComponentInitializer;
import com.fletchery.mod.api.FletcheryComponentRegistry;
import com.fletchery.mod.api.FletcheryComponentRegistry.Slot;

public class FletcheryAddonComponents implements FletcheryComponentInitializer {

    @Override
    public void onRegisterArrowComponents() {
        FletcheryComponentRegistry.register(Slot.TIP, new RubyArrowTip());
    }
}
