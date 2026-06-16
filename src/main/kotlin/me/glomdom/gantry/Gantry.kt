package me.glomdom.gantry

import io.github.pylonmc.rebar.addon.RebarAddon
import me.glomdom.gantry.block.interfaces.CoalPoweredMachine
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.Locale

class Gantry : JavaPlugin(), RebarAddon {
    override val javaPlugin: JavaPlugin = this
    override val languages: Set<Locale> = setOf(Locale.ENGLISH)
    override val material: Material = Material.BLAST_FURNACE

    override fun onEnable() {
        instance = this

        val pm = Bukkit.getPluginManager()

        registerWithRebar()

        pm.registerEvents(CoalPoweredMachine, this)

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