package com.youtube.hempfest.goldeco.listeners;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.PlayerData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.structure.EconomyStructure;
import com.youtube.hempfest.goldeco.util.*;
import com.youtube.hempfest.goldeco.util.libraries.ItemLibrary;
import com.youtube.hempfest.goldeco.util.libraries.ItemManager;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class PlayerListener implements EconomyStructure {

    OfflinePlayer op;

    public PlayerListener() {}

    public PlayerListener(OfflinePlayer op) {
        this.op = op;
    }

    @Override
    public void set(double amount) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op != null) {
            if (op.isOnline()) {
                PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                FileConfiguration fc = data.getConfig();
                fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", amount);
                data.saveConfig();
                me.msg(me.moneySet().replaceAll("%amount%", format(amount)));
                return;
            }
            PlayerData data = new PlayerData(op.getUniqueId());
            FileConfiguration fc = data.getConfig();
            fc.set("player." + GoldEconomy.getMainWorld() + ".balance", amount);
            data.saveConfig();
            return;
        }
    }

    @Override
    public void add(double amount) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op != null) {
            if (op.isOnline()) {
                PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                FileConfiguration fc = data.getConfig();
                double current = fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance");
                double result = current + amount;
                fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", result);
                data.saveConfig();
                me.msg(me.moneyGiven().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%balance%", format(result)));
                return;
            }
        PlayerData data = new PlayerData(op.getUniqueId());
        FileConfiguration fc = data.getConfig();
            double current = fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance");
            double result = current + amount;
            fc.set("player." + GoldEconomy.getMainWorld() + ".balance", result);
            data.saveConfig();
        return;
        }

    }

    @Override
    public void add(double amount, String worldName) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op != null) {
            if (op.isOnline()) {
                PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                FileConfiguration fc = data.getConfig();
                double current = fc.getDouble("player." + worldName + ".balance");
                double result = current + amount;
                fc.set("player." + worldName + ".balance", result);
                data.saveConfig();
                me.msg(me.moneyGiven().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%balance%", format(result)));
                return;
            }
            PlayerData data = new PlayerData(op.getUniqueId());
            FileConfiguration fc = data.getConfig();
            double current = fc.getDouble("player." + worldName + ".balance");
            double result = current + amount;
            fc.set("player." + worldName + ".balance", result);
            data.saveConfig();
            return;
        }
    }

    @Override
    public void remove(double amount) {
        if (op != null) {
            StringLibrary me = new StringLibrary(op.getPlayer());
            if (op.isOnline()) {
                PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                FileConfiguration fc = data.getConfig();
                double current = fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance");
                double result = current - amount;
                fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", result);
                data.saveConfig();
                me.msg(me.moneyTaken().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%balance%", format(result)));
                return;
            }
            PlayerData data = new PlayerData(op.getUniqueId());
            FileConfiguration fc = data.getConfig();
            double current = fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance");
            double result = current + amount;
            fc.set("player." + GoldEconomy.getMainWorld() + ".balance", result);
            data.saveConfig();
            return;
        }
    }

    @Override
    public void remove(double amount, String worldName) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op != null) {
            if (op.isOnline()) {
                PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                FileConfiguration fc = data.getConfig();
                double current = fc.getDouble("player." + worldName + ".balance");
                double result = current - amount;
                fc.set("player." + worldName + ".balance", result);
                data.saveConfig();
                me.msg(me.moneyTaken().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%balance%", format(result)));
                return;
            }
            PlayerData data = new PlayerData(op.getUniqueId());
            FileConfiguration fc = data.getConfig();
            double current = fc.getDouble("player." + worldName + ".balance");
            double result = current + amount;
            fc.set("player." + worldName + ".balance", result);
            data.saveConfig();
            return;
        }
    }

    @Override
    public void create() {

    }

    @Override
    public boolean has(Utility type) {
        boolean result = false;
        switch (type) {
            case BALANCE:
                if (op != null) {
                    PlayerData data = new PlayerData(op.getPlayer().getUniqueId());
                    FileConfiguration fc = data.getConfig();
                    if (fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance") != 0) {
                        result = true;
                    }
                    break;
                }
                PlayerData data = new PlayerData(op.getUniqueId());
                FileConfiguration fc = data.getConfig();
                if (fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance") != 0) {
                    result = true;
                }
                break;
        }
        return result;
    }

    @Override
    public boolean has(Utility type, String worldName) {
        boolean result = false;
        switch (type) {
            case BALANCE:
                PlayerData data = new PlayerData(op.getUniqueId());
                FileConfiguration fc = data.getConfig();
                if (fc.getDouble("player." + worldName + ".balance") != 0) {
                    result = true;
                }
                break;
        }
        return result;
    }

    @Override
    public String get(Utility type) {
        String result = "";
        switch (type) {
            case BALANCE:
                if (op != null) {
                    PlayerData data = new PlayerData(op.getUniqueId());
                    FileConfiguration fc = data.getConfig();
                    if (op.isOnline()) {
                        result = format(fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance"));
                    } else {
                        result = format(fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance"));
                    }

                }
                break;
        }
        return result;
    }

    @Override
    public double get(Utility type, String item) {
        double result = 0.0;
        Config items = Config.get("shop_items");
        switch (type) {
            case BUY_PRICE:
            result = items.getConfig().getDouble("Items." + item + ".purchase-price");
                break;
            case SELL_PRICE:
                result = items.getConfig().getDouble("Items." + item + ".sell-price");
                break;
        }
        return result;
    }

    public boolean isInventoryFull(Player p) { return (p.getInventory().firstEmpty() == -1); }

    @Override
    public void buy(String item, int amount) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        GoldEconomy plugin = GoldEconomy.getInstance();
        Config items = Config.get("shop_items");
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
//        ItemManager im = GoldEconomy.getItemManager();
        Material mat = ItemLibrary.getMaterial(item);
        double itemCost = items.getConfig().getDouble("Items." + ItemLibrary.getMaterial(item).name() + ".purchase-price") * amount;
        if (Double.parseDouble(get(Utility.BALANCE).replaceAll(",", "")) >= itemCost) {
            remove(itemCost);
            ItemStack material = new ItemStack(mat);
            for (int i = 0; i < amount; i++) {
                if (isInventoryFull(op.getPlayer())) {
                    me.msg(me.notEnoughSpace());
                    material.setAmount(amount);
                    op.getPlayer().getWorld().dropItemNaturally(op.getPlayer().getLocation(), material);
                    break;
                } else
                    op.getPlayer().getInventory().addItem(material);
            }
        op.getPlayer().updateInventory();
        me.msg("You bought " + '"' + amount + '"' + " " + "&7&o" + item + "&r for " + '"' + itemCost + '"' + " " + currency);
        } else {
            me.msg(me.notEnoughMoney().replaceAll("%world%", op.getPlayer().getWorld().getName()));
        }
    }

    @Override
    public void sell(String item, int amount) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        Config items = Config.get("shop_items");
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
        Material mat = ItemLibrary.getMaterial(item.toUpperCase());
        double itemCost = items.getConfig().getDouble("Items." + ItemLibrary.getMaterial(item).name() + ".sell-price") * amount;
        if (ItemManager.removeItem(op.getPlayer(), mat, amount).transactionSuccess) {
            add(itemCost);
            me.msg("You sold " + '"' + amount + '"' + " " + "&7&o" + item + "&r for " + '"' + itemCost + '"' + " " + currency);
        }
    }

    private String getPrimaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return fc.getString("Economy.custom-currency.name");
        } else
            return "GOLD";
    }

    private String getSecondaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return fc.getString("Economy.custom-currency.change");
        } else
            return "GOLD";
    }

    public List<String> getLeaderboard(Player p) {
        PlayerListener el = new PlayerListener();
        StringLibrary sl = new StringLibrary(p);
        List<String> array = new ArrayList<>();
        HashMap<String, Double> players = new HashMap<String, Double>();
        for (String playerName : el.getAllPlayers()) {
            PlayerData data = new PlayerData(UUID.fromString(playerName));
            FileConfiguration fc = data.getConfig();
            players.put(playerName, fc.getDouble("player." + p.getWorld().getName() + ".balance"));
        }
        HighestValue comp = new HighestValue(players);
        TreeMap<String, Double> sorted_map = new TreeMap<>(comp);


        sorted_map.putAll(players);
        String nextTop = "";
        Double nextTopBal = 0.0;

        for (Map.Entry<String, Double> player : sorted_map.entrySet()) {

            if (player.getValue() > nextTopBal) {
                nextTop = player.getKey();
            }
            array.add(nextTop);
        }
        return array;
    }

    public List<String> getAllPlayers(){
        List<String> users = new ArrayList<String>();
        for(File file : PlayerData.getDataFolder().listFiles()) {
            users.add(file.getName().replace(".yml", ""));
        }
        return users;
    }

    public String nameByUUID(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        if(player == null) return null;
        return player.getName();
    }

    public String format(double amount) {
        String number = String.valueOf(amount);
        Double numParsed = Double.parseDouble(number);
        String numString = String.format("%,.2f", numParsed);
        return numString;
    }
}
