package com.example.fletcheryaddon;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleFletcheryAddon implements ModInitializer {

    public static final String MOD_ID = "example_addon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Example Fletchery Addon initialized");
        AddonItems.register();
    }
}
