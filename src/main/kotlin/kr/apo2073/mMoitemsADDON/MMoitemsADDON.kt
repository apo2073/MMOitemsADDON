package kr.apo2073.mMoitemsADDON

import kr.apo2073.mMoitemsADDON.cmd.getSkilllCMd
import org.bukkit.plugin.java.JavaPlugin

class MMoitemsADDON : JavaPlugin() {
    override fun onEnable() {
        getCommand("getSkill")?.setExecutor(getSkilllCMd(this))
        logger.info("""
        
___  ______  ________ _ _                     
|  \/  ||  \/  |  _  (_) |                    
| .  . || .  . | | | |_| |_ ___ _ __ ___  ___ 
| |\/| || |\/| | | | | | __/ _ \ '_ ` _ \/ __|
| |  | || |  | \ \_/ / | ||  __/ | | | | \__ \
\_|  |_/\_|  |_/\___/|_|\__\___|_| |_| |_|___/
        """.trimIndent())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
