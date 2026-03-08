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
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.logistics.LogisticGroupType
import io.github.pylonmc.rebar.util.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import me.glomdom.gantry.content.recipes.HydraulicFormingPressRecipe
import me.glomdom.gantry.utils.GantryGuiItems
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory

class HydraulicFormingPress :
    RebarBlock,
    RebarDirectionalBlock,
    RebarFluidBufferBlock,
    RebarTickingBlock,
    RebarGuiBlock,
    RebarVirtualInventoryBlock,
    RebarLogisticBlock,
    RebarRecipeProcessor<HydraulicFormingPressRecipe> {

    val tickingInterval = getSettings().getOrThrow("tick-interval", ConfigAdapter.INTEGER)
    val fluidBufferAmount = getSettings().getOrThrow("buffer-amount", ConfigAdapter.DOUBLE)

    private val itemInputInventory: VirtualInventory = VirtualInventory(1)
    private val formInputOutputInventory: VirtualInventory = VirtualInventory(1)
    private val itemOutputInventory: VirtualInventory = VirtualInventory(1)

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
        setRecipeType(HydraulicFormingPressRecipe.RECIPE_TYPE)

        createFluidBuffer(PylonFluids.HYDRAULIC_FLUID, fluidBufferAmount, input = true, output = false)
        createFluidBuffer(PylonFluids.DIRTY_HYDRAULIC_FLUID, fluidBufferAmount, input = false, output = true)

        createFluidPoint(FluidPointType.INPUT, BlockFace.NORTH, context, false)
        createFluidPoint(FluidPointType.OUTPUT, BlockFace.SOUTH, context, false)

        facing = context.facing
        recipeProgressItem = ProgressItem(GuiItems.background())
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    override fun postInitialise() {
        createLogisticGroup("item-input", LogisticGroupType.INPUT, itemInputInventory)
        createLogisticGroup("form-input", LogisticGroupType.INPUT, formInputOutputInventory)
        createLogisticGroup("form-output", LogisticGroupType.OUTPUT, formInputOutputInventory)
        createLogisticGroup("item-output", LogisticGroupType.OUTPUT, itemInputInventory)

        itemOutputInventory.addPreUpdateHandler(DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER)
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
            .addIngredient('i', itemInputInventory)
            .addIngredient('F', GantryGuiItems.formInput())
            .addIngredient('f', formInputOutputInventory)
            .addIngredient('p', recipeProgressItem)
            .addIngredient('O', GuiItems.output())
            .addIngredient('o', itemOutputInventory)
            .build()
    }

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        super<RebarVirtualInventoryBlock>.onBreak(drops, context)
        super<RebarFluidBufferBlock>.onBreak(drops, context)
    }

    override fun tick() {}

    override fun onRecipeFinished(recipe: HydraulicFormingPressRecipe) {
        recipeProgressItem.setItem(GuiItems.background())

        // TODO: Damage form
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "item-input" to itemInputInventory,
            "form-input-output" to formInputOutputInventory,
            "item-output" to itemOutputInventory,
        )
    }
}