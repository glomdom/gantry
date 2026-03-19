package me.glomdom.gantry.datagen.dsl.builders

import me.glomdom.gantry.datagen.annotations.AddonDsl
import me.glomdom.gantry.datagen.dsl.definitions.InventoryDefinition
import me.glomdom.gantry.datagen.dsl.definitions.InventoriesDefinition

@AddonDsl
class InventoriesBuilder {
    private val inventories = mutableListOf<InventoryDefinition>()

    fun inventory(id: String, name: String) {
        inventories += InventoryDefinition(id, name)
    }

    fun build(): InventoriesDefinition {
        return InventoriesDefinition(inventories)
    }
}