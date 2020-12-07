package com.youtube.hempfest.goldeco.listeners.vault;

import com.youtube.hempfest.goldeco.GoldEconomy;
import com.youtube.hempfest.goldeco.data.PlayerData;
import com.youtube.hempfest.goldeco.data.independant.Config;
import com.youtube.hempfest.goldeco.listeners.BankListener;
import com.youtube.hempfest.goldeco.listeners.PlayerListener;
import com.youtube.hempfest.goldeco.util.Utility;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class VaultEconomy implements Economy {

    GoldEconomy plugin = GoldEconomy.getInstance();

    Economy economy = plugin.eco;

    @Override
    public boolean isEnabled() {
        return GoldEconomy.usingVault();
    }

    @Override
    public String getName() {
        return "GoldEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return GoldEconomy.usingBanks();
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        BigDecimal b1 = new BigDecimal(amount);

        MathContext m = new MathContext(3); // 4 precision

        // b1 is rounded using m
        BigDecimal b2 = b1.round(m);
        return String.valueOf(b2.doubleValue());
    }

    @Override
    public String currencyNamePlural() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.name") + "'s";
    }

    @Override
    public String currencyNameSingular() {
        Config main = Config.get("shop_config");
        FileConfiguration fc = main.getConfig();
        return fc.getString("Economy.custom-currency.name");
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        PlayerListener bl = new PlayerListener(player);
        return bl.has(Utility.BALANCE);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        PlayerListener bl = new PlayerListener(player);
        return bl.has(Utility.BALANCE, worldName);
    }

    @Override
    public double getBalance(String playerName) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.exists()) {
            FileConfiguration fc = data.getConfig();
            if (player.isOnline()) {
                return fc.getDouble("player." + player.getPlayer().getWorld().getName() + ".balance");
            }
            return fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance");
        }
        return 0;
    }

    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.exists()) {
            FileConfiguration fc = data.getConfig();
            return fc.getDouble("player." + world + ".balance");
        }
        return 0;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.exists()) {
            FileConfiguration fc = data.getConfig();
            if (player.isOnline()) {
                if (fc.getDouble(player.getPlayer().getWorld().getName() + ".balance") >= amount) {
                    return true;
                }
            }
            if (fc.getDouble(GoldEconomy.getMainWorld() + ".balance") >= amount) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        PlayerData data = new PlayerData(player.getUniqueId());
        if (data.exists()) {
            FileConfiguration fc = data.getConfig();
            if (player.isOnline()) {
                if (fc.getDouble("player." + worldName + ".balance") >= amount) {
                    return true;
                }
            }
            if (fc.getDouble("player." + GoldEconomy.getMainWorld() + ".balance") >= amount) {
                return true;
            }
        }
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        PlayerListener el = new PlayerListener(player);
        if (Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")) >= amount) {
            el.remove(amount);
            return transactionSuccess(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
        }
        return transactionFail(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        PlayerListener el = new PlayerListener(player);
        if (Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")) >= amount) {
            el.remove(amount, worldName);
            return transactionSuccess(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
        }
        return transactionFail(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        PlayerListener el = new PlayerListener(player);
        el.add(amount);
        return transactionSuccess(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        PlayerListener el = new PlayerListener(player);
        el.add(amount, worldName);
        return transactionSuccess(amount, Double.parseDouble(el.get(Utility.BALANCE).replaceAll(",", "")));
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        if (player.isOnline()) {
            Player p = player.getPlayer();
            BankListener bl = new BankListener(p, name, p.getWorld().getName());
            bl.create();
        }else {
            BankListener bl = new BankListener(player, name, GoldEconomy.getMainWorld());
            bl.create();
        }
        return transactionSuccess(0.0, 0.0);
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    private EconomyResponse transactionSuccess(double amount, double balance) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "");
    }

    private EconomyResponse transactionFail(double amount, double balance) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds!");
    }

}
