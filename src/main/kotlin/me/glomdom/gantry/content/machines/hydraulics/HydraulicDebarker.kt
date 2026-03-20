package me.glomdom.gantry.content.machines.hydraulics

import io.github.pylonmc.pylon.PylonFluids
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.RebarDirectionalBlock
import io.github.pylonmc.rebar.block.base.RebarFluidBufferBlock
import io.github.pylonmc.rebar.block.base.RebarGuiBlock
import io.github.pylonmc.rebar.block.base.RebarLogisticBlock
import io.github.pylonmc.rebar.block.base.RebarTickingBlock
import io.github.pylonmc.rebar.block.base.RebarVirtualInventoryBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.fluid.FluidPointType
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.persistence.PersistentDataContainer

class HydraulicDebarker :
    RebarBlock,
    RebarDirectionalBlock,
    RebarFluidBufferBlock,
    RebarTickingBlock,
    RebarGuiBlock,
    RebarVirtualInventoryBlock,
    RebarLogisticBlock {

    val tickingInterval = getSettings().getOrThrow("tick-interval", ConfigAdapter.INTEGER)
    val fluidBufferAmount = getSettings().getOrThrow("buffer-amount", ConfigAdapter.DOUBLE)

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        setTickInterval(tickingInterval)

        createFluidBuffer(PylonFluids.HYDRAULIC_FLUID, fluidBufferAmount, input = true, output = false)
        createFluidBuffer(PylonFluids.DIRTY_HYDRAULIC_FLUID, fluidBufferAmount, input = false, output = true)

        createFluidPoint(FluidPointType.INPUT, BlockFace.EAST, context, false)
        createFluidPoint(FluidPointType.OUTPUT, BlockFace.WEST, context, false)

        facing = context.facing
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)
}