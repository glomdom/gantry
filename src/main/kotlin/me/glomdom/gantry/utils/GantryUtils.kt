package me.glomdom.gantry.utils

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

    inline fun <reified TItem : RebarItem> gantryItem(mat: Material, key: NamespacedKey, page: SimpleStaticGuidePage): ItemStack {
        return ItemStackBuilder.rebar(mat, key).build().also {
            RebarItem.register(TItem::class.java, it)
            page.addItem(it)
        }
    }
}