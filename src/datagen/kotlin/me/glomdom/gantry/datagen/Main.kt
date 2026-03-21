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
            item("spent_rough_form", "Spent Rough Form")
            item("rough_rolled_strip_form", "Rough Rolled Strip Form")
            item("rough_rod_form", "Rough Rod Form")
            item("rough_fastener_pack_form", "Rough Fastener Pack Form")

            item("rough_iron_rod", "Rough Iron Rod")
            item("rough_iron_rolled_strip", "Rough Iron Rolled Strip")
            item("rough_fastener_pack", "Rough Fastener Pack")

            item("iron_scrap", "Iron Scrap")
            item("bark", "Bark")

            item("crude_iron_frame_plate", "Crude Iron Frame Plate")

            item("hydraulic_forming_press", "Hydraulic Forming Press") {
                lore("<arrow> Forms metals into forms using the power of hydraulics")
                lore("<arrow> <attr>Buffer:</attr> %buffer%")

                waila("Hydraulic Forming Press | %input-bar% | %output-bar%")
            }

            item("hydraulic_debarker", "Hydraulic Debarker") {
                lore("<arrow> Strips logs out of their bark")
                lore("<arrow> <attr>Buffer:</attr> %buffer%")

                waila("Hydraulic Debarker | %input-bar% | %output-bar%")
            }
        }

        guide {
            page("pressing", "Pressing")
        }

        gui {
            item("form_input", "Form Input")
            item("byproduct_output", "Byproduct Output")
        }

        inventories {
            inventory("form-input", "Form Input")
            inventory("byproduct-output", "Byproduct Output")
        }

        settings("hydraulic_forming_press") {
            number("tick-interval", 5)
            number("buffer-amount", 500)
        }

        settings("hydraulic_debarker") {
            number("tick-interval", 5)
            number("buffer-amount", 500)
            number("fluid-per-second", 100)
        }
    }

    YamlWriter(model).writeTo(outDir)
}