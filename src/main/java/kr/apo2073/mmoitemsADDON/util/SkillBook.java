package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.lib.Items.ItemBuilder;
import kr.apo2073.lib.Plugins.CompKt;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.data.AbilityData;
import net.Indyuce.mmoitems.stat.data.AbilityListData;
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
        MMoAddon addon =new MMoAddon(item);
        addon.setPlayer(player);
        ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                .setItemName(CompKt.txt("§l§d스킬북 §b[ "+ addon.getItemName()+ " §b]"))
                .setLore(getLore(item))
                .build();
        ItemMeta meta= book.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "IsBOOK"), PersistentDataType.STRING, addon.getTagsValue("MMOITEMS_ABILITY"));
        book.setItemMeta(meta);
        NBTItem nbtItem=NBTItem.get(book);
        LiveMMOItem liveMMOItem=new LiveMMOItem(book);
        AbilityData data=new AbilityData(addon.getAbilitiesJson().getAsJsonObject());
        AbilityListData listData=new AbilityListData(data);
        liveMMOItem.replaceData(ItemStats.ABILITIES, listData);
        return liveMMOItem.newBuilder().build();
    }
    public ItemStack getSkillBook(Player player, JsonArray json) {
        try {
            MMoAddon addon =new MMoAddon(player);
            addon.setPlayer(player);
            addon.setAbilitiesJson(json);
            ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                    .setItemName(CompKt.txt("§l§d스킬북 §b[ "
                            + addon.getItemSkillID().replace("_", " ")+ " §b]"))
                    .setLore(getLore(json, player))
                    .build();
            ItemMeta meta= book.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING, json.toString());
            book.setItemMeta(meta);
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ItemStack getNBTSkillBook(JsonArray json, Player player) {
        MMoAddon addon =new MMoAddon(player);
        addon.setAbilitiesJson(json);
        io.lumine.mythic.lib.api.explorer.ItemBuilder mmoItems=new io.lumine.mythic.lib.api.explorer.ItemBuilder(Material.ENCHANTED_BOOK,
                "§l§d스킬북 §b[ " + addon.getItemSkillID().replace("_", " ")+ " §b]"
                ).setLore(getLore(json, player).toArray(new String[0]));
        ItemMeta meta=mmoItems.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(MMoItemsADDON.plugin, "json"), PersistentDataType.STRING,json.toString());
        mmoItems.setItemMeta(meta);
        NBTItem nbtItem=NBTItem.get(mmoItems);
        nbtItem.addTag(new ItemTag("MMOITEM_ABILITY", json.toString()));
        return mmoItems;
    }

    public List<String> getLore(ItemStack item) {
        List<String> lore= new ArrayList<>();
        MMoAddon addon =new MMoAddon(item);
        for (JsonElement element: addon.getAbilitiesJson()) {
            JsonObject object=element.getAsJsonObject();
            String castMode=object.get("CastMode").getAsString();
            lore.add("§a>§8| §7"+castMode +" §8|§e|§8| §7§l"+ addon.getItemSkillID());
            addon.getModifiers().forEach((s, o) -> {
                lore.add(" §3>§8| §7"+s+"§8: §f"+o);
            });
        }
        return lore;
    }

    public List<String> getLore(JsonArray json, Player player) {
        List<String> lore= new ArrayList<>();
        for (JsonElement element: json) {
            MMoAddon addon =new MMoAddon(player);
            addon.setAbilitiesJson(json);
            JsonObject object=element.getAsJsonObject();
            String castMode=object.get("CastMode").getAsString();
            lore.add("§a>§8| §7"+castMode +" §8|§e|§8| §7§l"+ addon.getItemSkillID());
            addon.getModifiers().forEach((s, o) -> {
                lore.add(" §3>§8| §7"+s+"§8: §f"+o);
            });
        }
        return lore;
    }
}
