# Fletchery Expanded API

API for adding custom arrow components to the **Fletchery Expanded** mod.

With this API, addon developers can add:
- New feathers
- New shafts
- New tips
- New effects

---

## How to use

1. Download or clone this repository.
2. Copy the `com/fletchery/mod/api/` folder into your project.
3. Make sure your project has:
   - Fabric API
   - Minecraft 1.21.4+

That's it! No Maven, no Gradle configuration — just copy and use.

---

## Example: Adding a new "Ruby" tip

```java
import com.fletchery.mod.api.ComponentRegistration;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class RubyAddon {
    public static void register() {
        ComponentRegistration reg = new ComponentRegistration();
        
        reg.registerTip(
            Identifier.of("ruby_addon", "ruby"),            // Component ID
            stack -> stack.getItem() == Items.AMETHYST_SHARD, // Ingredient
            "ruby",                                          // Texture name
            (stack, props) -> {                              // Properties
                props.bonusDamage += 5;
                props.glowTarget = true;
                props.glowDuration = 100;
            }
        );
    }
}
