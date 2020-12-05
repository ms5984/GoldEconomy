package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.BankData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.Utility;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryBank extends EcoMenu {
    public InventoryBank(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return new ColoredString("&5&l&m▬▬▬▬▬▬▬▬▬&7[&6&lPlayer Bank&7]&5&l&m▬▬▬▬▬▬▬▬▬", ColoredString.ColorType.MC).toString();
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
    BankListener bl = new BankListener(p);
    switch (mat) {
        case CHEST:
            Bukkit.dispatchCommand(p, "eco bank create");
            new InventoryBank(menu).open();
            break;
        case LAVA_BUCKET:
            Bukkit.dispatchCommand(p, "eco bank reset");
            new InventoryBank(menu).open();
            break;
        case GOLD_NUGGET:
            menu.setAccountID(bl.get(Utility.NAME));
            Config main = new Config("shop_config");
            FileConfiguration fc = main.getConfig();
            List<String> items = new ArrayList<>(fc.getStringList("Economy.currency-items"));
            if (usingCustomCurrency()) {
                menu.setItemToEdit(getPrimaryDollar());
                menu.setItemTooEdit(getSecondaryDollar());
            } else {
                menu.setItemToEdit(items.get(0));
                menu.setItemTooEdit(items.get(1));
            }
            new InventoryBankOptions(menu).open();
            break;
        case TOTEM_OF_UNDYING:
            new InventoryShop(menu).open();
            break;
    }
    }

    private String getPrimaryDollar() {
        Config main = new Config("shop_config");
        FileConfiguration fc = main.getConfig();
            return fc.getString("Economy.custom-currency.name-item");
    }

    private String getSecondaryDollar() {
        Config main = new Config("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.change-item");
    }

    private boolean usingCustomCurrency() {
        Config main = new Config("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return true;
        }
        return false;
    }

    @Override
    public void setMenuItems() {
        PlayerListener el = new PlayerListener(manager.getOwner());
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&a&oGo back.");
        ItemStack chest = makeItem(Material.CHEST, "&7[&6&lCREATE&7]", "", "Description: &a&oClick to open an account", "&a&oin the world you stand.");
        ItemStack lava = makeItem(Material.LAVA_BUCKET, "&7[&6&lRESET&7]", "", "Description: &a&oClick to delete an open account", "&a&oin your world.");
        ItemStack nugg = makeItem(Material.GOLD_NUGGET, "&7[&6&lOPEN&7]", "", "Description: &a&oClick to deposit/withdraw", "&a&ointo your account.");
        inventory.setItem(10, chest);
        inventory.setItem(12, lava);
        BankData world = new BankData(manager.getOwner().getWorld().getName());
        if (!world.exists()) {
            world.getConfig().createSection("banks");
            world.saveConfig();
        }
        BankListener b = new BankListener(manager.getOwner(), null, manager.getOwner().getWorld().getName());
        if (b.has(Utility.BANK_ACCOUNT)) {
            BankListener bl = new BankListener(manager.getOwner(), b.get(Utility.NAME), manager.getOwner().getWorld().getName());
            ItemStack seaw = makeItem(Material.SEAGRASS, "&7[&6&lINFO&7]", "", "&6&oAccount #&f" + b.get(Utility.NAME), " ", "&e&oBalance: &f" + el.format(Double.valueOf(bl.get(Utility.BALANCE))));
            inventory.setItem(14, seaw);
        }
        inventory.setItem(16, nugg);
        inventory.setItem(22, back);
        setFillerGlass();
    }
}
