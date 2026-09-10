package com.github.ringoame196_s_mcPlugin

import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player

interface JumpBoots {
    val particle: Particle
    val particleCount: Int get() = 15

    val sound: Sound
    val soundVolume: Float get() = 1.0f
    val soundPitch: Float get() = 1.0f

    fun jump(
        player: Player
    )
}
