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
    public boolean isDebug= getConfig().getBoolean("debug");

    @Override
    public void onEnable() {
        plugin=this;
        debug("plugin enabled");

        this.getLogger().info("  __  __ __  __  ____ _____ _");
        this.getLogger().info(" |  \\/  |  \\/  |/ __ \\_   _| |");
        this.getLogger().info(" | \\  / | \\  / | |  | || | | |_ ___ _ __ ___  ___");
        this.getLogger().info(" | |\\/| | |\\/| | |  | || | | __/ _ \\ '_ ` _ \\/ __|");
        this.getLogger().info(" | |  | | |  | | |__| || |_| ||  __/ | | | | \\__ \\");
        this.getLogger().info(" |_|  |_|_|  |_|\\____/_____|\\__\\___|_| |_| |_|___/   With.아포칼립스");
        debug("config loaded : "+getConfig().getCurrentPath());
        saveDefaultConfig();
        debug(this.getName()+" - "+Bukkit.getServer().getVersion());

        new Register(this)
                .resistEventListener(new SkillBookEQUIPevent())
                .resistEventListener(new SkillBookGUI())
                .resistEventListener(new AnvilGUI())
                .resistTabExecutor("스킬북", new getSkillBookCmd())
                .resistCommandExecutor("스킬북추출", new getSkillBookFromItem());

        debug("Commands, Listeners Enabled");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
            new PapiRg().register(); debug("Hooked PAPI : "+ new PapiRg().getIdentifier());
        if (Bukkit.getPluginManager().getPlugin("Skript") != null) {
            addons = Skript.registerAddon(this);
            new SkriptGetBookWith();
            new SkriptGetID();
            new SkriptAddABILITY();
            new SkriptRemoveABILITY();
            debug("Hooked Skript : "+addons.getName());
        }
    }

    public void debug(String log) {
        reloadConfig();this.reloadConfig();
        if (isDebug) Bukkit.getLogger().warning("[ MMOAddon DEBUG ] "+log);
    }

    @Override
    public void onLoad() {
        debug("plugin loaded");
    }

    @Override
    public void onDisable() {
        debug("plugin disabled");
    }
}
