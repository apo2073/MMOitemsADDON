package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddAbCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        ItemStack item=player.getInventory().getItemInMainHand();
        MMoAddon addon=new MMoAddon(item);
        HashMap<String, Object> modifiers = new HashMap<>();
        modifiers.put("damage", "10.0");
        addon.addAbilities("FROZEN_AURA", "SHIFT_LEFT_CLICK", modifiers);
        //addon.removeAbilities("FROZEN_AURA");
        player.getInventory().setItemInMainHand(addon.getItem());
        return  true;
    }
}
