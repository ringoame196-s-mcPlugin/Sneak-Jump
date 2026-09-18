package com.github.ringoame196_s_mcPlugin

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.Plugin

class DoubleJump(plugin: Plugin) : ToggleSneak, JumpBoots {
    override val id: String = "double_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val color: Color = Color.WHITE
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.CLOUD
    override val sound = Sound.ENTITY_BAT_TAKEOFF
    override val soundPitch = 1.5f

    override fun isCancel(player: Player, e: Event?): Boolean {
        return DoubleJumpManager.hasJumped(player)
    }

    override fun isAction(player: Player, e: Event?): Boolean {
        if (player.isSneaking) return false
        if (player.isGrounded) return false
        return true
    }

    override fun onToggleSneak(player: Player) {
        jump(player)
    }

    override fun jump(
        player: Player,
    ) {
        player.jump()
        DoubleJumpManager.setJumped(player, true)
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.FEATHER)
        }
    }
}
