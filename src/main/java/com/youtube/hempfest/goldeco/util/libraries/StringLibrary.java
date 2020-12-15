package com.youtube.hempfest.goldeco.util.libraries;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.HighestValue;
import com.youtube.hempfest.hempcore.formatting.component.Text;
import com.youtube.hempfest.hempcore.formatting.component.Text_R2;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class StringLibrary {
    private final CommandSender p;
    private static final String PREFIX = "&7[&6&lEconomy&7]&r -"; // TODO: localization
    private static final Config SHOP_MESSAGES = Config.get("shop_messages");
    private static FileConfiguration fc = SHOP_MESSAGES.getConfig();
    public static final Text TEXT_1_16 = new Text();

    static {
        if (!SHOP_MESSAGES.exists()) {
            InputStream i = GoldEconomy.getInstance().getResource("shop_messages.yml");
            SHOP_MESSAGES.copyFromResource(i);
        }
    }

    public StringLibrary(@NotNull CommandSender p) {
        this.p = Objects.requireNonNull(p);
    }

    public void msg(String text) {
        p.sendMessage(new ColoredString(PREFIX + " " + text, ColoredString.ColorType.MC).toString());
    }

    public void text(String text) {
        p.sendMessage(new ColoredString(text, ColoredString.ColorType.MC).toString());
    }

    private static void sendMessage(Player player, String s) {
        player.sendMessage(new ColoredString(s, ColoredString.ColorType.MC).toString());
    }

    private static void sendComponent(Player player, TextComponent text) {
        player.spigot().sendMessage(text);
    }

    public static String invalidDouble() {
        return fc.getString("invalid-double");
    }

    public static String invalidInteger() {
        return fc.getString("invalid-integer");
    }

    public static String notEnoughMoney(String world) {
        return Objects.requireNonNull(fc.getString("not-enough-money")).replaceAll("%world%", world);
    }

    public static String notEnoughSpace() {
        return fc.getString("not-enough-space");
    }

    public static String amountTooLarge() {
        return fc.getString("amount-too-large");
    }

    public static String nameUnknown(String replacement) {
        return Objects.requireNonNull(fc.getString("name-unknown")).replaceAll("%args%", replacement);
    }

    public static String nameNeeded() {
        return fc.getString("name-needed");
    }

    public static String money(String world, String amount, String currency) {
        return Objects.requireNonNull(fc.getString("money"))
                .replaceAll("%world%", world)
                .replaceAll("%amount%", amount)
                .replaceAll("%currency%", currency);
    }

    public static String moneySent(String amount, String recipientName) {
        return Objects.requireNonNull(fc.getString("money-sent"))
                .replaceAll("%amount%", amount).replaceAll("%player%", recipientName);
    }

    public static String moneySet(String amount) {
        return Objects.requireNonNull(fc.getString("money-set")).replaceAll("%amount%", amount);
    }

    public static String moneyReceived(String senderName, String amount) {
        return Objects.requireNonNull(fc.getString("money-received"))
                .replaceAll("%player%", senderName).replaceAll("%amount%", amount);
    }

    public static String moneyTaken(String amount, String balance) {
        return Objects.requireNonNull(fc.getString("money-taken"))
                .replaceAll("%amount%", amount).replaceAll("%balance%", balance);
    }

    public static String moneyGiven(String amount, String balance) {
        return fc.getString("money-given");
    }

    public static String moneyDeposited(String amount) {
        return fc.getString("money-deposited");
    }

    public static String moneyWithdrawn(String amount) {
        return fc.getString("money-withdrawn");
    }

    public static String playerNotFound() {
        return fc.getString("player-not-found");
    }

    public static String maxWithdrawReached() {
        return fc.getString("max-withdraw-reached");
    }

    public static String accountMade(String account, String accountWorld) {
        return Objects.requireNonNull(fc.getString("account-made"))
                .replaceAll("%account%", account)
                .replaceAll("%account_world%", accountWorld);
    }

    public static String accountDeposit(String amount, String account) {
        return Objects.requireNonNull(fc.getString("account-deposit"))
                .replaceAll("%amount%", amount)
                .replaceAll("%account%", account);
    }

    public static String accountWithdraw(String amount, String account) {
        return Objects.requireNonNull(fc.getString("account-withdraw"))
                .replaceAll("%amount%", amount)
                .replaceAll("%account%", account);
    }

    public static String accountAlreadyMade() {
        return fc.getString("account-already-made");
    }

    public static String accountNotAllowed() {
        return fc.getString("account-not-allowed");
    }

    public static String accountBalanceSet(String account, String newBalance) {
        return Objects.requireNonNull(fc.getString("account-balance-set"))
                .replaceAll("%account%", account)
                .replaceAll("%balance%", newBalance);
    }

    public static String staffAccountSet(String account, String amount) {
        return Objects.requireNonNull(fc.getString("staff-account-set"))
                .replaceAll("%account%", account)
                .replaceAll("%amount%", amount);
    }

    public static String staffMoneySet(String player, String amount) {
        return Objects.requireNonNull(fc.getString("staff-money-set"))
                .replaceAll("%player%", player)
                .replaceAll("%amount%", amount);
    }

    public static String staffMoneyGiven(String amount, String player) {
        return Objects.requireNonNull(fc.getString("staff-money-given"))
                .replaceAll("%amount%", amount)
                .replaceAll("%player%", player);
    }

    public static String staffMoneyTaken(String amount, String player) {
        return Objects.requireNonNull(fc.getString("staff-money-taken"))
                .replaceAll("%amount%", amount)
                .replaceAll("%player%", player);
    }

    public static void getBuyList(Player p, int page) {
        int o = 10;

        HashMap<String, Double> players = new HashMap<>();

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
                            sendComponent(p, TEXT_1_16.textRunnable( "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to purchase.", "buy 1 " + nextTop));
                        } else {
                            sendComponent(p, Text_R2.textRunnable( "",
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
                        sendComponent(p, TEXT_1_16.textRunnable("&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "buylist " + point, "buylist " + last));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "buylist " + point, "buylist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "buylist " + point));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "buylist " + point));
                    }
                }


            }
            // endline
        } else
        {
            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");

        }
    }

    public static void getSellList(Player p, int page) {
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
                            sendComponent(p, TEXT_1_16.textRunnable("",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    "&6" + nextTop + " &a&oClick to sell.", "buy 1 " + nextTop));
                        } else {
                            sendComponent(p, Text_R2.textRunnable( "",
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
                        sendComponent(p, TEXT_1_16.textRunnable("&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "selllist " + point, "selllist " + last));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7] : &7[", "&c&lBACK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "selllist " + point, "selllist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "selllist " + point));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&oNavigate &7[", "&3&lNEXT", "&7]", "&b&oClick this to goto the &5&onext page.", "selllist " + point));
                    }
                }


            }
            // endline
        } else
        {
            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");

        }
    }


}
