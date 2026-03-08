package me.glomdom.gantry.utils

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import me.glomdom.gantry.utils.GantryUtils.gantryKey
import net.kyori.adventure.text.Component
import org.bukkit.Material
import xyz.xenondevs.invui.item.Item

object GantryGuiItems {
    fun formInput(): Item =
        Item.simple(
            ItemStackBuilder.gui(Material.LIGHT_BLUE_STAINED_GLASS_PANE, gantryKey("form_input"))
                .name(Component.translatable("gantry.gui.form_input"))
        )
}