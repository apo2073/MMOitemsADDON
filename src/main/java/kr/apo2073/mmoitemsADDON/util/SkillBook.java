package kr.apo2073.mmoitemsADDON.util;

import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTCompound;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.lib.Items.ItemBuilder;
import kr.apo2073.lib.Plugins.CompKt;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SkillBook {
    public ItemStack getSkillBook(Player player, ItemStack item) {
        Addon addon=new Addon(item).setPlayer(player);
        ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                .setItemName(CompKt.txt("§l§d스킬북 §b[ "+addon.getItemName()+ " §b]"))
                .setLore(getLore(item))
                .build();
        ItemMeta meta= book.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "IsBOOK"), PersistentDataType.STRING, addon.getTagsValue("MMOITEMS_ABILITY"));
        book.setItemMeta(meta);
        return book;
    }

    public List<String> getLore(ItemStack item) {
        List<String> lore= new ArrayList<>();
        Addon addon=new Addon(item);
        lore.add("§a>§8| §7"+addon.getItemCastMod() +" §8|§e|§8| §7§l"+addon.getItemSkillID());
        addon.getModifiers().forEach((s, o) -> {
            lore.add(" §3>§8| §7"+s+"§8: §f"+o);
        });
        return lore;
    }
}
