package com.youtube.hempfest.goldeco.listeners;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.BankData;
import com.youtube.hempfest.goldeco.structure.EconomyStructure;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.goldeco.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BankListener implements EconomyStructure {

    OfflinePlayer op;
    String accountID;
    String worldName;

    public BankListener(OfflinePlayer p) {
        this.op = p;
    }

    public BankListener(OfflinePlayer p, String accountID) {
        this.op = p;
        this.accountID = accountID;
    }

    public BankListener(OfflinePlayer p, String accountID, String worldName) {
        this.op = p;
        this.worldName = worldName;
        this.accountID = accountID;
    }

    @Override
    public void set(double amount) {
        BankData bank = BankData.get(worldName);
        FileConfiguration fc = bank.getConfig();
        fc.set("banks." + GoldEconomy.getBankOwner(accountID) + ".balance", amount);
        bank.saveConfig();
        OfflinePlayer pl = Bukkit.getPlayer(GoldEconomy.getBankOwner(accountID));
        if (pl != null) {
            if (pl.isOnline()) {
                StringLibrary me = new StringLibrary(pl.getPlayer());
//                PlayerListener el = new PlayerListener();
                me.msg(me.accountBalanceSet().replaceAll("%account%", accountID).replaceAll("%balance%", PlayerListener.format(amount)));
                return;
            }
        }
    }

    @Override
    public void add(double amount) {
    PlayerListener el = new PlayerListener(op);
        BankData bank = BankData.get(worldName);
        FileConfiguration fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current + amount;
        if (Double.valueOf(el.get(Utility.BALANCE).replaceAll(",", "")) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(lib.notEnoughMoney().replaceAll("%world%", op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.remove(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(lib.accountDeposit().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%account%", get(Utility.NAME)));
        }
    }

    @Override
    public void add(double amount, String worldName) {
        PlayerListener el = new PlayerListener(op);
        BankData bank = BankData.get(worldName);
        FileConfiguration fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current + amount;
        if (Double.valueOf(el.get(Utility.BALANCE).replaceAll(",", "")) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(lib.notEnoughMoney().replaceAll("%world%", op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.remove(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(lib.accountDeposit().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%account%", get(Utility.NAME)));
        }
    }

    @Override
    public void remove(double amount) {
        PlayerListener el = new PlayerListener(op);
        BankData bank = BankData.get(worldName);
        FileConfiguration fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current - amount;
        if (Double.valueOf(get(Utility.BALANCE)) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(lib.notEnoughMoney().replaceAll("%world%", op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.add(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(lib.accountWithdraw().replaceAll("%amount%", String.valueOf(amount)).replaceAll("%account%", get(Utility.NAME)));
        }
    }

    @Override
    public void remove(double amount, String worldName) {

    }

    @Override
    public void create() {
    if (!GoldEconomy.getWorlds().contains(worldName)) {
        if (op.isOnline()) {
            Player p = op.getPlayer();
            StringLibrary lib = new StringLibrary(p, "");
            lib.msg(lib.accountNotAllowed());
        }
        return;
    }
    BankData world = BankData.get(worldName);
    FileConfiguration fc = world.getConfig();
            if (!fc.isConfigurationSection(accountID)) {
                fc.set("banks." + op.getName() + ".accountID", accountID);
                fc.set("banks." + op.getName() + ".balance", 0.0);
                if (op.isOnline()) {
                    Player p = op.getPlayer();
                    StringLibrary lib = new StringLibrary(p, "");
                    lib.msg(lib.accountMade().replaceAll("%account%", accountID).replaceAll("%account_world%", p.getWorld().getName()));
                }
                world.saveConfig();
                return;
            }


    }

    @Override
    public boolean has(Utility type) {
        boolean result = false;
        switch (type) {
            case BANK_ACCOUNT:
                if (op != null) {
                    BankListener bl = new BankListener(op);
                    BankData bank = BankData.get(worldName);
                    if (!bank.exists()) {
                        bank.getConfig().createSection("banks");
                        bank.saveConfig();
                    }
                        if (bank.getConfig().getConfigurationSection("banks").getKeys(false).contains(op.getName()))
                        result = true;
                    break;
                }
                break;
        }
        return result;
    }

    @Override
    public boolean has(Utility type, String worldName) {
        boolean result = false;
        switch (type) {
            case BANK_ACCOUNT:
                    BankData bank = BankData.get(worldName);
                    if (bank.exists()) {
                        FileConfiguration fc = bank.getConfig();
                        if (!bank.getConfig().contains("banks")) {
                            result = false;
                            break;
                        }
                        if (fc.getConfigurationSection("banks").getKeys(false).contains(op.getName()))
                            result = true;
                    }
                break;
        }
        return result;
    }

    @Override
    public String get(Utility type) {
        String result = "";
        switch (type) {
            case NAME:
                if (op != null) {
                    if (op.isOnline()) {
                        Player p = op.getPlayer();
                        BankData data = BankData.get(p.getWorld().getName());
                        FileConfiguration fc = data.getConfig();

                        if (!fc.getConfigurationSection("banks").getKeys(false).isEmpty()) {
                            if (fc.getConfigurationSection("banks").getKeys(false).contains(p.getName())) {
                                result = fc.getString("banks." + p.getName() + ".accountID");
                            }
                        }
                        break;
                    }
                    BankData data = BankData.get(GoldEconomy.getMainWorld());
                    FileConfiguration fc = data.getConfig();
                    if (fc.getConfigurationSection("banks").getKeys(false).contains(op.getName())) {
                        result = fc.getString("banks." + op.getName() + ".accountID");
                    }
                    break;
                }
            case BALANCE:
                if (op.isOnline()) {
                    Player p = op.getPlayer();
                    BankData data = BankData.get(p.getWorld().getName());
                    FileConfiguration fc = data.getConfig();
                    if (!fc.getConfigurationSection("banks").getKeys(false).isEmpty()) {
                        if (fc.getConfigurationSection("banks").getKeys(false).contains(p.getName())) {
                            result = fc.getString("banks." + p.getName() + ".balance");
                        }
                    }
                    break;
                }
                BankData data = BankData.get(GoldEconomy.getMainWorld());
                FileConfiguration fc = data.getConfig();
                if (fc.getConfigurationSection("banks").getKeys(false).contains(op.getName())) {
                    result = fc.getString("banks." + op.getName() + ".balance");
                }
                break;
        }
        return result;
    }

    @Override
    public double get(Utility type, String item) {
        return 0.0;
    }

    @Override
    public void buy(String item, int amount) {

    }

    @Override
    public void sell(String item, int amount) {

    }
}
