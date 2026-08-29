package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.DoubleJumpManager
import com.github.ringoame196_s_mcPlugin.JumpItem
import com.github.ringoame196_s_mcPlugin.ToggleSneak
import com.github.ringoame196_s_mcPlugin.isGrounded
import com.github.ringoame196_s_mcPlugin.jump
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class Events(jumpItems: List<JumpItem>) : Listener {
    private val jumpItemMap: Map<String, JumpItem> = jumpItems.associateBy { it.id }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val player = e.player
        val boots = player.inventory.boots ?: return
        val jumpItem = jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (jumpItem !is ToggleSneak) return
        if (!jumpItem.canJump(player, e.isSneaking)) return
        if (!DoubleJumpManager.hasJumped(player)) {
            jumpItem.jump(player)
            DoubleJumpManager.setJumped(player, true)
        } else {
            sendCancelJump(player)
        }
    }

    private fun sendCancelJump(player: Player) {
        val message = "ジャンプできません"
        player.sendMessage(message)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player
        if (!DoubleJumpManager.hasJumped(player)) return
        if (player.isGrounded) {
            DoubleJumpManager.setJumped(player, false)
        }
    }
}
