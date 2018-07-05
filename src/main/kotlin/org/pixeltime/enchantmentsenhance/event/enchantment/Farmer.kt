/*
 *     Copyright (C) 2017-Present HealPotion
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.pixeltime.enchantmentsenhance.event.enchantment

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockPlaceEvent
import org.pixeltime.enchantmentsenhance.listener.EnchantmentListener
import org.pixeltime.enchantmentsenhance.manager.IM

class Farmer : EnchantmentListener() {
    override fun lang(): Array<String> {
        return arrayOf("农场")
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlace(blockPlaceEvent: BlockPlaceEvent) {
        val player = blockPlaceEvent.player
        try {
            val level = IM.getHighestLevel(player, this.name())
            if (level > 0) {
                if (player.itemInHand.type == Material.CARROT_ITEM) {
                    blockPlaceEvent.blockPlaced.setTypeIdAndData(Material.CARROT.id, 7.toByte(), true)
                }
                if (player.itemInHand.type == Material.MELON_SEEDS) {
                    blockPlaceEvent.blockPlaced.type = Material.MELON
                }
                if (player.itemInHand.type == Material.POTATO_ITEM) {
                    blockPlaceEvent.blockPlaced.setTypeIdAndData(Material.POTATO.id, 7.toByte(), true)
                }
                if (player.itemInHand.type == Material.SEEDS) {
                    blockPlaceEvent.blockPlaced.setTypeIdAndData(Material.CROPS.id, 7.toByte(), true)
                }
                if (player.itemInHand.type == Material.PUMPKIN_SEEDS) {
                    blockPlaceEvent.blockPlaced.type = Material.PUMPKIN
                }
            }
        } catch (ex: Exception) {
        }
    }
}
