package kr.apo2073.mmoitemsADDON.event;

import com.google.gson.*;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class SkillBookEQUIPevent implements Listener {
    @EventHandler(priority =EventPriority.MONITOR)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack book = e.getCursor();
        ItemStack item = e.getCurrentItem();
        if (book == null || book.getType() != Material.ENCHANTED_BOOK
                || book.getItemMeta().displayName().contains(Component.text("스킬북"))
                || item == null || item.getType()==Material.AIR) return;
        e.setCancelled(true);
        String key = book.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING
        );
        if (key == null) return;
        if (!player.hasPermission("mmoitems.magic")) return;
        MMoAddon addon = new MMoAddon(item);
        JsonArray json = new Gson().fromJson(key, JsonArray.class);
        try {
            for (JsonElement element : json) {
                JsonObject abilityObj = element.getAsJsonObject();
                String id = abilityObj.get("Id").getAsString();
                String cast = abilityObj.get("CastMode").getAsString();
                HashMap<String, Object> modifiersMap = new HashMap<>();
                JsonObject modifiersJson = abilityObj.getAsJsonObject("Modifiers");
                for (Map.Entry<String, JsonElement> entry : modifiersJson.entrySet()) {
                    if (entry.getValue().isJsonPrimitive()) {
                        JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
                        if (primitive.isNumber()) {
                            modifiersMap.put(entry.getKey(), primitive.getAsDouble());
                        } else if (primitive.isString()) {
                            modifiersMap.put(entry.getKey(), primitive.getAsString());
                        } else if (primitive.isBoolean()) {
                            modifiersMap.put(entry.getKey(), primitive.getAsBoolean());
                        }
                    }
                }
                addon.addAbilities(id, cast, modifiersMap);
            }

            e.setCurrentItem(addon.getItem());

        } catch (Exception ex) {
            player.sendMessage(Component.text(ex.getMessage()).color(NamedTextColor.RED));
        }
    }
}
