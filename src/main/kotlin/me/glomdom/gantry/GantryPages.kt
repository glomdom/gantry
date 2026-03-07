package me.glomdom.gantry

import io.github.pylonmc.rebar.content.guide.RebarGuide
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import me.glomdom.gantry.utils.GantryUtils.gantryKey

object GantryPages {
    val PRESSING = SimpleStaticGuidePage(gantryKey("pressing"))

    fun initialize() {
        RebarGuide.rootPage.addPage(GantryItems.ROUGH_PRESS_FORM, PRESSING)
    }
}