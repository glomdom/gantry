package me.glomdom.gantry.utils.extensions

import io.github.pylonmc.rebar.util.gui.unit.UnitFormat
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor

val UnitFormat.Companion.FUEL: UnitFormat
    get() = UnitFormat(
        name = "fuel_ticks",
        singular = Component.translatable("gantry.unit.fuel_ticks.singular"),
        plural = Component.translatable("gantry.unit.fuel_ticks.plural"),
        defaultStyle = Style.style(TextColor.color(0xffba19))
    )