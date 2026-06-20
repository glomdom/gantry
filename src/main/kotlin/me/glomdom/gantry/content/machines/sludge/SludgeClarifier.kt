package me.glomdom.gantry.content.machines.sludge

import io.github.pylonmc.pylon.PylonKeys
import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.block.interfaces.DirectionalRebarBlock
import io.github.pylonmc.rebar.block.interfaces.SimpleRebarMultiblock
import io.github.pylonmc.rebar.block.interfaces.TickingRebarBlock
import io.github.pylonmc.rebar.config.adapter.ConfigAdapter
import io.github.pylonmc.rebar.item.RebarItem
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.joml.Vector3i

class SludgeClarifier : RebarBlock, DirectionalRebarBlock, SimpleRebarMultiblock,
    TickingRebarBlock {

    private val tickingInterval = getSettingOrThrow("tick-interval", ConfigAdapter.INTEGER)

    @Suppress("Unused")
    constructor(block: Block, context: BlockCreateContext) : super(block, context) {
        facing = context.facing
        setTickInterval(tickingInterval)
        setMultiblockDirection(context.facing)
    }

    @Suppress("Unused")
    constructor(block: Block, pdc: PersistentDataContainer) : super(block, pdc)

    class Item(stack: ItemStack) : RebarItem(stack)

    override val components: Map<Vector3i, SimpleRebarMultiblock.MultiblockComponent>
        get() = buildMap {
            put(Vector3i(0, 1,  0), SimpleRebarMultiblock.MultiblockComponent.of(PylonKeys.REINFORCED_GLASS))
        }

    override fun tick() {
        if (!isFormedAndFullyLoaded()) return
    }
}