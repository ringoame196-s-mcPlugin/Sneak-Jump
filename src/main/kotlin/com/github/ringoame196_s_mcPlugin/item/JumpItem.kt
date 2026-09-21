package com.github.ringoame196_s_mcPlugin.item

import org.bukkit.Material
import org.bukkit.inventory.CraftingRecipe
import org.bukkit.inventory.ItemStack

interface JumpItem {
    val item: ItemStack
    val id: String
    val material: Material
    val recipe: CraftingRecipe?
}
