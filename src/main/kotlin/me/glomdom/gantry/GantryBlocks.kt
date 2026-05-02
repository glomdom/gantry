package me.glomdom.gantry

import me.glomdom.gantry.content.machines.hydraulics.HydraulicDebarker
import me.glomdom.gantry.content.machines.hydraulics.HydraulicFormingPress
import me.glomdom.gantry.registry.GantryRegistry
import org.bukkit.Material

object GantryBlocks : GantryRegistry() {
    val HYDRAULIC_FORMING_PRESS by rebarBlock<HydraulicFormingPress>(Material.SMOOTH_STONE, GantryKeys.HYDRAULIC_FORMING_PRESS)
    val HYDRAULIC_DEBARKER by rebarBlock<HydraulicDebarker>(Material.POLISHED_GRANITE, GantryKeys.HYDRAULIC_DEBARKER)
    val FIRE_BRICKS by simpleRebarBlock(Material.BRICKS,GantryKeys.FIRE_BRICKS)
}