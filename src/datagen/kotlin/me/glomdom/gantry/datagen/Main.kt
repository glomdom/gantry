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

            item("hydraulic_forming_press", "Hydraulic Forming Press") {
                lore("<arrow> Forms metals into forms using the power of hydraulics")
                lore("<arrow> <attr>Buffer:</attr> %buffer%")
            }
        }

        guide {
            page("pressing", "Pressing")
        }

        gui {
            item("form_input", "Form Input")
        }

        settings("hydraulic_forming_press") {
            number("tick-interval", 20)
            number("buffer-amount", 200)
        }
    }

    YamlWriter(model).writeTo(outDir)
}