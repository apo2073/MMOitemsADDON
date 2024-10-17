package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import kr.apo2073.mmoitemsADDON.util.SkillBook;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class getSkillBookCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        // /스킬북화 스킬이름 데미지 CastMode
        if (!player.hasPermission("mmoitems.skillbook")) return true;
        if (strings.length!=3) return true; // 명령어 사용법
        String skill=strings[0];
        int damage= Integer.parseInt(strings[1]);
        String castMode=strings[2]
                .replace("S", "SHIFT_")
                .replace("L", "LEFT_CLICK")
                .replace("R", "RIGHT_CLICK");
        MMoAddon MMoAddon =new MMoAddon(player);
        SkillBook skillBook=new SkillBook();
        ItemStack item=skillBook.getNBTSkillBook(MMoAddon.getAbilityToJSon(skill, damage, castMode), player);
        player.getInventory().addItem(item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return switch (strings.length) {
            case 1-> List.of("skill");
            case 2-> List.of("damage");
            case 3-> List.of("SL", "SR", "R", "L");
            default -> List.of("");
        };
    }
}
