package com.github.ringoame196_s_mcPlugin.events

import com.github.ringoame196_s_mcPlugin.BootsEvent
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
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.meta.Damageable

class Events(jumpItems: List<JumpItem>, private val messageManager: MessageManager) : Listener {
    private val jumpItemMap: Map<String, JumpItem> = jumpItems.associateBy { it.id }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        activationJump<ToggleSneak>(e)
    }

    @EventHandler
    fun onSneakHold(e: PlayerSneakHoldEvent) {
        activationJump<SneakHold>(e)
    }

    private inline fun <reified T : BootsEvent> activationJump(e: PlayerEvent) {
        val player = e.player
        val boots = player.inventory.boots ?: return
        val jumpItem = jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (jumpItem !is JumpBoots) return
        if (player.isFlying) return
        if (jumpItem !is T) return
        if (!jumpItem.isAction(player)) return
        if (jumpItem.isCancel(player)) {
            sendCancelJump(player)
            return
        }

        sendJump(player)
        jumpItem.jump(player)
        playJumpEffect(player, jumpItem)
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

    @EventHandler
    fun onToggleFlight(e: PlayerToggleFlightEvent) {
        val player = e.player

        if (!DoubleJumpManager.hasJumped(player)) return
        DoubleJumpManager.setJumped(player, false)
        sendRecharged(player)
    }

    @EventHandler
    fun onPrepareItemCraft(e: PrepareItemCraftEvent) {
        val recipe = e.recipe ?: return
        val result = recipe.result

        val resultMeta = result.itemMeta ?: return
        val jumpId = resultMeta.jump.id ?: return

        val ingredients = e.inventory.matrix.filterNotNull().filter { !it.type.isAir }

        for (item in ingredients) {
            val meta = item.itemMeta as? Damageable ?: continue

            if (meta.hasDamage() && meta.damage > 0) {
                e.inventory.result = null
                return
            }
        }
    }
}
