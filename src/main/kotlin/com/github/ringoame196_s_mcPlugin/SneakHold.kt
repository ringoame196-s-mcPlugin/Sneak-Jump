package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface SneakHold : BootsEvent {
    fun onSneakHold(player: Player)
}
