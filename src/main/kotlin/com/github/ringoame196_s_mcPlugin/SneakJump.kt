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

class SneakJump(plugin: Plugin) : SneakHold, JumpBoots {
    override val id: String = "sneak_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val color: Color = Color.BLUE
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.SWEEP_ATTACK
    override val sound = Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR
    override val soundPitch: Float = 0.7f

    override fun isCancel(player: Player): Boolean {
        return !player.isGrounded
    }

    override fun onSneakHold(player: Player) {
        jump(player)
    }

    override fun jump(
        player: Player,
    ) {
        player.jump(1.2)
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.GUNPOWDER)
        }
    }
}
