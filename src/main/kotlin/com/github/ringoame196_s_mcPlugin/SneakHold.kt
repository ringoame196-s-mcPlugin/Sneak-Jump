package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface SneakHold {
    fun canJump(player: Player): Boolean
    fun jump(
        player: Player
    )
}
