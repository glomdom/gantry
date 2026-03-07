package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.ItemDefinition

@AddonDsl
class ItemsBuilder {
    private val items = mutableListOf<ItemDefinition>()

    fun item(id: String, name: String, lore: List<String> = emptyList()) {
        items += ItemDefinition(id, name, lore)
    }

    fun build(): List<ItemDefinition> = items
}