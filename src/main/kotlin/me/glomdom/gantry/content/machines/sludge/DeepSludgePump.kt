package me.glomdom.gantry.content.machines.sludge

import io.github.pylonmc.pylon.PylonFluids
import io.github.pylonmc.pylon.PylonKeys
import io.github.pylonmc.pylon.content.components.FluidInputHatch
import io.github.pylonmc.pylon.content.components.FluidOutputHatch
import io.github.pylonmc.pylon.content.components.ItemInputHatch
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.block.interfaces.DirectionalRebarBlock
import io.github.pylonmc.rebar.block.interfaces.ProcessorRebarBlock
import io.github.pylonmc.rebar.block.interfaces.SimpleRebarMultiblock
import io.github.pylonmc.rebar.block.interfaces.TickingRebarBlock
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.GantryFluids
import me.glomdom.gantry.GantryKeys
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i

class DeepSludgePump : RebarBlock, DirectionalRebarBlock, SimpleRebarMultiblock,
    TickingRebarBlock {

    val tickingInterval = getSettingOrThrow("tick-interval", ConfigAdapter.INTEGER)
    val dieselPerTick = getSettingOrThrow("diesel-per-tick", ConfigAdapter.DOUBLE)
    val waterPerTick = getSettingOrThrow("water-per-tick", ConfigAdapter.DOUBLE)
    val sludgePerTick = getSettingOrThrow("sludge-per-tick", ConfigAdapter.DOUBLE)

    val WATER_INPUT_HATCH = Vector3i(1, -3, -1)
    val DIESEL_INPUT_HATCH = Vector3i(-1, -3, -1)
    val DEEP_SLUDGE_OUTPUT = Vector3i(0, -3, -2)

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        facing = context.facing
        setMultiblockDirection(context.facing)
        setTickInterval(tickingInterval)
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    class Item(stack: ItemStack) : RebarItem(stack)

    override fun onMultiblockFormed() {
        super.onMultiblockFormed()

        getMultiblockComponentOrThrow(FluidInputHatch::class.java, WATER_INPUT_HATCH).setFluidType(PylonFluids.WATER)
        getMultiblockComponentOrThrow(
            FluidInputHatch::class.java,
            DIESEL_INPUT_HATCH
        ).setFluidType(PylonFluids.BIODIESEL)

        getMultiblockComponentOrThrow(
            FluidOutputHatch::class.java,
            DEEP_SLUDGE_OUTPUT
        ).setFluidType(GantryFluids.DEEP_SLUDGE)
    }

    override val components: Map<Vector3i, SimpleRebarMultiblock.MultiblockComponent>
        get() = buildMap {
            put(Vector3i(1, -3, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, -3, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_BLOCK))
            put(Vector3i(-1, -3, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))

            put(Vector3i(1, -3, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_BLOCK))
            put(Vector3i(0, -3, 0), SimpleRebarMultiblock.MultiblockComponent.of(GantryKeys.STEEL_CHAIN))
            put(Vector3i(-1, -3, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_BLOCK))

            put(WATER_INPUT_HATCH, SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.FLUID_INPUT_HATCH))
            put(Vector3i(0, -3, -1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_BLOCK))
            put(DIESEL_INPUT_HATCH, SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.FLUID_INPUT_HATCH))

            put(DEEP_SLUDGE_OUTPUT, SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.FLUID_OUTPUT_HATCH))

            put(Vector3i(0, -2, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(1, -2, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, -2, 0), SimpleRebarMultiblock.MultiblockComponent.of(GantryKeys.STEEL_CHAIN))
            put(Vector3i(-1, -2, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, -2, -1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))

            put(Vector3i(0, -1, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(1, -1, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, -1, 0), SimpleRebarMultiblock.MultiblockComponent.of(GantryKeys.STEEL_CHAIN))
            put(Vector3i(-1, -1, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, -1, -1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))

            put(Vector3i(0, 0, 1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(1, 0, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(-1, 0, 0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
            put(Vector3i(0, 0, -1), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.STEEL_SUPPORT_BEAM))
        }

    override fun tick() {
        if (!isFormedAndFullyLoaded()) return

        val waterInputHatch = getMultiblockComponentOrThrow(FluidInputHatch::class.java, WATER_INPUT_HATCH)
        val dieselInputHatch = getMultiblockComponentOrThrow(FluidInputHatch::class.java, DIESEL_INPUT_HATCH)
        val sludgeOutputHatch = getMultiblockComponentOrThrow(FluidOutputHatch::class.java, DEEP_SLUDGE_OUTPUT)

        val waterToTake = waterPerTick * tickingInterval
        val dieselToTake = dieselPerTick * tickingInterval
        val sludgeToGive = sludgePerTick * tickingInterval

        if (
            waterInputHatch.fluidAmount(PylonFluids.WATER) < waterToTake ||
            dieselInputHatch.fluidAmount(PylonFluids.BIODIESEL) < dieselToTake ||
            sludgeOutputHatch.fluidCapacity(GantryFluids.DEEP_SLUDGE) < sludgeOutputHatch.fluidAmount(GantryFluids.DEEP_SLUDGE) + sludgeToGive
        ) {
            return
        }

        sludgeOutputHatch.addFluid(sludgeToGive)
        waterInputHatch.removeFluid(waterToTake)
        dieselInputHatch.removeFluid(dieselToTake)
    }
}