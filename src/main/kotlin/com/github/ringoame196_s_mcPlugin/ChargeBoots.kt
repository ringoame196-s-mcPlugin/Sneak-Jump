package com.github.ringoame196_s_mcPlugin

import org.bukkit.Sound
import org.bukkit.entity.Player

interface ChargeBoots {
    val chargeSound: Sound
    val chargeSoundVolume: Float get() = 1.0f
    val chargeSoundPitch: Float get() = 1.0f

    fun isCharge(player: Player): Boolean
    fun charge(player: Player)
}
