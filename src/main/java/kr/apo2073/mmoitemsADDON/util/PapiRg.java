package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.math3.analysis.function.Add;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PapiRg extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mmoitemsAddon";
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
        Addon addon=new Addon(player.getInventory().getItemInMainHand());
        for (JsonElement element: addon.getAbilitiesJson()) {
            JsonObject object=element.getAsJsonObject();
            if (Objects.equals(object.get("CastMode").getAsString(), "SHIFT_RIGHT_CLICK")
                    && params.equals("SHIFT_RIGHT_CLICK")
            ) {
                return object.get("Id").getAsString();
            }
            if (Objects.equals(object.get("CastMode").getAsString(), "SHIFT_LEFT_CLICK")
                    && params.equals("SHIFT_LEFT_CLICK")
            ) {
                return object.get("Id").getAsString();
            }
            if (Objects.equals(object.get("CastMode").getAsString(), "RIGHT_CLICK")
                    && params.equals("RIGHT_CLICK")
            ) {
                return object.get("Id").getAsString();
            }
            if (Objects.equals(object.get("CastMode").getAsString(), "LEFT_CLICK")
                    && params.equals("LEFT_CLICK")
            ) {
                return object.get("Id").getAsString();
            }
        }
        if (params.equals("castmode")) {
            return addon.getItemCastMod();
        }
        return "";
    }
}
