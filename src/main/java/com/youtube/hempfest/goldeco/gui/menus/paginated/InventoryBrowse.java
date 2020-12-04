package com.youtube.hempfest.goldeco.gui.menus.paginated;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.EcoMenuPaginated;
import com.youtube.hempfest.goldeco.gui.menus.InventoryItem;
import com.youtube.hempfest.goldeco.gui.menus.InventoryShop;
import com.youtube.hempfest.goldeco.util.libraries.ItemLibrary;
import com.youtube.hempfest.goldeco.util.libraries.ItemManager;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class InventoryBrowse extends EcoMenuPaginated {
    GoldEconomy plugin;
    public InventoryBrowse(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        MenuManager menu = GoldEconomy.menuViewer(manager.getOwner());
        if (menu.getPage() != 0) {
            page = menu.getPage();
            menu.setPage(0);
        }
        ItemManager man = GoldEconomy.getInstance().itemManager(GoldEconomy.getInstance(), manager.getOwner());
        int pgCount = man.getShopContents().size() / getMaxItemsPerPage();
        return "▬▬▬▬▬▬▬▬▬▬▬(PAGE #" + page + " / " + pgCount + ")▬▬▬▬▬▬▬▬▬▬▬";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemManager man = GoldEconomy.getInstance().itemManager(GoldEconomy.getInstance(), manager.getOwner());
        ArrayList<String> items = new ArrayList<String>(man.getShopContents());
        Material mat = e.getCurrentItem().getType();
        MenuManager menu = GoldEconomy.menuViewer(p);
        ItemManager im = new ItemManager(GoldEconomy.getInstance());

        for (String item : im.getShopContents()) {
            if (mat.equals(ItemLibrary.getMaterial(item))) {
                    if (mat.equals(Material.TOTEM_OF_UNDYING)) {
                        break;
                    }
                if (mat.equals(Material.DARK_OAK_BUTTON)) {
                    break;
                }
                if (mat.equals(Material.BLACK_STAINED_GLASS_PANE)) {
                    break;
                }
                if (mat.equals(Material.BLUE_STAINED_GLASS_PANE)) {
                    break;
                }
                if (mat.equals(Material.RED_STAINED_GLASS_PANE)) {
                    break;
                }
                if (mat.equals(Material.BARRIER)) {
                    break;
                }
                        menu.setItemToEdit(e.getCurrentItem().getItemMeta().getPersistentDataContainer()
                                .get(new NamespacedKey(GoldEconomy.getInstance(), "shop-item"), PersistentDataType.STRING));
                        menu.setPage(page);
                        new InventoryItem(menu).open();
                        break;
            }
        }
        switch (mat) {
            case TOTEM_OF_UNDYING:
                new InventoryShop(menu).open();
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
        MenuManager mang = GoldEconomy.menuViewer(manager.getOwner());
        if (mang.getPage() != 0) {
            page = mang.getPage();
            mang.setPage(0);
        }
        plugin = GoldEconomy.getInstance();
        ItemManager man = GoldEconomy.itemManager(plugin, manager.getOwner());
        ArrayList<String> items = new ArrayList<String>(man.getShopContents());
        //		return ChatColor.translateAlternateColorCodes('&', text);
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, new ColoredString("&a&oGo back.", ColoredString.ColorType.MC).toString(), "");
        inventory.setItem(45, back);
        addMenuBorder();
        // The thing you will be looping through to place items
        ///////////////////////////////////// Pagination loop template

        if (!items.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= items.size())
                    break;
                if (items.get(index) != null) {
                    ///////////////////////////
                    // Create an item from our collection and place it into the inventory
                    Material it = ItemLibrary.getMaterial(items.get(index));
                    if (it != null) {
                        ItemStack item = makePersistentItem(it, "&6&l&oITEM: &f&o&n" + items.get(index), "shop-item", items.get(index), " ", "&f&oBuy Price (&a&n" + man.getItemPrice(ItemManager.indexPrice.PURCHASE, items.get(index)) + "&f&o)", " ", "&c&oSell Price &f&o[" + man.getItemPrice(ItemManager.indexPrice.SELL, items.get(index)) + "&f&o]", " ", "&a&l&oClick to buy or sell.");
                        inventory.addItem(item);
                    }
                    ////////////////////////
                }
            }
            setFillerGlass();
        }
    }
}
