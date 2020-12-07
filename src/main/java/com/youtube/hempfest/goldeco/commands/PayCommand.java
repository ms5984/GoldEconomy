package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.GoldEconomyCommandBase;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.goldeco.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayCommand extends GoldEconomyCommandBase {
    private static final List<String> ALIASES = new ArrayList<>(Collections.singletonList("transfer"));

    public PayCommand() {
        super("pay", "GoldEconomy item purchasing", "/pay player", ALIASES);
    }

    @Override
    protected String permissionNode() {
        return "goldeconomy.use.pay";
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
            me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/pay playerName ##.##" + '"');
            return true;
        }

        if (length == 1) {
                if (!p.hasPermission(this.getPermission())) {
                    me.msg(noPermission(this.getPermission()));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/pay playerName ##.##" + '"');
            return true;
        }

        if (length == 2) {
            Player tar = Bukkit.getPlayer(args[0]);
            if (tar == null) {
                me.msg(me.playerNotFound());
                return true;
            }
            if (tar.getName().equals(p.getName())) {
                me.msg("&c&oYou cannot pay yourself your own wages...");
                return true;
            }
            if (!isDouble(args[1])) {
                me.msg(me.invalidDouble());
                return true;
            }
            PlayerListener player = new PlayerListener(p);
            PlayerListener target = new PlayerListener(tar);
            double pCurrent = Double.parseDouble(player.get(Utility.BALANCE).replaceAll(",", ""));
            double tCurrent = Double.parseDouble(target.get(Utility.BALANCE).replaceAll(",", ""));
            if (pCurrent < Double.parseDouble(args[1])) {
                me.msg(me.invalidDouble());
                return true;
            }
            player.set(pCurrent - Double.parseDouble(args[1]));
            target.set(tCurrent + Double.parseDouble(args[1]));
            me.msg(me.moneySent().replaceAll("%amount%", args[1]).replaceAll("%player%", tar.getName()));
            StringLibrary them = new StringLibrary(tar);
            them.msg(them.moneyReceived().replaceAll("%amount%", args[1]).replaceAll("%player%", p.getName()));
            return true;
        }

        return false;
    }
}
