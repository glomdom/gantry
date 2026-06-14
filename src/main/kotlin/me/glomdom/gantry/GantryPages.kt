package me.glomdom.gantry

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import me.glomdom.gantry.utils.gantryKey

object GantryPages {
    val COAL_POWERED_MACHINES = SimpleStaticGuidePage(gantryKey("coal_powered_machines"))

    fun initialize() {
        RebarGuide.rootPage.addPage(GantryItems.COAL_POWERED_PRESS, COAL_POWERED_MACHINES)
    }
}