package com.youtube.hempfest.goldeco.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class BankData {
    private final String n;
    private FileConfiguration fc;
    private File file;

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(BankData.class);
    private static final Map<String, BankData> BANK_DATA = new ConcurrentHashMap<>();

    private BankData(@NotNull final String n) { // Force to factory
        this.n = Objects.requireNonNull(n);
    }

    public String getName() {
        return this.n; // sanitize in constructor
    }

    @Contract("null->null")
    public static BankData get(final String n) {
        if (n == null) return null;
        return BANK_DATA.computeIfAbsent(n, BankData::new);
    }

    public boolean delete() {
        return this.getFile().delete();
    }

    public boolean exists() {
        if(this.fc == null || this.file == null) {
            final File temp = new File(getDataFolder(), this.getName() + ".yml");
            if(!temp.exists()) {
                return false;
            }
            this.file = temp;
        }
        return true;
    }

    public File getFile() {
        if(this.file == null) {
            this.file = new File(getDataFolder(), this.getName() + ".yml"); //create method get data folder
            if(!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.file;
    }

    public FileConfiguration getConfig() {
        if(this.fc == null) {
            this.fc = YamlConfiguration.loadConfiguration(this.getFile());
        }
        return this.fc;
    }

    private static File getDataFolder() {
        final File dir = new File(BankData.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        final File d = new File(dir.getParentFile().getPath(), PLUGIN.getName() + "/Banks/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public static String getBankWorld(String accountID) {
        return CompletableFuture.supplyAsync(() -> {
            for (String world : getBankWorlds()) {
                final BankData data = BankData.get(world);
                final FileConfiguration fc = data.getConfig();
                final ConfigurationSection banks = fc.getConfigurationSection("banks");
                if (banks == null) continue; // skip this file no banks section
                for (String player : banks.getKeys(false)) {
                    final String entry = fc.getString("banks." + player + ".accountID");
                    if (entry != null && entry.equals(accountID)) {
                        return world;
                    }
                }
            }
            return "";
        }).join();
    }

    public static String getBankOwner(String accountID) { // TODO: migrate to dedicated object/UUID
        return CompletableFuture.supplyAsync(() -> {
            for (String world : getBankWorlds()) {
                final BankData data = BankData.get(world);
                final FileConfiguration fc = data.getConfig();
                final ConfigurationSection banks = fc.getConfigurationSection("banks");
                if (banks == null) continue; // skip this file, it doesn't have any banks
                for (String owner : banks.getKeys(false)) {
                    final String entry = fc.getString("banks." + owner + ".accountID");
                    if (entry != null && entry.equals(accountID)) {
                        return owner;
                    }
                }
            }
            return "";
        }).join();
    }

    public static List<String> getBankWorlds() {
        return CompletableFuture.supplyAsync(() -> {
            final List<String> users = new ArrayList<>();
            for(File file : BankData.getDataFolder().listFiles()) {
                users.add(file.getName().replace(".yml", ""));
            }
            return users;
        }).join();
    }

    public static List<String> getBankAccounts() {
        return CompletableFuture.supplyAsync(() -> {
            final List<String> accounts = new ArrayList<>();
            for (String world : getBankWorlds()) {
                final BankData data = BankData.get(world);
                final FileConfiguration fc = data.getConfig();
                final ConfigurationSection banks = fc.getConfigurationSection("banks");
                if (banks == null) continue; // skip processing this file
                for (String player : banks.getKeys(false)) {
                    accounts.add(fc.getString("banks." + player + ".accountID"));
                }
            }
            return accounts;
        }).join();
    }

    public void reload() {
        this.fc = YamlConfiguration.loadConfiguration(getFile());
        final File defConfigStream = new File(PLUGIN.getDataFolder(), this.getName() + ".yml");
        if(defConfigStream.exists()) {
            final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            this.fc.setDefaults(defConfig);
        }
    }

    public void saveConfig() {
        try {
            this.getConfig().save(this.getFile());
        }catch(final IOException e) {
            e.printStackTrace();
        }
    }

}
