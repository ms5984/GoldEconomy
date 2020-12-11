package com.youtube.hempfest.goldeco.commands;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.gui.MenuManager;
import com.youtube.hempfest.goldeco.gui.menus.InventoryShop;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.listeners.vault.VaultListener;
import com.youtube.hempfest.goldeco.util.GoldEconomyCommandBase;
import com.youtube.hempfest.goldeco.util.libraries.ItemManager;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.goldeco.util.Utility;
import com.youtube.hempfest.hempcore.library.Items;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class EconomyCommand extends GoldEconomyCommandBase {
    private static final List<String> ALIASES = new ArrayList<>(Arrays.asList("eco", "geco"));

    Material currency;
    ItemStack currenc;

    public EconomyCommand() {
        super("economy", "GoldEconomy help", "/economy", ALIASES);
    }

    @Override
    protected String permissionNode() {
        return "goldeconomy.use";
    }

    private void sendPrefixedMessage(CommandSender player, String message) {
        StringLibrary lib = new StringLibrary(null);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', lib.prefix + " " + message));
    }

    private String format(double amount) {
        String number = String.valueOf(amount);
        Double numParsed = Double.parseDouble(number);
        String numString = String.format("%,.2f", numParsed);
        return numString;
    }

    private void setCurrency(Material currency) {
        this.currency = currency;
    }

    private Material getCurrency() {
        return currency;
    }

    private void setCurrency(ItemStack item) {
        this.currenc = item;
    }

    private ItemStack getCurrencyItem() {
        return currenc;
    }

    private String randomAlphaNumeric(int count) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private String getPrimaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return fc.getString("Economy.custom-currency.name");
        } else
            return "GOLD";
    }

    private String getSecondaryDollar() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return fc.getString("Economy.custom-currency.change");
        } else
            return "GOLD";
    }

    private String getPrimaryDollarItem() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.name-item");
    }

    private String getSecondaryDollarItem() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.change-item");
    }

    private boolean usingCustomCurrency() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        if (fc.getString("Economy.custom-currency.status").equals("on")) {
            return true;
        }
        return false;
    }

