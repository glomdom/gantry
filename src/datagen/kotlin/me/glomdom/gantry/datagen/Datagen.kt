package me.glomdom.gantry.datagen

import com.glomdom.rebardatagen.dsl.addonData
import com.glomdom.rebardatagen.writers.YamlWriter
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

            item("fire_bricks", "Fire Bricks")
            item("fire_brick", "Fire Brick")

            item("coal_powered_press", "Coal-Powered Press") {
                lore("<arrow> Presses ingots into plates.")
                lore("<arrow> Stronger than a hammer or a piston.")

                waila("Coal-Powered Press | %fuel-left%")
            }

            item("pyrolysis_oven_controller", "Pyrolysis Oven")
        }

        guide {
            page("coal_powered_machines", "Coal-Powered Machines")

            recipe("fuel_burning", "Fuel Burning")
            recipe("coal_powered_pressing", "<attr>Fuel required:</attr> %fuel-ticks%")
        }

        gui {
            item("fuel_input", "Fuel Input")
        }

        inventories {
            inventory("fuel", "Fuel")
        }

        units("fuel_ticks") {
            singular("fuel tick")
            plural("fuel ticks")
        }
    }

    YamlWriter().writeTo(model, outDir)
}