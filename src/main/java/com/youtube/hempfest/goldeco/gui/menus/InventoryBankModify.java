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

public class InventoryBankModify extends Menu {
    public InventoryBankModify(MenuManager manager) {
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
    switch (mat) {
        case CHEST:
            String accountID = manager.getAccountID();
            menu.setAccountID(accountID);
            new InventorySet(menu).open();
            break;
        case LAVA_BUCKET:
            Bukkit.dispatchCommand(p, "eco set " + manager.getAccountID() + " 0");
            break;
        case TOTEM_OF_UNDYING:
            new InventoryBanks(menu).open();
            break;
    }
    }

    @Override
    public void setMenuItems() {
        ItemStack accounts = makeItem(Material.CHEST, "&7[&a&lSET&7]", "", "&a&oSet the bank accounts balance");
        ItemStack players = makeItem(Material.LAVA_BUCKET, "&7[&f&lRESET&7]", "", "&a&oReset the bank accounts balance");
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, "&a&oGo back.", "");
        inventory.setItem(10, accounts);
        inventory.setItem(13, players);
        inventory.setItem(16, back);
        setFillerGlass();
    }
}
