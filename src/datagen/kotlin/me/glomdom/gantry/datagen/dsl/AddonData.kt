package me.glomdom.gantry.datagen.dsl

import me.glomdom.gantry.datagen.dsl.builders.AddonModelBuilder
import me.glomdom.gantry.datagen.dsl.definitions.AddonDefinition

fun addonData(block: AddonModelBuilder.() -> Unit): AddonDefinition {
    return AddonModelBuilder().apply(block).build()
}