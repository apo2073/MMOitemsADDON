package kr.apo2073.mmoAddon.cmd;

import kr.apo2073.lib.Plugins.CompKt;
import kr.apo2073.lib.Plugins.msgPerfix;
import kr.apo2073.mmoAddon.MMOAddons;
import kr.apo2073.mmoAddon.util.SkillBookGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class getSkillBookCmd implements TabExecutor {
    private MMOAddons mma=MMOAddons.plugin;
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        mma.debug("execute skill book command");
        if (!(commandSender instanceof Player player))  return false;
        if (!player.hasPermission("mmoaddon.skillbook")) return true;
        if (strings.length<2) {
            player.sendMessage(CompKt.txt(msgPerfix.ERROR.getPerfix()+" 명령어를 올바르게 사용하세요"));
            return true;
        }
        String skill = String.join(" ", Arrays.copyOfRange(strings, 1, strings.length));

        String castMode=strings[0];
        SkillBookGUI gui=new SkillBookGUI();
        ((Player) commandSender).openInventory(gui.getInv(skill, castMode));
        mma.debug("command execute successful");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return switch (strings.length) {
            case 2,3-> List.of("skill name");
            case 1-> List.of("SHIFT_LEFT_CLICK", "SHIFT_RIGHT_CLICK", "RIGHT_CLICK", "LEFT_CLICK");
            default -> List.of("");
        };
    }
}
