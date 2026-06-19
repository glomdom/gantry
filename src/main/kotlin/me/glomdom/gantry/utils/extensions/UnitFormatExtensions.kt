package me.glomdom.gantry.utils.extensions

import io.github.pylonmc.rebar.util.gui.unit.MetricPrefix
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

val UnitFormat.Companion.MILLIBUCKETS_PER_TICK: UnitFormat
    get() = UnitFormat(
        "buckets_per_tick",
        singular = Component.translatable("gantry.unit.buckets_per_tick.singular"),
        plural = Component.translatable("gantry.unit.buckets_per_tick.plural"),
        abbreviation = Component.translatable("gantry.unit.buckets_per_tick.abbr"),
        defaultStyle = Style.style(TextColor.color(0xe3835f2)),
        defaultPrefix = MetricPrefix.MILLI
    )
