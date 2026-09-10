package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

interface SneakHold : JumpBoots {
    fun canJump(player: Player): Boolean
}
