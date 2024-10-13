package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.lib.Plugins.msgPerfix;
import kr.apo2073.mmoitemsADDON.util.SkillBook;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class getSkillBookCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        // /스킬북화 스킬이름 데미지 CastMode

        return true;
    }
}
