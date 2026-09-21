package com.github.ringoame196_s_mcPlugin.message

import com.github.ringoame196_s_mcPlugin.Main
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

object DoubleJumpManager {
    private val key by lazy { NamespacedKey(Main.plugin, "has_double_jumped") }

    fun hasJumped(player: Player): Boolean {
        return player.persistentDataContainer.has(key, PersistentDataType.BYTE)
    }

    fun setJumped(player: Player, value: Boolean) {
        val pdc = player.persistentDataContainer
        if (value) {
            pdc.set(key, PersistentDataType.BYTE, 1)
        } else {
            pdc.remove(key)
        }
    }
}
