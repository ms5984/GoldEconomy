package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryBanks;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryWallets;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryStaff extends EcoMenu {
    public InventoryStaff(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        //		return ChatColor.translateAlternateColorCodes('&', text);
        return new ColoredString("&6&l&m▬▬▬▬▬▬&7[&3&lSTAFF CLEARANCE&7]&6&l&m▬▬▬▬▬▬", ColoredString.ColorType.MC).toString();
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
            new InventoryBanks(menu).open();
            break;
        case PLAYER_HEAD:
            new InventoryWallets(menu).open();
            break;
        case TOTEM_OF_UNDYING:
            new InventoryShop(menu).open();
            break;
    }
    }

    @Override
    public void setMenuItems() {
        ItemStack accounts = makeItem(Material.CHEST, "&7[&b&oAccounts&7]", "", "&a&oClick to view all accounts", "&a&oacross all worlds");
        ItemStack players = makeItem(Material.PLAYER_HEAD, "&7[&b&oPlayers&7]", "", "&a&oClick to view all player wallets");
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&a&oGo back.", "");
        inventory.setItem(10, accounts);
        inventory.setItem(13, players);
        inventory.setItem(16, back);
        setFillerGlass();
    }
}
