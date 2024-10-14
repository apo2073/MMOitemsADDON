package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.math3.analysis.function.Add;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

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
        Addon addon = new Addon(player.getInventory().getItemInMainHand());
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
            return addon.getItemCastMod();
        }
        return "";
    }
}
