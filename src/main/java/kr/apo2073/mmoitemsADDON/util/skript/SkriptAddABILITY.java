package kr.apo2073.mmoitemsADDON.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.gson.JsonArray;
import jdk.jfr.Experimental;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;


/**
 * @usage:
    set {_modifiedItem} to add ability "Fireball" castmode "RIGHT_CLICK" with modifier {damage: 10, range: 5} to player's held item
*/
@Experimental
public class SkriptAddABILITY extends SimpleExpression<ItemStack> {
    private Expression<String> skillExpr;
    private Expression<String> castModeExpr;
    private Expression<HashMap<String, Object>> modifiersExpr;
    private Expression<ItemStack> itemExpr;
    static {
        Skript.registerExpression(SkriptAddABILITY.class, ItemStack.class, ExpressionType.SIMPLE,
                "add (ability/skill) %string% with castmode %string% with modifier %hashmap% to %itemstack%");
    }
    @Nullable
    @Override
    protected ItemStack[] get(Event event) {
        String skill = skillExpr.getSingle(event);
        String castMode = castModeExpr.getSingle(event);
        HashMap<String, Object> modifiers = modifiersExpr.getSingle(event);
        ItemStack item = itemExpr.getSingle(event);

        if (skill == null || castMode == null || item == null || modifiers == null) return null;
        MMoAddon addon=new MMoAddon(item);

        JsonArray abilityJson = addon.getAbilityToJSon(skill, castMode, modifiers);
        addon.setAbilitiesJson(abilityJson);
        addon.addAbilities(skill, castMode, modifiers);


        return new ItemStack[]{addon.getItem()};
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "add ability " + skillExpr.toString(event, debug) + " to " + itemExpr.toString(event, debug);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        skillExpr = (Expression<String>) expressions[0];
        castModeExpr = (Expression<String>) expressions[1];
        modifiersExpr = (Expression<HashMap<String, Object>>) expressions[2];
        itemExpr = (Expression<ItemStack>) expressions[3];
        return true;
    }
}