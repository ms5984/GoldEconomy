package com.youtube.hempfest.goldeco.listeners;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.PlayerData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.structure.EconomyStructure;
import com.youtube.hempfest.goldeco.util.HighestValue;
import com.youtube.hempfest.goldeco.util.Utility;
import com.youtube.hempfest.goldeco.util.libraries.ItemManager;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.hempcore.library.Items;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements EconomyStructure {

    final OfflinePlayer op;
    final PlayerData data;
    final FileConfiguration fc;

//    public PlayerListener() {} // No longer using no-args for static actions

    public PlayerListener(@NotNull OfflinePlayer op) {
        this.op = op;
        this.data = PlayerData.get(op.getUniqueId()); // online+offline objects return same uuid
        this.fc = data.getConfig();
    }

    @Override
    public void set(double amount) {
        StringLibrary me = new StringLibrary(op.getPlayer()); // TODO: null check?
//        if (op != null) { // this null-check no longer needed object will not exist with null op
        // explanation of next line: op.getUniqueId() will return same UUID for both online+offline
//        final PlayerData data = PlayerData.get(op.getUniqueId());
//        final FileConfiguration fc = data.getConfig();
        if (op.isOnline()) {
            assert op.getPlayer() != null; // removes warn on line below
            fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", amount);
            data.saveConfig();
            me.msg(StringLibrary.moneySet(format(amount)));
            return;
        }
        fc.set("player." + GoldEconomy.getMainWorld() + ".balance", amount);
        data.saveConfig();
    }

    @Override
    public void add(double amount) {
        StringLibrary me = new StringLibrary(op.getPlayer()); // TODO: null-check
//        final PlayerData data = PlayerData.get(op.getUniqueId());
//        final FileConfiguration fc = data.getConfig();
        if (op.isOnline()) {
            double current = fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance");
            double result = current + amount;
            fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", result);
            data.saveConfig();
            me.msg(StringLibrary.moneyGiven(String.valueOf(amount), format(result)));
            return;
        }
        double current = fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance");
        double result = current + amount;
        fc.set("player." + GoldEconomy.getMainWorld() + ".balance", result);
        data.saveConfig();
    }

    @Override
    public void add(double amount, String worldName) {
        StringLibrary me = new StringLibrary(op.getPlayer()); // TODO: null-check
        if (op.isOnline()) {
            double current = fc.getDouble("player." + worldName + ".balance");
            double result = current + amount;
            fc.set("player." + worldName + ".balance", result);
            data.saveConfig();
            me.msg(StringLibrary.moneyGiven(String.valueOf(amount), format(result)));
            return;
        }
        double current = fc.getDouble("player." + worldName + ".balance");
        double result = current + amount;
        fc.set("player." + worldName + ".balance", result);
        data.saveConfig();
    }

    @Override
    public void remove(double amount) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op.isOnline()) {
            double current = fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance"); // TODO: null-check
            double result = current - amount;
            fc.set("player." + op.getPlayer().getWorld().getName() + ".balance", result);
            data.saveConfig();
            me.msg(StringLibrary.moneyTaken(String.valueOf(amount), format(result)));
            return;
        }
        double current = fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance");
        double result = current - amount; // Uhm, what?
        fc.set("player." + GoldEconomy.getMainWorld() + ".balance", result);
        data.saveConfig();
    }

    @Override
    public void remove(double amount, String worldName) {
        StringLibrary me = new StringLibrary(op.getPlayer());
        if (op.isOnline()) {
            double current = fc.getDouble("player." + worldName + ".balance");
            double result = current - amount;
            fc.set("player." + worldName + ".balance", result);
            data.saveConfig();
            me.msg(StringLibrary.moneyTaken(String.valueOf(amount), format(result)));
            return;
        }
        double current = fc.getDouble("player." + worldName + ".balance");
        double result = current - amount; // okay, if this is deliberate, then why?
        fc.set("player." + worldName + ".balance", result);
        data.saveConfig();
    }

    @Override
    public void create() {

    }

    @Override
    public boolean has(Utility type) {
        boolean result = false;
        switch (type) {
            case BALANCE:
                if (op.getPlayer() != null) { // I think you mean op.getPlayer(), so
                    if (fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance") != 0) {
                        result = true;
                    }
                    break;
                }
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
//                if (op != null) { // op is never null but is not always online/Player
                    if (op.isOnline() && op.getPlayer() != null) {
                        result = format(fc.getDouble("player." + op.getPlayer().getWorld().getName() + ".balance"));
                    } else {
                        result = format(fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance"));
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
    public void buy(String item, int amount) { // TODO: This method needs to decide how to deal with OfflinePlayers
        StringLibrary me = new StringLibrary(op.getPlayer());
//        GoldEconomy plugin = GoldEconomy.getInstance();
        Config items = Config.get("shop_items");
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
//        ItemManager im = GoldEconomy.getItemManager();
        Material mat = Items.getMaterial(item);
        double itemCost = items.getConfig().getDouble("Items." + Items.getMaterial(item).name() + ".purchase-price") * amount;
        if (Double.parseDouble(get(Utility.BALANCE).replaceAll(",", "")) >= itemCost) {
            remove(itemCost);
            ItemStack material = new ItemStack(mat);
            for (int i = 0; i < amount; i++) {
                if (isInventoryFull(op.getPlayer())) {
                    me.msg(StringLibrary.notEnoughSpace());
                    material.setAmount(amount);
                    op.getPlayer().getWorld().dropItemNaturally(op.getPlayer().getLocation(), material);
                    break;
                } else
                    op.getPlayer().getInventory().addItem(material);
            }
        op.getPlayer().updateInventory();
        me.msg("You bought " + '"' + amount + '"' + " " + "&7&o" + item + "&r for " + '"' + itemCost + '"' + " " + currency);
        } else {
            me.msg(StringLibrary.notEnoughMoney(op.getPlayer().getWorld().getName()));
        }
    }

    @Override
    public void sell(String item, int amount) { // TODO: null-check getPlayer
        StringLibrary me = new StringLibrary(op.getPlayer());
        Config items = Config.get("shop_items");
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
        Material mat = Items.getMaterial(item.toUpperCase());
        double itemCost = items.getConfig().getDouble("Items." + Items.getMaterial(item).name() + ".sell-price") * amount;
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

    public static List<String> getLeaderboard(Player p) {
//        StringLibrary sl = new StringLibrary(p);
        List<String> array = new ArrayList<>();
        HashMap<String, Double> players = new HashMap<String, Double>();
        for (String playerName : getAllPlayers()) {
            PlayerData data = PlayerData.get(UUID.fromString(playerName));
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

    public static List<String> getAllPlayers(){
        final List<String> users = new ArrayList<>();
        for(File file : PlayerData.getDataFolder().listFiles()) {
            users.add(file.getName().replace(".yml", ""));
        }
        return users;
    }

    public static String nameByUUID(UUID id) {
        // This method in Bukkit actually returns dummy objects for invalid player UUIDs
        final OfflinePlayer player = CompletableFuture.supplyAsync(() -> Bukkit.getOfflinePlayer(id)).join();
        if(player == null) return null; // TODO: replace this check as this is not a solution (never returns null)
        final String playerName = player.getName();
        return (playerName == null) ? id.toString() : playerName;
    }

    public static String format(double amount) {
/*        String number = String.valueOf(amount);
        Double numParsed = Double.parseDouble(number);*/ // wyd
        return String.format("%,.2f", amount);
    }
}
