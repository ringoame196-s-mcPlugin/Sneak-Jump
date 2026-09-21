package com.github.ringoame196_s_mcPlugin.message

import com.github.ringoame196_s_mcPlugin.extensions.jump
import com.github.ringoame196_s_mcPlugin.item.JumpBoots
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

object JumpItemManager {
    lateinit var configManager: ConfigManager

    fun createBoots(jumpItem: JumpBoots): ItemStack {
        val item = ItemStack(jumpItem.material)
        val meta = item.itemMeta as LeatherArmorMeta
        val displayName = configManager.getDisplayName(jumpItem.id)
        meta.setDisplayName(displayName)
        meta.setColor(jumpItem.bootsColor)
        meta.jump.id = jumpItem.id
        item.setItemMeta(meta)
        return item
    }
}
