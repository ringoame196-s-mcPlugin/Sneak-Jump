package com.github.ringoame196_s_mcPlugin

import com.destroystokyo.paper.event.player.PlayerJumpEvent
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

class TNTJump(private val plugin: Plugin) : ToggleSneak, JumpBoots, PlayerJump {
    override val id: String = "tnt_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val color: Color = Color.RED
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.CLOUD
    override val sound = Sound.ENTITY_GENERIC_EXPLODE
    override val soundPitch = 1.5f
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
        // 上限（actionCount = 5）を超えないように強制制限
        if (current < actionCount) {
            jumpCountMap[player.uniqueId] = current + 1
            sendChargeMessage(player)
        }
    }

    private fun reset(player: Player) {
        jumpCountMap[player.uniqueId] = 0
    }

    private fun getChargeCount(player: Player): Int {
        return jumpCountMap.getOrDefault(player.uniqueId, 0)
    }

    private fun sendChargeMessage(player: Player) {
        val c = getChargeCount(player)
        player.sendActionBar("$c/$actionCount")
    }
}
