package me.glomdom.gantry.datagen.dsl

import me.glomdom.gantry.datagen.dsl.builders.AddonDefinitionBuilder
import me.glomdom.gantry.datagen.dsl.definitions.AddonDefinition

fun addonData(block: AddonDefinitionBuilder.() -> Unit): AddonDefinition {
    return AddonDefinitionBuilder().apply(block).build()
}