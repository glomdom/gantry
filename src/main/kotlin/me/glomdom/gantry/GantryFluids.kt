package me.glomdom.gantry

import me.glomdom.gantry.registry.GantryRegistry
import me.glomdom.gantry.utils.gantryKey
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

object GantryFluids : GantryRegistry() {
    val RAW_SLUDGE by rebarFluid(gantryKey("raw_sludge"), TextColor.fromHexString("#303030")!!, Material.GRAY_CONCRETE)
}