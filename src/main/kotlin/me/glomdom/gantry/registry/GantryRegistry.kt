package me.glomdom.gantry.registry

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.fluid.RebarFluid
import io.github.pylonmc.rebar.fluid.tags.FluidTemperature
import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import me.glomdom.gantry.content.item.GantryItemFactory
import net.kyori.adventure.text.format.TextColor
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

    protected fun rebarFluid(
        key: NamespacedKey,
        color: TextColor,
        material: Material,
        temp: FluidTemperature = FluidTemperature.NORMAL
    ) =
        entry(
            registerAction = {
                it.addTag(temp)
                it.register()
            },
            builder = {
                RebarFluid(key, color, material)
            }
        )

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

    protected fun simpleRebarBlockItem(material: Material, key: NamespacedKey, page: SimpleStaticGuidePage) =
        entry(
            registerAction = { item ->
                RebarItem.register(RebarItem::class.java, item, key)
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
    ): ReadOnlyProperty<Any?, ItemStack> =
        factoryItem<TItem>(GantryItemFactory.create(material, key, page), key, page, builderBlock)

    protected inline fun <reified TItem : RebarItem> factoryItem(
        factory: GantryItemFactory,
        key: NamespacedKey,
        page: SimpleStaticGuidePage,
        crossinline builderBlock: GantryItemFactory.() -> GantryItemFactory = { this }
    ): ReadOnlyProperty<Any?, ItemStack> {
        val built = factory.builderBlock()

        return entry(
            builder = { built.build() },
            registerAction = { item ->
                if (built.isBlockItem()) {
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