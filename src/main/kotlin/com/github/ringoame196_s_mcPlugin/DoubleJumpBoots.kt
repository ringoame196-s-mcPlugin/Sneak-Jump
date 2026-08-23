package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DoubleJumpBoots : JumpItem, ToggleSneak {
    override val id: String = "double_jump_boots"
    override val material: Material = Material.CHAINMAIL_BOOTS
    override val item: ItemStack = JumpItemManager.createItem(this)

    override fun onPlayerToggleSneak(
        player: Player,
        isSneaking: Boolean,
    ) {
        if (!isSneaking) return
        if (player.isGrounded) return
        player.jump()
    }
}
