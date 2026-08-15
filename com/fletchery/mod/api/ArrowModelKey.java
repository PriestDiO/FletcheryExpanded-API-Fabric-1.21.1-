package com.fletchery.mod.api;

import com.fletchery.mod.FletcheryExpanded;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public record ArrowModelKey(Identifier featherId, Identifier shaftId, Identifier tipId, Identifier effectId) {

    public static ArrowModelKey from(NbtCompound nbt) {
        return new ArrowModelKey(
                idOrDefault(nbt.getString("feather")),
                idOrDefault(nbt.getString("shaft")),
                idOrDefault(nbt.getString("tip")),
                nbt.getString("effect").isEmpty() ? NONE : idOrDefault(nbt.getString("effect"))
        );
    }

    private static final Identifier NONE = Identifier.of(FletcheryExpanded.MOD_ID, "none");

    private static Identifier idOrDefault(String key) {
        if (key == null || key.isEmpty()) return NONE;
        Identifier id = Identifier.tryParse(key.contains(":") ? key : "minecraft:" + key);
        return id != null ? id : NONE;
    }

    public Identifier toModelPath() {
        String path = "item/arrow_ext/" +
                sanitize(featherId) + "__" + sanitize(shaftId) + "__" +
                sanitize(tipId) + "__" + sanitize(effectId);
        return Identifier.of(FletcheryExpanded.MOD_ID, path);
    }

    private static String sanitize(Identifier id) {
        return id.getNamespace() + "_" + id.getPath();
    }
}
