package kr.apo2073.mmoitemsADDON.cmd;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import kr.apo2073.mmoitemsADDON.util.SkillBook;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class getSkillBookFromItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if (!player.hasPermission("mmoitems.skillbookO")) return true;
        ItemStack item=player.getInventory().getItemInMainHand();
        if (item.getType()== Material.AIR) return true;
        MMoAddon addon=new MMoAddon(item);
        SkillBook book=new SkillBook();
        JsonArray array= addon.getAbilitiesJson();
        for (JsonElement element: array) {
            JsonObject object=element.getAsJsonObject();
            addon.removeAbilities(object.get("Id").getAsString());
            JsonArray array1 = new JsonArray();
            array1.add(object);
            player.getInventory().addItem(book.getSkillBook(player, array1));
            player.getInventory().setItemInMainHand(addon.getItem());
        }
        return true;
    }
}
