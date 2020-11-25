package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.Menu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class InventoryPlayerTake extends Menu {
    public InventoryPlayerTake(MenuManager manager) {
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
        String name = nameByUUID(UUID.fromString(manager.getPlayerName()));
        double amount = 0;
    switch (mat) {

        case RED_WOOL:
            amount = 500;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case ORANGE_WOOL:
            amount = 1000;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case YELLOW_WOOL:
            amount = 2500;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case LIME_WOOL:
            amount = 5000;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case GREEN_WOOL:
            amount = 8500;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case BLUE_WOOL:
            amount = 10000;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case LIGHT_BLUE_WOOL:
            amount = 500000;
            Bukkit.dispatchCommand(p,"eco take " + name + " " + amount);
            break;
        case TOTEM_OF_UNDYING:
            String account = manager.getPlayerName();
            menu.setPlayerName(account);
            new InventoryPlayerModify(menu).open();
            break;
    }
    }

    @Override
    public void setMenuItems() {
        ItemStack a1 = makeItem(Material.RED_WOOL, "&b&l500", "", "&c&oTake &d&n500 &c&ofrom their balance");
        ItemStack a2 = makeItem(Material.ORANGE_WOOL, "&b&l1,000", "", "&c&oTake &d&n1,000 &c&ofrom their balance");
        ItemStack a3 = makeItem(Material.YELLOW_WOOL, "&b&l2,500", "", "&c&oTake &d&n2,500 &c&ofrom their balance");
        ItemStack a4 = makeItem(Material.LIME_WOOL, "&b&l5,000", "", "&c&oTake &d&n5,000 &c&ofrom their balance");
        ItemStack a5 = makeItem(Material.GREEN_WOOL, "&b&l8,500", "", "&c&oTake &d&n8,500 &c&ofrom their balance");
        ItemStack a6 = makeItem(Material.BLUE_WOOL, "&b&l10,000", "", "&c&oTake &d&n10,000 &c&ofrom their balance");
        ItemStack a7 = makeItem(Material.LIGHT_BLUE_WOOL, "&b&l500,000 :)", "", "&c&oTake &d&n500,000 &c&ofrom their balance");
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&c&oGo back.", "");
        inventory.setItem(10, a1);
        inventory.setItem(11, a2);
        inventory.setItem(12, a3);
        inventory.setItem(13, a4);
        inventory.setItem(14, a5);
        inventory.setItem(15, a6);
        inventory.setItem(16, a7);
        inventory.setItem(22, back);
        setFillerGlass();
    }
}
