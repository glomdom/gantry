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
import me.glomdom.gantry.GantryKeys
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

class DebarkerRecipe(
    val recipeKey: NamespacedKey,
    val fluidPerSecond: Double,
    val input: ItemStack,
    val outputs: List<ItemStack>,
) : RebarRecipe {
    override val inputs: List<RecipeInput> = listOf(RecipeInput.of(input))
    override val results: List<FluidOrItem> = outputs.map(FluidOrItem::of)

    override fun display(): Gui {
        val gui = Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # # # # # #",
                "# # i # p # o O #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('i', ItemButton(input))
            .addIngredient('p', ItemButton(ItemStackBuilder.of(Material.DIAMOND_AXE).build()))
            .addIngredient('o', ItemButton(outputs[0]))
            .addIngredient('o', ItemButton(outputs[1]))
            .addIngredient('#', GuiItems.backgroundBlack())

        return gui.build()
    }

    override fun getKey(): NamespacedKey =
        recipeKey

    companion object {
        val RECIPE_TYPE = object : ConfigurableRecipeType<DebarkerRecipe>(GantryKeys.HYDRAULIC_DEBARKER) {
            override fun loadRecipe(
                key: NamespacedKey,
                section: ConfigSection
            ): DebarkerRecipe {
                val outputs = section.getOrThrow("outputs", ConfigAdapter.LIST.from(ConfigAdapter.ITEM_STACK))

                require(outputs.count() < 3) { "DebarkerRecipe received an output count of more than 2" }

                return DebarkerRecipe(
                    recipeKey = key,
                    input = section.getOrThrow("input", ConfigAdapter.ITEM_STACK),
                    outputs = outputs,
                    fluidPerSecond = section.getOrThrow("fluid-per-second", ConfigAdapter.DOUBLE),
                )
            }
        }
    }
}