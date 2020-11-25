package com.youtube.hempfest.goldeco.util.libraries;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    GoldEconomy plugin;
    Player p;
    public boolean transactionSuccess = false;
    public ItemManager(GoldEconomy plugin) {
        this.plugin = plugin;
    }
    public ItemManager(GoldEconomy plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    public static enum indexPrice { PURCHASE, SELL}

    public List<String> getShopContents() {
        List<String> array = new ArrayList<>();
        Config items = new Config("shop_items");
        FileConfiguration fc = items.getConfig();
        for (String item : fc.getConfigurationSection("Items").getKeys(false)) {
            array.add(item);
        }
        return array;
    }

    public List<String> getShopContentss() {
        List<String> array = new ArrayList<>();
        Config items = new Config("shop_items");
        FileConfiguration fc = items.getConfig();
        for (Material mat : Material.values()) {
            array.add(mat.name());
        }
        return array;
    }

    public double getItemPrice(indexPrice price, String item) {
        double result = 0.0;
        Config items = new Config("shop_items");
        switch (price) {
            case PURCHASE:
                result = items.getConfig().getDouble("Items." + item + ".purchase-price");
                break;
            case SELL:
                result = items.getConfig().getDouble("Items." + item + ".sell-price");
                break;
        }
        return result;
    }

    public int getAmount(Player arg0, ItemStack arg1) {
        if (arg1 == null)
            return 0;
        int amount = 0;
        for (int i = 0; i < 36; i++) {
            ItemStack slot = arg0.getInventory().getItem(i);
            if (slot == null || !slot.isSimilar(arg1))
                continue;
            amount += slot.getAmount();
        }
        return amount;
    }

    public void removeItem(Material mat, int amount) {
        if (getAmount(p, new ItemStack(mat)) < amount) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou do not have enough " + mat.name().toLowerCase().replaceAll("_", "") + "."));
            return;
        }
        if (amount == 0) {
            this.transactionSuccess = true;
            return;
        }
        ItemStack item = new ItemStack(mat);
        if (p.getInventory().contains(item.getType())) {
            for (int i = 0; i < amount; i++) {
                p.getInventory().removeItem(item);
            }
            this.transactionSuccess = true;
        }
    }

    public void removeItem(ItemStack item, int amount) {
        if (getAmount(p, item) < amount) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&oYou do not have enough " + item.getType().name().toLowerCase().replaceAll("_", "") + "."));
            return;
        }
        if (amount == 0) {
            this.transactionSuccess = true;
            return;
        }
        if (p.getInventory().contains(item)) {
            for (int i = 0; i < amount; i++) {
                p.getInventory().removeItem(item);
            }
            this.transactionSuccess = true;
        }
    }


}
