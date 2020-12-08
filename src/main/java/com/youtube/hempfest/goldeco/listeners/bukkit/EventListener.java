package com.youtube.hempfest.goldeco.listeners.bukkit;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.BankData;
import com.youtube.hempfest.goldeco.data.PlayerData;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.Utility;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryHolder;

public class EventListener implements Listener {

    private void loadPlayerFiles(Player player) {
        final PlayerData data = PlayerData.get(player.getUniqueId());
        BankData world = BankData.get(player.getWorld().getName());
        FileConfiguration fc = data.getConfig();
        fc.set("player." + player.getWorld().getName() + ".balance", GoldEconomy.startingBalance());
        if (!world.exists()) {
            FileConfiguration fc2 = world.getConfig();
            fc2.createSection("banks");
            world.saveConfig();
        }
        data.saveConfig();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerLogin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerListener el = new PlayerListener(p);
        if (!el.has(Utility.BALANCE)) {
            loadPlayerFiles(p);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMenuClick(InventoryClickEvent e) {

        InventoryHolder holder = e.getInventory().getHolder();
        // If the inventoryholder of the inventory clicked on
        // is an instance of Menu, then gg. The reason that
        // an InventoryHolder can be a Menu is because our Menu
        // class implements InventoryHolder!!
        if (holder instanceof EcoMenu) {
            e.setCancelled(true); // prevent them from fucking with the inventory
            if (e.getCurrentItem() == null) { // deal with null exceptions
                return;
            }
            // Since we know our inventoryholder is a menu, get the Menu Object representing
            // the menu we clicked on
            EcoMenu ecoMenu = (EcoMenu) holder;
            // Call the handleMenu object which takes the event and processes it
            ecoMenu.handleMenu(e);
        }

    }


}
