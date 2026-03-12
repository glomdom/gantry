package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.ItemDefinition

@AddonDsl
class ItemBuilder(private val key: String, private val name: String) {
    private val lore = mutableListOf<String>()
    private var waila = String()

    fun lore(line: String) {
        lore += line
    }

    fun waila(text: String) {
        waila = text
    }

    fun build(): ItemDefinition {
        return ItemDefinition(key, name, lore, waila)
    }
}