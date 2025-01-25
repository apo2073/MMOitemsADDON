package kr.apo2073.mmoAddon.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kr.apo2073.lib.Items.ItemBuilder;
import kr.apo2073.lib.Plugins.CompKt;
import kr.apo2073.mmoAddon.MMOAddons;
import kr.apo2073.mmoAddon.exception.SkillBookNULL;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SkillBook {
    MMOAddons mma= MMOAddons.plugin;
    @Deprecated
    public ItemStack getSkillBook(ItemStack item) {
        try {
            mma.reloadConfig();
            MMoAddon addon = new MMoAddon(item);
            ItemStack book = new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                    .setItemName(CompKt.txt("§l§d스킬북 §b[ " + addon.getItemName() + " §b]"))
                    .setDescription(getLore(item))
                    .setCustomModelData(mma.getConfig().get("skillbook."+addon.getItemName())==null
                            ? 0 : mma.getConfig().getInt("skillbook."+addon.getItemName()))
                    .build();
            ItemMeta meta = book.getItemMeta();
            meta.getPersistentDataContainer().set(new NamespacedKey(MMOAddons.plugin, "json"), PersistentDataType.STRING, addon.getTagsValue("MMOITEMS_ABILITY"));
            mma.debug("skill book pdc set : "+meta.getPersistentDataContainer().get(new NamespacedKey(MMOAddons.plugin, "json"), PersistentDataType.STRING));
            book.setItemMeta(meta);
            mma.debug("getting skill book from item : "+item.getItemMeta().getDisplayName());
            return book;
        } catch (Exception e) {
            return null;
        }
    }
    public ItemStack getSkillBook(Player player, JsonArray json) {
        try {
            mma.reloadConfig();
            MMoAddon addon =new MMoAddon(player);
            addon.setAbilitiesJson(json);
            String name="§l§d스킬북 §b[ " + addon.getItemSkillID().replace("_", " ")+ " §b]";
            ItemStack book= new ItemBuilder(new ItemStack(Material.ENCHANTED_BOOK))
                    .setItemName(StringToAdventureComponent.toAdventureComponent(name))
                    .setDescription(getLore(json, player))
                    .setCustomModelData(mma.getConfig().get("skillbook."+addon.getItemSkillID())==null
                            ? 0 : mma.getConfig().getInt("skillbook."+addon.getItemSkillID()))
                    .build();
            ItemMeta meta= book.getItemMeta();

            meta.getPersistentDataContainer().set(new NamespacedKey(MMOAddons.plugin, "json"), PersistentDataType.STRING, "\""+json+"\"");
            mma.debug("skill book pdc set : "+meta.getPersistentDataContainer().get(new NamespacedKey(MMOAddons.plugin, "json"), PersistentDataType.STRING));

            book.setItemMeta(meta);
            mma.debug("getting skill book from json : "+json);

            return book;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SkillBookNULL();
        }
    }

    @Deprecated
    public ItemStack getNBTSkillBook(JsonArray json, Player player) {
        MMoAddon addon =new MMoAddon(player);
        addon.setAbilitiesJson(json);
        io.lumine.mythic.lib.api.explorer.ItemBuilder mmoItems=new io.lumine.mythic.lib.api.explorer.ItemBuilder(Material.ENCHANTED_BOOK,
                "§l§d스킬북 §b[ " + addon.getItemSkillID().replace("_", " ")+ " §b]"
                ).setLore(getLore(json, player).toArray(new String[0]));
        ItemMeta meta=mmoItems.getItemMeta();
        meta.getPersistentDataContainer().set(new NamespacedKey(MMOAddons.plugin, "json"), PersistentDataType.STRING, "\""+json+"\"");
        mmoItems.setItemMeta(meta);
        return mmoItems;
    }

    public List<Component> getLore(ItemStack item) {
        List<Component> lore= new ArrayList<>();
        MMoAddon addon =new MMoAddon(item);
        for (JsonElement element: addon.getAbilitiesJson()) {
            JsonObject object=element.getAsJsonObject();
            String castMode=object.get("CastMode").getAsString();
            lore.add(StringToAdventureComponent.toAdventureComponent("§a>§8| §7"+castMode +" §8|§e|§8| §7§l"+ addon.getItemSkillID()));
            addon.getModifiers().forEach((s, o) -> {
                lore.add(StringToAdventureComponent.toAdventureComponent(" §3>§8| §7"+s+"§8: §f"+o));
            });
        }
        mma.debug("skill book lore returned");
        return lore;
    }

    public List<Component> getLore(JsonArray json, Player player) {
        List<Component> lore= new ArrayList<>();
        for (JsonElement element: json) {
            MMoAddon addon =new MMoAddon(player);
            addon.setAbilitiesJson(json);
            JsonObject object=element.getAsJsonObject();
            String castMode=object.get("CastMode").getAsString();
            lore.add(StringToAdventureComponent.toAdventureComponent("§a>§8| §7"+castMode +" §8|§e|§8| §7§l"+ addon.getItemSkillID()));
            addon.getModifiers().forEach((s, o) -> {
                lore.add(StringToAdventureComponent.toAdventureComponent(" §3>§8| §7"+s+"§8: §f"+o));
            });
        }
        mma.debug("skill book lore returned");
        return lore;
    }
}
