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

class TNTJump(plugin: Plugin) : ToggleSneak, SneakHold, JumpBoots {
    override val id: String = "tnt_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val color: Color = Color.RED
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.CLOUD
    override val sound = Sound.ENTITY_BAT_TAKEOFF
    override val soundPitch = 1.5f

    companion object {
        private val jumpCountMap = mutableMapOf<UUID, Int>()
    }

    override fun jump(
        player: Player,
    ) {
        jumpCountMap[player.uniqueId] = jumpCountMap.getOrDefault(player.uniqueId, 0) + 1

        if (jumpCountMap.getOrDefault(player.uniqueId, 0) > 5) {
            player.jump(2.0)
            jumpCountMap[player.uniqueId] = 0
            DoubleJumpManager.setJumped(player, true)
        }
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.TNT)
        }
    }
}
