package kr.apo2073.mmoitemsADDON.event;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

public class SkillBookEQUIPevent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack book = e.getCursor();
        ItemStack item = e.getCurrentItem();
        if (book.getType() != Material.ENCHANTED_BOOK || item == null) return;
        String key = book.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING
        );
        if (key == null) return;
        e.setCancelled(true);
        MMoAddon addon = new MMoAddon(item);
        JsonArray json = new Gson().fromJson(key, JsonArray.class);
        try {
            json.forEach(element -> {
                String id = element.getAsJsonObject().get("Id").getAsString();
                String cast = element.getAsJsonObject().get("CastMode").getAsString();
                HashMap<String, Object> modifiersMap = new HashMap<>();
                JsonObject modifiersJson = element.getAsJsonObject().getAsJsonObject("Modifiers");
                modifiersJson.entrySet().forEach(entry -> {
                    modifiersMap.put(entry.getKey(), entry.getValue().getAsDouble());
                });
                addon.addAbilities(id, cast, modifiersMap);
            });

            e.setCurrentItem(addon.getItem());
            book.setAmount(book.getAmount() - 1);
        } catch (Exception ex) {
            player.sendMessage(Component.text(ex.getMessage()).color(NamedTextColor.RED));
        }
    }


}
