package com.youtube.hempfest.goldeco.util.libraries;

import com.youtube.hempfest.goldeco.util.HighestValue;
import com.youtube.hempfest.hempcore.formatting.component.Text;
import com.youtube.hempfest.hempcore.formatting.component.Text_R2;
import com.youtube.hempfest.hempcore.formatting.string.ColoredString;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Messaging {
    private final CommandSender p;
    public static final Text TEXT_1_16 = new Text();

    public Messaging(@NotNull CommandSender p) {
        this.p = Objects.requireNonNull(p);
    }

    public void msg(String text) {
        p.sendMessage(new ColoredString(StringLibrary.Lang.PREFIX.get() + " " + text, ColoredString.ColorType.MC).toString());
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

    public static void getBuyList(Player p, int page) {
        int o = 10;

        HashMap<String, Double> players = new HashMap<>();

        // Filling the hashMap
        for (String itemName : ItemManager.getShopContents()) {
            players.put(itemName, ItemManager.getItemPrice(ItemManager.indexPrice.PURCHASE, itemName));
        }

//        sendMessage(p, "&7&m------------&7&l[&6&oPage &l" + page + " &7: &6&oBuylist&7&l]&7&m------------");
        sendMessage(p, StringLibrary.buyList1(page));
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
                p.sendMessage(ChatColor.WHITE + StringLibrary.Lang.ListEmpty.get());
            } else {
                int i1 = 0, k = 0;
                page--;
                HighestValue comp =  new HighestValue(players);
                TreeMap<String,Double> sorted_map = new TreeMap<>(comp);


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
//                                    "&6" + nextTop + " &a&oClick to purchase.", "buy 1 " + nextTop));
                                    StringLibrary.buyHover(nextTop), "buy 1 " + nextTop));
                        } else {
                            sendComponent(p, Text_R2.textRunnable( "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
//                                    "&6" + nextTop + " &a&oClick to purchase.", "buy 1 " + nextTop));
                                    StringLibrary.buyHover(nextTop), "buy 1 " + nextTop));
                        }

                    }
                    players.remove(nextTop);
                    nextTop = "";
                    nextTopBal = 0.0;

                }

                int point; point = page + 1; if (page >= 1) {
                    int last; last = point - 1; point = point + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7] : &7[",
                                "&c&l".concat(StringLibrary.Lang.BACK.get()).concat("&7]"),
                                StringLibrary.Lang.ListToNextPage.get(),
                                StringLibrary.Lang.ListGoBack.get(), "buylist " + point, "buylist " + last));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7] : &7[",
                                "&c&l".concat(StringLibrary.Lang.BACK.get()).concat("&7]"),
                                StringLibrary.Lang.ListToNextPage.get(),
                                StringLibrary.Lang.ListGoBack.get(), "buylist " + point, "buylist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7]",
                                StringLibrary.Lang.ListToNextPage.get(), "buylist " + point));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7]",
                                StringLibrary.Lang.ListToNextPage.get(), "buylist " + point));
                    }
                }


            }
            // endline
        } else
        {
//            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");
            sendMessage(p, totalPageCount == 1 ? StringLibrary.Lang.OnlyOnePage.get() : StringLibrary.pagesLeft(totalPageCount));
        }
    }

    public static void getSellList(Player p, int page) {
        int o = 10;

        HashMap<String, Double> players = new HashMap<>();

        // Filling the hashMap
        for (String itemName : ItemManager.getShopContents()) {
            players.put(itemName, ItemManager.getItemPrice(ItemManager.indexPrice.SELL, itemName));
        }

//        sendMessage(p, "&7&m------------&7&l[&6&oPage &l" + page + " &7: &6&oSellList&7&l]&7&m------------");
        sendMessage(p, StringLibrary.sellList1(page));
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
                p.sendMessage(ChatColor.WHITE + StringLibrary.Lang.ListEmpty.get());
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
                                    StringLibrary.sellHover(nextTop), "sell 1 " + nextTop)); // This used to say buy 1
                        } else {
                            sendComponent(p, Text_R2.textRunnable( "",
                                    " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + nextTopBal,
                                    StringLibrary.sellHover(nextTop), "sell 1 " + nextTop)); // ^same
                        }

                    }
                    players.remove(nextTop);
                    nextTop = "";
                    nextTopBal = 0.0;

                }

                int point; point = page + 1; if (page >= 1) {
                    int last; last = point - 1; point = point + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7] : &7[",
                                "&c&l".concat(StringLibrary.Lang.BACK.get()).concat("&7]"),
                                StringLibrary.Lang.ListToNextPage.get(),
                                StringLibrary.Lang.ListGoBack.get(),
                                "selllist " + point, "selllist " + last));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7] : &7[",
                                "&c&l".concat(StringLibrary.Lang.BACK.get()).concat("&7]"),
                                StringLibrary.Lang.ListToNextPage.get(),
                                StringLibrary.Lang.ListGoBack.get(),
                                "selllist " + point, "selllist " + last));
                    }
                } if (page == 0) {
                    point = page + 1 + 1;
                    if (Bukkit.getServer().getVersion().contains("1.16")) {
                        sendComponent(p, TEXT_1_16.textRunnable("&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7]",
                                StringLibrary.Lang.ListToNextPage.get(),
                                "selllist " + point));
                    } else {
                        sendComponent(p, Text_R2.textRunnable( "&b&o" + StringLibrary.Lang.navigate.get() + " &7[",
                                "&3&l".concat(StringLibrary.Lang.NEXT.get()), "&7]",
                                StringLibrary.Lang.ListToNextPage.get(),
                                "selllist " + point));
                    }
                }


            }
            // endline
        } else
        {
//            p.sendMessage(ChatColor.DARK_AQUA + "There are only " + ChatColor.GRAY + totalPageCount + ChatColor.DARK_AQUA + " pages!");
            sendMessage(p, totalPageCount == 1 ? StringLibrary.Lang.OnlyOnePage.get() : StringLibrary.pagesLeft(totalPageCount));
        }
    }

}
