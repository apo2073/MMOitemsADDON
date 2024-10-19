package kr.apo2073.mmoitemsADDON.cmd;

import kr.apo2073.mmoitemsADDON.util.MMoAddon;
import kr.apo2073.mmoitemsADDON.util.SkillBook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class getSkillBookCmd implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) return false;
        if (!player.hasPermission("mmoitems.skillbook")) return true;
        if (strings.length!=3) return true; // 명령어 사용법

        String skill=strings[0];
        //double damage= Double.parseDouble(strings[2]);
        String castMode=strings[1];

        MMoAddon addon =new MMoAddon(player);
        SkillBook skillBook=new SkillBook();
        HashMap<String, Object> modifiers = new HashMap<>();
        //modifiers.put("damage", damage);
        modifiers.put("cooldown", strings[2]);
        ItemStack item;

        try {
            item = skillBook.getSkillBook(player, addon.getAbilityToJSon(skill, castMode, modifiers));
        } catch (Exception e) {
            commandSender.sendMessage(e.getMessage());
            e.printStackTrace();
            return false;
        }

        player.getInventory().addItem(item);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return switch (strings.length) {
            case 1-> List.of("skill");
            //case 3-> List.of("damage");
            case 2-> List.of("SHIFT_LEFT_CLICK", "SHIFT_RIGHT_CLICK", "RIGHT_CLICK", "LEFT_CLICK");
            case 3-> List.of("cooldown");
            default -> List.of("");
        };
    }
}
