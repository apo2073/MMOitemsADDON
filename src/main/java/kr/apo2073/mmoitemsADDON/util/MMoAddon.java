package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.*;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.mmoitemsADDON.exception.TheresNoItemIdiot;
import kr.apo2073.mmoitemsADDON.exception.WhereIsABILITIES;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static kr.apo2073.mmoitemsADDON.MMoItemsADDON.plugin;

public class MMoAddon {
    private Player player;
    private JsonArray abilitiesJson;
    private ItemStack item;
    private NBTItem nbtItem;

    public MMoAddon(Player player) { this.player=player; }

    public MMoAddon(ItemStack items) {
        this.item=items;
        this.nbtItem=NBTItem.get(items);
        String ab=nbtItem.getString("MMOITEMS_ABILITY");
        this.abilitiesJson = new Gson().fromJson(ab, JsonArray.class);
        /*nbtItem.getTags().forEach(tags-> {
            plugin.getLogger().info(tags+ " : "+nbtItem.getString(tags));
        });*/
    }

    public void setPlayer(Player player) { this.player = player; this.item=player.getInventory().getItemInMainHand();}
    public Player getPlayer() { return player; }

    public void setItem(ItemStack item) {
        this.item=item;
        this.nbtItem=NBTItem.get(item);
        this.abilitiesJson = JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
    }
    public void setAbilitiesJson(JsonArray abilitiesJson) { this.abilitiesJson = abilitiesJson;}

    public NBTItem getNbtItem() {return nbtItem;}

    public void setNbtItem(NBTItem nbtItem) {this.nbtItem = nbtItem;}

    public ItemStack getItem() {return item;}

    public String getAbilitiesJsonToString() {
        return abilitiesJson.getAsString();
    }
    public JsonArray getAbilitiesJson() { return abilitiesJson; }

    public String getTags() {
        if (item==null || nbtItem==null)
            throw new TheresNoItemIdiot();
        return nbtItem.getTags().toString();
    }
    public String getTagsValue(String tags) { return nbtItem.getString(tags); }
    // [{"Id":"FROZEN_AURA","CastMode":"RIGHT_CLICK","Modifiers":{"duration":8.0,"cooldown":12.0,"amplifier":2.0,"radius":10.0}}]

    public String getItemId() {
        if (item==null || nbtItem==null)
            throw new TheresNoItemIdiot();
        return nbtItem.getString("MMOITEMS_ITEM_ID");
    }
    public String getItemName() {
        if (item==null || nbtItem==null)
            throw new TheresNoItemIdiot();
        return nbtItem.getString("MMOITEMS_NAME");
    }
    public String getItemSkillID() {
        try {
            JsonElement idElement = abilitiesJson.get(0).getAsJsonObject().get("Id");
            if (idElement==null) throw new WhereIsABILITIES();
            return (
                    idElement != null
                    && !idElement.isJsonNull()
            ) ? idElement.getAsString() : "There is No ID";
        } catch (WhereIsABILITIES e) {
            return ChatColor.RED+"NONE";
        }
    }

    public String getItemCastMod() {
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < abilitiesJson.size(); i++) {
                JsonElement castModeElement = abilitiesJson.get(i).getAsJsonObject().get("CastMode");
                if (castModeElement == null) throw new WhereIsABILITIES();

                builder.append(castModeElement);
                if (i < abilitiesJson.size() - 1) {
                    builder.append(", ");
                }
            }
            return !builder.isEmpty() ? builder.toString() : "";
        } catch (WhereIsABILITIES e) {
            return "";
        }
    }

    public JsonArray getAbilityToJSon(String skill, int damage, String castMode) {
        return new Gson().fromJson(
                "[{\"Id\":\""+skill+"\",\"CastMode\":\""+castMode+"\",\"Modifiers\":{\"damage\":"+damage+"}}]",
                JsonArray.class
        );
    }

    public Map<String, Object> getModifiers() {
    return Optional.ofNullable(abilitiesJson)
        .filter(array -> !array.isEmpty())
        .map(array -> array.get(0).getAsJsonObject().getAsJsonObject("Modifiers"))
        .map(modifiers -> {
            Map<String, Object> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : modifiers.entrySet()) {
                JsonElement value = entry.getValue();
                if (value.isJsonPrimitive()) {
                    JsonPrimitive primitive = value.getAsJsonPrimitive();
                    if (primitive.isNumber()) result.put(entry.getKey(), primitive.getAsDouble());
                    else if (primitive.isString()) result.put(entry.getKey(), primitive.getAsString());
                    else if (primitive.isBoolean()) result.put(entry.getKey(), primitive.getAsBoolean());
                }
            }
            return result;
        })
        .orElse(new HashMap<>());
    }

    @SafeVarargs
    public final void setAbilities(Map<String, String>... value) {
        try {
            JsonArray object= new JsonArray();
            for (Map<String,String> map: value) {
                map.forEach((s, s2) -> {
                    object.getAsJsonObject().addProperty(s, s2);
                });
            }
            nbtItem.addTag(new ItemTag("MMOITEMS_ABILITY", object.getAsString()));
        } catch (Exception e) {
            plugin.getLogger().info(e.getMessage());
        }
    }
    public void setAbilities(String json) {
        try {
            abilitiesJson.add(json);
            nbtItem.addTag(new ItemTag("MMOITEMS_ABILITY", abilitiesJson));
        } catch (Exception e) {
            plugin.getLogger().info(e.getMessage());
        }
    }
    public void addAbilities(String json) {
        JsonArray array=new Gson().fromJson(abilitiesJson, JsonArray.class);
        array.add(new Gson().fromJson(json, JsonElement.class));
        nbtItem.addTag(new ItemTag("MMOITEMS_ABILITY", array.getAsString()));
    }

    public void apply() {
        var ref = new Object() {
            int a = 0;
        };
        /*player.getInventory().forEach(itemStack -> {
            if (itemStack!=this.item) ref.a +=1;
            else player.getInventory().setItem(ref.a, item);
        });*/
        player.getInventory().setItemInMainHand(item);
    }
}
