package me.glomdom.gantry

import io.github.pylonmc.pylon.PylonPages
import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.content.machines.hydraulics.HydraulicFormingPress
import me.glomdom.gantry.content.item.GantryItemFactory
import me.glomdom.gantry.content.item.forms.RoughBaseForm
import me.glomdom.gantry.content.item.forms.RoughRolledStripForm
import me.glomdom.gantry.utils.GantryUtils.gantryItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GantryItems {
    lateinit var ROUGH_PRESS_FORM: ItemStack private set
    lateinit var SPENT_ROUGH_FORM: ItemStack private set

    lateinit var ROUGH_ROLLED_STRIP_FORM: ItemStack private set
    lateinit var ROUGH_ROD_FORM: ItemStack private set
    lateinit var ROUGH_FASTENER_PACK_FORM: ItemStack private set

    lateinit var HYDRAULIC_FORMING_PRESS: ItemStack private set

    lateinit var ROUGH_IRON_ROD: ItemStack private set
    lateinit var ROUGH_IRON_ROLLED_STRIP: ItemStack private set
    lateinit var ROUGH_FASTENER_PACK: ItemStack private set

    lateinit var IRON_SCRAP: ItemStack private set

    lateinit var CRUDE_IRON_FRAME_PLATE: ItemStack private set

    fun registerAll() {
        ROUGH_PRESS_FORM =
            GantryItemFactory.create(Material.CLAY_BALL, GantryKeys.ROUGH_PRESS_FORM, GantryPages.PRESSING)
                .build(RebarItem::class.java)

        SPENT_ROUGH_FORM =
            GantryItemFactory.create(Material.CLAY_BALL, GantryKeys.SPENT_ROUGH_FORM, GantryPages.PRESSING)
                .build(RebarItem::class.java)

        ROUGH_ROLLED_STRIP_FORM =
            GantryItemFactory.create(Material.GRAY_CARPET, GantryKeys.ROUGH_ROLLED_STRIP_FORM, GantryPages.PRESSING)
                .durability(25)
                .build(RoughBaseForm::class.java)

        ROUGH_ROD_FORM =
            GantryItemFactory.create(Material.GRAY_CARPET, GantryKeys.ROUGH_ROD_FORM, GantryPages.PRESSING)
                .durability(25)
                .build(RoughBaseForm::class.java)

        ROUGH_FASTENER_PACK_FORM =
            GantryItemFactory.create(Material.GRAY_CARPET, GantryKeys.ROUGH_FASTENER_PACK_FORM, GantryPages.PRESSING)
                .durability(25)
                .build(RoughBaseForm::class.java)

        ROUGH_IRON_ROD = gantryItem<RebarItem>(Material.COBBLED_DEEPSLATE_WALL, GantryKeys.ROUGH_IRON_ROD, PylonPages.COMPONENTS)
        ROUGH_IRON_ROLLED_STRIP = gantryItem<RebarItem>(Material.IRON_CHAIN, GantryKeys.ROUGH_IRON_ROLLED_STRIP, PylonPages.COMPONENTS)
        ROUGH_FASTENER_PACK = gantryItem<RebarItem>(Material.TRIPWIRE_HOOK, GantryKeys.ROUGH_FASTENER_PACK, PylonPages.COMPONENTS)

        CRUDE_IRON_FRAME_PLATE = gantryItem<RebarItem>(Material.COBBLED_DEEPSLATE_SLAB, GantryKeys.CRUDE_IRON_FRAME_PLATE, PylonPages.COMPONENTS)

        IRON_SCRAP = gantryItem<RebarItem>(Material.IRON_NUGGET, GantryKeys.IRON_SCRAP, PylonPages.RESOURCES)

        HYDRAULIC_FORMING_PRESS =
            GantryItemFactory.create(Material.SMOOTH_STONE, GantryKeys.HYDRAULIC_FORMING_PRESS, PylonPages.HYDRAULIC_MACHINES)
                .asBlockItem()
                .build(HydraulicFormingPress.Item::class.java)
    }
}