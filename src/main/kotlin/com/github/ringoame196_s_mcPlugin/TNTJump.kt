package com.github.ringoame196_s_mcPlugin

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.github.ringoame196_s_mcPlugin.message.MessageKey
import com.github.ringoame196_s_mcPlugin.message.MessageManager
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.Plugin
import java.util.UUID

class TNTJump(private val plugin: Plugin, private val messageManager: MessageManager) : ToggleSneak, JumpBoots, PlayerJump {
    override val id: String = "tnt_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val bootsColor: Color = Color.RED
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val jumpParticle = Particle.CLOUD
    override val jumpSound = Sound.ENTITY_GENERIC_EXPLODE
    override val jumpSoundPitch = 1.5f
    private val actionCount = 5

    companion object {
        private val jumpCountMap = mutableMapOf<UUID, Int>()
    }

    override fun isAction(player: Player, e: Event?): Boolean {
        return when (e) {
            // シフトを押した瞬間（!isSneaking）かつ、まだチャージが上限（5回）未満の時だけチャージ許可
            is PlayerToggleSneakEvent -> !player.isSneaking && getChargeCount(player) < actionCount
            is PlayerJumpEvent -> player.isSneaking
            else -> false
        }
    }

    override fun isCancel(player: Player, e: Event?): Boolean {
        return when (e) {
            is PlayerToggleSneakEvent -> false
            is PlayerJumpEvent -> getChargeCount(player) < actionCount
            else -> false
        }
    }

    override fun cancel(player: Player) {
        reset(player)
    }

    override fun onToggleSneak(player: Player) {
        charge(player)
    }

    override fun onPlayerJump(player: Player) {
        jump(player)
    }

    override fun jump(player: Player) {
        // バニラのジャンプ処理が完了した直後（1 ticks後）に真上ベクトルの適用とリセットを行う
        org.bukkit.Bukkit.getScheduler().runTask(
            plugin,
            Runnable {
                if (player.isOnline) {
                    // 水平速度をリセットして真上に強く吹き飛ばす
                    player.velocity = player.velocity.setX(0.0).setZ(0.0).setY(2.0)
                    sendJumpSuccess(player, messageManager)
                    playJumpEffect(player)
                }
            }
        )
        reset(player)
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.TNT)
        }
    }

    private fun charge(player: Player) {
        val current = getChargeCount(player)
        if (current < actionCount) {
            jumpCountMap[player.uniqueId] = current + 1
        }

        if (getChargeCount(player) < actionCount) {
            sendChargeMessage(player)
        } else {
            sendBoostMessage(player)
        }
    }

    private fun reset(player: Player) {
        jumpCountMap[player.uniqueId] = 0
    }

    private fun getChargeCount(player: Player): Int {
        return jumpCountMap.getOrDefault(player.uniqueId, 0)
    }

    private fun sendChargeMessage(player: Player) {
        val count = getChargeCount(player)

        // チャージ進行状況に合わせて視覚的なゲージを作成
        val symbol = "■"
        val filled = symbol.repeat(count)
        val empty = symbol.repeat(actionCount - count)

        val message = "${ChatColor.RED}${ChatColor.BOLD}TNT CHARGE ${ChatColor.GRAY}[${ChatColor.RED}$filled${ChatColor.DARK_GRAY}$empty${ChatColor.GRAY}] ${ChatColor.GOLD}$count${ChatColor.WHITE}/$actionCount"
        player.sendActionBar(message)
    }

    private fun sendBoostMessage(player: Player) {
        val message = messageManager.get(MessageKey.TNT_JUMP_CHARGED_MESSAGE)
        player.sendActionBar(message)
    }
}
