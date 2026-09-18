package com.github.ringoame196_s_mcPlugin

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Event

interface JumpBoots : JumpItem {
    val color: Color
    val particle: Particle
    val particleCount: Int get() = 15

    val sound: Sound
    val soundVolume: Float get() = 1.0f
    val soundPitch: Float get() = 1.0f

    fun jump(
        player: Player
    )

    fun playJumpEffect(player: Player) {
        val world = player.world
        val location = player.location
        val particle = this.particle
        val particleCount = this.particleCount
        val sound = this.sound
        val volume = this.soundVolume
        val pitch = this.soundPitch
        world.spawnParticle(particle, location, particleCount, 0.2, 0.1, 0.2, 0.05)
        player.playSound(player, sound, volume, pitch)
    }

    fun isAction(player: Player, e: Event? = null): Boolean = true
    fun isCancel(player: Player, e: Event? = null): Boolean = false
}
