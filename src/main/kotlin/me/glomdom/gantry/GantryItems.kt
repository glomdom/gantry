package me.glomdom.gantry

import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.utils.GantryUtils.gantryItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GantryItems {
    lateinit var ROUGH_PRESS_FORM: ItemStack private set
    lateinit var ROUGH_ROLLED_STRIP_FORM: ItemStack private set
    lateinit var ROUGH_ROD_FORM: ItemStack private set
    lateinit var ROUGH_FASTENER_PACK_FORM: ItemStack private set

    fun registerAll() {
        ROUGH_PRESS_FORM = gantryItem<RebarItem>(Material.CLAY_BALL, GantryKeys.ROUGH_PRESS_FORM, GantryPages.PRESSING)
        ROUGH_ROLLED_STRIP_FORM = gantryItem<RebarItem>(Material.GRAY_CARPET, GantryKeys.ROUGH_ROLLED_STRIP_FORM, GantryPages.PRESSING)
        ROUGH_ROD_FORM = gantryItem<RebarItem>(Material.GRAY_CARPET, GantryKeys.ROUGH_ROD_FORM, GantryPages.PRESSING)
        ROUGH_FASTENER_PACK_FORM = gantryItem<RebarItem>(Material.GRAY_CARPET, GantryKeys.ROUGH_FASTENER_PACK_FORM, GantryPages.PRESSING)
    }
}