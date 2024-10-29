package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.mmoitemsADDON.util.SkillBookGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class getSkillBookCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if (!player.hasPermission("mmoitems.skillbook")) return true;
        if (strings.length<2) return true; // 명령어 사용법

        String argsString = String.join(" ", strings);

        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(argsString);
        String skill = matcher.find() ? matcher.group(1).replace("", "") : null;

        String castMode=strings[strings.length-1];
        SkillBookGUI gui=new SkillBookGUI();
        ((Player) commandSender).openInventory(gui.getInv(skill, castMode));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return switch (strings.length) {
            case 1-> List.of("\"(skill name)\"");
            case 2-> List.of("SHIFT_LEFT_CLICK", "SHIFT_RIGHT_CLICK", "RIGHT_CLICK", "LEFT_CLICK");
            default -> List.of("");
        };
    }
}
