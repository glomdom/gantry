package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.AddonDefinition
import me.glomdom.gantry.datagen.dsl.definitions.GuidePageDefinition
import me.glomdom.gantry.datagen.dsl.definitions.ItemDefinition

@AddonDsl
class AddonDefinitionBuilder {
    private var addon: String? = null
    private val guidePages = mutableListOf<GuidePageDefinition>()
    private val items = mutableListOf<ItemDefinition>()

    fun addon(value: String) {
        addon = value
    }

    fun guide(block: PagesBuilder.() -> Unit) {
        guidePages += PagesBuilder().apply(block).build()
    }

    fun items(block: ItemsBuilder.() -> Unit) {
        items += ItemsBuilder().apply(block).build()
    }

    fun build(): AddonDefinition {
        return AddonDefinition(
            name = requireNotNull(addon) { "Missing addon name" },
            guidePages = guidePages,
            items = items
        )
    }
}