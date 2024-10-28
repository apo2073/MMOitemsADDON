package kr.apo2073.mmoitemsADDON.util.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import jdk.jfr.Experimental;
import kr.apo2073.mmoitemsADDON.util.SkillBook;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Experimental
public class SkriptGetBook extends SimpleExpression<ItemStack> {
    static {
        Skript.registerExpression(SkriptGetBook.class, ItemStack.class, ExpressionType.SIMPLE, "[(a/the)] [skill] book of %item%");
    }
    private Expression<ItemStack> itemExpr;
    @Override
    protected @Nullable ItemStack[] get(Event event) {
        ItemStack item = itemExpr.getSingle(event);
        SkillBook book=new SkillBook();
        if (item==null) return null;
        return new ItemStack[]{book.getSkillBook(item)};
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
        return "skill book of "+itemExpr.toString(event, b);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        itemExpr = (Expression<ItemStack>) expressions[0];
        return true;
    }
}
