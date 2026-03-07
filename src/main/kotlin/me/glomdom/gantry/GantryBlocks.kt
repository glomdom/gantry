package me.glomdom.gantry

import me.glomdom.gantry.content.machines.hydraulics.HydraulicFormingPress
import me.glomdom.gantry.utils.GantryUtils.gantryBlock
import org.bukkit.Material

object GantryBlocks {
    fun registerAll() {
        gantryBlock<HydraulicFormingPress>(Material.SMOOTH_STONE, GantryKeys.HYDRAULIC_FORMING_PRESS)
    }
}