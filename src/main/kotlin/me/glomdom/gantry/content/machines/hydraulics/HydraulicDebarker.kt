package me.glomdom.gantry.content.machines.hydraulics

import io.github.pylonmc.pylon.PylonFluids
import io.github.pylonmc.pylon.util.PylonUtils
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock
import io.github.pylonmc.rebar.block.base.RebarGuiBlock
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock
import io.github.pylonmc.rebar.block.base.RebarRecipeProcessor
import io.github.pylonmc.rebar.block.base.RebarTickingBlock
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.fluid.FluidPointType
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import io.github.pylonmc.rebar.waila.WailaDisplay
import me.glomdom.gantry.content.recipes.DebarkerRecipe
import me.glomdom.gantry.utils.GantryGuiItems
import net.kyori.adventure.text.format.TextColor
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory

class HydraulicDebarker :
    RebarBlock,
    RebarDirectionalBlock,
    RebarFluidBufferBlock,
    RebarTickingBlock,
    RebarGuiBlock,
    RebarVirtualInventoryBlock,
    RebarLogisticBlock,
    RebarRecipeProcessor<DebarkerRecipe> {

    private val tickingInterval = getSettings().getOrThrow("tick-interval", ConfigAdapter.INTEGER)
    private val fluidBufferAmount = getSettings().getOrThrow("buffer-amount", ConfigAdapter.DOUBLE)
    private val fluidPerSecond = getSettings().getOrThrow("fluid-per-second", ConfigAdapter.DOUBLE)

    private val itemInputInventory = VirtualInventory(1)
    private val itemOutputInventory = VirtualInventory(1)
    private val byproductOutputInventory = VirtualInventory(1)

    class Item(stack: ItemStack) : RebarItem(stack) {
        val fluidBufferAmount = getSettings().getOrThrow("buffer-amount", ConfigAdapter.DOUBLE)

        override fun getPlaceholders(): List<RebarArgument> {
            return listOf(
                RebarArgument.of("buffer", UnitFormat.MILLIBUCKETS.format(fluidBufferAmount))
            )
        }
    }

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        setTickInterval(tickingInterval)

        createFluidBuffer(PylonFluids.HYDRAULIC_FLUID, fluidBufferAmount, input = true, output = false)
        createFluidBuffer(PylonFluids.DIRTY_HYDRAULIC_FLUID, fluidBufferAmount, input = false, output = true)

        createFluidPoint(FluidPointType.INPUT, BlockFace.EAST, context, false)
        createFluidPoint(FluidPointType.OUTPUT, BlockFace.WEST, context, false)

        setRecipeType(DebarkerRecipe.RECIPE_TYPE)

        facing = context.facing
        recipeProgressItem = ProgressItem(GuiItems.background())
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        super<RebarVirtualInventoryBlock>.onBreak(drops, context)
        super<RebarFluidBufferBlock>.onBreak(drops, context)
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "input" to itemInputInventory,
            "output" to itemOutputInventory,
            "byproduct-output" to byproductOutputInventory,
        )
    }

    override fun tick() {
    }

    override fun onRecipeFinished(recipe: DebarkerRecipe) {
    }

    override fun getWaila(player: Player): WailaDisplay {
        return WailaDisplay(
            defaultWailaTranslationKey.arguments(
                RebarArgument.of(
                    "input-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.HYDRAULIC_FLUID),
                        fluidCapacity(PylonFluids.HYDRAULIC_FLUID),
                        20,
                        TextColor.fromHexString("#212cd9")
                    )
                ),
                RebarArgument.of(
                    "output-bar", PylonUtils.createFluidAmountBar(
                        fluidAmount(PylonFluids.DIRTY_HYDRAULIC_FLUID),
                        fluidCapacity(PylonFluids.DIRTY_HYDRAULIC_FLUID),
                        20,
                        TextColor.fromHexString("#48459b")
                    )
                )
            )
        )
    }

    override fun createGui(): Gui {
        return Gui.builder()
            .setStructure(
                "# # I # # # O B #",
                "# # i # p # o b #",
                "# # I # # # O B #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('I', GuiItems.input())
            .addIngredient('i', itemInputInventory)
            .addIngredient('p', recipeProgressItem)
            .addIngredient('O', GuiItems.output())
            .addIngredient('o', itemOutputInventory)
            .addIngredient('B', GantryGuiItems.byproductOutput())
            .addIngredient('b', byproductOutputInventory)
            .build()
    }
}