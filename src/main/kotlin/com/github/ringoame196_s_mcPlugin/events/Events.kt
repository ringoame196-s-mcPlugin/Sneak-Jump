package com.github.ringoame196_s_mcPlugin.events

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.github.ringoame196_s_mcPlugin.boots_event.BootsEvent
import com.github.ringoame196_s_mcPlugin.message.DoubleJumpManager
import com.github.ringoame196_s_mcPlugin.item.JumpBoots
import com.github.ringoame196_s_mcPlugin.item.JumpItem
import com.github.ringoame196_s_mcPlugin.boots_event.PlayerJump
import com.github.ringoame196_s_mcPlugin.custom_event.PlayerSneakHoldEvent
import com.github.ringoame196_s_mcPlugin.boots_event.SneakHold
import com.github.ringoame196_s_mcPlugin.boots_event.ToggleSneak
import com.github.ringoame196_s_mcPlugin.extensions.isGrounded
import com.github.ringoame196_s_mcPlugin.extensions.jump
import com.github.ringoame196_s_mcPlugin.message.MessageKey
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.meta.Damageable

class Events(jumpItems: List<JumpItem>, private val messageManager: MessageManager) : Listener {
    private val jumpItemMap: Map<String, JumpItem> = jumpItems.associateBy { it.id }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        activationJump<ToggleSneak>(e.player, e) { boots ->
            boots.onToggleSneak(e.player)
        }
    }

    @EventHandler
    fun onSneakHold(e: PlayerSneakHoldEvent) {
        activationJump<SneakHold>(e.player, e) { boots ->
            boots.onSneakHold(e.player)
        }
    }

    @EventHandler
    fun onPlayerJump(e: PlayerJumpEvent) {
        activationJump<PlayerJump>(e.player, e) { boots ->
            boots.onPlayerJump(e.player)
        }
    }

    private inline fun <reified T : BootsEvent> activationJump(
        player: Player,
        e: Event,
        action: (T) -> Unit
    ) {
        val boots = player.inventory.boots ?: return
        val jumpItem = jumpItemMap[boots.itemMeta.jump.id] ?: return
        if (jumpItem !is JumpBoots) return
        if (player.isFlying) return
        if (jumpItem !is T) return // インターフェースの型チェック
        if (!jumpItem.isAction(player, e)) return

        if (jumpItem.isCancel(player) || jumpItem.isCancel(player, e)) {
            jumpItem.sendJumpCancel(player, messageManager)
            jumpItem.cancel(player)
            return
        }

        action(jumpItem)
    }

    private fun sendRecharged(player: Player) {
        val message = messageManager.get(MessageKey.JUMP_RECHARGED_MESSAGE)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, *TextComponent.fromLegacyText(message))
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
