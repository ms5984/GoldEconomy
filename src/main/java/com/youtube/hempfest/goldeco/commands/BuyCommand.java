package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.GoldEconomyCommandBase;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyCommand extends GoldEconomyCommandBase {

    private static final List<String> ALIASES = new ArrayList<>(Collections.singletonList("purchase"));

    public BuyCommand() {
        super("buy", "GoldEconomy item purchasing", "/buy item", ALIASES);
    }

    @Override
    protected String permissionNode() {
        return "goldeconomy.use.buy";
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
            Bukkit.dispatchCommand(p, "eco buy");
            return true;
        }

        if (length == 1) {
            me = new StringLibrary(p, args[0]);
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            String item = args[0];
            if (isInt(item)) {
                me.msg(me.nameUnknown().replaceAll("%args%", item));
                return true;
            }
            Bukkit.dispatchCommand(p, "eco buy " + item);
            return true;
        }

        if (length == 2) {
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            String item = args[1];
            String amount = args[0];
            Bukkit.dispatchCommand(p, "eco buy " + item + " " + amount);
            return true;
        }

        return false;
    }
}
