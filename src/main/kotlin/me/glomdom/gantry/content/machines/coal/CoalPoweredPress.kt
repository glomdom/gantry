package me.glomdom.gantry.content.machines.coal

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock
import io.github.pylonmc.rebar.block.base.RebarInventoryBlock
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.waila.WailaDisplay
import me.glomdom.gantry.utils.extensions.fuelInput
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
    RebarVirtualInventoryBlock {

    private val inputInventory = VirtualInventory(1)
    private val fuelInventory = VirtualInventory(1)
    private val outputInventory = VirtualInventory(1)

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        facing = context.facing
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    class Item(stack: ItemStack) : RebarItem(stack)

    override fun getVirtualInventories(): Map<String, VirtualInventory> {
        return mapOf(
            "input" to inputInventory,
            "fuel" to fuelInventory,
            "output" to outputInventory,
        )
    }

    override fun getWaila(player: Player): WailaDisplay {
        return WailaDisplay(defaultWailaTranslationKey)
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

    override fun onBreak(drops: MutableList<ItemStack>, context: BlockBreakContext) {
        super<RebarInventoryBlock>.onBreak(drops, context)
    }
}