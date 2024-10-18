package kr.apo2073.mmoitemsADDON.event;

import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SkillBookEQUIPevent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player= e.getWhoClicked().getKiller();
        ItemStack book=e.getCursor();
        ItemStack item=e.getCurrentItem();
        if (book.getType()!= Material.ENCHANTED_BOOK || item==null) return;
        System.out.println("a");
        if (book.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING)==null
        ) return;
        System.out.println("B");
        String key=book.getItemMeta().getPersistentDataContainer().get(
                new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING
        );
        MMoAddon MMoAddon =new MMoAddon(item);
        System.out.println(key);

    }
}
