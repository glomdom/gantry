package me.glomdom.gantry.content.machines.hydraulics

import io.github.pylonmc.pylon.PylonFluids
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
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import me.glomdom.gantry.content.recipes.DebarkerRecipe
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
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

    private val itemInputInventory = VirtualInventory(1)
    private val itemOutputInventory = VirtualInventory(1)

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        setTickInterval(tickingInterval)
        setRecipeType(DebarkerRecipe.RECIPE_TYPE)

        createFluidBuffer(PylonFluids.HYDRAULIC_FLUID, fluidBufferAmount, input = true, output = false)
        createFluidBuffer(PylonFluids.DIRTY_HYDRAULIC_FLUID, fluidBufferAmount, input = false, output = true)

        createFluidPoint(FluidPointType.INPUT, BlockFace.EAST, context, false)
        createFluidPoint(FluidPointType.OUTPUT, BlockFace.WEST, context, false)

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
        )
    }

    override fun tick() {
    }

    override fun onRecipeFinished(recipe: DebarkerRecipe) {
    }

    override fun createGui(): Gui {
        return Gui.builder()
            .setStructure(
                "# # I # # # O # #",
                "# # i # p # o # #",
                "# # I # # # O # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('I', GuiItems.input())
            .addIngredient('i', itemInputInventory)
            .addIngredient('p', recipeProgressItem)
            .addIngredient('O', GuiItems.output())
            .addIngredient('o', itemOutputInventory)
            .build()
    }
}