# Fletchery Expanded — API for mod developers

[Русский](API.md) | **English**

This document describes how to register your own arrow components (feathers, shafts, tips, effects)
from another mod so they show up in the fletching table, in JEI/REI, and get their own texture.

A fully working addon example lives in [`examples/example-addon`](../examples/example-addon).

## How it works

Fletchery Expanded looks for the `fletchery_component` entrypoint in every installed mod's
`fabric.mod.json`. When the component registry is initialized, it calls
`onRegisterArrowComponents()` on every entrypoint found — that's where you register your components
via `FletcheryComponentRegistry`.

The slot (`FEATHER`, `SHAFT`, `TIP`, `EFFECT`) determines which of the four fletching table slots the
component belongs to.

## Step 1. Add the dependency

Add Fletchery Expanded as a compile-time dependency (point the Maven repo/jar to however you're
distributing it, e.g. the Modrinth Maven or a local jar in `libs/`):

```gradle
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    modImplementation "maven.modrinth:fletchery-expanded:<version>"
}
```

## Step 2. Register the entrypoint

In your mod's `fabric.mod.json`, add:

```json
{
  "entrypoints": {
    "fletchery_component": [
      "com.example.fletcheryaddon.MyFletcheryComponents"
    ]
  }
}
```

## Step 3. Implement `FletcheryComponentInitializer`

```java
package com.example.fletcheryaddon;

import com.fletchery.mod.api.FletcheryComponentInitializer;
import com.fletchery.mod.api.FletcheryComponentRegistry;
import com.fletchery.mod.api.FletcheryComponentRegistry.Slot;

public class MyFletcheryComponents implements FletcheryComponentInitializer {
    @Override
    public void onRegisterArrowComponents() {
        FletcheryComponentRegistry.register(Slot.TIP, new RubyArrowTip());
    }
}
```

## Step 4. Implement `ArrowComponent`

```java
package com.example.fletcheryaddon;

import com.fletchery.mod.api.ArrowComponent;
import com.fletchery.mod.arrow.ArrowProperties;
import com.fletchery.mod.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class RubyArrowTip implements ArrowComponent {

    @Override
    public Identifier id() {
        // unique component id, usually = the registry id of the item
        return Identifier.of("example_addon", "ruby");
    }

    @Override
    public Item item() {
        // the item the player places in the fletching table slot to use this component
        return MyItems.RUBY;
    }

    @Override
    public String modelFile() {
        // name of the tip model file (no extension), see "Models and textures" below
        return "ruby_tip";
    }

    @Override
    public void apply(ArrowProperties props, ModConfig cfg) {
        // configure the resulting arrow's stats here
        props.bonusDamage += 3;
        props.armorPiercing = true;
    }
}
```

### `ArrowProperties` fields

`ArrowProperties` is a set of public fields (damage, speed, gravity, ignite, potion effects, armor
piercing, explosions, etc.) that you can modify in `apply()`. For the full, up-to-date list of fields,
check the source: `com.fletchery.mod.arrow.ArrowProperties`. Components are applied sequentially for
all four slots, so you can both add to existing values (`+=`) and set flags (`= true`).

## Models and textures

Every component needs an item texture/model (for inventory display and the 3D preview in the fletching
table) — register it the usual Fabric way (your mod's resource pack,
`assets/<namespace>/models/item/...`, `assets/<namespace>/textures/item/...`).

`modelFile()` is used by Fletchery's system to generate the composite arrow model — make sure the name
you provide matches resources your mod actually ships.

## Registration and initialization order

- Every call into `FletcheryComponentRegistry` (other than registration itself via the entrypoint)
  triggers `ensureReady()`, which lazily initializes the registry on first use — you don't need to call
  it manually.
- Registering the same `id()` twice throws `IllegalStateException` — ids must be unique within a slot.
- Don't register components outside of `onRegisterArrowComponents()` (e.g. in a static class
  initializer) — this can cause a race condition during game startup.

## JEI / REI

Components registered via the API are automatically included in the combinations shown by the mod's
JEI/REI integration — no separate recipe registration needed.

## Version compatibility

The API is stable within a major version of the mod (see `CHANGELOG.md`). Breaking changes to the
`ArrowComponent`/`ArrowProperties` interfaces will be announced separately with a migration guide.

## Questions

If something doesn't work or documentation is missing, open an [issue](../../issues) in this
repository.
