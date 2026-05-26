package me.glomdom.gantry.recipes

import io.github.pylonmc.rebar.config.ConfigSection
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.guide.button.ItemButton
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.recipe.ConfigurableRecipeType
import io.github.pylonmc.rebar.recipe.FluidOrItem
import io.github.pylonmc.rebar.recipe.RebarRecipe
import io.github.pylonmc.rebar.recipe.RecipeInput
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import me.glomdom.gantry.GantryItems
import me.glomdom.gantry.utils.GantryUtils.gantryKey
import me.glomdom.gantry.utils.extensions.FUEL
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.gui.Gui

class CoalPoweredPressRecipe(
    val recipeKey: NamespacedKey,
    val input: ItemStack,
    val output: ItemStack,
    val recipeTicks: Int,
    val fuelPerTick: Int,
) : RebarRecipe {
    override val inputs = listOf(RecipeInput.of(input))
    override val results = listOf(FluidOrItem.of(output))

    override fun getKey(): NamespacedKey = recipeKey

    @Suppress("UnstableApiUsage")
    override fun display(): Gui {
        return Gui.builder()
            .setStructure(
                "# # # # # # # # #",
                "# # # # b # # # #",
                "# # # i m o # # #",
                "# # # # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('i', ItemButton(input))
            .addIngredient('m', ItemButton(ItemStackBuilder.of(GantryItems.COAL_POWERED_PRESS).clearLore().build()))
            .addIngredient(
                'b',
                GuiItems.progressCyclingItem(
                    recipeTicks * fuelPerTick,
                    ItemStackBuilder.of(Material.FLINT_AND_STEEL)
                        .name(Component.translatable("gantry.guide.recipe.fuel_burning"))
                        .clearLore()
                        .lore(
                            Component.translatable("gantry.guide.recipe.coal_powered_pressing").arguments(
                                RebarArgument.of("fuel-ticks", UnitFormat.FUEL.format(recipeTicks * fuelPerTick))
                            )
                        )
                )
            )
            .addIngredient('o', ItemButton(output))
            .addIngredient('#', GuiItems.backgroundBlack())
            .build()
    }

    companion object {
        val RECIPE_TYPE = object : ConfigurableRecipeType<CoalPoweredPressRecipe>(gantryKey("coal_powered_press")) {
            override fun loadRecipe(key: NamespacedKey, section: ConfigSection): CoalPoweredPressRecipe {
                return CoalPoweredPressRecipe(
                    recipeKey = key,
                    input = section.getOrThrow("input", ConfigAdapter.ITEM_STACK),
                    output = section.getOrThrow("output", ConfigAdapter.ITEM_STACK),
                    recipeTicks = section.getOrThrow("recipe-ticks", ConfigAdapter.INTEGER),
                    fuelPerTick = section.getOrThrow("fuel-per-tick", ConfigAdapter.INTEGER)
                )
            }
        }
    }
}