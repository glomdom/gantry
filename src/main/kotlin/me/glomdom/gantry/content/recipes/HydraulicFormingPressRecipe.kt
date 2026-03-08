package me.glomdom.gantry.content.recipes

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType
import io.github.pylonmc.rebar.recipe.FluidOrItem
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.RecipeInput
import io.github.pylonmc.rebar.util.gui.GuiItems
import me.glomdom.gantry.GantryKeys
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

/**
 * @param input item to be formed
 * @param output resulting item
 */
class HydraulicFormingPressRecipe(val recipeKey: NamespacedKey, val input: RecipeInput.Item, val output: ItemStack) : RebarRecipe {
    override val inputs: List<RecipeInput> = listOf(input)
    override val results: List<FluidOrItem> = listOf(FluidOrItem.of(output))

    override fun display(): Gui {
        val gui = Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
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
                    input = section.getOrThrow("input", ConfigAdapter.RECIPE_INPUT_ITEM),
                    output = section.getOrThrow("outputs", ConfigAdapter.ITEM_STACK)
                )
            }
        }
    }
}