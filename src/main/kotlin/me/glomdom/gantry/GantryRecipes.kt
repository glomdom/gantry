package me.glomdom.gantry

import me.glomdom.gantry.recipes.CoalPoweredPressRecipe

object GantryRecipes {
    fun registerAll() {
        CoalPoweredPressRecipe.RECIPE_TYPE.register()
    }
}