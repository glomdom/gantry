package me.glomdom.gantry.utils

import me.glomdom.gantry.Gantry
import org.bukkit.NamespacedKey

object GantryUtils {
    fun gantryKey(key: String): NamespacedKey {
        return NamespacedKey(Gantry.instance, key)
    }
}