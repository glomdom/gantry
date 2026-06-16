package me.glomdom.gantry.block.interfaces

import io.github.pylonmc.rebar.datatypes.RebarSerializers
import io.github.pylonmc.rebar.event.RebarBlockDeserializeEvent
import io.github.pylonmc.rebar.event.RebarBlockSerializeEvent
import io.github.pylonmc.rebar.event.RebarBlockUnloadEvent
import io.github.pylonmc.rebar.util.MachineUpdateReason
import me.glomdom.gantry.utils.gantryKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.jetbrains.annotations.ApiStatus
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.inventory.get
import java.util.IdentityHashMap

interface CoalPoweredMachine {
    val fuelInventory: VirtualInventory
    val fuelBuffer: Double

    var fuelLeft: Double
        get() = coalPoweredBlocks[this] ?: 0.0
        set(value) {
            coalPoweredBlocks[this] = value
        }

    fun tryConsumeFuel(): Boolean {
        val item = fuelInventory[0] ?: return false
        val itemType = item.type.asItemType() ?: return false
        if (!itemType.isFuel) return false

        val burnDuration = itemType.burnDuration.toDouble()
        val spaceLeftInBuffer = fuelBuffer - fuelLeft
        val maxItemsThatFit = (spaceLeftInBuffer / burnDuration).toInt()
        val itemsToConsume = minOf(maxItemsThatFit, item.amount)

        if (itemsToConsume <= 0) return false

        fuelLeft += burnDuration * itemsToConsume
        fuelInventory.setItem(MachineUpdateReason(), 0, item.subtract(itemsToConsume))

        return true
    }

    @ApiStatus.Internal
    companion object : Listener {
        private val FUEL_LEFT_KEY = gantryKey("fuel_left")

        private val coalPoweredBlocks = IdentityHashMap<CoalPoweredMachine, Double>()

        @EventHandler
        private fun onDeserialize(event: RebarBlockDeserializeEvent) {
            val block = event.rebarBlock
            if (block is CoalPoweredMachine) {
                event.pdc.get(FUEL_LEFT_KEY, RebarSerializers.DOUBLE)?.let {
                    coalPoweredBlocks[block] = it
                }
            }
        }

        @EventHandler
        private fun onSerialize(event: RebarBlockSerializeEvent) {
            val block = event.rebarBlock
            if (block is CoalPoweredMachine) {
                event.pdc.set(FUEL_LEFT_KEY, RebarSerializers.DOUBLE, coalPoweredBlocks[block] ?: 0.0)
            }
        }

        @EventHandler
        private fun onUnload(event: RebarBlockUnloadEvent) {
            val block = event.rebarBlock
            if (block is CoalPoweredMachine) {
                coalPoweredBlocks.remove(block)
            }
        }
    }
}