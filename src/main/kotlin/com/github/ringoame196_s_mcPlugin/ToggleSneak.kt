package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface ToggleSneak : BootsEvent {
    fun onToggleSneak(player: Player)
}
