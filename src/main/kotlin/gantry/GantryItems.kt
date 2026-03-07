package me.glomdom.gantry

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object GantryItems {
    lateinit var ROUGH_PRESS_FORM: ItemStack
        private set

    fun registerAll() {
        ROUGH_PRESS_FORM = gantryItem<RebarItem>(Material.CLAY_BALL, GantryKeys.ROUGH_PRESS_FORM)
    }
}

inline fun <reified T : RebarItem> gantryItem(mat: Material, key: NamespacedKey): ItemStack {
    return ItemStackBuilder.rebar(mat, key).build().also {
        RebarItem.register(T::class.java, it)
    }
}