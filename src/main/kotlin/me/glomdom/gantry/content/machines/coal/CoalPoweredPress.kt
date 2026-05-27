package me.glomdom.gantry.content.machines.coal

import io.github.pylonmc.pylon.util.PylonUtils
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock
import io.github.pylonmc.rebar.block.base.RebarInventoryBlock
import io.github.pylonmc.rebar.block.base.RebarTickingBlock
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.util.DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER
import io.github.pylonmc.rebar.util.MachineUpdateReason
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.waila.WailaDisplay
import me.glomdom.gantry.utils.GantryUtils
import me.glomdom.gantry.utils.GantryUtils.gantryKey
import me.glomdom.gantry.utils.extensions.fuelInput
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.get

class CoalPoweredPress :
    RebarBlock,
    RebarDirectionalBlock,
    RebarInventoryBlock,
    RebarTickingBlock,
    RebarVirtualInventoryBlock {

    private val inputInventory = VirtualInventory(1)
    private val fuelInventory = VirtualInventory(1)
    private val outputInventory = VirtualInventory(1)

    private var fuelLeft: Double
    private var fuelMax: Double

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        facing = context.facing
        setTickInterval(5)

        fuelLeft = 0.0
        fuelMax = 0.0
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc) {
        fuelLeft = pdc.get(FUEL_LEFT_KEY, RebarSerializers.DOUBLE)
            ?: throw IllegalStateException("Coal-Powered Press cannot get data from pdc for key $FUEL_LEFT_KEY")

        fuelMax = pdc.get(FUEL_MAX_KEY, RebarSerializers.DOUBLE)
            ?: throw IllegalStateException("Coal-Powered Press cannot get data from pdc for key $FUEL_MAX_KEY")
    }

    class Item(stack: ItemStack) : RebarItem(stack)

    override fun write(pdc: PersistentDataContainer) {
        pdc.set(FUEL_LEFT_KEY, RebarSerializers.DOUBLE, fuelLeft)
    }

    override fun postInitialise() {
        outputInventory.addPreUpdateHandler(DISALLOW_PLAYERS_FROM_ADDING_ITEMS_HANDLER)
        outputInventory.addPostUpdateHandler { _ -> tryStartRecipe() }

        inputInventory.addPostUpdateHandler { event ->
            if (event.updateReason !is MachineUpdateReason) {
                tryStartRecipe()
            }
        }

        fuelInventory.addPostUpdateHandler { event ->
            if (event.updateReason !is MachineUpdateReason) {
                tryAddFuel()
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
            .addIngredient('O', GuiItems.output())
            .addIngredient('o', outputInventory)
            .build()
    }

    override fun tick() {}

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        super<RebarInventoryBlock>.onBreak(drops, context)
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "input" to inputInventory,
            "fuel" to fuelInventory,
            "output" to outputInventory,
        )
    }

    override fun getWaila(player: Player): WailaDisplay {
        return WailaDisplay(
            defaultWailaTranslationKey.arguments(
                RebarArgument.of(
                    "fuel-left",
                    if (fuelLeft == 0.0) Component.text("No fuel") else PylonUtils.createProgressBar(
                        fuelLeft,
                        fuelMax,
                        20,
                        TextColor.fromHexString("#ffba19")
                    )
                )
            )
        )
    }

    private fun tryStartRecipe() {}
    private fun tryAddFuel() {}

    companion object {
        val FUEL_LEFT_KEY = gantryKey("fuel_left")
        val FUEL_MAX_KEY = gantryKey("fuel_max")
    }
}