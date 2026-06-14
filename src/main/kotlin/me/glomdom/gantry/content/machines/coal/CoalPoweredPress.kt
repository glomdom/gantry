package me.glomdom.gantry.content.machines.coal

import io.github.pylonmc.rebar.Rebar
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.block.interfaces.DirectionalRebarBlock
import io.github.pylonmc.rebar.block.interfaces.GuiRebarBlock
import io.github.pylonmc.rebar.block.interfaces.RecipeProcessorRebarBlock
import io.github.pylonmc.rebar.block.interfaces.TickingRebarBlock
import io.github.pylonmc.rebar.block.interfaces.VirtualInventoryRebarBlock
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER
import io.github.pylonmc.rebar.util.MachineUpdateReason
import io.github.pylonmc.rebar.util.ProgressBar
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import io.github.pylonmc.rebar.waila.WailaDisplay
import me.glomdom.gantry.recipes.CoalPoweredPressRecipe
import me.glomdom.gantry.utils.DISALLOW_NON_FUEL_ITEMS_TO_BE_ADDED
import me.glomdom.gantry.utils.extensions.FUEL
import me.glomdom.gantry.utils.extensions.fuelInput
import me.glomdom.gantry.utils.gantryKey
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.get

class CoalPoweredPress :
    RebarBlock,
    GuiRebarBlock,
    DirectionalRebarBlock,
    VirtualInventoryRebarBlock,
    TickingRebarBlock,
    RecipeProcessorRebarBlock<CoalPoweredPressRecipe> {

    private val inputInventory = VirtualInventory(1)
    private val fuelInventory = VirtualInventory(1)
    private val outputInventory = VirtualInventory(1)
    private val fuelBuffer: Double = getSettingOrThrow("fuel-buffer", ConfigAdapter.DOUBLE)

    private var fuelLeft: Double

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        facing = context.facing
        setTickInterval(5)
        recipeProgressItem = ProgressItem(GuiItems.background())
        setRecipeType(CoalPoweredPressRecipe.RECIPE_TYPE)

        fuelLeft = 0.0
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc) {
        fuelLeft = pdc.get(FUEL_LEFT_KEY, RebarSerializers.DOUBLE)
            ?: throw IllegalStateException("Coal-Powered Press at ${block.location} cannot get data from pdc for key $FUEL_LEFT_KEY")
    }

    class Item(stack: ItemStack) : RebarItem(stack)

    override fun write(pdc: PersistentDataContainer) {
        pdc.set(FUEL_LEFT_KEY, RebarSerializers.DOUBLE, fuelLeft)
    }

    override fun postInitialise() {
        outputInventory.addPreUpdateHandler(DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER)
        fuelInventory.addPreUpdateHandler(DISALLOW_NON_FUEL_ITEMS_TO_BE_ADDED)

        outputInventory.addPostUpdateHandler { _ -> tryStartRecipe() }
        inputInventory.addPostUpdateHandler { event ->
            if (event.updateReason !is MachineUpdateReason) {
                tryStartRecipe()
            }
        }

        fuelInventory.addPostUpdateHandler { event ->
            if (event.updateReason !is MachineUpdateReason) {
                tryConsumeFuel()
            }
        }
    }

    override fun createGui(): Gui {
        return Gui.builder()
            .setStructure(
                "# I F # # # O # #",
                "# i f # p # o # #",
                "# I F # # # O # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('I', GuiItems.input())
            .addIngredient('i', inputInventory)
            .addIngredient('F', GuiItems.fuelInput())
            .addIngredient('f', fuelInventory)
            .addIngredient('p', recipeProgressItem)
            .addIngredient('O', GuiItems.output())
            .addIngredient('o', outputInventory)
            .build()
    }

    override fun tick() {
        if (!isProcessingRecipe) return

        val recipe = currentRecipe ?: return
        val fuelCost = (recipe.fuelPerTick * tickInterval).toDouble()
        if (fuelLeft < fuelCost) {
            if (!tryConsumeFuel() || fuelLeft < fuelCost) return
        }

        fuelLeft -= (fuelLeft - fuelCost).coerceAtLeast(0.0)
        progressRecipe(tickInterval)
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "input" to inputInventory,
            "fuel" to fuelInventory,
            "output" to outputInventory,
        )
    }

    override fun getWaila(player: Player): WailaDisplay {
        return WailaDisplay.of(this, player)
            .add(
                if (fuelLeft == 0.0) {
                    Component.translatable("gantry.message.no-fuel")
                } else {
                    ProgressBar.fuelRemainingTicks(fuelBuffer.toInt(), fuelLeft.toInt())
                }
            )
    }

    override fun onRecipeFinished(recipe: CoalPoweredPressRecipe) {
        recipeProgressItem.setItem(GuiItems.background())
        outputInventory.addItem(MachineUpdateReason(), recipe.output.clone())
    }

    fun tryStartRecipe() {
        if (isProcessingRecipe) return

        val stack = inputInventory[0];
        if (stack == null || stack.isEmpty) return
        if (lastRecipe?.let { tryStartRecipe(it, stack) } == true) return

        for (recipe in CoalPoweredPressRecipe.RECIPE_TYPE) {
            if (tryStartRecipe(recipe, stack)) {
                break
            }
        }
    }

    private fun tryStartRecipe(recipe: CoalPoweredPressRecipe, input: ItemStack): Boolean {
        if (recipe.input != input || !outputInventory.canHold(recipe.output)) return false
        if (fuelLeft == 0.0) return false

        startRecipe(recipe, recipe.recipeTicks)
        recipeProgressItem.setItem(ItemStackBuilder.asOne(ItemStack.of(Material.FLINT_AND_STEEL)).clearLore())
        inputInventory.setItem(MachineUpdateReason(), 0, input.subtract(recipe.input.amount))

        return true
    }

    private fun tryConsumeFuel(): Boolean {
        val item = fuelInventory[0] ?: return false
        val itemType = item.type.asItemType() ?: return false
        if (!itemType.isFuel) return false

        val burnDuration = itemType.burnDuration.toDouble()
        val spaceLeftInBuffer = fuelBuffer - fuelLeft;
        val maxItemsThatFit = (spaceLeftInBuffer / burnDuration).toInt()
        val itemsToConsume = minOf(maxItemsThatFit, item.amount)
        if (itemsToConsume <= 0) return false

        fuelLeft += burnDuration * itemsToConsume
        fuelInventory.setItem(MachineUpdateReason(), 0, item.subtract(itemsToConsume))

        return true
    }

    companion object {
        val FUEL_LEFT_KEY = gantryKey("fuel_left")
    }
}

private fun ProgressBar.Companion.fuelRemainingTicks(total: Int, remaining: Int): ComponentLike {
    return ProgressBar()
        .barColor(TextColor.fromHexString("#e4b09f")!!)
        .proportion(remaining.toDouble() / total.toDouble())
        .suffix(
            Component.text(" ")
                .append(UnitFormat.FUEL.format(remaining))
        )
}
