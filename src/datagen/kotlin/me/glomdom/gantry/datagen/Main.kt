package me.glomdom.gantry.datagen

import me.glomdom.gantry.datagen.dsl.addonData
import me.glomdom.gantry.datagen.writers.YamlWriter
import java.nio.file.Path

fun main(args: Array<String>) {
    require(args.isNotEmpty()) { "Expected output directory path as first argument" }

    val outDir = Path.of(args[0])

    val model = addonData {
        addon("Gantry")

        items {
            item("rough_press_form", "Rough Press Form")
            item("rough_rolled_strip_form", "Rough Rolled Strip Form")
            item("rough_rod_form", "Rough Rod Form")
            item("rough_fastener_pack_form", "Rough Fastener Pack Form")
        }

        guide {
            page("pressing", "Pressing")
        }
    }

    YamlWriter(model).writeTo(outDir)
}