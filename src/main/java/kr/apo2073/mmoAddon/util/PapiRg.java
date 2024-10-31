package kr.apo2073.mmoAddon.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kr.apo2073.mmoAddon.MMOAddons;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PapiRg extends PlaceholderExpansion {
    private MMOAddons mma=MMOAddons.plugin;
    @Override
    public @NotNull String getIdentifier() {
        return "mmoaddon";
    }

    @Override
    public @NotNull String getAuthor() {
        return "아포칼립스";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.1";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        try {
            MMoAddon addon = new MMoAddon(player.getInventory().getItemInMainHand());
            String formattedParams = params.toLowerCase();

            for (JsonElement element : addon.getAbilitiesJson()) {
                JsonObject object = element.getAsJsonObject();
                JsonElement castModeElement = object.get("CastMode");
                if (castModeElement != null) {
                    String castMode = castModeElement.getAsString().toLowerCase();
                    if (formattedParams.equals(castMode)) {
                        return object.get("Id").getAsString();
                    }
                    JsonObject modifiers = object.getAsJsonObject("Modifiers");
                    if (modifiers != null) {
                        for (Map.Entry<String, JsonElement> entry : modifiers.entrySet()) {
                            String modifierKey = entry.getKey().toLowerCase();
                            if (formattedParams.equals(castMode + "_" + modifierKey)) {
                                return entry.getValue().getAsString();
                            }
                        }
                    }
                }
            }
            if (params.equals("castmode")) {
                return addon.getItemCastMode().replace("\"", "");
            }
            if (params.contains("tags_")) {
                String param = params.replace("tags_", "");
                return addon.getTagsValue(param);
            }
            if (params.equals("tags")) addon.getTags();
            mma.debug("called papi : "+params);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "";
    }
}
