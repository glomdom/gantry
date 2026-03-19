package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.AddonDefinition
import me.glomdom.gantry.datagen.dsl.definitions.GuiDefinition
import me.glomdom.gantry.datagen.dsl.definitions.GuidePageDefinition
import me.glomdom.gantry.datagen.dsl.definitions.InventoriesDefinition
import me.glomdom.gantry.datagen.dsl.definitions.ItemDefinition
import me.glomdom.gantry.datagen.dsl.definitions.SettingsDefinition

@AddonDsl
class AddonDefinitionBuilder {
    private var addon: String? = null
    private val guidePages = mutableListOf<GuidePageDefinition>()
    private val items = mutableListOf<ItemDefinition>()
    private val settings = mutableListOf<SettingsDefinition>()
    private val guis = mutableListOf<GuiDefinition>()
    private val inventories = mutableListOf<InventoriesDefinition>()

    fun addon(value: String) {
        addon = value
    }

    fun guide(block: PagesBuilder.() -> Unit) {
        guidePages += PagesBuilder().apply(block).build()
    }

    fun items(block: ItemsBuilder.() -> Unit) {
        items += ItemsBuilder().apply(block).build()
    }

    fun gui(block: GuiBuilder.() -> Unit) {
        guis += GuiBuilder().apply(block).build()
    }

    fun settings(key: String, block: SettingsBuilder.() -> Unit) {
        settings += SettingsDefinition(
            key = key,
            entries = SettingsBuilder().apply(block).build()
        )
    }

    fun inventories(block: InventoriesBuilder.() -> Unit) {
        inventories += InventoriesBuilder().apply(block).build()
    }

    fun build(): AddonDefinition {
        return AddonDefinition(
            name = requireNotNull(addon) { "Missing addon name" },
            guidePages = guidePages,
            items = items,
            settings = settings,
            guis = guis,
            inventories = inventories,
        )
    }
}