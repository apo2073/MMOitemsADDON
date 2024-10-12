package kr.apo2073.mmoitemsADDON;

import kr.apo2073.mmoitemsADDON.cmd.AddAbCmd;
import org.bukkit.plugin.java.JavaPlugin;

public class MMoItemsADDON extends JavaPlugin {
    public static MMoItemsADDON plugin;
    @Override
    public void onEnable() {
        plugin=this;
        getLogger().info("""
               \s
                ___  ______  ________ _ _                    \s
                |  \\/  ||  \\/  |  _  (_) |                   \s
                | .  . || .  . | | | |_| |_ ___ _ __ ___  ___\s
                | |\\/| || |\\/| | | | | | __/ _ \\ '_ ` _ \\/ __|
                | |  | || |  | \\ \\_/ / | ||  __/ | | | | \\__ \\
                \\_|  |_/\\_|  |_/\\___/|_|\\__\\___|_| |_| |_|___/
               \s""");
        new AddAbCmd(this);
    }

    @Override
    public void onDisable() {

    }
}
