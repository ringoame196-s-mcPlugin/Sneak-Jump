package com.github.ringoame196_s_mcPlugin

import org.bukkit.inventory.ItemStack

object JumpItemManager {
    lateinit var configManager: ConfigManager

    fun createItem(jumpItem: JumpItem): ItemStack {
        val item = ItemStack(jumpItem.material)
        val meta = item.itemMeta
        val displayName = configManager.getDisplayName(jumpItem.id)
        meta.setDisplayName(displayName)
        meta.jump.id = jumpItem.id
        item.setItemMeta(meta)
        return item
    }
}
