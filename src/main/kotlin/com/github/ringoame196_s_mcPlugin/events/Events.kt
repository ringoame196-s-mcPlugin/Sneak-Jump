package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.DoubleJumpManager
import com.github.ringoame196_s_mcPlugin.JumpItem
import com.github.ringoame196_s_mcPlugin.ToggleSneak
import com.github.ringoame196_s_mcPlugin.isGrounded
import com.github.ringoame196_s_mcPlugin.jump
import com.github.ringoame196_s_mcPlugin.message.MessageKey
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleSneakEvent

class Events(jumpItems: List<JumpItem>, private val messageManager: MessageManager) : Listener {
    private val jumpItemMap: Map<String, JumpItem> = jumpItems.associateBy { it.id }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val player = e.player
        val boots = player.inventory.boots ?: return
        val jumpItem = jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (jumpItem !is ToggleSneak) return
        if (!jumpItem.canJump(player, e.isSneaking)) return
        if (!DoubleJumpManager.hasJumped(player)) {
            sendJump(player)
            jumpItem.jump(player)
            DoubleJumpManager.setJumped(player, true)
        } else {
            sendCancelJump(player)
        }
    }

    private fun sendJump(player: Player) {
        val message = messageManager.get(MessageKey.JUMP_MESSAGE)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    private fun sendRecharged(player: Player) {
        val message = messageManager.get(MessageKey.JUMP_RECHARGED_MESSAGE)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
    }

    private fun sendCancelJump(player: Player) {
        val message = messageManager.get(MessageKey.NO_CAN_JUMP_MESSAGE)
        val sound = Sound.BLOCK_NOTE_BLOCK_BELL
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
        player.playSound(player, sound, 1f, 1f)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        val player = e.player
        if (!DoubleJumpManager.hasJumped(player)) return

        val from = e.from
        val to = e.to

        if (from.y == to.y && from.blockX == to.blockX && from.blockZ == to.blockZ) return
        if (player.isGrounded) {
            DoubleJumpManager.setJumped(player, false)
            sendRecharged(player)
        }
    }
}
