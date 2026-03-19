package me.glomdom.gantry.utils

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import me.glomdom.gantry.Gantry
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object GantryUtils {
    fun gantryKey(key: String): NamespacedKey {
        return NamespacedKey(Gantry.instance, key)
    }

    inline fun <reified TItem : RebarItem> gantryItem(material: Material, key: NamespacedKey, page: SimpleStaticGuidePage): ItemStack {
        return ItemStackBuilder.rebar(material, key).build().also {
            RebarItem.register(TItem::class.java, it)
            page.addItem(it)
        }
    }

    inline fun <reified TItem : RebarItem> gantryItemWithDurability(
        material: Material,
        key: NamespacedKey,
        page: SimpleStaticGuidePage,
        durability: Int
    ): ItemStack {
        return ItemStackBuilder.rebar(material, key).durability(durability).build().also {
            RebarItem.register(TItem::class.java, it)
            page.addItem(it)
        }
    }

    inline fun <reified TItem : RebarItem> gantryBlockItem(material: Material, key: NamespacedKey, page: SimpleStaticGuidePage): ItemStack {
        return ItemStackBuilder.rebar(material, key).build().also {
            RebarItem.register(TItem::class.java, it, key)
            page.addItem(it)
        }
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
}