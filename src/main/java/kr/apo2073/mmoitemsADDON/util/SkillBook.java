package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        Addon addon=new Addon(item);
        addon.setPlayer(player);
        ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                .setItemName(CompKt.txt("§l§d스킬북 §b[ "+addon.getItemName()+ " §b]"))
                .setLore(getLore(item))
                .build();
        ItemMeta meta= book.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "IsBOOK"), PersistentDataType.STRING, addon.getTagsValue("MMOITEMS_ABILITY"));
        book.setItemMeta(meta);
        return book;
    }
    public ItemStack getSkillBook(Player player, JsonArray json) {
        try {
            Addon addon=new Addon(player);
            addon.setPlayer(player);
            addon.setAbilitiesJson(json);
            ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                    .setItemName(CompKt.txt("§l§d스킬북 §b[ "
                            + addon.getItemSkillID().replace("_", " ")+ " §b]"))
                    .setLore(getLore(json, player))
                    .build();
            ItemMeta meta= book.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "IsBOOK"), PersistentDataType.STRING, json.toString());
            book.setItemMeta(meta);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getLore(ItemStack item) {
        List<String> lore= new ArrayList<>();
        Addon addon=new Addon(item);
        for (JsonElement element: addon.getAbilitiesJson()) {
            JsonObject object=element.getAsJsonObject();
            String castMode=object.get("CastMode").getAsString();
            lore.add("§a>§8| §7"+castMode +" §8|§e|§8| §7§l"+addon.getItemSkillID());
            addon.getModifiers().forEach((s, o) -> {
                lore.add(" §3>§8| §7"+s+"§8: §f"+o);
            });
        }
        return lore;
    }

    public List<String> getLore(JsonArray json, Player player) {
        List<String> lore= new ArrayList<>();
        Addon addon=new Addon(player);
        addon.setAbilitiesJson(json);
        lore.add("§a>§8| §7"+addon.getItemCastMod() +" §8|§e|§8| §7§l"+addon.getItemSkillID());
        addon.getModifiers().forEach((s, o) -> {
            lore.add(" §3>§8| §7"+s+"§8: §f"+o);
        });
        return lore;
    }

}
