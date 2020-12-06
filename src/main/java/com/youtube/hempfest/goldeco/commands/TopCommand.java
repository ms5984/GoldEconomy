package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.data.PlayerData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.GoldEconomyCommandBase;
import com.youtube.hempfest.goldeco.util.versions.ComponentR1_16;
import com.youtube.hempfest.goldeco.util.versions.ComponentR1_8_1;
import com.youtube.hempfest.goldeco.util.HighestValue;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TopCommand extends GoldEconomyCommandBase {
    private static final List<String> ALIASES = new ArrayList<>(Collections.singletonList("richest"));

    public TopCommand() {
        super("top", "GoldEconomy richest player list", "/top", ALIASES);
    }

    @Override
    protected String permissionNode() {
        return "goldeconomy.use.top";
    }

    private void sendComponent(Player player, TextComponent text) {
        player.spigot().sendMessage((BaseComponent) text);
    }

    private void getLeaderboard(Player p, int page) {
        PlayerListener el = new PlayerListener();
        StringLibrary sl = new StringLibrary(p);
            int o = 10;

            HashMap<String, Double> players = new HashMap<String, Double>();

            // Filling the hashMap
            for (String playerName : el.getAllPlayers()) {
                PlayerData data = new PlayerData(UUID.fromString(playerName));
                        FileConfiguration fc = data.getConfig();
                        if (fc.isDouble("player." + p.getWorld().getName() + ".balance")) {
                            players.put(el.nameByUUID(UUID.fromString(playerName)), fc.getDouble("player." + p.getWorld().getName() + ".balance"));
                        }
            }

            sl.text("&7&m------------&7&l[&6&oPage &l" + page + " &7: &6&oRichest Players&7&l]&7&m------------");
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
                                        " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + el.format(nextTopBal),
                                        "&6" + nextTop + " &a&oplaces &7#&6" + k + "&a&o on page " + pagee + ".", "shop"));
                            } else {
                                sendComponent(p, ComponentR1_8_1.textRunnable( "",
                                        " &7# &3&l" + k + " &b&o" + nextTop + " &7: &6&l" + el.format(nextTopBal),
                                        "&6" + nextTop + " &a&oplaces &7#&6" + k + "&a&o on page " + pagee + ".", "shop"));
                            }

                        }
                        players.remove(nextTop);
                        nextTop = "";
                        nextTopBal = 0.0;

                    }

                    int point; point = page + 1; if (page >= 1) {
                        int last; last = point - 1; point = point + 1;
                        if (Bukkit.getServer().getVersion().contains("1.16")) {
                            sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lCLICK", "&7] : &7[", "&c&lCLICK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "top " + point, "top " + last));
                        } else {
                            sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lCLICK", "&7] : &7[", "&c&lCLICK&7]", "&b&oClick this to goto the &5&onext page.", "&b&oClick this to go &d&oback a page.", "top " + point, "top " + last));
                        }
                    } if (page == 0) {
                        point = page + 1 + 1;
                        if (Bukkit.getServer().getVersion().contains("1.16")) {
                            sendComponent(p, ComponentR1_16.textRunnable(p, "&b&oNavigate &7[", "&3&lCLICK", "&7]", "&b&oClick this to goto the &5&onext page.", "top " + point));
                        } else {
                            sendComponent(p, ComponentR1_8_1.textRunnable( "&b&oNavigate &7[", "&3&lCLICK", "&7]", "&b&oClick this to goto the &5&onext page.", "top " + point));
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

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(notPlayer());
            return true;
        }

        /*
        // VARIABLE CREATION
        //  \/ \/ \/ \/ \/ \/
         */
        int length = args.length;
        Player p = (Player) commandSender;
        Config main = new Config("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
        /*
        //  /\ /\ /\ /\ /\ /\
        //
         */

        if (length == 0) {
            StringLibrary me = new StringLibrary(p);
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            getLeaderboard(p, 1);
            return true;
        }

        if (length == 1) {
            StringLibrary me = new StringLibrary(p);
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            if (!isInt(args[0])) {
                me.msg(me.invalidInteger());
                return true;
            }
            getLeaderboard(p, Integer.parseInt(args[0]));
        }



        return false;
    }
}
