package me.glomdom.gantry

import me.glomdom.gantry.content.machines.coal.CoalPoweredPress
import me.glomdom.gantry.registry.GantryRegistry
import org.bukkit.Material

object GantryBlocks : GantryRegistry() {
    val FIRE_BRICKS by simpleRebarBlock(Material.BRICKS,GantryKeys.FIRE_BRICKS)
    val COAL_POWERED_PRESS by rebarBlock<CoalPoweredPress>(Material.FURNACE, GantryKeys.COAL_POWERED_PRESS)
}