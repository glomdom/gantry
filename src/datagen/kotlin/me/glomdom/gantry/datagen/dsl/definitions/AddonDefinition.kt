package me.glomdom.gantry.datagen.dsl.definitions

data class AddonDefinition(val name: String, val guidePages: List<GuidePageDefinition>, val items: List<ItemDefinition>)
