package com.github.ringoame196_s_mcPlugin

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface JumpItem {
    val item: ItemStack
    val id: String
    val material: Material
}
