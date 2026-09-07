package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        // 外部クラスから Main.plugin でアクセスできるようにする
        lateinit var plugin: Main
            private set
    }

    override fun onEnable() {
        super.onEnable()
        plugin = this

        val jumpItems =
            listOf(
                DoubleJumpBoots(plugin),
            )

        registerEvents(jumpItems)
        registerCommands(jumpItems)
        registerRecipes(jumpItems)
    }

    private fun registerEvents(jumpItems: List<JumpItem>) {
        val messageManager = MessageManager(plugin)
        server.pluginManager.registerEvents(Events(jumpItems, messageManager), plugin)
    }

    private fun registerCommands(jumpItems: List<JumpItem>) {
        val command = getCommand("sneak-jump")
        command?.setExecutor(Command(jumpItems))
    }

    private fun registerRecipes(jumpItems: List<JumpItem>) {
        RecipeManager.registerRecipes(jumpItems)
    }
}
