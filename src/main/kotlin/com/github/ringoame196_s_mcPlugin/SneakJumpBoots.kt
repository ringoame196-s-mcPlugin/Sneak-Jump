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
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.plugin.Plugin

class SneakJumpBoots(plugin: Plugin) : JumpItem, SneakHold {
    override val id: String = "sneak_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val item: ItemStack by lazy { createItem() }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }

    override fun canJump(player: Player): Boolean {
        return player.isGrounded
    }

    override fun jump(
        player: Player,
    ) {
        player.jump(1.2)
        playJumpEffect(player)
    }

    fun playJumpEffect(player: Player) {
        val world = player.world
        val location = player.location
        val particle = Particle.CLOUD
        val sound = Sound.ENTITY_BAT_TAKEOFF
        world.spawnParticle(particle, location, 15, 0.2, 0.1, 0.2, 0.05)
        player.playSound(player, sound, 0.8f, 1.5f)
    }

    private fun createItem(): ItemStack {
        val item = JumpItemManager.createItem(this)
        val meta = item.itemMeta as LeatherArmorMeta
        meta.setColor(Color.BLUE)
        item.setItemMeta(meta)
        return item
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.GUNPOWDER)
        }
    }
}
