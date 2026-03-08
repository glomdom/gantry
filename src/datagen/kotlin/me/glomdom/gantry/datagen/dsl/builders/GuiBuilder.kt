package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.GuiDefinition
import me.glomdom.gantry.datagen.dsl.definitions.ItemDefinition

@AddonDsl
class GuiBuilder {
    private val items = mutableListOf<ItemDefinition>()

    fun item(id: String, name: String, block: ItemBuilder.() -> Unit = {}) {
        items += ItemBuilder(id, name).apply(block).build()
    }

    fun build(): GuiDefinition {
        return GuiDefinition(
            items
        )
    }
}