package kr.apo2073.mmoitemsADDON.event;

import com.google.gson.*;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SkillBookEQUIPevent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack book = e.getCursor();
        ItemStack item = e.getCurrentItem();

        if (book.getType() != Material.ENCHANTED_BOOK
                || Objects.requireNonNull(
                        book.getItemMeta()
                                .displayName()).contains(Component.text("스킬북"))
                || item == null || item.getType() == Material.AIR) return;
        String key = book.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING
        );
        if (NBTItem.get(item).getType()==null) return;
        if (key == null) return;
        if (!player.hasPermission("mmoitems.magic")) return;
        MMoAddon addon = new MMoAddon(item);
        JsonArray json = new Gson().fromJson(key, JsonArray.class);
        try {
            for (JsonElement element : json) {

                for (JsonElement je: addon.getAbilitiesJson())
                    if (je.getAsJsonObject().get("CastMode")
                            .equals(element.getAsJsonObject().get("CastMode")))
                        addon.removeAbilities(je.getAsJsonObject()
                                .get("Id").getAsString());

                JsonObject object = element.getAsJsonObject();
                String id = object.get("Id").getAsString();
                String cast = object.get("CastMode").getAsString();
                HashMap<String, Object> modifiersMap = new HashMap<>();
                JsonObject modJson = object.getAsJsonObject("Modifiers");
                for (Map.Entry<String, JsonElement> entry : modJson.entrySet()) {
                    if (entry.getValue().isJsonPrimitive()) {
                        JsonPrimitive pri = entry.getValue().getAsJsonPrimitive();
                        if (pri.isNumber()) {
                            modifiersMap.put(entry.getKey(), pri.getAsDouble());
                        } else if (pri.isString()) {
                            modifiersMap.put(entry.getKey(), pri.getAsString());
                        } else if (pri.isBoolean()) {
                            modifiersMap.put(entry.getKey(), pri.getAsBoolean());
                        }
                    }
                }
                addon.addAbilities(id, cast, modifiersMap);
                e.setCurrentItem(addon.getItem());
            }
            e.setResult(Event.Result.DENY);
            e.setCursor(null);
            player.updateInventory();

        } catch (Exception ex) {
            player.sendMessage(Component.text(ex.getMessage()).color(NamedTextColor.RED));
        }
    }
}
