package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.ItemDefinition

@AddonDsl
class ItemBuilder(private val key: String, private val name: String) {
    private val lore = mutableListOf<String>()

    fun lore(line: String) {
        lore += line
    }

    fun build(): ItemDefinition {
        return ItemDefinition(key, name, lore)
    }
}