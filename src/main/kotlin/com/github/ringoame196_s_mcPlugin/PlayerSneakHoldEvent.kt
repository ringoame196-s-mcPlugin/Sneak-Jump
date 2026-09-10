package com.github.ringoame196_s_mcPlugin

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerSneakHoldEvent(val player: Player, val holdTimeTicks: Long) : Event(), Cancellable {
    private var isCancelled = false

    override fun isCancelled(): Boolean = isCancelled

    override fun setCancelled(cancel: Boolean) {
        this.isCancelled = cancel
    }

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}
