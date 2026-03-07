package me.glomdom.gantry.datagen.writers

import me.glomdom.gantry.datagen.dsl.AddonModel
import java.nio.file.Files
import java.nio.file.Path

class YamlWriter(private val model: AddonModel) {
    fun writeTo(root: Path) {
        val file = root.resolve("lang/en.yml")

        Files.createDirectories(file.parent)
        Files.writeString(file, buildYaml())
    }

    private fun buildYaml(): String = buildString {
        appendLine("addon: \"${model.addon}\"")
        appendLine("guide:")
        appendLine("  page:")

        for (page in model.guidePages) {
            appendLine("    ${page.id}: \"${page.title}\"")
        }

        appendLine("item:")

        for (item in model.items) {
            appendLine("  ${item.id}:")
            appendLine("    name: \"${item.name}\"")

            if (item.lore.isEmpty()) continue

            appendLine("    lore: |-")
            for (loreLine in item.lore) {
                appendLine("      $loreLine")
            }
        }
    }
}