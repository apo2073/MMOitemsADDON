package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.mmoitemsADDON.MMoItemsADDON;
import kr.apo2073.mmoitemsADDON.util.Addon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static kr.apo2073.mmoitemsADDON.MMoItemsADDON.plugin;

public class AddAbCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return false;

        return  true;

    }
}
