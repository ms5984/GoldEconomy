package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.Menu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryBanks;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryWallets;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryPlayerModify extends Menu {
    public InventoryPlayerModify(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return color("&6&l&m▬▬▬▬▬▬▬▬▬▬&7[&3&l" + nameByUUID(UUID.fromString(manager.getPlayerName())) + "&7]&6&l&m▬▬▬▬▬▬▬▬▬▬");
    }

    private String nameByUUID(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        if(player == null) return null;
        return player.getName();
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
    Material mat = e.getCurrentItem().getType();
        MenuManager menu = GoldEconomy.menuViewer(p);
        BankListener bl = new BankListener(p, "", p.getWorld().getName());
    switch (mat) {
        case CHEST:
            String account = manager.getPlayerName();
            menu.setPlayerName(account);
            new InventoryPlayerAdd(menu).open();
            break;
        case LAVA_BUCKET:
            String name = manager.getPlayerName();
            menu.setPlayerName(name);
            new InventoryPlayerTake(menu).open();
            break;
        case TOTEM_OF_UNDYING:
            new InventoryWallets(menu).open();
            break;
    }
    }

    @Override
    public void setMenuItems() {
        ItemStack accounts = makeItem(Material.CHEST, "&7[&a&lADD&7]", "", "&a&oAdd to the players balance");
        ItemStack players = makeItem(Material.LAVA_BUCKET, "&7[&c&lTAKE&7]", "", "&c&oTake from the players balance");
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&a&oGo back.", "");
        inventory.setItem(10, accounts);
        inventory.setItem(13, players);
        inventory.setItem(16, back);
        setFillerGlass();
    }
}
