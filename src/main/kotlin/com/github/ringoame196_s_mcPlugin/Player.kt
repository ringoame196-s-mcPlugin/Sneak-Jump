package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player

fun Player.jump(height: Double = 0.6) {
    val velocity = this.velocity
    velocity.y = height
    this.velocity = velocity
}
