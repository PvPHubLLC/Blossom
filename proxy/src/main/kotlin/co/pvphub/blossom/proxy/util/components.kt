package co.pvphub.blossom.proxy.util

import net.kyori.adventure.text.Component

operator fun Component.plus(other: Component) = append(other)