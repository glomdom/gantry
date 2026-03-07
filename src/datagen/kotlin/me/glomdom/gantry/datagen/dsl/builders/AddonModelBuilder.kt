package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.AddonModel
import me.glomdom.gantry.datagen.dsl.GuidePageDefinition
import me.glomdom.gantry.datagen.dsl.ItemDefinition

@AddonDsl
class AddonModelBuilder {
    private var addon: String? = null
    private val guidePages = mutableListOf<GuidePageDefinition>()
    private val items = mutableListOf<ItemDefinition>()

    fun addon(value: String) {
        addon = value
    }

    fun guide(block: GuideBuilder.() -> Unit) {
        guidePages += GuideBuilder().apply(block).build()
    }

    fun items(block: ItemsBuilder.() -> Unit) {
        items += ItemsBuilder().apply(block).build()
    }

    fun build(): AddonModel {
        return AddonModel(
            addon = requireNotNull(addon) { "Missing addon name" },
            guidePages = guidePages,
            items = items
        )
    }
}