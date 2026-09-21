package com.github.ringoame196_s_mcPlugin.boots_event

import org.bukkit.entity.Player

interface SneakHold : BootsEvent {
    fun onSneakHold(player: Player)
}
