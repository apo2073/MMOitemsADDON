package kr.apo2073.mmoitemsADDON.util;

import kr.apo2073.lib.Items.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SkillBookGUI implements Listener {
    private Inventory inv;
    public Inventory getInv(String skill, String castMode) {
        inv = Bukkit.createInventory(null, 9 * 6, "§6Skill §fBook Maker");
        inv.setItem(4, new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                .setDisplayName("§l§d스킬북 §b[ "+skill+" §b]")
                .build());
        inv.setItem(19, new ItemBuilder(new ItemStack(Material.PAPER))
                .setDisplayName("Skill ID")
                .setDescription(List.of(Component.text(skill)
                        .color(NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
                .build());
        inv.setItem(20, new ItemBuilder(new ItemStack(Material.STICK))
                .setDisplayName("CastMode")
                .setDescription(List.of(Component.text(castMode)
                        .color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
                .build());
        inv.setItem(23, new ItemBuilder(new ItemStack(Material.LAPIS_LAZULI))
                .setDisplayName("§bmana")
                .setLore(List.of("0"))
                .build());
        inv.setItem(24, new ItemBuilder(new ItemStack(Material.RABBIT_FOOT))
                .setDisplayName("§bstamina")
                .setLore(List.of("0"))
                .build());
        inv.setItem(25, new ItemBuilder(new ItemStack(Material.CLOCK))
                .setDisplayName("§bcooldown")
                .setLore(List.of("0"))
                .build());
        inv.setItem(32, new ItemBuilder(new ItemStack(Material.POTION))
                .setDisplayName("§bheal")
                .setLore(List.of("0"))
                .addItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                .build());
        inv.setItem(33, new ItemBuilder(new ItemStack(Material.SPLASH_POTION))
                .setDisplayName("§bduration")
                .setLore(List.of("0", "§c[ BUG ]"))
                .addItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS)
                .build());
        inv.setItem(34, new ItemBuilder(new ItemStack(Material.BREWING_STAND))
                .setDisplayName("§bextra")
                .setLore(List.of("0", "§c[ BUG ]"))
                .build());
        inv.setItem(41, new ItemBuilder(new ItemStack(Material.HEART_OF_THE_SEA))
                .setDisplayName("§bradius")
                .setLore(List.of("0"))
                .build());
        inv.setItem(42, new ItemBuilder(new ItemStack(Material.IRON_SWORD))
                .setDisplayName("§bdamage")
                .setLore(List.of("0", "§c[ BUG ]"))
                .build());
        inv.setItem(49, new ItemBuilder(new ItemStack(Material.LIME_STAINED_GLASS_PANE))
                .setDisplayName("§aDone")
                .build());
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getOriginalTitle().contains("§6Skill §fBook Maker")) return;
        Player player = (Player) e.getWhoClicked();
        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        e.setCancelled(true);

        switch (clicked.getType()) {
            case LAPIS_LAZULI, RABBIT_FOOT, CLOCK, POTION, HEART_OF_THE_SEA -> {
                if (clicked.getItemMeta() == null || clicked.getItemMeta().getLore() == null || clicked.getItemMeta().getLore().isEmpty())
                    return;

                String loreValue = clicked.getItemMeta().getLore().get(0);
                int currentValue;
                try {
                    currentValue = Integer.parseInt(loreValue);
                } catch (NumberFormatException ex) {
                    return;
                }

                if (e.getClick().isLeftClick()) {
                    currentValue++;
                } else if (e.getClick().isRightClick()) {
                    currentValue--;
                }

                currentValue = Math.max(0, currentValue);

                ItemMeta meta = clicked.getItemMeta();
                meta.setLore(List.of(String.valueOf(currentValue)));
                clicked.setItemMeta(meta);

                e.getInventory().setItem(e.getSlot(), clicked);
            }
            case IRON_SWORD, SPLASH_POTION, BREWING_STAND  -> {
                if (clicked.getItemMeta() == null
                        || clicked.getItemMeta().getLore() == null
                        || clicked.getItemMeta().getLore().isEmpty())
                    return;

                String loreValue = clicked.getItemMeta().getLore().get(0);
                double currentValue;
                try {
                    currentValue = Double.parseDouble(loreValue);
                } catch (NumberFormatException ex) {
                    return;
                }

                if (e.getClick().isLeftClick()) {
                    currentValue += 0.1;
                } else if (e.getClick().isRightClick()) {
                    currentValue -= 0.1;
                }

                currentValue = Math.max(0, currentValue);
                currentValue = Math.round(currentValue * 10.0) / 10.0;

                ItemMeta meta = clicked.getItemMeta();
                meta.setLore(List.of(String.format("%.1f", currentValue), "§c[ BUG ]"));
                clicked.setItemMeta(meta);

                e.getInventory().setItem(e.getSlot(), clicked);
            }
            case LIME_STAINED_GLASS_PANE -> {
                MMoAddon addon=new MMoAddon(player);
                String skillID= Objects.requireNonNull(e.getInventory().getItem(19)).getItemMeta().getLore().get(0).replace("§d", "");
                String castMode= Objects.requireNonNull(e.getInventory().getItem(20)).getItemMeta().getLore().get(0).replace("§a", "");
                HashMap<String, Object> modifiers = new HashMap<>();

                try {
                    for (int i = 0; i < e.getInventory().getSize(); i++) {
                        ItemStack item = e.getInventory().getItem(i);
                        if (item != null && item.getItemMeta() != null
                                && item.getItemMeta().getLore() != null
                                && !item.getItemMeta().getLore().isEmpty()
                        ) {
                            String itemName = item.getItemMeta().getDisplayName();
                            String loreValue = item.getItemMeta().getLore().get(0);
                            try {
                                if (Double.parseDouble(loreValue)==0.0) continue;
                            } catch (NumberFormatException ex) {
                                continue;
                            }

                            modifiers.put(itemName.replace("§b", ""), loreValue);
                        }
                    }
                } catch (NumberFormatException ex) {
                    player.sendMessage(Component.text(ex.getMessage()).color(NamedTextColor.RED));
                    ex.printStackTrace();
                }

                SkillBook skillBook=new SkillBook();
                ItemStack book= skillBook.getSkillBook(player, addon.getAbilityToJSon(skillID, castMode, modifiers));
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                player.getInventory().addItem(book);
            }
        }
    }
}
