package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.Menu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryBanks;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryWallets;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventorySet extends Menu {
    public InventorySet(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return color("&6&l&m▬▬▬▬▬▬▬▬▬▬▬▬&7[&3&l" + manager.getAccountID() + "&7]&6&l&m▬▬▬▬▬▬▬▬▬▬▬▬");
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
        double amount = 0;
    switch (mat) {

        case RED_WOOL:
            amount = 500;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case ORANGE_WOOL:
            amount = 1000;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case YELLOW_WOOL:
            amount = 2500;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case LIME_WOOL:
            amount = 5000;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case GREEN_WOOL:
            amount = 8500;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case BLUE_WOOL:
            amount = 10000;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case LIGHT_BLUE_WOOL:
            amount = 500000;
            Bukkit.dispatchCommand(p,"eco set " + manager.getAccountID() + " " + amount);
            break;
        case TOTEM_OF_UNDYING:
            String account = manager.getAccountID();
            menu.setAccountID(account);
            new InventoryBankModify(menu).open();
            break;
    }
    }

    @Override
    public void setMenuItems() {
        ItemStack a1 = makeItem(Material.RED_WOOL, "&b&l500", "", "&a&oSet the balance to &d&n500");
        ItemStack a2 = makeItem(Material.ORANGE_WOOL, "&b&l1,000", "", "&a&oSet the balance to &d&n1,000");
        ItemStack a3 = makeItem(Material.YELLOW_WOOL, "&b&l2,500", "", "&a&oSet the balance to &d&n2,500");
        ItemStack a4 = makeItem(Material.LIME_WOOL, "&b&l5,000", "", "&a&oSet the balance to &d&n5,000");
        ItemStack a5 = makeItem(Material.GREEN_WOOL, "&b&l8,500", "", "&a&oSet the balance to &d&n8,500");
        ItemStack a6 = makeItem(Material.BLUE_WOOL, "&b&l10,000", "", "&a&oSet the balance to &d&n10,000");
        ItemStack a7 = makeItem(Material.LIGHT_BLUE_WOOL, "&b&l500,000 :)", "", "&a&oSet the balance to &d&n500,000");
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&a&oGo back.", "");
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
