package com.youtube.hempfest.goldeco.listeners;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.BankData;
import com.youtube.hempfest.goldeco.structure.EconomyStructure;
import com.youtube.hempfest.goldeco.util.libraries.StringLibrary;
import com.youtube.hempfest.goldeco.util.Utility;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankListener implements EconomyStructure {

    OfflinePlayer op;
    String accountID;
    String worldName;

    BankData bank;
    FileConfiguration fc;

    /**
     * @deprecated messes with multi-world balances. Use at own risk
     */
    @Deprecated
    public BankListener(@NotNull OfflinePlayer p) { // Messes with multi-world balances
        this.op = p;
    }

    /**
     * Replaces above method but requires an instance of Player, helping to affirm
     * that it's a good idea for us to use their current World to run transactions.
     */
    public BankListener(@NotNull Player p) {
        this.op = p;
        this.worldName = p.getWorld().getName();
    }

    @Deprecated
    public BankListener(@NotNull OfflinePlayer p, String accountID) { // one bank per world = one bank per player per world
        this.op = p;
        this.accountID = accountID;
    }

    /**
     * Use this to interact with accounts for which you already know the ID and world
     */
    public BankListener(String accountID, String worldName) {
        this.accountID = accountID;
        this.worldName = worldName;
    }

    public BankListener(OfflinePlayer p, String accountID, String worldName) {
        this.op = p;
        this.worldName = worldName;
        this.accountID = accountID;
    }

    @Override
    public void set(double amount) {
        bank = BankData.get(worldName);
        fc = bank.getConfig();
        fc.set("banks." + GoldEconomy.getBankOwner(accountID) + ".balance", amount);
        bank.saveConfig();
        OfflinePlayer pl = Bukkit.getPlayer(GoldEconomy.getBankOwner(accountID));
        if (pl != null) {
            if (pl.isOnline()) {
                StringLibrary me = new StringLibrary(pl.getPlayer());
//                PlayerListener el = new PlayerListener();
                me.msg(StringLibrary.accountBalanceSet(accountID, PlayerListener.format(amount)));
                return;
            }
        }
    }

    @Override
    public void add(double amount) {
        if (op == null) return; // we don't deal with null
    PlayerListener el = new PlayerListener(op);
        bank = BankData.get(worldName);
        fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current + amount;
        if (Double.valueOf(el.get(Utility.BALANCE).replaceAll(",", "")) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(StringLibrary.notEnoughMoney(op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.remove(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(StringLibrary.accountDeposit(String.valueOf(amount), get(Utility.NAME)));
        }
    }

    @Override
    public void add(double amount, String worldName) {
        if (op == null) return;
        PlayerListener el = new PlayerListener(op);
        bank = BankData.get(worldName);
        fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current + amount;
        if (Double.valueOf(el.get(Utility.BALANCE).replaceAll(",", "")) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(StringLibrary.notEnoughMoney(op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.remove(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(StringLibrary.accountDeposit(String.valueOf(amount), get(Utility.NAME)));
        }
    }

    @Override
    public void remove(double amount) {
        if (op == null) return;
        PlayerListener el = new PlayerListener(op);
        bank = BankData.get(worldName);
        fc = bank.getConfig();
        double current = fc.getDouble("banks." + op.getName() + ".balance");
        double result = current - amount;
        if (Double.valueOf(get(Utility.BALANCE)) < amount) {
            // not enough
            if (op.isOnline()) {
                StringLibrary lib = new StringLibrary(op.getPlayer());
                lib.msg(StringLibrary.notEnoughMoney(op.getPlayer().getWorld().getName()));
            }
            return;
        }
        el.add(amount);
        fc.set("banks." + op.getName() + ".balance", result);
        bank.saveConfig();
        if (op.isOnline()) {
            StringLibrary lib = new StringLibrary(op.getPlayer());
            lib.msg(StringLibrary.accountWithdraw(String.valueOf(amount), get(Utility.NAME)));
        }
    }

    @Override
    public void remove(double amount, String worldName) { // TODO

    }

    @Override
    public void create() { // TODO: Examine this method
    if (!GoldEconomy.getWorlds().contains(worldName)) {
        if (op.isOnline()) {
            Player p = op.getPlayer();
            StringLibrary lib = new StringLibrary(p);
            lib.msg(StringLibrary.accountNotAllowed());
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
                    StringLibrary lib = new StringLibrary(p);
                    lib.msg(StringLibrary.accountMade(accountID, p.getWorld().getName()));
                }
                world.saveConfig();
                return;
            }


    }

    @Override
    public boolean has(Utility type) {
        switch (type) {
            case BANK_ACCOUNT:
                if (op != null) {
                    bank = BankData.get(worldName);
/*                    if (!bank.exists()) {
                        bank.getConfig().createSection("banks");
                        bank.saveConfig();
                    }*/ // TODO: move this type of code to BankData
                    final ConfigurationSection banks = bank.getConfig().getConfigurationSection("banks");
                    if (banks != null && banks.getKeys(false).contains(op.getName())) { // Just needed a null-check
                        return true;
                    }
                };
        }
        return false;
    }

    @Override
    public boolean has(Utility type, String worldName) {
        switch (type) {
            case BANK_ACCOUNT:
                    bank = BankData.get(worldName);
                    if (bank.exists()) {
                        FileConfiguration fc = bank.getConfig();
                        if (!bank.getConfig().contains("banks")) {
                            return false;
                        }
                        final ConfigurationSection banks = fc.getConfigurationSection("banks");
                        if (banks != null && banks.getKeys(false).contains(op.getName()))
                            return true;
                    }
        }
        return false;
    }

    @Override
    public String get(Utility type) {
        String result = "";
        switch (type) {
            case NAME:
                if (op != null) {
                    final Player p = op.getPlayer();
                    if (op.isOnline() && p != null) {
                        BankData data = BankData.get(p.getWorld().getName());
                        fc = data.getConfig();

                        final ConfigurationSection banks = fc.getConfigurationSection("banks");
                        if (banks != null && !banks.getKeys(false).isEmpty()) {
                            if (banks.getKeys(false).contains(p.getName())) {
                                result = fc.getString("banks." + p.getName() + ".accountID");
                            }
                        }
                        break;
                    }
                    BankData data = BankData.get(GoldEconomy.getMainWorld());
                    fc = data.getConfig();
                    final ConfigurationSection banks = fc.getConfigurationSection("banks");
                    if (banks != null && banks.getKeys(false).contains(op.getName())) {
                        result = fc.getString("banks." + op.getName() + ".accountID");
                    }
                    break;
                }
            case BALANCE:
                if (op != null && op.isOnline()) {
                    final Player p = op.getPlayer();
                    assert p != null;
                    BankData data = BankData.get(p.getWorld().getName());
                    fc = data.getConfig();
                    final ConfigurationSection banks = fc.getConfigurationSection("banks");
                    if (banks != null && !banks.getKeys(false).isEmpty()) {
                        if (banks.getKeys(false).contains(p.getName())) {
                            result = fc.getString("banks." + p.getName() + ".balance");
                        }
                    }
                    break;
                }
                bank = BankData.get(GoldEconomy.getMainWorld());
                fc = bank.getConfig();
                final ConfigurationSection banks = fc.getConfigurationSection("banks");
                if (banks != null && banks.getKeys(false).contains(op.getName())) {
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
