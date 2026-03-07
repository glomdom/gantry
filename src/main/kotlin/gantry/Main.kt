package me.glomdom.gantry

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.util.Locale

class Gantry : JavaPlugin(), RebarAddon {
    override fun onEnable() {
        _instance = this

        registerWithRebar()
    }

    override val javaPlugin: JavaPlugin = this
    override val languages: Set<Locale> = setOf(Locale.ENGLISH)
    override val material: Material = Material.BLAST_FURNACE

    companion object {
        private var _instance: Gantry? = null

        val instance: Gantry
            get() = _instance ?: throw IllegalStateException("Gantry is not initialized yet")
    }
}