/*    private String nameByUUID(UUID id) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(id);
        if(player == null) return null;
        return player.getName();
    }*/

    private boolean isInventoryFull(Player p) { return (p.getInventory().firstEmpty() == -1); }

    @Override
    public boolean execute(CommandSender commandSender, String commandLabel, String[] args) {
        if (!(commandSender instanceof Player)) {
            int length = args.length;
            StringLibrary me = new StringLibrary(null);
            Config main = Config.get("shop_config");
            FileConfiguration fc = main.getConfig();
            if (length == 0) {
                ArrayList<String> help = new ArrayList<>();
                sendPrefixedMessage(commandSender, me.prefix + " Command list");
                help.add("&m-------------------------------");
                help.add(" &7/shop&f,&7menu&f,&7gui&f - &oOpens the economy GUI.");
                help.add(" &7/buy&f - &oBuy an item from the shop");
                help.add(" &7/sell&f - &oSell an item from the shop");
                help.add(" &7/deposit&f - &oDeposit currency into your account");
                help.add(" &7/withdraw&f - &oWithdraw currency from your account");
                help.add(" &7/pay&f - &oPay an online player some money.");
                help.add(" &7/eco &ctake&f - &b&oTake a specified amount of money from a player");
                help.add(" &7/eco &agive&f - &b&oGive a specified amount of money to a player");
                help.add(" &7/eco &eset&f - &b&oSet a specified players wallet balance");
                help.add(" &7/eco &breload&f - &b&oReload the configuration.");
                help.add(" &7/eco &6update&f - &b&oCheck if the config needs an update.");
                help.add("&m-------------------------------");
                help.add(" ");
                for (String list : help) {
                    sendMessage(commandSender, list);
                }
                return true;
            }


            if (length == 1) {
                if (args[0].equalsIgnoreCase("fix") || args[0].equalsIgnoreCase("update")) {
                    if (main.exists()) {
                        if (Objects.equals(fc.getString("Version"), GoldEconomy.getInstance().getDescription().getVersion())) {
                            sendPrefixedMessage(commandSender, "&3&oThe configuration is already up to date.");
                            return true;
                        }
                    }
                    InputStream m1 = GoldEconomy.getInstance().getResource("shop_config.yml");
                    Config.copyTo(m1, main);
                    sendPrefixedMessage(commandSender, "&3&oReloaded and updated configuration &b&o" + '"' + main.getName() + '"');
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    main.reload();
                    Config lang = Config.get("shop_messages");
                    lang.reload();
                    if (GoldEconomy.usingVault()) {
                        VaultListener listener = new VaultListener(GoldEconomy.getInstance());
                        listener.unhook();
                        listener.hook();
                    }
                    sendPrefixedMessage(commandSender, "&b&oReloaded configuration");
                }
                if (args[0].equalsIgnoreCase("give")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco give playerName ##.##" + '"');
                    return true;
                }

                if (args[0].equalsIgnoreCase("take")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco take playerName ##.##" + '"');
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco set playerName&f,&7&oaccountID ##.##" + '"');
                    return true;
                }
                return true;
            }

            if (length == 2) {
                if (args[0].equalsIgnoreCase("give")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco give playerName ##.##" + '"');
                    return true;
                }

                if (args[0].equalsIgnoreCase("take")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco take playerName ##.##" + '"');
                    return true;
                }

                if (args[0].equalsIgnoreCase("set")) {
                    sendPrefixedMessage(commandSender, "&c&oNot enough arguments. Expected &7&o" + '"' + "/eco set playerName&f,&7&oaccountID ##.##" + '"');
                    return true;
                }

            }

            if (length == 3) {
                if (args[0].equalsIgnoreCase("give")) {
                    if (!isDouble(args[2])) {
                        sendPrefixedMessage(commandSender, me.invalidDouble());
                        return true;
                    }
//                    PlayerListener list = new PlayerListener();
                    String uuid = "";
                    for (String id : PlayerListener.getAllPlayers()) {
                        if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                            uuid = id;
                        }
                    }
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    PlayerListener el = new PlayerListener(pl);
                    double current = Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", ""));
                    el.set(current + Double.parseDouble(args[2]));
                    sendPrefixedMessage(commandSender, me.staffMoneyGiven().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                    return true;
                }
                if (args[0].equalsIgnoreCase("take")) {
                    if (!isDouble(args[2])) {
                        me.msg(me.invalidDouble());
                        return true;
                    }
//                    PlayerListener list = new PlayerListener();
                    String uuid = "";
                    for (String id : PlayerListener.getAllPlayers()) {
                        if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                            uuid = id;
                        }
                    }
                    OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                    PlayerListener el = new PlayerListener(pl);
                    double current = Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", ""));
                    el.set(current - Double.parseDouble(args[2]));
                    me.msg(me.staffMoneyTaken().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                    return true;
                }
                if (args[0].equalsIgnoreCase("set")) {
                    String worldName = args[2];
//                    PlayerListener list = new PlayerListener();
                    String uuid = "";
                    for (String id : PlayerListener.getAllPlayers()) {
                        if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                            uuid = id;
                        }
                    }
                    if (!isDouble(args[2])) {
                        sendPrefixedMessage(commandSender, me.invalidDouble());
                        return true;
                    }
                    // this might be an account type
                    // check for account type
                    if (GoldEconomy.getBankAccounts().contains(args[1])) {
                        worldName = GoldEconomy.getBankWorld(args[1]);
                        BankListener bl = new BankListener(null, args[1], worldName);
                        bl.set(Double.parseDouble(args[2]));
                        sendPrefixedMessage(commandSender, me.staffAccountSet().replaceAll("%account%", args[1]).replaceAll("%amount%", args[2]));
                    } else {
                        try {
                            OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                            if (PlayerListener.getAllPlayers().contains(pl.getUniqueId().toString())) {

                                PlayerListener el = new PlayerListener(pl);
                                el.set(Double.parseDouble(args[2]));
                                sendPrefixedMessage(commandSender, me.staffMoneySet().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                                return true;
                            } else
                                sendPrefixedMessage(commandSender, "&c&oInvalid response. Expected:&r [&7playerName &ror &7accountID&r].");
                        } catch (IllegalArgumentException e) {
                            sendPrefixedMessage(commandSender, "&c&oInvalid response. Expected:&r [&7playerName &ror &7accountID&r].");
                        }
                    }
                    return true;
                }


                return true;
            }
        }

        /*
        // VARIABLE CREATION
        //  \/ \/ \/ \/ \/ \/
         */
        int length = args.length;
        Player p = (Player) commandSender;
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        StringLibrary me = new StringLibrary(p);
        String withdrawType = "";
        try {
            switch (fc.getString("Economy.drop-type")) {
                case "dollar":
                    withdrawType = "dollar";
                    break;

                case "change":
                    withdrawType = "change";
                    break;
            }
        } catch (NullPointerException e) {
            me.msg("&c&oA newer feature is available and needed to further use the plugin. Copy your current configuration settings and delete the old file before restarting. This will generate a new config with the correct format to which you can copy over old settings.");
        }
        String currency = getPrimaryDollar();

        /*
        //  /\ /\ /\ /\ /\ /\
        //
         */
        if (length == 0) {
            ArrayList<String> help = new ArrayList<>();
            me.msg("Command list");
            help.add("&m-------------------------------");
            help.add(" &7/shop&f,&7menu&f,&7gui&f - &oOpens the economy GUI.");
            help.add(" &7/buy&f - &oBuy an item from the shop");
            help.add(" &7/sell&f - &oSell an item from the shop");
            help.add(" &7/deposit&f - &oDeposit currency into your account");
            help.add(" &7/withdraw&f - &oWithdraw currency from your account");
            help.add(" &7/pay&f - &oPay an online player some money.");
            if (p.hasPermission(this.getPermission() + ".staff")) {
                help.add(" &7/eco &ctake&f - &b&oTake a specified amount of money from a player");
                help.add(" &7/eco &agive&f - &b&oGive a specified amount of money to a player");
                help.add(" &7/eco &eset&f - &b&oSet a specified players wallet balance");
                help.add(" &7/eco &breload&f - &b&oReload the configuration.");
                help.add(" &7/eco &6update&f - &b&oCheck if the config needs an update.");
            }
            help.add("&m-------------------------------");
            help.add(" ");
            for (String list : help) {
                me.text(list);
            }
            return true;
        }

        if (length == 1) {
            if (args[0].equalsIgnoreCase("buy") || args[0].equalsIgnoreCase("sell")) {
                me = new StringLibrary(p, args[0]);
                me.msg(me.nameUnknown().replaceAll("%args%", "Type an item type by name including a specified amount."));
            }
            if (args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit")) {
                me.msg(me.invalidDouble());
            }
            if (args[0].equalsIgnoreCase("bal") || args[0].equalsIgnoreCase("balance")) {
                PlayerListener el = new PlayerListener(p);
                me.msg(me.money().replaceAll("%world%", p.getWorld().getName()).replaceAll("%amount%", el.get(Utility.BALANCE)).replaceAll("%currency%", currency));
            }
            if (args[0].equalsIgnoreCase("shop")) {
                MenuManager menu = GoldEconomy.menuViewer(p);
                new InventoryShop(menu).open();
            }
            if (args[0].equalsIgnoreCase("fix") || args[0].equalsIgnoreCase("update")) {
                if (!p.hasPermission(this.getPermission() + ".update")) {
                    me.msg(noPermission(this.getPermission() + ".update"));
                    return true;
                }
                if (main.exists()) {
                    if (Objects.equals(fc.getString("Version"), GoldEconomy.getInstance().getDescription().getVersion())) {
                        me.msg("&3&oThe configuration is already up to date.");
                        return true;
                    }
                }
                    InputStream m1 = GoldEconomy.getInstance().getResource("shop_config.yml");
                    Config.copyTo(m1, main);
                    me.msg("&3&oReloaded and updated configuration &b&o" + '"' + main.getName() + '"');
                return true;
            }
            if (args[0].equalsIgnoreCase("ru")) {
                if (!p.hasPermission(this.getPermission() + ".update")) {
                    me.msg(noPermission(this.getPermission() + ".update"));
                    return true;
                }
                Config message = Config.get("shop_messages");
                InputStream m1 = GoldEconomy.getInstance().getResource("shop_config_ru.yml");
                InputStream m2 = GoldEconomy.getInstance().getResource("shop_messages_ru.yml");
                Config.copyTo(m1, main);
                Config.copyTo(m2, message);
                me.msg("&3&oЯзык был изменён на &b&o" + '"' + "русский" + '"');
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!p.hasPermission(this.getPermission() + ".reload")) {
                    me.msg(noPermission(this.getPermission() + ".reload"));
                    return true;
                }
                main.reload();
                Config lang = Config.get("shop_messages");
                lang.reload();
                if (GoldEconomy.usingVault()) {
                    VaultListener listener = new VaultListener(GoldEconomy.getInstance());
                    listener.unhook();
                    listener.hook();
                }
                me.msg("&b&oReloaded configuration");
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission(this.getPermission() + ".give")) {
                    me.msg(noPermission(this.getPermission() + ".give"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco give playerName ##.##" + '"');
                return true;
            }

            if (args[0].equalsIgnoreCase("take")) {
                if (!p.hasPermission(this.getPermission() + ".take")) {
                    me.msg(noPermission(this.getPermission() + ".take"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco take playerName ##.##" + '"');
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (!p.hasPermission(this.getPermission() + ".set")) {
                    me.msg(noPermission(this.getPermission() + ".set"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco set playerName&f,&7&oaccountID ##.##" + '"');
                return true;
            }
            return true;
        }

        if (length == 2) {
            if (args[0].equalsIgnoreCase("buy")) {
                Material item = Items.getMaterial(args[1]);
                if (item == null) {
                    // item not found
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                if (!GoldEconomy.usingShop()) {
                    me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                    return true;
                }
                if (!ItemManager.getShopContents().contains(args[1])) {
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                PlayerListener el = new PlayerListener(p);
                el.buy(args[1], 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("sell")) {
                Material item = Items.getMaterial(args[1]);
                if (item == null) {
                    // item not found
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                if (!GoldEconomy.usingShop()) {
                    me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                    return true;
                }
                if (!ItemManager.getShopContents().contains(args[1])) {
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                PlayerListener el = new PlayerListener(p);
                el.sell(args[1], 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("deposit")) {
                try {
                int amount = Integer.parseInt(args[1]);
                PlayerListener el = new PlayerListener(p);
//                ItemManager im = GoldEconomy.getItemManager();
                ArrayList<Material> mats = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                boolean hasAny = false;
                for (String currencies : fc.getStringList("Economy.currency-items")) {
                    if (Items.getMaterial(currencies) == null) {
                        GoldEconomy.getInstance().getLogger().info(String.format("[%s] - There was an issue while loading currency types", GoldEconomy.getInstance().getDescription().getName()));
                        GoldEconomy.getInstance().getLogger().info(String.format("[%s] - Check to make sure you're using proper material names", GoldEconomy.getInstance().getDescription().getName()));
                        break;
                    }
                    names.add(currencies);
                    mats.add(Items.getMaterial(currencies));
                }
                for (Material mat : mats) {
                    if (p.getInventory().contains(mat)) {
                    hasAny = true;
                    setCurrency(mat);
                    break;
                    }
                }
                boolean transactionSuccess = false;
                if (hasAny) {
                    transactionSuccess = ItemManager.removeItem(p, getCurrency(), amount).transactionSuccess;
                } else {
                    me.msg(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&oYou have no money to deposit. &fValid types: " + names.toString()));
                }
                if (transactionSuccess) {
                    double value = fc.getDouble("Economy.currency-worth." + getCurrency().name()) * amount;
                    if (String.valueOf(value).contains("-")) {
                        me.msg("There was a problem with the amount you've chosen");
                        return true;
                    }
                    el.add(value);
                }
                return true;
                } catch (NumberFormatException e) {
                    me.msg("There was a problem with the amount you've chosen");
                }
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                try {
                    int amount = Integer.parseInt(args[1]);
                    PlayerListener el = new PlayerListener(p);
//                    ItemManager im = GoldEconomy.getItemManager();
                    if (amount > 640) {
                        me.msg(me.maxWithdrawReached());
                        return true;
                    }
                    List<String> items = fc.getStringList("Economy.currency-items");
                    double cost = fc.getDouble("Economy.currency-worth." + items.get(0)) * amount;
                    if (!withdrawType.equals("dollar")) {
                        cost = fc.getDouble("Economy.currency-worth." + items.get(1)) * amount;
                    }
                    if (cost > Double.parseDouble(el.get(Utility.BALANCE).replace(",", ""))) {
                        me.msg(me.notEnoughMoney().replaceAll("%world%", p.getWorld().getName()));
                        return true;
                    }
                    double worth = fc.getInt("Economy.currency-worth." + items.get(0));
                    if (!withdrawType.equals("dollar")) {
                        worth = fc.getDouble("Economy.currency-worth." + items.get(1));
                    }
                    double result = worth * amount;
                    if (String.valueOf(result).contains("-")) {
                        me.msg("There was a problem with the amount you've chosen");
                        return true;
                    }
                    el.remove(result);
                    ItemStack item = new ItemStack(Items.getMaterial(items.get(0)));
                    if (!withdrawType.equals("dollar")) {
                        item = new ItemStack(Items.getMaterial(items.get(1)));
                    }
                    if (usingCustomCurrency()) {
                        item = new ItemStack(Items.getMaterial(getPrimaryDollarItem()));
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&o" + getPrimaryDollar()));
                        item.setItemMeta(meta);
                    }
                    for (int i = 0; i < amount; i++) {
                        p.getLocation().getWorld().dropItem(p.getLocation(), item);
                    }
                    return true;
                } catch (NumberFormatException e) {
                    me.msg(me.amountTooLarge());
                }
            }

            if (args[0].equalsIgnoreCase("bank")) {
                BankListener bl = new BankListener(p, randomAlphaNumeric(7), p.getWorld().getName());
                if (GoldEconomy.usingBanks()) {
                    if (args[1].equalsIgnoreCase("create")) {
                        if (!bl.has(Utility.BANK_ACCOUNT, p.getWorld().getName())) {
                            bl.create();
                        } else {
                            me.msg(me.accountAlreadyMade());
                            return true;
                        }
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("reset")) {
                            bl.create();
                        return true;
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission(this.getPermission() + ".give")) {
                    me.msg(noPermission(this.getPermission() + ".give"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco give playerName ##.##" + '"');
                return true;
            }

            if (args[0].equalsIgnoreCase("take")) {
                if (!p.hasPermission(this.getPermission() + ".take")) {
                    me.msg(noPermission(this.getPermission() + ".take"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco take playerName ##.##" + '"');
                return true;
            }

            if (args[0].equalsIgnoreCase("set")) {
                if (!p.hasPermission(this.getPermission() + ".set")) {
                    me.msg(noPermission(this.getPermission() + ".set"));
                    return true;
                }
                me.msg("&c&oNot enough arguments. Expected &7&o" + '"' + "/eco set playerName&f,&7&oaccountID ##.##" + '"');
                return true;
            }

        }

        if (length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                if (!p.hasPermission(this.getPermission() + ".give")) {
                    me.msg(noPermission(this.getPermission() + ".give"));
                    return true;
                }
                if (!isDouble(args[2])) {
                    me.msg(me.invalidDouble());
                    return true;
                }
//                PlayerListener list = new PlayerListener();
                String uuid = "";
                for (String id : PlayerListener.getAllPlayers()) {
                    if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                        uuid = id;
                    }
                }
                OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                PlayerListener el = new PlayerListener(pl);
                double current = Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", ""));
                el.set(current + Double.parseDouble(args[2]));
                me.msg(me.staffMoneyGiven().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                return true;
            }
            if (args[0].equalsIgnoreCase("take")) {
                if (!p.hasPermission(this.getPermission() + ".take")) {
                    me.msg(noPermission(this.getPermission() + ".take"));
                    return true;
                }
                if (!isDouble(args[2])) {
                    me.msg(me.invalidDouble());
                    return true;
                }
//                PlayerListener list = new PlayerListener();
                String uuid = "";
                for (String id : PlayerListener.getAllPlayers()) {
                    if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                        uuid = id;
                    }
                }
                OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                PlayerListener el = new PlayerListener(pl);
                double current = Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", ""));
                el.set(current - Double.parseDouble(args[2]));
                me.msg(me.staffMoneyTaken().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                return true;
            }
            if (args[0].equalsIgnoreCase("set")) {
                String worldName = args[2];
                if (!p.hasPermission(this.getPermission() + ".set")) {
                    me.msg(noPermission(this.getPermission() + ".set"));
                    return true;
                }
//                PlayerListener list = new PlayerListener();
                String uuid = "";
                for (String id : PlayerListener.getAllPlayers()) {
                    if (PlayerListener.nameByUUID(UUID.fromString(id)).equals(args[1])) {
                        uuid = id;
                    }
                }
                if (!isDouble(args[2])) {
                    me.msg(me.invalidDouble());
                    return true;
                }
                // this might be an account type
                // check for account type
                    if (GoldEconomy.getBankAccounts().contains(args[1])) {
                        if (!p.hasPermission(this.getPermission() + ".set.account")) {
                            me.msg(noPermission(this.getPermission() + ".set.account"));
                            return true;
                        }
                        worldName = GoldEconomy.getBankWorld(args[1]);
                        BankListener bl = new BankListener(p, args[1], worldName);
                        bl.set(Double.parseDouble(args[2]));
                        me.msg(me.staffAccountSet().replaceAll("%account%", args[1]).replaceAll("%amount%", args[2]));
                    } else {
                        try {
                            OfflinePlayer pl = Bukkit.getOfflinePlayer(UUID.fromString(uuid));

                            if (PlayerListener.getAllPlayers().contains(pl.getUniqueId().toString())) {
                                if (!p.hasPermission(this.getPermission() + ".set.player")) {
                                    me.msg(noPermission(this.getPermission() + ".set.player"));
                                    return true;
                                }

                                PlayerListener el = new PlayerListener(pl);
                                el.set(Double.parseDouble(args[2]));
                                me.msg(me.staffMoneySet().replaceAll("%player%", args[1]).replaceAll("%amount%", args[2]));
                                return true;
                            } else
                                me.msg("&c&oInvalid response. Expected:&r [&7playerName &ror &7accountID&r].");
                        } catch (IllegalArgumentException e) {
                            me.msg("&c&oInvalid response. Expected:&r [&7playerName &ror &7accountID&r].");
                        }
                    }
                return true;
            }
            if (args[0].equalsIgnoreCase("buy")) {
                if (!GoldEconomy.usingShop()) {
                    me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                    return true;
                }
                if (!ItemManager.getShopContents().contains(args[1])) {
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                try {
                    Material item = Items.getMaterial(args[1]);
                    if (item == null) {
                        // item not found
                        me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                        return true;
                    }
                    PlayerListener el = new PlayerListener(p);
                    el.buy(args[1], Integer.parseInt(args[2]));
                    return true;
                } catch (NumberFormatException e) {
                    me.msg(me.amountTooLarge());
                }
            }
            if (args[0].equalsIgnoreCase("sell")) {
                if (!GoldEconomy.usingShop()) {
                    me.msg("&c&oUse of the &6&oGoldEconomy &c&oitem shop is disabled on this server.");
                    return true;
                }
                if (!ItemManager.getShopContents().contains(args[1])) {
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                try {
                Material item = Items.getMaterial(args[1]);
                if (item == null) {
                    // item not found
                    me.msg(me.nameUnknown().replaceAll("%args%", args[1]));
                    return true;
                }
                PlayerListener el = new PlayerListener(p);
                el.sell(args[1], Integer.parseInt(args[2]));
                return true;
                } catch (NumberFormatException e) {
                    me.msg(me.amountTooLarge());
                }
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (args[2].equalsIgnoreCase(getPrimaryDollar())) {

                    int amount = Integer.parseInt(args[1]);
                    PlayerListener el = new PlayerListener(p);
//                    ItemManager im = GoldEconomy.getItemManager();
                   if (amount > Double.valueOf(el.get(Utility.BALANCE))) {
                       me.msg(me.notEnoughMoney().replaceAll("%world%", p.getWorld().getName()));
                       return true;
                   }
                    if (amount > 640) {
                        me.msg(me.maxWithdrawReached());
                        return true;
                    }
                    el.remove(amount);
                    ItemStack item = new ItemStack(Material.GOLD_INGOT, amount);
                    if (usingCustomCurrency()) {
                        item = new ItemStack(Items.getMaterial(getPrimaryDollarItem()));
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&o" + getPrimaryDollar()));
                        item.setItemMeta(meta);
                    }
                    for (int i = 0; i < amount; i++) {
                        p.getLocation().getWorld().dropItem(p.getLocation(), item);
                    }
                    return true;
                }
                if (args[2].equalsIgnoreCase(getSecondaryDollar())) {

                    int amount = Integer.parseInt(args[1]);
                    PlayerListener el = new PlayerListener(p);
//                    ItemManager im = GoldEconomy.getItemManager();
                    if (amount > Double.valueOf(el.get(Utility.BALANCE))) {
                        me.msg(me.notEnoughMoney().replaceAll("%world%", p.getWorld().getName()));
                        return true;
                    }
                    if (amount > 640) {
                        me.msg(me.maxWithdrawReached());
                        return true;
                    }
                    el.remove(amount);
                    ItemStack item = new ItemStack(Material.GOLD_INGOT, amount);
                    if (usingCustomCurrency()) {
                        item = new ItemStack(Items.getMaterial(getSecondaryDollarItem()));
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&a&o" + getPrimaryDollar()));
                        item.setItemMeta(meta);
                    }
                    for (int i = 0; i < amount; i++) {
                        p.getLocation().getWorld().dropItem(p.getLocation(), item);
                    }
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("deposit")) {
                if (args[2].equalsIgnoreCase(getPrimaryDollar())) {

                int amount = Integer.parseInt(args[1]);
                PlayerListener el = new PlayerListener(p);
//                ItemManager im = GoldEconomy.getItemManager();
                ArrayList<Material> mats = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                boolean hasAny = false;
                    mats.add(Items.getMaterial(getPrimaryDollarItem()));
                    names.add(getPrimaryDollar());
                    ItemStack item = new ItemStack(Items.getMaterial(getPrimaryDollarItem()));
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&o" + getPrimaryDollar()));
                    item.setItemMeta(meta);
                if (ItemManager.removeItem(p, item, amount).transactionSuccess) {
                    double value = fc.getDouble("Economy.currency-worth." + getCurrency().name()) * amount;
                    if (usingCustomCurrency()) {
                        value = fc.getDouble("Economy.custom-currency.name-value");
                    }
                    el.add(value);
                }
                    return true;
                }
                if (args[2].equalsIgnoreCase(getSecondaryDollar())) {

                    int amount = Integer.parseInt(args[1]);
                    PlayerListener el = new PlayerListener(p);
//                    ItemManager im = GoldEconomy.getItemManager();
                    ArrayList<Material> mats = new ArrayList<>();
                    ArrayList<String> names = new ArrayList<>();
                    boolean hasAny = false;
                        mats.add(Items.getMaterial(getSecondaryDollarItem()));
                    names.add(getSecondaryDollar());
                    for (Material mat : mats) {
                        if (p.getInventory().contains(mat)) {
                            ItemStack item = new ItemStack(mat);
                            if (p.getInventory().contains(item.getType())) {
                                for (int i = 0; i < amount; i++) {
                                    hasAny = true;
                                    setCurrency(mat);
                                    break;
                                }

                            }

                        }
                    }
                    boolean transactionSuccess = false;
                    if (hasAny) {
                        transactionSuccess = ItemManager.removeItem(p, getCurrency(), amount).transactionSuccess;
                    } else {
                        me.msg(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&c&oYou have no money to deposit. &fValid types: " + names.toString()));
                    }
                    if (transactionSuccess) {
                        double value = fc.getDouble("Economy.currency-worth." + getCurrency().name()) * amount;
                        if (usingCustomCurrency()) {
                            value = fc.getDouble("Economy.custom-currency.change-value");
                        }
                        el.add(value);
                    }
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("bank")) {
                String worldName = args[2];
                if (GoldEconomy.usingBanks()) {
                    if (args[1].equalsIgnoreCase("create")) {
                        BankListener bl = new BankListener(p, randomAlphaNumeric(7), worldName);
                        if (!bl.has(Utility.BANK_ACCOUNT)) {
                            bl.create();
                        } else {
                            me.msg(me.accountAlreadyMade());
                            return true;
                        }
                    }
                }
                return true;
            }
        }


        return false;
    }
}
