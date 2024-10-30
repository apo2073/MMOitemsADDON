package kr.apo2073.mmoAddon.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import kr.apo2073.mmoAddon.util.MMoAddon;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
* @usage give remove ability "FIREBOLT" on player's tool to player
*/
public class SkriptRemoveABILITY extends SimpleExpression<ItemStack> {
    private Expression<String> skillExpr;
    private Expression<ItemStack> itemExpr;
    static {
        Skript.registerExpression(SkriptRemoveABILITY.class, ItemStack.class, ExpressionType.SIMPLE,
                "remove (ability|skill) %string% (on|from) %itemstack%");
    }
    @Nullable
    @Override
    protected ItemStack[] get(Event event) {
        String skill = skillExpr.getSingle(event);
        ItemStack item = itemExpr.getSingle(event);

        if (skill == null|| item == null) return null;
        MMoAddon addon=new MMoAddon(item);

        addon.removeAbilities(skill);

        return new ItemStack[]{addon.getItem()};
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

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        skillExpr = (Expression<String>) expressions[0];
        itemExpr = (Expression<ItemStack>) expressions[1];
        return true;
    }
}
