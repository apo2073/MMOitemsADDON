package kr.apo2073.mMoitemsADDON.cmd

import io.lumine.mythic.lib.api.item.NBTItem
import kr.apo2073.mMoitemsADDON.MMoitemsADDON
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.item.mmoitem.MMOItem
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

val mmoitem=MMOItems.plugin
class getSkilllCMd(plugin: JavaPlugin):CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>?): Boolean {
        if (p0 !is LivingEntity) return false
        val player=p0 as Player
        val item=player.inventory.itemInMainHand
        val nbtItem= NBTItem.get(item)
        player.sendMessage(nbtItem.tags.joinToString(", "))
        return true
    }
}