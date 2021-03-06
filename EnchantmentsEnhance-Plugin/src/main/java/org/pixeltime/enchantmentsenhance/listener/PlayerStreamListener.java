/*
 *     Copyright (C) 2017-Present 25 (https://github.com/25)
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

package org.pixeltime.enchantmentsenhance.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.pixeltime.enchantmentsenhance.Main;
import org.pixeltime.enchantmentsenhance.manager.SettingsManager;
import org.pixeltime.enchantmentsenhance.mysql.DataStorage;
import org.pixeltime.enchantmentsenhance.mysql.PlayerStat;
import org.pixeltime.enchantmentsenhance.util.Util;

public class PlayerStreamListener implements Listener {

    public static final Main m = Main.getMain();


    public PlayerStreamListener() {
    }


    /**
     * Sends a message to greet joined player.
     *
     * @param e
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (SettingsManager.config.getBoolean("enableWelcomeMessage")) {
            Util.sendMessage(SettingsManager.lang.getString("config.welcome")
                    .replaceAll("%player%", player.getName()), player);
        }

        if (PlayerStat.getPlayerStats(player.getName()) != null) {
            PlayerStat.removePlayer(player.getName());
        }
        PlayerStat.getPlayers().add(new PlayerStat(player));
    }


    /**
     * When a player leaves the server, listener saves a player's data from
     * hashmap to file, but will not write to disk.
     *
     * @param e
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent e) {
        String playername = e.getPlayer().getName();
        PlayerStat playerstat = PlayerStat.getPlayerStats(playername);
        if (playerstat != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        DataStorage.get().saveStats(playerstat);
                        PlayerStat.removePlayer(playername);
                    } catch (Exception ex) {
                        // Unexpected Error.
                        ex.printStackTrace();
                    }
                }
            }.runTaskLater(Main.getMain(), 20);
        }
    }


    /**
     * When a player gets kicked off the server, listener saves a player's data
     * from hashmap to file, but will not write to disk.
     *
     * @param e
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(PlayerKickEvent e) {
        if (PlayerStat.getPlayerStats(e.getPlayer().getName()) != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    PlayerStat.removePlayer(e.getPlayer().getName());
                }
            }.runTaskLater(Main.getMain(), 20);
        }
    }

    /**
     * Informs Plugin author.
     *
     * @param e
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin2(PlayerJoinEvent e) {
        if (e.getPlayer() != null) {
            Player player = e.getPlayer();
            if (player.getName().equalsIgnoreCase("Fearr")) {
                Util.sendMessage(
                        ("&cThis server is using your Enchantment Enhance plugin. It is using v"
                                + Bukkit.getServer().getPluginManager().getPlugin(
                                "EnchantmentsEnhance").getDescription().getVersion()
                                + "."), player);
            }
        }
    }
}
