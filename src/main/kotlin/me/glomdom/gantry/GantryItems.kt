package me.glomdom.gantry

import io.github.pylonmc.pylon.PylonPages
import me.glomdom.gantry.content.machines.hydraulics.HydraulicFormingPress
import me.glomdom.gantry.content.item.forms.RoughBaseForm
import me.glomdom.gantry.content.machines.hydraulics.HydraulicDebarker
import me.glomdom.gantry.registry.GantryRegistry
import org.bukkit.Material

object GantryItems : GantryRegistry() {
    val ROUGH_PRESS_FORM by simpleRebarItem(Material.CLAY_BALL, GantryKeys.ROUGH_PRESS_FORM, GantryPages.PRESSING)
    val SPENT_ROUGH_FORM by simpleRebarItem(Material.CLAY_BALL, GantryKeys.SPENT_ROUGH_FORM, GantryPages.PRESSING)

    val ROUGH_ROLLED_STRIP_FORM by factoryItem<RoughBaseForm>(
        Material.GRAY_CARPET,
        GantryKeys.ROUGH_ROLLED_STRIP_FORM,
        GantryPages.PRESSING
    ) {
        durability(25)
    }

    val ROUGH_ROD_FORM by factoryItem<RoughBaseForm>(Material.GRAY_CARPET, GantryKeys.ROUGH_ROD_FORM, GantryPages.PRESSING) {
        durability(25)
    }

    val ROUGH_FASTENER_PACK_FORM by factoryItem<RoughBaseForm>(
        Material.GRAY_CARPET,
        GantryKeys.ROUGH_FASTENER_PACK_FORM,
        GantryPages.PRESSING
    ) {
        durability(25)
    }

    val HYDRAULIC_FORMING_PRESS by factoryItem<HydraulicFormingPress.Item>(
        Material.SMOOTH_STONE,
        GantryKeys.HYDRAULIC_FORMING_PRESS,
        PylonPages.HYDRAULIC_MACHINES
    ) {
        asBlockItem()
    }

    val HYDRAULIC_DEBARKER by factoryItem<HydraulicDebarker.Item>(
        Material.POLISHED_GRANITE,
        GantryKeys.HYDRAULIC_DEBARKER,
        PylonPages.HYDRAULIC_MACHINES
    ) {
        asBlockItem()
    }

    val ROUGH_IRON_ROD by simpleRebarItem(Material.COBBLED_DEEPSLATE_WALL, GantryKeys.ROUGH_IRON_ROD, PylonPages.COMPONENTS)
    val ROUGH_IRON_ROLLED_STRIP by simpleRebarItem(Material.IRON_CHAIN, GantryKeys.ROUGH_IRON_ROLLED_STRIP, PylonPages.COMPONENTS)
    val ROUGH_FASTENER_PACK by simpleRebarItem(Material.TRIPWIRE_HOOK, GantryKeys.ROUGH_FASTENER_PACK, PylonPages.COMPONENTS)

    val IRON_SCRAP by simpleRebarItem(Material.IRON_NUGGET, GantryKeys.IRON_SCRAP, PylonPages.RESOURCES)
    val BARK by simpleRebarItem(Material.OAK_BUTTON, GantryKeys.BARK, PylonPages.RESOURCES)

    val CRUDE_IRON_FRAME_PLATE by simpleRebarItem(Material.COBBLED_DEEPSLATE_SLAB, GantryKeys.CRUDE_IRON_FRAME_PLATE, PylonPages.COMPONENTS)

    val FIRE_BRICK by simpleRebarItem(Material.BRICK, GantryKeys.FIRE_BRICK, PylonPages.COMPONENTS)
    val FIRE_BRICKS by simpleRebarItem(Material.BRICKS, GantryKeys.FIRE_BRICKS, PylonPages.COMPONENTS)
    val PYROLYSIS_OVEN_CONTROLLER by simpleRebarItem(Material.FURNACE, GantryKeys.PYROLYSIS_OVEN_CONTROLLER, PylonPages.MACHINES)
}