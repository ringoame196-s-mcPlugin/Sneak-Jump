package com.github.ringoame196_s_mcPlugin.extensions

import org.bukkit.entity.Player

fun Player.jump(height: Double = 0.6) {
    val velocity = this.velocity
    velocity.y = height
    this.velocity = velocity
}

val Player.isGrounded: Boolean
    get() {
        val location = location.clone().subtract(0.0, 0.1, 0.0)
        val block = location.block
        return !block.isEmpty && !block.isLiquid
    }
