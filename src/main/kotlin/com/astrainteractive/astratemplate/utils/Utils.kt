package com.astrainteractive.astratemplate.utils

import com.astrainteractive.astratemplate.sqldatabase.Database
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.sql.ResultSet


fun ItemStack.setDisplayName(name: String) {
    val meta = itemMeta!!
    meta.setDisplayName(name)
    itemMeta = meta
}

fun Player.playSound(sound: String) {
    playSound(location, sound, 1f, 1f)
}

fun ItemStack.displayNameOrMaterialName(): String {
    val name = itemMeta!!.displayName
    if (name.isNullOrEmpty())
        return type.name
    return name
}
