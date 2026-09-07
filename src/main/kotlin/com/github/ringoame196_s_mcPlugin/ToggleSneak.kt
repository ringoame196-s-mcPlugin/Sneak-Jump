package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface ToggleSneak {
    fun canJump(player: Player, isSneaking: Boolean): Boolean
    fun jump(
        player: Player
    )
}
