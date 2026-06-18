package me.glomdom.gantry

import io.github.pylonmc.pylon.PylonPages
import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.content.machines.coal.CoalPoweredPress
import me.glomdom.gantry.content.machines.sludge.DeepSludgePump
import me.glomdom.gantry.registry.GantryRegistry
import org.bukkit.Material

object GantryItems : GantryRegistry() {
    val STEEL_PLATE by simpleRebarItem(Material.GRAY_CARPET, GantryKeys.STEEL_PLATE, PylonPages.COMPONENTS)
    val STEEL_CHAIN by simpleRebarBlockItem(Material.IRON_CHAIN, GantryKeys.STEEL_CHAIN, PylonPages.COMPONENTS)

    val FIRE_BRICK by simpleRebarItem(Material.BRICK, GantryKeys.FIRE_BRICK, PylonPages.COMPONENTS)
    val FIRE_BRICKS by factoryItem<RebarItem>(
        Material.BRICKS,
        GantryKeys.FIRE_BRICKS,
        PylonPages.COMPONENTS
    ) { asBlockItem() }

    val PYROLYSIS_OVEN_CONTROLLER by factoryItem<RebarItem>(
        Material.FURNACE,
        GantryKeys.PYROLYSIS_OVEN_CONTROLLER,
        PylonPages.MACHINES
    ) { asBlockItem() }

    val RAW_ZINC by simpleRebarItem(Material.RAW_IRON, GantryKeys.RAW_ZINC, PylonPages.METALS)
    val RAW_NICKEL by simpleRebarItem(Material.RAW_IRON, GantryKeys.RAW_NICKEL, PylonPages.METALS)
    val RAW_INVAR by simpleRebarItem(Material.RAW_IRON, GantryKeys.RAW_INVAR, PylonPages.METALS)

    val COAL_POWERED_PRESS by factoryItem<CoalPoweredPress.Item>(
        Material.FURNACE,
        GantryKeys.COAL_POWERED_PRESS,
        GantryPages.COAL_POWERED_MACHINES
    ) { asBlockItem() }

    val DEEP_SLUDGE_PUMP by factoryItem<DeepSludgePump.Item>(
        Material.NETHERITE_BLOCK,
        GantryKeys.DEEP_SLUDGE_PUMP,
        GantryPages.DEEP_SLUDGE_PROCESSING
    ) { asBlockItem() }
}