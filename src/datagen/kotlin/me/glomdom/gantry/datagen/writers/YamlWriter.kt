package me.glomdom.gantry.datagen.writers

import me.glomdom.gantry.datagen.dsl.definitions.AddonDefinition
import me.glomdom.gantry.datagen.dsl.definitions.SettingsDefinition
import java.nio.file.Files
import java.nio.file.Path

class YamlWriter(private val addonDefinition: AddonDefinition) {
    fun writeTo(root: Path) {
        val translationsPath = root.resolve("lang/en.yml")

        Files.createDirectories(translationsPath.parent)
        Files.createDirectories(root.resolve("settings/"))
        Files.writeString(translationsPath, buildTranslationYml())

        for (settingsDefinition in addonDefinition.settings) {
            Files.writeString(root.resolve("settings/${settingsDefinition.key}.yml"), buildSettingsYml(settingsDefinition))
        }
    }

    private fun buildTranslationYml(): String = buildString {
        appendLine("addon: \"${addonDefinition.name}\"")
        appendLine("guide:")
        appendLine("  page:")

        for (page in addonDefinition.guidePages) {
            appendLine("    ${page.id}: \"${page.title}\"")
        }

        appendLine("gui:")
        for (guiDefinition in addonDefinition.guis) {
            for (item in guiDefinition.items) {
                appendLine("  ${item.id}: \"${item.name}\"")
            }
        }

        appendLine("item:")

        for (item in addonDefinition.items) {
            appendLine("  ${item.id}:")
            appendLine("    name: \"${item.name}\"")

            if (item.lore.isNotEmpty()) {
                appendLine("    lore: |-")
                for (loreLine in item.lore) {
                    appendLine("      $loreLine")
                }
            }

            if (item.waila.isNotEmpty()) {
                appendLine("    waila: ${item.waila}")
            }
        }
    }

    private fun buildSettingsYml(settingsDefinition: SettingsDefinition): String = buildString {
        for (entry in settingsDefinition.entries) {
            when (val value = entry.value!!) {
                is String -> {
                    appendLine("${entry.key}: \"$value\"")
                }

                is Number -> {
                    appendLine("${entry.key}: $value")
                }

                else -> {
                    error("Unsupported settings entry value type for key `${entry.key}`: ${value::class.qualifiedName}")
                }
            }
        }
    }
}