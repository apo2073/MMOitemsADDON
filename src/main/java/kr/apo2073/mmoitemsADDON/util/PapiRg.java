package kr.apo2073.mmoitemsADDON.util;

import io.lumine.mythic.lib.math3.analysis.function.Add;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiRg extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mmoitems";
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
        if (params.contains("right_click")) {
            return "";
        }
        if (params.contains("left_click")) {
            return "";
        }

        if (params.equals("castmode")) {
            return addon.getItemCastMod();
        }
        return "";
    }
}
