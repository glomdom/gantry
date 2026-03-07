package me.glomdom.gantry

import me.glomdom.gantry.content.recipes.HydraulicFormingPressRecipe

object GantryRecipes {
    fun registerAll() {
        HydraulicFormingPressRecipe.RECIPE_TYPE.register()
    }
}