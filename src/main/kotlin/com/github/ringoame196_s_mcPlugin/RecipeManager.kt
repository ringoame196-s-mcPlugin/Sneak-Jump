package com.github.ringoame196_s_mcPlugin

import org.bukkit.Bukkit

object RecipeManager {
    fun registerRecipes(jumpItems: List<JumpItem>) {
        for (jumpItem in jumpItems) {
            val recipe = jumpItem.recipe ?: continue

            if (Bukkit.getRecipe(recipe.key) != null) {
                Bukkit.removeRecipe(recipe.key)
            }

            Bukkit.addRecipe(recipe)
        }
    }
}
