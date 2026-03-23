package me.glomdom.gantry.content.recipes

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType
import io.github.pylonmc.rebar.recipe.FluidOrItem
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.RecipeInput
import io.github.pylonmc.rebar.util.gui.GuiItems
import me.glomdom.gantry.GantryItems
import me.glomdom.gantry.utils.GantryUtils.gantryKey
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

class DebarkerRecipe(
    val recipeKey: NamespacedKey,
    val input: ItemStack,
    val output: ItemStack,
    val byproduct: ItemStack?,
) : RebarRecipe {
    override val inputs: List<RecipeInput> = listOf(RecipeInput.of(input))
    override val results: List<FluidOrItem> =
        listOf(FluidOrItem.of(output), FluidOrItem.of(byproduct ?: ItemStackBuilder.of(Material.AIR).build()))

    override fun display(): Gui {
        val gui = Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # i # p # o b #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('i', ItemButton(input))
            .addIngredient('p', ItemButton(GantryItems.HYDRAULIC_DEBARKER))
            .addIngredient('o', ItemButton(output))
            .addIngredient('b', ItemButton(byproduct ?: ItemStackBuilder.of(Material.AIR).build()))
            .addIngredient('#', GuiItems.backgroundBlack())

        return gui.build()
    }

    override fun getKey(): NamespacedKey =
        recipeKey

    companion object {
        val RECIPE_TYPE = object : ConfigurableRecipeType<DebarkerRecipe>(gantryKey("debarker")) {
            override fun loadRecipe(
                key: NamespacedKey,
                section: ConfigSection
            ): DebarkerRecipe {
                return DebarkerRecipe(
                    recipeKey = key,
                    input = section.getOrThrow("input", ConfigAdapter.ITEM_STACK),
                    output = section.getOrThrow("output", ConfigAdapter.ITEM_STACK),
                    byproduct = section.get("byproduct", ConfigAdapter.ITEM_STACK),
                )
            }
        }
    }
}