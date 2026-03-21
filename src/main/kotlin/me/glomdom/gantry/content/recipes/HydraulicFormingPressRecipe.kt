package me.glomdom.gantry.content.recipes

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType
import io.github.pylonmc.rebar.recipe.FluidOrItem
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.RecipeInput
import io.github.pylonmc.rebar.util.gui.GuiItems
import me.glomdom.gantry.GantryItems
import me.glomdom.gantry.GantryKeys
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

/**
 * @param input item to be formed
 * @param output resulting item
 */
class HydraulicFormingPressRecipe(
    val recipeKey: NamespacedKey,
    val fluidPerSecond: Double,
    val form: ItemStack,
    val input: ItemStack,
    val output: ItemStack,
    val byproduct: ItemStack
) : RebarRecipe {
    override val inputs: List<RecipeInput> = listOf(RecipeInput.of(input), RecipeInput.of(form))
    override val results: List<FluidOrItem> = listOf(FluidOrItem.of(output))

    override fun display(): Gui {
        val gui = Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# i f # m # o b #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('i', ItemButton(input))
            .addIngredient('f', ItemButton(form))
            .addIngredient('m', ItemButton(GantryItems.HYDRAULIC_FORMING_PRESS))
            .addIngredient('o', ItemButton(output))
            .addIngredient('b', ItemButton(byproduct))
            .addIngredient('#', GuiItems.backgroundBlack())

        return gui.build()
    }

    override fun getKey(): NamespacedKey =
        recipeKey

    companion object {
        val RECIPE_TYPE = object : ConfigurableRecipeType<HydraulicFormingPressRecipe>(GantryKeys.HYDRAULIC_FORMING_PRESS) {
            override fun loadRecipe(
                key: NamespacedKey,
                section: ConfigSection
            ): HydraulicFormingPressRecipe {
                return HydraulicFormingPressRecipe(
                    recipeKey = key,
                    input = section.getOrThrow("input", ConfigAdapter.ITEM_STACK),
                    output = section.getOrThrow("output", ConfigAdapter.ITEM_STACK),
                    fluidPerSecond = section.getOrThrow("fluid-per-second", ConfigAdapter.DOUBLE),
                    form = section.getOrThrow("form", ConfigAdapter.ITEM_STACK),
                    byproduct = section.getOrThrow("byproduct", ConfigAdapter.ITEM_STACK)
                )
            }
        }
    }
}