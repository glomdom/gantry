package me.glomdom.gantry.item

import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

class GantryItemFactory private constructor(
    private val material: Material,
    private val key: NamespacedKey,
    private val page: SimpleStaticGuidePage,
) {
    private var durability: Int? = null
    private var blockItem: Boolean = false

    fun durability(value: Int) = apply {
        durability = value
    }

    fun asBlockItem() = apply {
        blockItem = true
    }

    fun <TItem : RebarItem> build(itemClass: Class<TItem>): ItemStack {
        val builder = ItemStackBuilder.rebar(material, key)
        durability?.let(builder::durability)

        val item = builder.build()

        if (blockItem) {
            RebarItem.register(itemClass, item, key)
        } else {
            RebarItem.register(itemClass, item)
        }

        page.addItem(item)

        return item
    }

    companion object {
        fun create(
            material: Material,
            key: NamespacedKey,
            page: SimpleStaticGuidePage,
        ) = GantryItemFactory(material, key, page)
    }
}