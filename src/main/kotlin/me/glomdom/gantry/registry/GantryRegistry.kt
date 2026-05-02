package me.glomdom.gantry.registry

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import me.glomdom.gantry.content.item.GantryItemFactory
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import kotlin.properties.ReadOnlyProperty

abstract class GantryRegistry {
    private val registrationQueue = mutableListOf<() -> Unit>()

    protected fun <T> entry(
        registerAction: (T) -> Unit = {},
        builder: () -> T
    ): ReadOnlyProperty<Any?, T> {
        val instance = builder()
        registrationQueue.add { registerAction(instance) }

        return ReadOnlyProperty { _, _ -> instance }
    }

    protected fun simpleRebarItem(material: Material, key: NamespacedKey, page: SimpleStaticGuidePage) =
        entry(
            registerAction = { item ->
                RebarItem.register(RebarItem::class.java, item)
                page.addItem(item)
            },
            builder = {
                ItemStackBuilder.rebar(material, key).build()
            }
        )

    protected fun simpleRebarBlock(material: Material, key: NamespacedKey) =
        entry(
            registerAction = { _ ->
                RebarBlock.register(key, material, RebarBlock::class.java)
            },
            builder = {}
        )

    protected inline fun <reified TBlock : RebarBlock> rebarBlock(material: Material, key: NamespacedKey) =
        entry(
            registerAction = { _ ->
                RebarBlock.register(key, material, TBlock::class.java)
            },
            builder = {}
        )

    protected inline fun <reified TItem : RebarItem> factoryItem(
        material: Material,
        key: NamespacedKey,
        page: SimpleStaticGuidePage,
        crossinline builderBlock: GantryItemFactory.() -> GantryItemFactory = { this }
    ): ReadOnlyProperty<Any?, ItemStack> {
        val factory = GantryItemFactory.create(material, key, page).builderBlock()

        return entry(
            builder = {
                factory.build<TItem>()
            },
            registerAction = { item ->
                if (factory.isBlockItem()) {
                    RebarItem.register(TItem::class.java, item, key)
                } else {
                    RebarItem.register(TItem::class.java, item)
                }

                page.addItem(item)
            }
        )
    }

    fun registerAll() {
        registrationQueue.forEach { it.invoke() }
        registrationQueue.clear()
    }
}