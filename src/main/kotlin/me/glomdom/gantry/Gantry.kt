package me.glomdom.gantry

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.Locale

class Gantry : JavaPlugin(), RebarAddon {
    override val javaPlugin: JavaPlugin = this
    override val languages: Set<Locale> = setOf(Locale.ENGLISH)
    override val material: Material = Material.BLAST_FURNACE

    override fun onEnable() {
        instance = this

        registerWithRebar()
        GantryItems.registerAll()
        GantryBlocks.registerAll()
        GantryPages.initialize()
        GantryRecipes.registerAll()
    }

    companion object {
        lateinit var instance: Gantry
            private set
    }
}