package kr.apo2073.mmoAddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import kr.apo2073.lib.Plugins.Register;
import kr.apo2073.mmoAddon.cmd.getSkillBookCmd;
import kr.apo2073.mmoAddon.cmd.getSkillBookFromItem;
import kr.apo2073.mmoAddon.event.AnvilGUI;
import kr.apo2073.mmoAddon.event.SkillBookEQUIPevent;
import kr.apo2073.mmoAddon.util.PapiRg;
import kr.apo2073.mmoAddon.util.SkillBookGUI;
import kr.apo2073.mmoAddon.util.skript.SkriptAddABILITY;
import kr.apo2073.mmoAddon.util.skript.SkriptGetBookWith;
import kr.apo2073.mmoAddon.util.skript.SkriptGetID;
import kr.apo2073.mmoAddon.util.skript.SkriptRemoveABILITY;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MMOAddons extends JavaPlugin {
    public static MMOAddons plugin;
    private SkriptAddon addons;
    @Override
    public void onEnable() {
        plugin=this;

        getLogger().info("  __  __ __  __  ____ _____ _");
        getLogger().info(" |  \\/  |  \\/  |/ __ \\_   _| |");
        getLogger().info(" | \\  / | \\  / | |  | || | | |_ ___ _ __ ___  ___");
        getLogger().info(" | |\\/| | |\\/| | |  | || | | __/ _ \\ '_ ` _ \\/ __|");
        getLogger().info(" | |  | | |  | | |__| || |_| ||  __/ | | | | \\__ \\");
        getLogger().info(" |_|  |_|_|  |_|\\____/_____|\\__\\___|_| |_| |_|___/   With.아포칼립스");
        saveDefaultConfig();

        new Register(this)
                .resistEventListener(new SkillBookEQUIPevent())
                .resistEventListener(new SkillBookGUI())
                .resistEventListener(new AnvilGUI())
                .resistTabExecutor("스킬북", new getSkillBookCmd())
                .resistCommandExecutor("스킬북추출", new getSkillBookFromItem());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new PapiRg().register();
        if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
            addons = Skript.registerAddon(this);
            new SkriptGetBookWith();
            new SkriptGetID();
            new SkriptAddABILITY();
            new SkriptRemoveABILITY();
        }
    }

    @Override
    public void onDisable() {
        getLogger().warning("MMOAddons disabled");
    }
}
