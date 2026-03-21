package me.glomdom.gantry.utils

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import me.glomdom.gantry.GantryKeys
import net.kyori.adventure.text.Component
import org.bukkit.Material
import xyz.xenondevs.invui.item.Item

object GantryGuiItems {
    fun formInput(): Item =
        Item.simple(
            ItemStackBuilder.gui(Material.LIGHT_BLUE_STAINED_GLASS_PANE, GantryKeys.GUI_FORM_INPUT)
                .name(Component.translatable("gantry.gui.form_input"))
        )

    fun byproductOutput(): Item =
        Item.simple(
            ItemStackBuilder.gui(Material.YELLOW_STAINED_GLASS_PANE, GantryKeys.GUI_BYPRODUCT_OUTPUT)
                .name(Component.translatable("gantry.gui.byproduct_output"))
        )
}