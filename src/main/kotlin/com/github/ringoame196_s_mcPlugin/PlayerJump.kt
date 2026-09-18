package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface PlayerJump : BootsEvent {
    fun onPlayerJump(player: Player)
}
