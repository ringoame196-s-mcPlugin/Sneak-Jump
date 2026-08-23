package com.github.ringoame196_s_mcPlugin.commands

import com.github.ringoame196_s_mcPlugin.JumpItem
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class Command(jumpItems: List<JumpItem>) : CommandExecutor, TabCompleter {
    private val jumpItemMap: Map<String, JumpItem> = jumpItems.associateBy { it.id }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        if (args.isEmpty()) return false
        val subCommand = args[0]
        when (subCommand) {
            CommandConst.GIVE_COMMAND -> giveCommand(sender, args)
            else -> return false
        }

        return true
    }

    private fun giveCommand(
        sender: CommandSender,
        args: Array<out String>,
    ) {
        val itemId = args[1]
        val item = jumpItemMap[itemId]?.item ?: return
        if (sender !is Player) return
        sender.inventory.addItem(item)
    }

    override fun onTabComplete(
        commandSender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>,
    ): MutableList<String>? {
        return when (args.size) {
            1 -> mutableListOf(CommandConst.GIVE_COMMAND)
            2 ->
                when (args[0]) {
                    CommandConst.GIVE_COMMAND -> jumpItemMap.keys.toMutableList()
                    else -> mutableListOf()
                }
            else -> mutableListOf()
        }
    }
}
