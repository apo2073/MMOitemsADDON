package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.exception.TheresNoItemIdiot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static kr.apo2073.mmoitemsADDON.MMoItemsADDON.plugin;

public class MMOItemsAddon {
    private Player player;
    private JsonArray json;
    private ItemStack item;
    private NBTItem nbtItem;

    public MMOItemsAddon(Player player) { this.player=player; }

    public MMOItemsAddon(ItemStack item) {
        this.item=item;
        this.nbtItem=NBTItem.get(item);
        plugin.getLogger().info(this.nbtItem.getTags().toString());
        plugin.getLogger().info(this.nbtItem.getString("MMOITEMS_ABILITY"));
        this.json= JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
    }

    public MMOItemsAddon setPlayer(Player player) { this.player = player; this.item=player.getInventory().getItemInMainHand(); return this;}
    public Player getPlayer() { return player; }

    public MMOItemsAddon setItem(ItemStack item) {
        this.item=item;
        this.nbtItem=NBTItem.get(item);
        this.json= JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
        return this;
    }
    public MMOItemsAddon setJson(JsonArray json) { this.json = json; return this;}

    public NBTItem getNbtItem() {return nbtItem;}

    public MMOItemsAddon setNbtItem(NBTItem nbtItem) {this.nbtItem = nbtItem; return this;}

    public ItemStack getItem() {return item;}

    public String getJsonToString(ItemStack item) {
        this.nbtItem=NBTItem.get(item);
        this.json=JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
        return json.toString();
    }
    public JsonArray getJson() { return json; }

    public String getTags() {
        if (item==null || nbtItem==null)
            throw new TheresNoItemIdiot();
        return nbtItem.getTags().toString();
    }
    // [{"Id":"FROZEN_AURA","CastMode":"RIGHT_CLICK","Modifiers":{"duration":8.0,"cooldown":12.0,"amplifier":2.0,"radius":10.0}}]

    public String getItemId() {
        if (item==null || nbtItem==null || json==null)
            throw new TheresNoItemIdiot();
        return json.get(0).getAsJsonObject().get("id").getAsString();
    }
    public String getItemCastMod() {
        if (item==null || nbtItem==null || json==null)
            throw new TheresNoItemIdiot();
        return json.get(0).getAsJsonObject().get("CastMode").getAsString();
    }
    public String getItemCooldown() {
        if (item==null || nbtItem==null || json==null)
            throw new TheresNoItemIdiot();
        return json.get(0).getAsJsonObject().get("Modifiers").getAsJsonObject().get("cooldown").getAsString();
    }
    public MMOItemsAddon setAbilities(String castMode, String ability) {
        try {
            nbtItem.removeTag("MMOITEMS_ABILITY");
            JsonArray object= new JsonArray();
            object.getAsJsonObject().addProperty("id", ability);
            object.getAsJsonObject().addProperty("CastMode", castMode);
            ItemTag tag=new ItemTag("MMOITEMS_ABILITY", object.getAsString());
            nbtItem.addTag(tag);
        } catch (Exception e) {
            plugin.getLogger().info(e.getMessage());
        }
        return this;
    }
    public MMOItemsAddon setAbilities(String castMode, String ability, int cooldown) {
        return this;
    }
    public MMOItemsAddon setAbilities(String castMode, String ability, int cooldown , int duration) {
        return this;
    }
    public MMOItemsAddon setAbilities(String castMode, String ability, int cooldown , int duration, int damage) {
        return this;
    }

    public void apply() {
        player.getInventory().setItemInMainHand(nbtItem.toItem());
    }
}
