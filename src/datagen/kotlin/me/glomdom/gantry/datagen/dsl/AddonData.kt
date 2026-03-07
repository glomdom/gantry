package me.glomdom.gantry.datagen.dsl

import me.glomdom.gantry.datagen.dsl.builders.AddonModelBuilder

fun addonData(block: AddonModelBuilder.() -> Unit): AddonModel {
    return AddonModelBuilder().apply(block).build()
}