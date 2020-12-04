package com.youtube.hempfest.goldeco.gui.menus;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.gui.EcoMenu;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.paginated.InventoryBrowse;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryItem extends EcoMenu {
    public InventoryItem(MenuManager manager) {
        super(manager);
    }

    @Override
    public String getMenuName() {
        return manager.getItemToEdit() + " | Shopping...";
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
    Material selected = Material.matchMaterial(manager.getItemToEdit());
    switch (mat) {
        case TOTEM_OF_UNDYING:
            int page = menu.getPage();
            menu.setPage(page);
            new InventoryBrowse(menu).open();
            break;

    }
    if (mat.equals(selected)) {
        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("SELL")) {
            if (e.getCurrentItem().getAmount() == 1) {
                Bukkit.dispatchCommand(p, "eco sell " + manager.getItemToEdit() + " 1");
            }
            if (e.getCurrentItem().getAmount() == 32) {
                Bukkit.dispatchCommand(p, "eco sell " + manager.getItemToEdit() + " 32");
            }
            if (e.getCurrentItem().getAmount() == 64) {
                Bukkit.dispatchCommand(p, "eco sell " + manager.getItemToEdit() + " 64");
            }
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName().contains("BUY")) {
            if (e.getCurrentItem().getAmount() == 1) {
                Bukkit.dispatchCommand(p, "eco buy " + manager.getItemToEdit() + " 1");
            }
            if (e.getCurrentItem().getAmount() == 32) {
                Bukkit.dispatchCommand(p, "eco buy " + manager.getItemToEdit() + " 32");
            }
            if (e.getCurrentItem().getAmount() == 64) {
                Bukkit.dispatchCommand(p, "eco buy " + manager.getItemToEdit() + " 64");
            }
        }
    }
    }

    @Override
    public void setMenuItems() {
        //		return ChatColor.translateAlternateColorCodes('&', text);
        ItemStack back = makeItem(Material.TOTEM_OF_UNDYING, new ColoredString("&a&oGo back.", ColoredString.ColorType.MC).toString(), "");
        inventory.setItem(4, back);
        ItemStack s1 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fSELL &7x1", "Click to sell in quantities of 1.");
        s1.setAmount(1);
        ItemStack s32 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fSELL &7x32", "Click to sell in quantities of 32.");
        s32.setAmount(32);
        ItemStack s64 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fSELL &7x64", "Click to sell in quantities of 64.");
        s64.setAmount(64);
        inventory.setItem(10, s1);
        inventory.setItem(11, s32);
        inventory.setItem(12, s64);
        inventory.setItem(13, FILLER_GLASS);
        inventory.setItem(22, FILLER_GLASS);
        ItemStack b1 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fBUY &7x1", "Click to buy in quantities of 1.");
        b1.setAmount(1);
        ItemStack b32 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fBUY &7x32", "Click to buy in quantities of 32.");
        b32.setAmount(32);
        ItemStack b64 = makeItem(Material.matchMaterial(manager.getItemToEdit()), "&b&l" + manager.getItemToEdit() + " &r| &fBUY &7x64", "Click to buy in quantities of 64.");
        b64.setAmount(64);
        inventory.setItem(14, b1);
        inventory.setItem(15, b32);
        inventory.setItem(16, b64);
        setFillerGlass();
    }
}
