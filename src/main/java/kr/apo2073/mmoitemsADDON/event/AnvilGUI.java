package kr.apo2073.mmoitemsADDON.event;

import jdk.jfr.Experimental;
import kr.apo2073.mmoitemsADDON.util.SkillBookGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AnvilGUI implements Listener {
    public Map<Player, String> skill=new HashMap<>();
    public Map<Player, String> castmode=new HashMap<>();
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getType() == InventoryType.ANVIL)) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        SkillBookGUI gui = new SkillBookGUI();
        if (e.getView().getOriginalTitle().contains("Skill Name")
                || e.getView().getOriginalTitle().contains("CastMode")) {
            Player player = (Player) e.getWhoClicked();
            if (e.getRawSlot() == 2) {
                ItemStack item = e.getCurrentItem();
                if (item.hasItemMeta()) {
                    String text = item.getItemMeta().getDisplayName();
                    System.out.println(text);
                    player.closeInventory();
                    if (e.getView().getOriginalTitle().contains("Skill Name")) {
                        skill.put(player, text);
                    } else if (e.getView().getOriginalTitle().contains("CastMode")) {
                        castmode.put(player, text);
                    }
                    Inventory inv = gui.getInv(skill.get(player), castmode.get(player));
                    player.openInventory(inv);
                }
            }
            e.setCancelled(true);
        }
    }
}
