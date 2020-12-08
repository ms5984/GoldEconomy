package com.youtube.hempfest.goldeco.gui.menus.paginated;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.EcoMenuPaginated;
import com.youtube.hempfest.goldeco.gui.menus.InventoryBankModify;
import com.youtube.hempfest.goldeco.gui.menus.InventoryStaff;
import com.youtube.hempfest.goldeco.listeners.BankAccount;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;

import com.youtube.hempfest.hempcore.HempCore;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class InventoryBanks extends EcoMenuPaginated {
    GoldEconomy plugin;
    public InventoryBanks(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return "▬▬▬▬▬▬▬▬▬▬▬▬▬[ACCOUNTS]▬▬▬▬▬▬▬▬▬▬▬▬▬";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ArrayList<String> items = new ArrayList<String>(GoldEconomy.getBankAccounts());
        Material mat = e.getCurrentItem().getType();
        MenuManager menu = GoldEconomy.menuViewer(p);
        String player = e.getCurrentItem().getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(HempCore.getInstance(), "account"), PersistentDataType.STRING);
        switch (mat) {
            case TOTEM_OF_UNDYING:
                new InventoryStaff(menu).open();
                break;
            case CHEST:
                menu.setAccountID(player);
                new InventoryBankModify(menu).open();
                break;
            case BARRIER:
                p.closeInventory();
                break;
            case DARK_OAK_BUTTON:
                if (net.md_5.bungee.api.ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")) {
                    if (page == 0) {
                        p.sendMessage(net.md_5.bungee.api.ChatColor.GRAY + "You are already on the first page.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
                    } else {
                        page = page - 1;
                        super.open();
                    }
                } else if (net.md_5.bungee.api.ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
                        .equalsIgnoreCase("Right")) {
                    if (!((index + 1) >= items.size())) {
                        page = page + 1;
                        super.open();
                    } else {
                        p.sendMessage(net.md_5.bungee.api.ChatColor.GRAY + "You are on the last page.");
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 8, 1);
                    }
                }
                break;
        }
    }

    @Override
    public void setMenuItems() {
        ArrayList<String> items = new ArrayList<String>(GoldEconomy.getBankAccounts());
        //		return ChatColor.translateAlternateColorCodes('&', text);
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, new ColoredString("&a&oGo back.", ColoredString.ColorType.MC).toString(), "");
        inventory.setItem(45, back);
        addMenuBorder();
        // The thing you will be looping through to place items
        ///////////////////////////////////// Pagination loop template

        if (items != null && !items.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= items.size())
                    break;
                if (items.get(index) != null) {
                    ///////////////////////////
                    // Create an item from our collection and place it into the inventory
                    BankAccount bank = new BankAccount(items.get(index)).queryWorld();
//                    PlayerListener listener = new PlayerListener();
                    ItemStack playerIcon = makePersistentItem(Material.CHEST, "&7#&3&l" + items.get(index), "account", items.get(index)," ", "&6&oOwner: &f&n" + GoldEconomy.getBankOwner(items.get(index)), "", "&6&oWorld: &f&n" + GoldEconomy.getBankWorld(items.get(index)), "", "&6&lBalance: &f&n" + PlayerListener.format(bank.getBalance()));
                    inventory.addItem(playerIcon);

                    ////////////////////////
                }
            }
            setFillerGlass();
        }
    }
}
