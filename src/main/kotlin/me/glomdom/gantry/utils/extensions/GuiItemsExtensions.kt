package me.glomdom.gantry.utils.extensions

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import me.glomdom.gantry.utils.gantryKey
import net.kyori.adventure.text.Component
import org.bukkit.Material
import xyz.xenondevs.invui.item.Item

fun GuiItems.fuelInput(name: String = ""): Item = Item.simple(
    ItemStackBuilder.gui(Material.BLACK_STAINED_GLASS_PANE, gantryKey("fuel_input"))
        .name(Component.translatable("gantry.gui.fuel_input"))
)