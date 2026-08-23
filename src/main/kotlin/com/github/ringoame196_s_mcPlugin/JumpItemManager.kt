package com.github.ringoame196_s_mcPlugin

import org.bukkit.inventory.ItemStack

object JumpItemManager {
    fun createItem(jumpItem: JumpItem): ItemStack {
        val item = ItemStack(jumpItem.material)
        val meta = item.itemMeta
        meta.setDisplayName("ダブルジャンプ")
        meta.jump.id = jumpItem.id
        item.setItemMeta(meta)
        return item
    }
}
