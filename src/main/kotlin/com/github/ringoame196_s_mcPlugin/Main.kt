package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events
import com.github.ringoame196_s_mcPlugin.events.SneakHoldDetector
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

        saveDefaultConfig()
        val configManager = ConfigManager(plugin.config)
        JumpItemManager.configManager = configManager
        val messageManager = MessageManager(plugin)

        val jumpItems =
            listOf(
                DoubleJump(plugin),
                SneakJump(plugin),
                TNTJump(plugin, messageManager)
            )

        registerEvents(jumpItems, messageManager)
        registerCommands(jumpItems)
        registerRecipes(jumpItems, configManager)
    }

    private fun registerEvents(jumpItems: List<JumpItem>, messageManager: MessageManager) {
        val sneakHoldDetector = SneakHoldDetector(plugin, 10L)
        server.pluginManager.registerEvents(Events(jumpItems, messageManager), plugin)
        server.pluginManager.registerEvents(sneakHoldDetector, this)
    }

    private fun registerCommands(jumpItems: List<JumpItem>) {
        val command = getCommand("sneak-jump")
        command?.setExecutor(Command(jumpItems))
    }

    private fun registerRecipes(jumpItems: List<JumpItem>, configManager: ConfigManager) {
        if (configManager.isCraftingEnabled) {
            RecipeManager.registerRecipes(jumpItems)
        } else {
            RecipeManager.removeRecipes(jumpItems)
        }
    }
}
