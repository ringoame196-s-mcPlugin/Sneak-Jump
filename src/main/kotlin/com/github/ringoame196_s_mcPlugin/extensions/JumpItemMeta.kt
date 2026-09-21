package com.github.ringoame196_s_mcPlugin.extensions

import com.github.ringoame196_s_mcPlugin.Main
import org.bukkit.NamespacedKey
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType

class JumpItemMeta(val rawMeta: ItemMeta) {
    private val idKey = NamespacedKey(Main.plugin, "jump_item_key")

    var id: String?
        get() = rawMeta.persistentDataContainer.get(idKey, PersistentDataType.STRING)
        set(value) {
            if (value == null) {
                rawMeta.persistentDataContainer.remove(idKey)
            } else {
                rawMeta.persistentDataContainer.set(idKey, PersistentDataType.STRING, value)
            }
        }
}

val ItemMeta.jump: JumpItemMeta
    get() = JumpItemMeta(this)
