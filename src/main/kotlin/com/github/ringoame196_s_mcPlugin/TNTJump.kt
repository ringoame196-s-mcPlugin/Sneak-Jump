package com.github.ringoame196_s_mcPlugin

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.Plugin
import java.util.UUID

class TNTJump(plugin: Plugin) : ToggleSneak, JumpBoots, ChargeBoots {
    override val id: String = "tnt_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val color: Color = Color.RED
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.CLOUD
    override val sound = Sound.ENTITY_GENERIC_EXPLODE
    override val soundPitch = 1.5f
    override val chargeSound = Sound.ENTITY_CREEPER_DEATH
    private val actionCount = 5

    companion object {
        private val jumpCountMap = mutableMapOf<UUID, Int>()
    }

    override fun isAction(player: Player): Boolean {
        if (player.isSneaking) return false
        if (!player.isGrounded) return false
        return true
    }

    override fun onToggleSneak(player: Player) {
        jump(player)
    }

    override fun jump(
        player: Player,
    ) {
        player.jump(2.0)
        jumpCountMap[player.uniqueId] = 0
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.TNT)
        }
    }

    override fun isCharge(player: Player): Boolean {
        return jumpCountMap.getOrDefault(player.uniqueId, 0) < actionCount - 1
    }

    override fun charge(player: Player) {
        jumpCountMap[player.uniqueId] = jumpCountMap.getOrDefault(player.uniqueId, 0) + 1
        sendChargeMessage(player)
    }

    private fun sendChargeMessage(player: Player) {
        val c = jumpCountMap.getOrDefault(player.uniqueId, 0)
        player.sendActionBar("$c/$actionCount")
    }
}
