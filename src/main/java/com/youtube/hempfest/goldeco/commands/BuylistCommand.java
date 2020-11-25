package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class BuylistCommand extends BukkitCommand {

    public BuylistCommand(String name, String description, String permission, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        setPermission(permission);
    }

    private void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private String notPlayer() {
        return String.format("[%s] - You aren't a player..", GoldEconomy.getInstance().getDescription().getName());
    }

    private boolean isInt(String e) {
        try {
            Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String noPermission(String permission) {
        return "You don't have permission " + '"' + permission + '"';
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
        StringLibrary me = new StringLibrary(p);
        /*
        //  /\ /\ /\ /\ /\ /\
        //
         */

        if (length == 0) {
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            if (!GoldEconomy.usingShop()) {
                me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                return true;
            }
            me.getBuyList(p, 1);
            return true;
        }

        if (length == 1) {
                if (!p.hasPermission(this.getPermission())) {
                    me.msg(noPermission(this.getPermission()));
                    return true;
                }
            if (!GoldEconomy.usingShop()) {
                me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                return true;
            }
            me.getBuyList(p, Integer.parseInt(args[0]));
            return true;
        }

        return false;
    }
}
