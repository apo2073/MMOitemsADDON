package kr.apo2073.mmoAddon.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.gson.JsonArray;
import kr.apo2073.mmoAddon.util.MMoAddon;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
* @usage give add skill "GRAND_HEAL" on castmode "SHIFT_LEFT_CLICK" from player's tool to player
*/
public class SkriptAddABILITY extends SimpleExpression<ItemStack> {
    private Expression<String> skillExpr;
    private Expression<String> castModeExpr;
    private Expression<String> modifiersExpr;
    private Expression<ItemStack> itemExpr;

    static {
        Skript.registerExpression(SkriptAddABILITY.class, ItemStack.class, ExpressionType.SIMPLE,
                "add (ability|skill) %string% (on|using) [castmode] %string% [with [modifier] %-string%] (on|from) %itemstack%");
    }

    @Nullable
    @Override
    protected ItemStack[] get(Event event) {
        String skill = skillExpr.getSingle(event);
        String castMode = castModeExpr.getSingle(event);
        ItemStack item = itemExpr.getSingle(event);
        JsonArray abilityJson;

        if (skill == null || castMode == null || item == null) return null;
        MMoAddon addon = new MMoAddon(item);

        if (modifiersExpr!=null) {
            String modifiersStr = modifiersExpr.getSingle(event);
            HashMap<String, Object> modifiers = new HashMap<>();
            modifiersStr = modifiersStr.replaceAll("[{}]", "");
            String[] pairs = modifiersStr.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    try {
                        modifiers.put(keyValue[0].trim(), Double.parseDouble(keyValue[1].trim()));
                    } catch (NumberFormatException ignored) {
                        modifiers.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }
            abilityJson = addon.getAbilityToJSon(skill, castMode, modifiers);
            addon.addAbilities(skill, castMode, modifiers);
        } else {
            abilityJson = addon.getAbilityToJSon(skill, castMode);
            addon.addAbilities(skill, castMode);
        }
        addon.setAbilitiesJson(abilityJson);

        return new ItemStack[]{addon.getItem()};
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        skillExpr = (Expression<String>) expressions[0];
        castModeExpr = (Expression<String>) expressions[1];
        modifiersExpr = expressions.length>3 ? (Expression<String>) expressions[2] : null;
        itemExpr = (Expression<ItemStack>) expressions[3];
        return true;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "add ability " + skillExpr.toString(event, debug) + " to " + itemExpr.toString(event, debug);
    }
}