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
        append(buildGuidePages())
        append(buildGuis())
        append(buildItems())
        append(buildInventories())
    }

    private fun buildGuidePages(): String = buildString {
        if (addonDefinition.guidePages.isEmpty()) return@buildString

        appendLine("guide:")
        appendLine("  page:")

        for (page in addonDefinition.guidePages) {
            appendLine("    ${page.id}: \"${page.title}\"")
        }
    }

    private fun buildGuis(): String = buildString {
        if (addonDefinition.guis.isEmpty()) return@buildString

        appendLine("gui:")
        for (guiDefinition in addonDefinition.guis) {
            for (item in guiDefinition.items) {
                appendLine("  ${item.id}: \"${item.name}\"")
            }
        }
    }

    private fun buildItems(): String = buildString {
        if (addonDefinition.items.isEmpty()) return@buildString

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

    private fun buildInventories(): String = buildString {
        if (addonDefinition.inventories.isEmpty()) return@buildString

        appendLine("inventory:")

        for (inventory in addonDefinition.inventories) {
            for (display in inventory.displays) {
                appendLine("  ${display.id}: ${display.name}")
            }
        }
    }

    private fun buildSettingsYml(settingsDefinition: SettingsDefinition): String = buildString {
        for (entry in settingsDefinition.entries) {
            when (val value = entry.value) {
                is String -> {
                    appendLine("${entry.key}: \"$value\"")
                }

                is Number -> {
                    appendLine("${entry.key}: $value")
                }

                null -> {
                    error("Received null as a value for key `${entry.key}`")
                }

                else -> {
                    error("Unsupported settings entry value type for key `${entry.key}`: ${value::class.qualifiedName}")
                }
            }
        }
    }
}