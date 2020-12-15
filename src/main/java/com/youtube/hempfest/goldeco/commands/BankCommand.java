package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.util.GoldEconomyCommandBase;
import com.youtube.hempfest.goldeco.util.libraries.Messaging;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankCommand extends GoldEconomyCommandBase {
    private static final List<String> ALIASES = new ArrayList<>(Collections.singletonList("account"));

    public BankCommand() {
        super("bank", "GoldEconomy bank account system", "/bank", ALIASES);
    }

    @Override
    protected String permissionNode() {
        return "goldeconomy.use.bank";
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
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        String currency = fc.getString("Economy.custom-currency.name");
        Messaging me = new Messaging(p);
        /*
        //  /\ /\ /\ /\ /\ /\
        //
         */

        if (length == 0) {
            if (!p.hasPermission(this.getPermission())) {
                me.msg(noPermission(this.getPermission()));
                return true;
            }
            ArrayList<String> help = new ArrayList<>();
            me.msg("Command list");
            help.add("&m-------------------------------");
            help.add(" &7/shop&f - &oOpens the economy GUI.");
            help.add(" &7/bank &aset&f <&bplayerName&f>&f <&bamount&f>&f <&bworldName&f> - &oSet a players bank balance.");
            help.add(" &7/bank &cadd&f <&bplayerName&f>&f <&bamount&f>&f <&bworldName&f> - &oAdd to a players bank balance.");
            help.add("&m-------------------------------");
            help.add(" ");
            for (String list : help) {
                me.text(list);
            }
            return true;
        }



        return false;
    }
}
