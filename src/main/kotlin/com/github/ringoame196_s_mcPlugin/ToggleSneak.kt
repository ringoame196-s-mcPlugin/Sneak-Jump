package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface ToggleSneak {
    fun onPlayerToggleSneak(player: Player, isSneaking: Boolean)
}
