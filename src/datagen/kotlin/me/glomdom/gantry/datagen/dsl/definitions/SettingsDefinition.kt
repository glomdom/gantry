package me.glomdom.gantry.datagen.dsl.definitions

/**
 * Defines settings for something inside `resources/settings/`
 */
data class SettingsDefinition(val key: String, val entries: List<SettingsEntryDefinition<*>>)