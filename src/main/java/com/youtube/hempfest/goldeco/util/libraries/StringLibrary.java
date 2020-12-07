package com.youtube.hempfest.goldeco.util.libraries;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.HighestValue;
import com.youtube.hempfest.goldeco.util.versions.ComponentR1_16;
import com.youtube.hempfest.goldeco.util.versions.ComponentR1_8_1;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.util.*;

public class StringLibrary {
    Player p;
    String text;
    public String prefix = "&7[&6&lEconomy&7]&r -";
    private Config lang = Config.get("shop_messages");
    private FileConfiguration fc = lang.getConfig();

    public StringLibrary(Player p) {
        this.p = p;
    }

    public StringLibrary(Player p, String text) {
        this.p = p;
        this.text = text;
    }

    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void msg(String text) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " " + text));
    }

    public void text(String text) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    public void sendMessage(Player player, String s) {
        player.sendMessage(color(s));
    }

    public void sendComponent(Player player, TextComponent text) {
        player.spigot().sendMessage((BaseComponent) text);
    }

    public String invalidDouble() {
    run(lang);
    return fc.getString("invalid-double");
    }

    public String invalidInteger() {
        run(lang);
        return fc.getString("invalid-integer");
    }

    public String notEnoughMoney() {
        run(lang);
        return fc.getString("not-enough-money");
    }

    public String notEnoughSpace() {
        run(lang);
        return fc.getString("not-enough-space");
    }

    public String amountTooLarge() {
        run(lang);
        return fc.getString("amount-too-large");
    }

    public String nameNeeded() {
        run(lang);
        return fc.getString("name-needed");
    }

    public String nameUnknown() {
        run(lang);
        return fc.getString("name-unknown");
    }

    public String money() {
        run(lang);
        return fc.getString("money");
    }

    public String moneySent() {
        run(lang);
        return fc.getString("money-sent");
    }

    public String moneySet() {
        run(lang);
        return fc.getString("money-set");
    }

    public String moneyReceived() {
        run(lang);
        return fc.getString("money-received");
    }

    public String moneyGiven() {
        run(lang);
        return fc.getString("money-given");
    }

    public String moneyTaken() {
        run(lang);
        return fc.getString("money-taken");
    }

    public String moneyDeposited() {
        run(lang);
        return fc.getString("money-deposited");
    }

    public String moneyWithdrawn() {
        run(lang);
        return fc.getString("money-withdrawn");
    }

    public String playerNotFound() {
        run(lang);
        return fc.getString("player-not-found");
    }

    public String maxWithdrawReached() {
        run(lang);
        return fc.getString("max-withdraw-reached");
    }

    public String accountMade() {
        run(lang);
        return fc.getString("account-made");
    }

    public String accountDeposit() {
        run(lang);
        return fc.getString("account-deposit");
    }

    public String accountWithdraw() {
        run(lang);
        return fc.getString("account-withdraw");
    }

    public String accountAlreadyMade() {
        run(lang);
        return fc.getString("account-already-made");
    }

    public String accountNotAllowed() {
        run(lang);
        return fc.getString("account-not-allowed");
    }

    public String accountBalanceSet() {
        run(lang);
        return fc.getString("account-balance-set");
    }

    public String staffMoneySet() {
        run(lang);
        return fc.getString("staff-money-set");
    }

    public String staffAccountSet() {
        run(lang);
        return fc.getString("staff-account-set");
    }

    public String staffMoneyGiven() {
        run(lang);
        return fc.getString("staff-money-given");
    }

    public String staffMoneyTaken() {
        run(lang);
        return fc.getString("staff-money-taken");
    }

    public void run(Config c) {
        if (!c.exists()) {
            InputStream i = GoldEconomy.getInstance().getResource("shop_messages.yml");
            Config.copyTo(i, lang);
        }
    }

    public void getBuyList(Player p, int page) {
        int o = 10;

        HashMap<String, Double> players = new HashMap<String, Double>();

        // Filling the hashMap
        for (String itemName : ItemManager.getShopContents()) {
            players.put(itemName, ItemManager.getItemPrice(ItemManager.indexPrice.PURCHASE, itemName));
        }

        sendMessage(p, "&7&m------------&7&l[&6&oPage &l" + page + " &7: &6&oBuylist&7&l]&7&m------------");
        int totalPageCount = 1;
        if ((players.size() % o) == 0) {
            if (players.size() > 0) {
                totalPageCount = players.size() / o;
            }
        } else {
            totalPageCount = (players.size() / o) + 1;
        }
        String nextTop = "";
        Double nextTopBal = 0.0;




        if (page <= totalPageCount) {
            // beginline
            if (players.isEmpty()) {
                p.sendMessage(ChatColor.WHITE + "The list is empty!");
            } else {
                int i1 = 0, k = 0;
                page--;
                HighestValue comp =  new HighestValue(players);
                TreeMap<String,Double> sorted_map =new TreeMap<String,Double>(comp);


                sorted_map.putAll(players);


                for (Map.Entry<String, Double> clanName : sorted_map.entrySet()) {

                    if (clanName.getValue() > nextTopBal) {
                        nextTop = clanName.getKey();
                        nextTopBal = clanName.getValue();



                    }

                    int pagee = page + 1;

                    k++;
                    if ((((page * o) + i1 + 1) == k) && (k != ((page * o) + o + 1))) {
                        i1++;
                        if (Bukkit.getServer().getVersion().contains("1.16")) {
                            sendComponent(p, ComponentR1_16.textRunnable(p, "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to purchase.", "buy 1 " + nextTop));
                        } else {
                            sendComponent(p, ComponentR1_8_1.textRunnable( "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to purchase.", "buy 1 " + nextTop));
                        }

                    }
                    players.remove(nextTop);
                    nextTop = "";
                    nextTopBal = 0.0;

                }

                int point; point = page + 1; if (page >= 1) {
                    int last; last = point - 1; point = point + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "buylist " + point, "buylist " + last));
                    } else {
                        sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "buylist " + point, "buylist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "buylist " + point));
                    } else {
                        sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "buylist " + point));
                    }
                }


            }
            // endline
        } else
        {
            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");

        }
        return;
    }

    public void getSellList(Player p, int page) {
        int o = 10;

        HashMap<String, Double> players = new HashMap<String, Double>();

        // Filling the hashMap
        for (String itemName : ItemManager.getShopContents()) {
            players.put(itemName, ItemManager.getItemPrice(ItemManager.indexPrice.SELL, itemName));
        }

        sendMessage(p, "&7&m------------&7&l[&6&oPage &l" + page + " &7: &6&oSellList&7&l]&7&m------------");
        int totalPageCount = 1;
        if ((players.size() % o) == 0) {
            if (players.size() > 0) {
                totalPageCount = players.size() / o;
            }
        } else {
            totalPageCount = (players.size() / o) + 1;
        }
        String nextTop = "";
        Double nextTopBal = 0.0;




        if (page <= totalPageCount) {
            // beginline
            if (players.isEmpty()) {
                p.sendMessage(ChatColor.WHITE + "The list is empty!");
            } else {
                int i1 = 0, k = 0;
                page--;
                HighestValue comp =  new HighestValue(players);
                TreeMap<String,Double> sorted_map =new TreeMap<String,Double>(comp);


                sorted_map.putAll(players);


                for (Map.Entry<String, Double> clanName : sorted_map.entrySet()) {

                    if (clanName.getValue() > nextTopBal) {
                        nextTop = clanName.getKey();
                        nextTopBal = clanName.getValue();



                    }

                    int pagee = page + 1;

                    k++;
                    if ((((page * o) + i1 + 1) == k) && (k != ((page * o) + o + 1))) {
                        i1++;
                        if (Bukkit.getServer().getVersion().contains("1.16")) {
                            sendComponent(p, ComponentR1_16.textRunnable(p, "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to sell.", "buy 1 " + nextTop));
                        } else {
                            sendComponent(p, ComponentR1_8_1.textRunnable( "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to sell.", "buy 1 " + nextTop));
                        }

                    }
                    players.remove(nextTop);
                    nextTop = "";
                    nextTopBal = 0.0;

                }

                int point; point = page + 1; if (page >= 1) {
                    int last; last = point - 1; point = point + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "selllist " + point, "selllist " + last));
                    } else {
                        sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "selllist " + point, "selllist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "selllist " + point));
                    } else {
                        sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "selllist " + point));
                    }
                }


            }
            // endline
        } else
        {
            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");

        }
        return;
    }


}
