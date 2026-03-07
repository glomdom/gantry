package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.GuidePageDefinition

@AddonDsl
class PagesBuilder {
    private val pages = mutableListOf<GuidePageDefinition>()

    fun page(id: String, title: String) {
        pages += GuidePageDefinition(id, title)
    }

    fun build(): List<GuidePageDefinition> = pages
}