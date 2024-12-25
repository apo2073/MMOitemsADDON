package kr.apo2073.mmoAddon.util;

import com.google.gson.*;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.api.skills.SkillManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import kr.apo2073.mmoAddon.MMOAddons;
import kr.apo2073.mmoAddon.exception.TheresNoItemInMMOADDON;
import kr.apo2073.mmoAddon.exception.WhereIsABILITIES;
import net.Indyuce.mmocore.MMOCore;
import net.Indyuce.mmoitems.ItemStats;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.item.mmoitem.LiveMMOItem;
import net.Indyuce.mmoitems.skill.RegisteredSkill;
import net.Indyuce.mmoitems.stat.data.AbilityData;
import net.Indyuce.mmoitems.stat.data.AbilityListData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MMoAddon {
    private MMOAddons mma= MMOAddons.plugin;
    private Player player;
    private JsonArray abilitiesJson;
    private ItemStack item;
    private ItemMeta meta;
    private NBTItem nbtItem;
    private LiveMMOItem liveMMOItem;

    public MMoAddon(Player player) {
        this.player=player;
        mma.debug("MMoAddon initialized with player: " + player.getName());
    }

    public MMoAddon(ItemStack items) {
        mma.debug("MMoAddon initialized with item: "+items.getItemMeta().getDisplayName());
        this.item = items;
        this.meta = items.getItemMeta();
        this.nbtItem = NBTItem.get(items);
        String ab = nbtItem.getString("MMOITEMS_ABILITY");
        this.abilitiesJson = new Gson().fromJson(ab, JsonArray.class);
        mma.debug("abilitiesJson, item, nbtItem set " + ab);
        if (nbtItem.getType()==null) return;
        this.liveMMOItem = new LiveMMOItem(nbtItem);
        mma.debug("liveMMOItem set : "+ this.liveMMOItem.getId());
    }

    @Deprecated
    public void setPlayer(Player player) {
        this.player = player;
        mma.debug("Player set to: " + player.getName());
    }
    @Deprecated
    public Player getPlayer() { return player; }

    public ItemStack getItem() { return item; }

    public void setItem(ItemStack item) {
        mma.debug("Item setting: " + item.toString());
        this.item = item;
        this.meta = item.getItemMeta();
        this.nbtItem = NBTItem.get(item);
        this.abilitiesJson = JsonParser.parseString(this.nbtItem.getString("MMOITEMS_ABILITY")).getAsJsonArray();
        if (nbtItem.getType() == null) return;
        liveMMOItem = new LiveMMOItem(item);
        mma.debug("Item and liveMMOItem initialized with: " + liveMMOItem.getId());
    }

    public void setAbilitiesJson(JsonArray abilitiesJson) {
        this.abilitiesJson = abilitiesJson;
        mma.debug("Abilities JSON set: " + abilitiesJson.toString());
    }

    public NBTItem getNbtItem() { return nbtItem; }

    public void setNbtItem(NBTItem nbtItem) {
        this.nbtItem = nbtItem;
        mma.debug("NBTItem set: " + nbtItem.toString());
    }

    public LiveMMOItem getLiveMMOItem() { return liveMMOItem; }

    public void setLiveMMOItem(LiveMMOItem liveMMOItem) {
        this.liveMMOItem = liveMMOItem;
        mma.debug("LiveMMOItem set: " + liveMMOItem.getId());
    }

    public String getAbilitiesJsonToString() {
        mma.debug("Getting abilities JSON as string.");
        return abilitiesJson.getAsString();
    }

    public JsonArray getAbilitiesJson() {
        mma.debug("Getting abilities JSON.");
        return abilitiesJson;
    }

    public String getTags() {
        mma.debug("Fetching tags from NBTItem.");
        if (item == null || nbtItem == null)
            throw new TheresNoItemInMMOADDON();
        return String.join(", ", nbtItem.getTags());
    }

    public String getTagsValue(String tags) {
        mma.debug("Fetching tag value for tag: " + tags);
        return nbtItem.getString(tags);
    }

    public String getItemId() {
        mma.debug("Getting item ID.");
        if (item == null || nbtItem == null)
            throw new TheresNoItemInMMOADDON();
        return nbtItem.getString("MMOITEMS_ITEM_ID");
    }

    public String getItemName() {
        mma.debug("Getting item name.");
        if (item == null || nbtItem == null)
            throw new TheresNoItemInMMOADDON();
        return nbtItem.getString("MMOITEMS_NAME");
    }

    public String getItemSkillID() {
        try {
            mma.debug("Getting item skill ID.");
            JsonElement idElement = abilitiesJson.get(0).getAsJsonObject().get("Id");
            if (idElement == null) throw new WhereIsABILITIES();
            return !idElement.isJsonNull() ? idElement.getAsString() : "There is No ID";
        } catch (WhereIsABILITIES e) {
            return ChatColor.RED + "NONE";
        }
    }

    public String getItemCastMode() {
        mma.debug("Getting item cast mode.");
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
        mma.debug("Creating ability JSON for skill: " + skill + ", castMode: " + castMode);
        JsonObject json = new JsonObject();
        JsonObject modifiers = new JsonObject();
        for (HashMap<String, Object> map : value) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                modifiers.addProperty(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
            }
        }
        json.add("Modifiers", modifiers);
        json.addProperty("Id", skill);
        json.addProperty("CastMode", castMode);

        JsonArray array = new JsonArray();
        array.add(json);
        return array;
    }

    public Map<String, Object> getModifiers() {
        mma.debug("Fetching modifiers.");
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
        mma.debug("Adding abilities: skill=" + skill + ", castMode=" + castMode);
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
                    .forEach(entry -> modifiers.addProperty(entry.getKey(), /*(Double) */entry.getValue().toString()));
            if (MMOItems.plugin.getSkills().getSkill(skill)!=null) {
                mma.debug("Adding ability to MMOItems.");
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
                mma.debug("Adding ability to MythicBukkit.");
                MMOCore mmoCore = MMOCore.plugin;
                if (mmoCore == null) return;
                MythicBukkit mythicBukkit = MythicBukkit.inst();
                if (mythicBukkit == null) return;
                SkillManager manager = mythicBukkit.getSkillManager();
                Optional<Skill> skills = manager.getSkill(skill);
                if (skills.isEmpty()) return;
                data = new AbilityData(
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
        mma.debug("Removing ability with skillID: " + skillID);
        try {
            if (liveMMOItem == null) return;
            if (MMOItems.plugin.getSkills().getSkill(skillID) != null) {
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
