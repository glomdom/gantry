package me.glomdom.gantry.utils.extensions

import io.github.pylonmc.rebar.util.ProgressBar
import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextColor

fun ProgressBar.Companion.fuelRemainingTicks(total: Int, remaining: Int): ComponentLike {
    return ProgressBar()
        .barColor(TextColor.fromHexString("#e4b09f")!!)
        .proportion(remaining.toDouble() / total.toDouble())
        .suffix(
            Component.text(" ")
                .append(UnitFormat.FUEL.format(remaining))
        )
}
