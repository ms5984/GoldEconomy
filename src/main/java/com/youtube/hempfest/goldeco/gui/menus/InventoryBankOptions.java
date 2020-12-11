package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.Utility;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import com.youtube.hempfest.hempcore.library.Items;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryBankOptions extends EcoMenu {
    public InventoryBankOptions(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return manager.getAccountID() + " | Modifying...";
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
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        List<String> items = new ArrayList<>(fc.getStringList("Economy.currency-items"));
        if (mat.equals(Items.getMaterial(items.get(0)))) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FROM")) {
                if (e.getCurrentItem().getAmount() == 1) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 1;
                    bl.add(cost);
                }
                if (e.getCurrentItem().getAmount() == 32) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 32;
                    bl.add(cost);
                }
                if (e.getCurrentItem().getAmount() == 64) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 64;
                    bl.add(cost);
                }
                menu.setAccountID(bl.get(Utility.NAME));
                if (usingCustomCurrency()) {
                    menu.setItemToEdit(getPrimaryDollarItem());
                    menu.setItemTooEdit(getSecondaryDollarItem());
                } else {
                    menu.setItemToEdit(items.get(0));
                    menu.setItemTooEdit(items.get(1));
                }
                new InventoryBankOptions(menu).open();
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("TO")) {
                if (e.getCurrentItem().getAmount() == 1) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 1;
                    bl.remove(cost);
                }
                if (e.getCurrentItem().getAmount() == 32) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 32;
                    bl.remove(cost);
                }
                if (e.getCurrentItem().getAmount() == 64) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * 64;
                    p.updateInventory();
                    bl.remove(cost);
                }
                menu.setAccountID(bl.get(Utility.NAME));
                if (usingCustomCurrency()) {
                    menu.setItemToEdit(getPrimaryDollarItem());
                    menu.setItemTooEdit(getSecondaryDollarItem());
                } else {
                    menu.setItemToEdit(items.get(0));
                    menu.setItemTooEdit(items.get(1));
                }
                new InventoryBankOptions(menu).open();
            }
        }
        if (mat.equals(Items.getMaterial(items.get(1)))) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("FROM")) {
                if (e.getCurrentItem().getAmount() == 1) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 1;
                    bl.add(cost);
                }
                if (e.getCurrentItem().getAmount() == 32) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 32;
                    bl.add(cost);
                }
                if (e.getCurrentItem().getAmount() == 64) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 64;
                    bl.add(cost);
                }
                menu.setAccountID(bl.get(Utility.NAME));
                if (usingCustomCurrency()) {
                    menu.setItemToEdit(getPrimaryDollarItem());
                    menu.setItemTooEdit(getSecondaryDollarItem());
                } else {
                    menu.setItemToEdit(items.get(0));
                    menu.setItemTooEdit(items.get(1));
                }
                new InventoryBankOptions(menu).open();
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("TO")) {
                if (e.getCurrentItem().getAmount() == 1) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 1;
                    bl.remove(cost);
                }
                if (e.getCurrentItem().getAmount() == 32) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 32;
                    bl.remove(cost);
                }
                if (e.getCurrentItem().getAmount() == 64) {
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * 64;
                    bl.remove(cost);
                }
                menu.setAccountID(bl.get(Utility.NAME));
                if (usingCustomCurrency()) {
                    menu.setItemToEdit(getPrimaryDollarItem());
                    menu.setItemTooEdit(getSecondaryDollarItem());
                } else {
                    menu.setItemToEdit(items.get(0));
                    menu.setItemTooEdit(items.get(1));
                }
                new InventoryBankOptions(menu).open();
            }
        }
    switch (mat) {
        case GOLD_NUGGET:

        break;
        case GOLD_INGOT:

            break;
        case TOTEM_OF_UNDYING:
            new InventoryBank(menu).open();
            break;
        default:
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

    private String getPrimaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
            return fc.getString("Economy.custom-currency.name");
    }

    private boolean usingCustomCurrency() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return true;
        }
        return false;
    }

    private String getSecondaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
            return fc.getString("Economy.custom-currency.change");
    }

    @Override
    public void setMenuItems() {
        String currency = getPrimaryDollar();
        String scurrency = getSecondaryDollar();
        if (!usingCustomCurrency()) {
            currency = "INGOT";
        }
        if (!usingCustomCurrency()) {
            scurrency = "NUGGET";
        }
        BankListener b = new BankListener(manager.getOwner());
        PlayerListener el = new PlayerListener(manager.getOwner());
        BankListener bl = new BankListener(manager.getOwner(), b.get(Utility.NAME));
        //		return ChatColor.translateAlternateColorCodes('&', text);
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, new ColoredString("&a&oGo back.", ColoredString.ColorType.MC).toString(), "");
        ItemStack head = makeItem(Material.PLAYER_HEAD, "&7[&6&l" + manager.getOwner().getName() + "&7]", "", "&e&oBalance: &f" + el.get(Utility.BALANCE), " ", "&a&oWorld: &f" + manager.getOwner().getWorld().getName());
        ItemStack info = makeItem(Material.SEAGRASS, "&7[&6&lINFO&7]", "", "&6&oAccount #&f" + b.get(Utility.NAME), " ", "&e&oBalance: &f" + PlayerListener.format(Double.valueOf(bl.get(Utility.BALANCE))));
        inventory.setItem(2, info);
        inventory.setItem(6, head);
        inventory.setItem(4, back);
        inventory.setItem(13, COLORED_GLASS);
        inventory.setItem(22, COLORED_GLASS);
        ItemStack s1;
        s1 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &eTRANSFER FROM WALLET &7x1", "Click to deposit in quantities of 1.");
        s1.setAmount(1);
        ItemStack s32;
        s32 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &eTRANSFER FROM WALLET &7x32", "Click to deposit in quantities of 32.");
        s32.setAmount(32);
        ItemStack s64;
        s64 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &eTRANSFER FROM WALLET &7x64", "Click to deposit in quantities of 64.");
        s64.setAmount(64);
        inventory.setItem(10, s1);
        inventory.setItem(11, s32);
        inventory.setItem(12, s64);
        ItemStack b1;
        b1 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &6TRANSFER TO WALLET &7x1", "Click to withdraw in quantities of 1.");
        b1.setAmount(1);
        ItemStack b32;
        b32 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &6TRANSFER TO WALLET &7x32", "Click to withdraw in quantities of 32.");
        b32.setAmount(32);
        ItemStack b64;
        b64 = makeItem(Items.getMaterial(manager.getItemToEdit()), "&b&l" + currency.replace("INGOT", manager.getItemToEdit()) + " &r| &6TRANSFER TO WALLET &7x64", "Click to withdraw in quantities of 64.");
        b64.setAmount(64);
        inventory.setItem(14, b1);
        inventory.setItem(15, b32);
        inventory.setItem(16, b64);
        ItemStack ss1;
        ss1 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &eTRANSFER FROM WALLET &7x1", "Click to deposit in quantities of 1.");
        ss1.setAmount(1);
        ItemStack ss32;
        ss32 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &eTRANSFER FROM WALLET &7x32", "Click to deposit in quantities of 32.");
        ss32.setAmount(32);
        ItemStack ss64;
        ss64 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &eTRANSFER FROM WALLET &7x64", "Click to deposit in quantities of 64.");
        ss64.setAmount(64);
        inventory.setItem(19, ss1);
        inventory.setItem(20, ss32);
        inventory.setItem(21, ss64);
        ItemStack bb1;
        bb1 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &6TRANSFER TO WALLET &7x1", "Click to withdraw in quantities of 1.");
        bb1.setAmount(1);
        ItemStack bb32;
        bb32 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &6TRANSFER TO WALLET &7x32", "Click to withdraw in quantities of 32.");
        bb32.setAmount(32);
        ItemStack bb64;
        bb64 = makeItem(Items.getMaterial(manager.getItemTooEdit()), "&b&l" + scurrency.replace("NUGGET", manager.getItemTooEdit()) + " &r| &6TRANSFER TO WALLET &7x64", "Click to withdraw in quantities of 64.");
        bb64.setAmount(64);
        inventory.setItem(23, bb1);
        inventory.setItem(24, bb32);
        inventory.setItem(25, bb64);
        setFillerGlass();
    }
}
