package me.glomdom.gantry

import io.github.pylonmc.pylon.PylonPages
import io.github.pylonmc.rebar.item.RebarItem
import me.glomdom.gantry.content.machines.coal.CoalPoweredPress
import me.glomdom.gantry.registry.GantryRegistry
import org.bukkit.Material

object GantryItems : GantryRegistry() {
    val COAL_POWERED_PRESS by factoryItem<CoalPoweredPress.Item>(
        Material.FURNACE,
        GantryKeys.COAL_POWERED_PRESS,
        GantryPages.COAL_POWERED_MACHINES
    ) { asBlockItem() }

    val STEEL_PLATE by simpleRebarItem(Material.GRAY_CARPET, GantryKeys.STEEL_PLATE, PylonPages.COMPONENTS)

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
}