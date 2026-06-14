package me.glomdom.gantry.utils

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.i18n.RebarArgument.Companion.of
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import me.glomdom.gantry.Gantry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent
import xyz.xenondevs.invui.inventory.event.PlayerUpdateReason
import java.util.function.Consumer
import kotlin.math.roundToInt

fun gantryKey(key: String): NamespacedKey {
    return NamespacedKey(Gantry.instance, key)
}

inline fun <reified TBlock : RebarBlock> gantryBlock(material: Material, key: NamespacedKey) {
    RebarBlock.register(key, material, TBlock::class.java)
}

inline fun <reified TItem : RebarItem> anyStackIsNot(vararg stacks: ItemStack?): Boolean {
    return stacks.any { stack ->
        if (stack == null || stack.type.isAir) return@any false

        val rebarItem = RebarItem.from<TItem>(stack)
        rebarItem == null
    }
}

inline fun <reified TItem : RebarItem> anyStackIs(vararg stacks: ItemStack?): Boolean {
    return stacks.any { stack ->
        if (stack == null || stack.type.isAir) return@any false

        val rebarItem = RebarItem.from<TItem>(stack)
        rebarItem != null
    }
}

val DISALLOW_NON_FUEL_ITEMS_TO_BE_ADDED = Consumer<ItemPreUpdateEvent> { event: ItemPreUpdateEvent ->
    if (!event.isRemove && event.updateReason is PlayerUpdateReason) {
        val itemType = event.newItem?.type?.asItemType()

        if (itemType?.isFuel != true) {
            event.isCancelled = true
        }
    }
}