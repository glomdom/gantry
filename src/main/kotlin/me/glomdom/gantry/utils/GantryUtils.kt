package me.glomdom.gantry.utils

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.Gantry
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object GantryUtils {
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
}