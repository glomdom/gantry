package me.glomdom.gantry.content.item

import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

class GantryItemFactory private constructor(
    private val key: NamespacedKey,
    private val page: SimpleStaticGuidePage,
    private val material: Material? = null,
    private val customStack: ItemStack? = null,
) {
    private var durability: Int? = null
    private var blockItem: Boolean = false

    fun durability(value: Int) = apply {
        durability = value
    }

    fun asBlockItem() = apply {
        blockItem = true
    }

    fun build(): ItemStack {
        // Use the custom stack directly if one was supplied.
        customStack?.let { return it }

        val builder = ItemStackBuilder.rebar(material!!, key)
        durability?.let(builder::durability)

        return builder.build()
    }

    fun isBlockItem(): Boolean = blockItem

    companion object {
        fun create(
            material: Material,
            key: NamespacedKey,
            page: SimpleStaticGuidePage,
        ) = GantryItemFactory(key, page, material = material)

        fun of(
            stack: ItemStack,
            key: NamespacedKey,
            page: SimpleStaticGuidePage,
        ) = GantryItemFactory(key, page, customStack = stack)
    }
}