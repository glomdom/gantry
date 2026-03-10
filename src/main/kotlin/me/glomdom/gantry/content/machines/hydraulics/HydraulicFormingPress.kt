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
import io.github.pylonmc.rebar.logistics.LogisticGroupType
import io.github.pylonmc.rebar.logistics.slot.VirtualInventoryLogisticSlot
import io.github.pylonmc.rebar.util.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER
import io.github.pylonmc.rebar.util.damageItem
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import io.github.pylonmc.rebar.waila.WailaDisplay
import me.glomdom.gantry.Gantry
import me.glomdom.gantry.content.item.forms.RoughBaseForm
import me.glomdom.gantry.content.recipes.HydraulicFormingPressRecipe
import me.glomdom.gantry.utils.GantryGuiItems
import me.glomdom.gantry.utils.GantryUtils.anyStackIsNot
import net.kyori.adventure.text.format.TextColor
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
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
        createLogisticGroup("item-input", LogisticGroupType.INPUT, VirtualInventoryLogisticSlot(itemInputInventory, 0))
        createLogisticGroup("form-input", LogisticGroupType.INPUT, VirtualInventoryLogisticSlot(formInputOutputInventory, 0))
        createLogisticGroup("form-output", LogisticGroupType.OUTPUT, VirtualInventoryLogisticSlot(formInputOutputInventory, 0))
        createLogisticGroup("item-output", LogisticGroupType.OUTPUT, VirtualInventoryLogisticSlot(itemOutputInventory, 0))

        itemOutputInventory.addPreUpdateHandler(DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER)
        formInputOutputInventory.addPreUpdateHandler { event ->
            if (anyStackIsNot<RoughBaseForm>(event.newItem, event.previousItem)) {
                event.isCancelled = true
            }
        }

//        itemOutputInventory.addPostUpdateHandler { event -> tryStartRecipe(); }
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

    override fun tick() {
//        damageItem()
    }

    override fun onRecipeFinished(recipe: HydraulicFormingPressRecipe) {
        recipeProgressItem.setItem(GuiItems.background())

        damageItem(formInputOutputInventory.getItem(0)!!, 1, block.world)
        val form = formInputOutputInventory.getItem(0)!! as RoughBaseForm;
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

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "item-input" to itemInputInventory,
            "form-input-output" to formInputOutputInventory,
            "item-output" to itemOutputInventory,
        )
    }
}