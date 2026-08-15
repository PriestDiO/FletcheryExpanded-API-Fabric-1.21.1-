# Fletchery Expanded

A deep crafting system for arrows for Fabric 1.21.x. Turns the fletching
table into a powerful workstation: combine feathers, shafts, tips, and
effects to craft thousands of unique arrows.

[Russian version / Русская версия](README.ru.md)

## For players

See the mod page on [Modrinth](#) / [CurseForge](#) for installation and
feature overview.

## For mod developers — Arrow Component API

Since version X.X, Fletchery Expanded exposes a public API that lets other
mods register their own feathers, shafts, tips, and effects, which then
appear in the fletching table alongside the built-in ones.

- **Getting started, Gradle setup, code examples:** [`docs/API_USAGE.md`](docs/API_USAGE.md)
  ([Russian: `docs/API_USAGE.ru.md`](docs/API_USAGE.ru.md))
- **How to verify your addon actually works:** [`docs/TESTING.md`](docs/TESTING.md)
- **Runnable example addon:** [`examples/addon-example/`](examples/addon-example/)
- **API source (what you're actually depending on):**
  [`src/main/java/com/fletchery/mod/api/`](src/main/java/com/fletchery/mod/api/)

### Quick start

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    modImplementation "com.github.<your-github-user>:fletchery-expanded:<release-tag>"
}
```

```json
// your mod's fabric.mod.json
"entrypoints": {
    "fletchery_component": ["com.yourmod.YourFletcheryAddon"]
}
```

```java
public class YourFletcheryAddon implements FletcheryComponentInitializer {
    @Override
    public void onRegisterArrowComponents() {
        FletcheryComponentRegistry.register(
            FletcheryComponentRegistry.Slot.TIP,
            new YourCustomTip()
        );
    }
}
```

Full walkthrough in [`docs/API_USAGE.md`](docs/API_USAGE.md).

### Known limitation

Client-side model rendering for third-party (non-built-in) components is
not implemented yet — addon arrows will craft, apply their stats, and
behave correctly in combat, but will currently render with a
missing/placeholder texture until this is added. Tracked as an open item;
see the note in `src/main/java/com/fletchery/mod/api/ArrowModelKey.java`.

## License

MIT
