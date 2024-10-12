package kr.apo2073.mmoitemsADDON.cmd;

import io.lumine.mythic.lib.math3.analysis.function.Add;
import kr.apo2073.mmoitemsADDON.util.MMOItemsAddon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AddAbCmd implements CommandExecutor {
    public AddAbCmd(JavaPlugin plugin) {
        plugin.getCommand("getSkill").setExecutor(this::onCommand);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        new MMOItemsAddon(((Player) commandSender).getInventory().getItemInMainHand())
                .setPlayer((Player) commandSender).setAbilities("RIGHT_CLICK", "FROZEN_AURA").apply();
        return  true;

    }
}
