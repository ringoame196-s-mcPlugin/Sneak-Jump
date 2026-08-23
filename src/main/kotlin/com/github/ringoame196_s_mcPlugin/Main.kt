package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.commands.Command
import com.github.ringoame196_s_mcPlugin.events.Events
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
                DoubleJumpBoots(),
            )

        registerEvents(jumpItems)
        registerCommands(jumpItems)
    }

    private fun registerEvents(jumpItems: List<JumpItem>) {
        server.pluginManager.registerEvents(Events(jumpItems), plugin)
    }

    private fun registerCommands(jumpItems: List<JumpItem>) {
        val command = getCommand("sneak-jump")
        command?.setExecutor(Command(jumpItems))
    }
}
