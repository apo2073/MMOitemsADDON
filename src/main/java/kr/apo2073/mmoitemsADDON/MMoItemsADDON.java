package kr.apo2073.mmoitemsADDON;

import kr.apo2073.lib.Plugins.Register;
import kr.apo2073.mmoitemsADDON.cmd.AddAbCmd;
import kr.apo2073.mmoitemsADDON.cmd.getSkillBookCmd;
import kr.apo2073.mmoitemsADDON.cmd.getSkillBookFromItem;
import kr.apo2073.mmoitemsADDON.event.SkillBookEQUIPevent;
import kr.apo2073.mmoitemsADDON.util.PapiRg;
import kr.apo2073.mmoitemsADDON.util.SkillBookGUI;
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
        new Register(this)
                .resistEventListener(new SkillBookEQUIPevent())
                .resistEventListener(new SkillBookGUI())
                .resistCommandExecutor("getSkill", new AddAbCmd())
                .resistTabExecutor("스킬북", new getSkillBookCmd())
                .resistCommandExecutor("스킬북추출", new getSkillBookFromItem());
        new PapiRg().register();
    }

    @Override
    public void onDisable() {

    }
}
