package com.youtube.hempfest.goldeco.util;

import com.youtube.hempfest.goldeco.GoldEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.List;

public abstract class GoldEconomyCommandBase extends BukkitCommand {
    public GoldEconomyCommandBase(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
        setPermission(permissionNode());
    }

    protected abstract String permissionNode();

    protected void sendMessage(CommandSender player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    protected String notPlayer() {
        return String.format("[%s] - You aren't a player..", GoldEconomy.getInstance().getDescription().getName());
    }

    protected boolean isInt(String e) {
        try {
            Integer.parseInt(e);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected boolean isDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected String noPermission(String permission) {
        return "You don't have permission " + '"' + permission + '"';
    }
}
