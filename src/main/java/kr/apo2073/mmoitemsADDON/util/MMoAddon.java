package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.*;
import io.lumine.mythic.lib.api.item.NBTItem;
import kr.apo2073.mmoitemsADDON.exception.TheresNoItemIdiot;
import kr.apo2073.mmoitemsADDON.exception.WhereIsABILITIES;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.stat.data.AbilityData;
import net.Indyuce.mmoitems.stat.data.AbilityListData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MMoAddon {
    private Player player;
    private JsonArray abilitiesJson;
    private ItemStack item;
    private NBTItem nbtItem;
    private LiveMMOItem liveMMOItem;

    public MMoAddon(Player player) { this.player=player; }

    public MMoAddon(ItemStack items) {
        this.item = items;
        this.nbtItem = NBTItem.get(items);
        String ab = nbtItem.getString("MMOITEMS_ABILITY");
        this.abilitiesJson = new Gson().fromJson(ab, JsonArray.class);
        if (nbtItem.getType()==null) return;
        this.liveMMOItem = new LiveMMOItem(nbtItem);
    }

    public void setPlayer(Player player) { this.player = player;}
    public Player getPlayer() { return player; }

    public void setItem(ItemStack item) {
        this.item=item;
        this.nbtItem=NBTItem.get(item);
        this.abilitiesJson = JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
        if (nbtItem.getType()==null) return;
        liveMMOItem=new LiveMMOItem(item);
    }
    public ItemStack getItem() {return item;}

    public void setAbilitiesJson(JsonArray abilitiesJson) { this.abilitiesJson = abilitiesJson;}

    public NBTItem getNbtItem() {return nbtItem;}
    public void setNbtItem(NBTItem nbtItem) {this.nbtItem = nbtItem;}

    public LiveMMOItem getLiveMMOItem() { return liveMMOItem; }
    public void setLiveMMOItem(LiveMMOItem liveMMOItem) {this.liveMMOItem = liveMMOItem;}

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

    public String getItemCastMode() {
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

    public JsonArray getAbilityToJSon(String skill, String castMode, HashMap<String,Object>... value) {
        JsonObject json = new JsonObject();
        JsonObject modifiers = new JsonObject();
        for (HashMap<String, Object> map : value) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                modifiers.addProperty(entry.getKey(), entry.getValue().toString());
            }
        }
        json.addProperty("Id", skill);
        json.addProperty("CastMode", castMode);
        json.add("Modifiers", modifiers);

        JsonArray array=new JsonArray();
        array.add(json);
        return array;
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

    public void addAbilities(String json) {
        if (liveMMOItem == null || liveMMOItem.getData(ItemStats.ABILITIES).isEmpty()) return;
        JsonElement element = new Gson().fromJson(json, JsonElement.class);
        AbilityListData abilityData = (AbilityListData) liveMMOItem.getData(ItemStats.ABILITIES);
        if (element.isJsonArray()) {
            for (JsonElement abilityElement : element.getAsJsonArray()) {
                abilityData.add(new AbilityData(abilityElement.getAsJsonObject()));
            }
        } else {
            abilityData.add(new AbilityData(element.getAsJsonObject()));
        }
        liveMMOItem.replaceData(ItemStats.ABILITIES, abilityData);
        this.item = liveMMOItem.newBuilder().build();
        this.nbtItem = NBTItem.get(this.item);
        this.liveMMOItem = new LiveMMOItem(nbtItem);
    }

    public void addAbilities(String skill, String castMode, HashMap<String,Object>... value) {
        try {
            if (liveMMOItem == null) return;
            AbilityListData abilityData;
            if (liveMMOItem.hasData(ItemStats.ABILITIES)) abilityData = ((AbilityListData) liveMMOItem.getData(ItemStats.ABILITIES));
            else abilityData= new AbilityListData();
            JsonObject modifiers = new JsonObject();
            for (HashMap<String, Object> map : value) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry==null) continue;
                    if (entry.getValue()==null) entry.setValue(0.0);
                    modifiers.addProperty(entry.getKey(), entry.getValue().toString());
                }
            }
            JsonObject skillJson = new JsonObject();
            skillJson.addProperty("Id", skill);
            skillJson.addProperty("CastMode", castMode);
            skillJson.add("Modifiers", modifiers);
            abilityData.add(new AbilityData(skillJson));

            liveMMOItem.setData(ItemStats.ABILITIES, abilityData);
            this.item = liveMMOItem.newBuilder().build();
            this.nbtItem = NBTItem.get(this.item);
            this.liveMMOItem = new LiveMMOItem(nbtItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAbilities(String skillID) {
        if (liveMMOItem==null) return;
        if (liveMMOItem.getData(ItemStats.ABILITIES).isEmpty())return;
        AbilityListData abilityData= ((AbilityListData)liveMMOItem.getData(ItemStats.ABILITIES));
        boolean IsRemoved= abilityData.getAbilities().removeIf(abilityData1 -> {
            return abilityData1.getHandler().getId().equals(skillID);
        });
        if (!IsRemoved) return;
        liveMMOItem.replaceData(ItemStats.ABILITIES, abilityData);
        this.item= liveMMOItem.newBuilder().build();
        this.nbtItem=NBTItem.get(this.item);
        this.liveMMOItem=new LiveMMOItem(nbtItem);
    }
}
