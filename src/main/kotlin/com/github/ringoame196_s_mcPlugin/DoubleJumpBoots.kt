package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class DoubleJumpBoots : JumpItem, ToggleSneak {
    override val id: String = "double_jump_boots"
    override val material: Material = Material.CHAINMAIL_BOOTS
    override val item: ItemStack = JumpItemManager.createItem(this)

    override fun canJump(player: Player, isSneaking: Boolean): Boolean {
        if (isSneaking) return false
        if (player.isGrounded) return false
        return true
    }

    override fun jump(
        player: Player,
    ) {
        player.jump()
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
}
