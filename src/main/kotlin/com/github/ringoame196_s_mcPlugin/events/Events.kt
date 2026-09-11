package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.DoubleJumpManager
import com.github.ringoame196_s_mcPlugin.JumpBoots
import com.github.ringoame196_s_mcPlugin.JumpItem
import com.github.ringoame196_s_mcPlugin.PlayerSneakHoldEvent
import com.github.ringoame196_s_mcPlugin.SneakHold
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
import org.bukkit.event.entity.EntityDamageEvent
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
            playJumpEffect(player, jumpItem)
            DoubleJumpManager.setJumped(player, true)
        } else {
            sendCancelJump(player)
        }
    }

    @EventHandler
    fun onSneakHold(e: PlayerSneakHoldEvent) {
        val player = e.player
        val boots = player.inventory.boots ?: return
        val jumpItem = jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (jumpItem !is SneakHold) return
        if (jumpItem.canJump(player)) {
            sendJump(player)
            jumpItem.jump(player)
            playJumpEffect(player, jumpItem)
        } else {
            sendCancelJump(player)
        }
    }

    private fun sendJump(player: Player) {
        val message = messageManager.get(MessageKey.JUMP_MESSAGE)
        player.sendActionBar(message)
    }

    private fun playJumpEffect(player: Player, jumpBoots: JumpBoots) {
        val world = player.world
        val location = player.location
        val particle = jumpBoots.particle
        val particleCount = jumpBoots.particleCount
        val sound = jumpBoots.sound
        val volume = jumpBoots.soundVolume
        val pitch = jumpBoots.soundPitch
        world.spawnParticle(particle, location, particleCount, 0.2, 0.1, 0.2, 0.05)
        player.playSound(player, sound, volume, pitch)
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

    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return
        val boots = player.inventory.boots ?: return
        jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (e.cause == EntityDamageEvent.DamageCause.FALL) {
            e.isCancelled = true
        }
    }
}
