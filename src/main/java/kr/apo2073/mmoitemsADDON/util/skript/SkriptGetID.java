package kr.apo2073.mmoitemsADDON.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import jdk.jfr.Experimental;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Experimental
public class SkriptGetID extends SimpleExpression<String> {
    static {
        Skript.registerExpression(SkriptGetID.class, String.class, ExpressionType.COMBINED, "[(a/the)] id of %item%");
    }
    private Expression<ItemStack> itemExpr;
    @Override
    protected @Nullable String[] get(Event event) {
        ItemStack item=itemExpr.getSingle(event);
        if (item==null) return null;
        MMoAddon addon=new MMoAddon(item);
        return new String[]{addon.getItemId()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean b) {
        return "MMOItems ID of"+itemExpr.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        itemExpr=(Expression<ItemStack>) exprs[0];
        return true;
    }
}
