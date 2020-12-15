package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryBrowse;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryTop;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.libraries.Messaging;
import com.youtube.hempfest.goldeco.util.Utility;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryShop extends EcoMenu {
    public InventoryShop(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        //		return ChatColor.translateAlternateColorCodes('&', text);
        return new ColoredString("&5&l&m▬▬▬▬▬▬▬▬&7[&6&lEconomy Menu&7]&5&l&m▬▬▬▬▬▬▬▬", ColoredString.ColorType.MC).toString();
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
        case SHIELD:
            if (p.hasPermission("goldeconomy.use.shop.adminpanel")) {
                new InventoryStaff(menu).open();
            } else {
                p.closeInventory();
                Messaging me = new Messaging(p);
                me.msg("&c&oNice try, administrative powers needed beyond the 5th dimension.");
            }
            break;
        case FEATHER:
            if (!GoldEconomy.usingShop()) {
                Messaging me = new Messaging(p);
                p.closeInventory();
                me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                return;
            }
            new InventoryBrowse(menu).open();
            break;
        case CHEST:
            new InventoryBank(menu).open();
            break;
        case BARRIER:
            p.closeInventory();
            break;
        case DIAMOND_BLOCK:
            new InventoryTop(menu).open();
            break;
        case PLAYER_HEAD:
            menu.setAccountID(bl.get(Utility.NAME));
            if (usingCustomCurrency()) {
                menu.setItemToEdit(getPrimaryDollarItem());
                menu.setItemTooEdit(getSecondaryDollarItem());
            } else {
                Config main = Config.get("shop_config");
                FileConfiguration fc = main.getConfig();
                List<String> items = new ArrayList<>(fc.getStringList("Economy.currency-items"));
                menu.setItemToEdit(items.get(0));
                menu.setItemTooEdit(items.get(1));
            }
            new InventoryPlayer(menu).open();
            break;
    }
    }

    private String getPrimaryDollarItem() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.name-item");
    }

    private String getSecondaryDollarItem() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.change-item");
    }

    private boolean usingCustomCurrency() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return true;
        }
        return false;
    }

    @Override
    public void setMenuItems() {
        PlayerListener el = new PlayerListener(manager.getOwner());
        ItemStack feather = makeItem(Material.FEATHER, "&7[&6&lEconomy&7]", "", "&a&oClick to browse the shop.");
        ItemStack chest = makeItem(Material.CHEST, "&7[&6&lBank&7]", "", "&a&oClick to open the bank GUI.");
        ItemStack admin = makeItem(Material.SHIELD, "&7[&6&lAdmin Panel&7]", "", "&c&oTop secret.");
        ItemStack close = makeItem(Material.BARRIER, "&7[&4&lEXIT&7]", "");
        ItemStack top = makeItem(Material.DIAMOND_BLOCK, "&7[&6&lLEADERBOARD&7]", " ", "&a&oClick to view the leaderboard of", "&a&orichest players on the server.");
        ItemStack author = makeItem(Material.WRITABLE_BOOK, "&7[&6&lAuthors&7]", "", "&b&oHempfest&f, &b&oIlluminatiiiiii", "", "Description: &a&oThis plugin was created", "&a&oand designed by Hempfest with", "&a&ocontributions from Illuminatiiiiii");
        ItemStack head = makeItem(Material.PLAYER_HEAD, "&7[&6&l" + manager.getOwner().getName() + "&7]", "", "&e&oBalance: &f" + el.get(Utility.BALANCE), " ", "&a&oWorld: &f" + manager.getOwner().getWorld().getName());
        inventory.setItem(22, feather);
        inventory.setItem(12, chest);
        inventory.setItem(13, close);
        inventory.setItem(10, admin);
        inventory.setItem(14, head);
        inventory.setItem(4, top);
        inventory.setItem(16, author);
        setFillerGlass();
    }
}
