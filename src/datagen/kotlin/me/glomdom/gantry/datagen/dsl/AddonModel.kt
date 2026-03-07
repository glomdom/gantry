package me.glomdom.gantry.datagen.dsl

data class AddonModel(val addon: String, val guidePages: List<GuidePageDefinition>, val items: List<ItemDefinition>)
data class GuidePageDefinition(val id: String, val title: String)
data class ItemDefinition(val id: String, val name: String, val lore: List<String>)