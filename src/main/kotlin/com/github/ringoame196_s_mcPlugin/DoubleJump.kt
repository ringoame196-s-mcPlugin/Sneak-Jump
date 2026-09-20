package com.github.ringoame196_s_mcPlugin

import com.github.ringoame196_s_mcPlugin.message.MessageManager
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

class DoubleJump(plugin: Plugin, private val messageManager: MessageManager) : ToggleSneak, JumpBoots {
    override val id: String = "double_jump_boots"
    override val material: Material = Material.LEATHER_BOOTS
    override val bootsColor: Color = Color.WHITE
    override val durabilityCost = 0
    override val item: ItemStack by lazy { JumpItemManager.createBoots(this) }
    override val recipe: CraftingRecipe by lazy { createRecipe(plugin) }
    override val jumpParticle = Particle.CLOUD
    override val jumpSound = Sound.ENTITY_BAT_TAKEOFF
    override val jumpSoundPitch = 1.5f

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
        reduceDurability(player)
        DoubleJumpManager.setJumped(player, true)
        sendJumpSuccess(player, messageManager)
        playJumpEffect(player)
    }

    private fun createRecipe(plugin: Plugin): CraftingRecipe {
        val key = NamespacedKey(plugin, "${id}_shapeless")
        return ShapelessRecipe(key, item).apply {
            addIngredient(Material.LEATHER_BOOTS)
            addIngredient(Material.FEATHER)
        }
    }
}
