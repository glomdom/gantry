package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.SettingsEntryDefinition
import me.glomdom.gantry.datagen.dsl.definitions.SettingsNumberEntryDefinition
import me.glomdom.gantry.datagen.dsl.definitions.SettingsStringEntryDefinition

@AddonDsl
class SettingsBuilder {
    private val entries = mutableListOf<SettingsEntryDefinition<*>>()

    fun string(key: String, value: String) {
        entries += SettingsStringEntryDefinition(key, value)
    }

    fun number(key: String, value: Number) {
        entries += SettingsNumberEntryDefinition(key, value)
    }

    fun build(): List<SettingsEntryDefinition<*>> = entries
}