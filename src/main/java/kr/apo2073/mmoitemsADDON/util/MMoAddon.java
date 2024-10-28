package kr.apo2073.mmoitemsADDON.util;

import com.google.gson.*;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.lib.MythicLib;
import io.lumine.mythic.lib.api.item.ItemTag;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.skill.SkillMetadata;
import io.lumine.mythic.lib.skill.handler.MythicMobsSkillHandler;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.result.SkillResult;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.exception.TheresNoItemIdiot;
import kr.apo2073.mmoitemsADDON.exception.WhereIsABILITIES;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmocore.api.MMOCoreAPI;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.skill.RegisteredSkill;
import net.Indyuce.mmoitems.stat.data.AbilityData;
import net.Indyuce.mmoitems.stat.data.AbilityListData;
import net.Indyuce.mmoitems.stat.data.type.StatData;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MMoAddon {
    private MMoItemsADDON mma=MMoItemsADDON.plugin;
    private Player player;
    private JsonArray abilitiesJson;
    private ItemStack item;
    private ItemMeta meta;
    private NBTItem nbtItem;
    private LiveMMOItem liveMMOItem;

    public MMoAddon(Player player) { this.player=player; }

    public MMoAddon(ItemStack items) {
        this.item = items;
        this.meta = items.getItemMeta();
        this.nbtItem = NBTItem.get(items);
        String ab = nbtItem.getString("MMOITEMS_ABILITY");
        this.abilitiesJson = new Gson().fromJson(ab, JsonArray.class);
        if (nbtItem.getType()==null) return;
        this.liveMMOItem = new LiveMMOItem(nbtItem);
    }

    @Deprecated
    public void setPlayer(Player player) { this.player = player;}
    @Deprecated
    public Player getPlayer() { return player; }

    public ItemStack getItem() { return item; }
    public void setItem(ItemStack item) {
        this.item=item;
        this.meta = item.getItemMeta();
        this.nbtItem=NBTItem.get(item);
        this.abilitiesJson = JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
        if (nbtItem.getType()==null) return;
        liveMMOItem=new LiveMMOItem(item);
    }

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
        return String.join(", ", nbtItem.getTags());
    }
    public String getTagsValue(String tags) { return nbtItem.getString(tags); }

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
            return !idElement.isJsonNull() ? idElement.getAsString() : "There is No ID";
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

    @SafeVarargs
    public final JsonArray getAbilityToJSon(String skill, String castMode, HashMap<String, Object>... value) {
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
            }).orElse(new HashMap<>());
    }

    @SafeVarargs
    public final void addAbilities(String skill, String castMode, HashMap<String, Object>... value) {
        try {
            if (liveMMOItem == null) return;
            AbilityListData abilityData = liveMMOItem.hasData(ItemStats.ABILITIES)
                    ? (AbilityListData) liveMMOItem.getData(ItemStats.ABILITIES)
                    : new AbilityListData();

            AbilityData data;
            JsonObject modifiers = new JsonObject();
            Arrays.stream(value)
                    .flatMap(map -> map.entrySet().stream())
                    .filter(entry -> entry != null && entry.getKey() != null && entry.getValue() != null)
                    .forEach(entry -> modifiers.addProperty(entry.getKey(), (Double) entry.getValue()));
            if (MMOItems.plugin.getSkills().getSkill(skill)!=null) {
                JsonObject skillJson = new JsonObject();
                skillJson.addProperty("Id", skill);
                skillJson.addProperty("CastMode", castMode);
                skillJson.add("Modifiers", modifiers);
                data = new AbilityData(
                        MMOItems.plugin.getSkills().getSkill(skill),
                        TriggerType.valueOf(castMode)
                );
                modifiers.entrySet().forEach(entry-> {
                    data.setModifier(entry.getKey(), entry.getValue().getAsDouble());
                });
            } else {
                MMOCore mmoCore= MMOCore.plugin;
                MythicBukkit mythicBukkit=MythicBukkit.inst();
                SkillManager manager=mythicBukkit.getSkillManager();
                Optional<Skill> skills=manager.getSkill(skill);
                if (skills.isEmpty()) return;
                data=new AbilityData(
                        new RegisteredSkill(mmoCore.skillManager.getSkill(skill).getHandler(),
                                mmoCore.skillManager.getSkill(skill).getName()),
                        TriggerType.valueOf(castMode)
                );
                modifiers.entrySet().forEach(entry-> {
                    data.setModifier(entry.getKey(), entry.getValue().getAsDouble());
                });
            }


            abilityData.add(data);
            liveMMOItem.setData(ItemStats.ABILITIES, abilityData);
            this.item = liveMMOItem.newBuilder().build();
            this.nbtItem = NBTItem.get(this.item);
            this.liveMMOItem = new LiveMMOItem(nbtItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAbilities(String skillID) {
        try {
            if (liveMMOItem==null) return;
            if (MMOItems.plugin.getSkills().getSkill(skillID)!=null) {
                if (liveMMOItem.getData(ItemStats.ABILITIES).isEmpty()) return;
                AbilityListData abilityData = liveMMOItem.hasData(ItemStats.ABILITIES)
                        ? (AbilityListData) liveMMOItem.getData(ItemStats.ABILITIES)
                        : new AbilityListData();
                boolean IsRemoved = abilityData.getAbilities().removeIf(abilityData1 -> {
                    return abilityData1.getHandler().getId().equals(skillID);
                });
                if (!IsRemoved) return;
                liveMMOItem.replaceData(ItemStats.ABILITIES, abilityData);
                this.item = liveMMOItem.newBuilder().build();
                this.nbtItem = NBTItem.get(this.item);
                this.liveMMOItem = new LiveMMOItem(nbtItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}