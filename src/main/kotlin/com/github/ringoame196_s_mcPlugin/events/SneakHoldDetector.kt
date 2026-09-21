package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.custom_event.PlayerSneakHoldEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class SneakHoldDetector(
    private val plugin: Plugin,
    private val holdThresholdTicks: Long = 10L // デフォルト: 0.5秒 (10ticks)
) : Listener {

    private val tasks = mutableMapOf<UUID, BukkitTask>()

    @EventHandler
    fun onToggleSneak(e: PlayerToggleSneakEvent) {
        val player = e.player
        val uuid = player.uniqueId

        if (!e.isSneaking) {
            tasks.remove(uuid)?.cancel()
            return
        }

        tasks.remove(uuid)?.cancel()

        // タイマースタート
        val task = Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                tasks.remove(uuid)

                // 長押し時点でプレイヤーがオンラインかつスニーク中か最終確認
                if (player.isOnline && player.isSneaking) {
                    val holdEvent = PlayerSneakHoldEvent(player, holdThresholdTicks)
                    Bukkit.getPluginManager().callEvent(holdEvent)
                }
            },
            holdThresholdTicks
        )

        tasks[uuid] = task
    }

    // プレイヤー退出時にタスクを破棄してメモリリーク防止
    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        tasks.remove(e.player.uniqueId)?.cancel()
    }
}
