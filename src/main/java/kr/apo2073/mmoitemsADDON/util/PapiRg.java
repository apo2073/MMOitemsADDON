package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PapiRg extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mmoitem";
    }

    @Override
    public @NotNull String getAuthor() {
        return "아포칼립스";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        try {
            MMoAddon addon = new MMoAddon(player.getInventory().getItemInMainHand());
            for (JsonElement element : addon.getAbilitiesJson()) {
                JsonObject object = element.getAsJsonObject();
                JsonElement castModeElement = object.get("CastMode");
                if (castModeElement != null) {
                    String castMode = castModeElement.getAsString().toLowerCase();
                    String formattedParams = params.toLowerCase();
                    if (formattedParams.equals(castMode)) {
                        return object.get("Id").getAsString();
                    }
                    for (Map.Entry<String, Object> mod : addon.getModifiers().entrySet()) {
                        String modifierKey = mod.getKey().toLowerCase();
                        if (formattedParams.equals(castMode + "_" + modifierKey)) {
                            return mod.getValue().toString();
                        }
                    }
                }
            }
            if (params.equals("castmode")) {
                return addon.getItemCastMode();
            }
            if (params.contains("tags_")) {
                String param = params.replace("tags_", "");
                return addon.getTagsValue(param);
            }
            if (params.equals("tags")) addon.getTags();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }
}
