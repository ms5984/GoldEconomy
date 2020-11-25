package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class SellCommand extends BukkitCommand {

    public SellCommand(String name, String description, String permission, String usageMessage, List<String> aliases) {
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
            Bukkit.dispatchCommand(p, "eco sell");
            return true;
        }

        if (length == 1) {
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            String item = args[0];
            if (isInt(item)) {
                me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                return true;
            }
            Bukkit.dispatchCommand(p, "eco sell " + item);
            return true;
        }

        if (length == 2) {
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            String item = args[1];
            String amount = args[0];
            Bukkit.dispatchCommand(p, "eco sell " + item + " " + amount);
            return true;
        }

        return false;
    }
}
