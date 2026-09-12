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

class DoubleJumpBoots(plugin: Plugin) : JumpItem, ToggleSneak {
    override val id: String = "double_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val item: ItemStack by lazy { createItem() }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val particle = Particle.CLOUD
    override val sound = Sound.ENTITY_BAT_TAKEOFF
    override val soundPitch = 1.5f

    override fun isCancel(player: Player): Boolean {
        return DoubleJumpManager.hasJumped(player)
    }

    override fun isAction(player: Player): Boolean {
        if (player.isSneaking) return false
        if (player.isGrounded) return false
        return true
    }

    override fun jump(
        player: Player,
    ) {
        player.jump()
        DoubleJumpManager.setJumped(player, true)
    }

    private fun createItem(): ItemStack {
        val item = JumpItemManager.createItem(this)
        val meta = item.itemMeta as LeatherArmorMeta
        meta.setColor(Color.WHITE)
        item.setItemMeta(meta)
        return item
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.FEATHER)
        }
    }
}
