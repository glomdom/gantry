package me.glomdom.gantry

import io.github.pylonmc.pylon.PylonPages
import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import me.glomdom.gantry.utils.gantryKey
import org.bukkit.Material

object GantryPages {
    val COAL_POWERED_MACHINES = SimpleStaticGuidePage(gantryKey("coal_powered_machines"))
    val DEEP_SLUDGE_PROCESSING = SimpleStaticGuidePage(gantryKey("deep_sludge_processing"))

    fun initialize() {
        RebarGuide.rootPage.addPage(GantryItems.COAL_POWERED_PRESS, COAL_POWERED_MACHINES)
        PylonPages.MACHINES.addPage(Material.GRAY_CONCRETE, DEEP_SLUDGE_PROCESSING)
    }
}