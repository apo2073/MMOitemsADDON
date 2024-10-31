package kr.apo2073.mmoAddon.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.gson.JsonArray;
import kr.apo2073.mmoAddon.util.MMoAddon;
import kr.apo2073.mmoAddon.util.SkillBook;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
* @usage : give a skill book of "FROZEN_AURA" on "RIGHT_CLICK" with modifier "cooldown: 1.0" to player
*/
public class SkriptGetBookWith extends SimpleExpression<ItemStack> {
    private Expression<String> skillExpr;
    private Expression<String> castExpr;
    private Expression<String> modifiersExpr;

    static {
        Skript.registerExpression(SkriptGetBookWith.class,
                ItemStack.class,
                ExpressionType.SIMPLE,
                "[(a|the)] [skill] book of %string% (on|using) [castmode] %string% [with [modifier] %-string%]"
        );
    }

    @Override
    protected @Nullable ItemStack[] get(Event event) {
        String skill = skillExpr.getSingle(event);
        String cast = castExpr.getSingle(event);
        JsonArray json;
        MMoAddon addon = new MMoAddon((Player) null);
        SkillBook book = new SkillBook();

        if (skill == null || cast == null) return null;

        if (modifiersExpr!=null){
            String modifiersStr = modifiersExpr.getSingle(event);
            HashMap<String, Object> hash = new HashMap<>();
            modifiersStr = modifiersStr.replaceAll("[{}]", "");
            String[] pairs = modifiersStr.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    try {
                        hash.put(keyValue[0].trim(), Double.parseDouble(keyValue[1].trim()));
                    } catch (NumberFormatException ignored) {
                        hash.put(keyValue[0].trim(), keyValue[1].trim());
                    }
                }
            }
            json=addon.getAbilityToJSon(skill, cast, hash);
        } else {
            json = addon.getAbilityToJSon(skill, cast);
        }


        ItemStack item = book.getSkillBook(null, json);

        if (item == null) return null;
        return new ItemStack[]{item};
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        skillExpr = (Expression<String>) expressions[0];
        castExpr = (Expression<String>) expressions[1];
        modifiersExpr = expressions.length > 2 ? (Expression<String>) expressions[2] : null;
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
    public String toString(@Nullable Event event, boolean b) {
        return "skill book with modifiers";
    }
}