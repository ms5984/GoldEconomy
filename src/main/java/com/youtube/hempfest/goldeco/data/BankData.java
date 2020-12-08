package com.youtube.hempfest.goldeco.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class BankData {
    private final String n;
    private FileConfiguration fc;
    private File file;

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(BankData.class);
    private static final ArrayList<BankData> BANK_DATA = new ArrayList<>();

    private BankData(@NotNull final String n) { // Force to factory
        this.n = Objects.requireNonNull(n);
        BANK_DATA.add(this);
    }

/*    public static void copy(InputStream in, File file) { // unused lol
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public String getName() {
/*        if(this.n == null) {
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }*/
        return this.n; // sanitize in constructor
    }

/*    public static JavaPlugin getInstance() { // NPE works better
        if(plugin == null) {
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return plugin;
    }*/


    public static BankData get(final String n) {
        if (n == null) return null;
        for(final BankData c: BankData.BANK_DATA) {
            if(c.getName().equals(n)) {
                return c;
            }
        }
        return new BankData(n);
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

    public static File getDataFolder() {
        final File dir = new File(BankData.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        final File d = new File(dir.getParentFile().getPath(), PLUGIN.getName() + "/Banks/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public void reload() {
/*        if(this.file == null) {
            this.file = new File(getDataFolder(), this.getName() + ".yml");
            if(!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }*/ // should just do #getFile
        this.fc = YamlConfiguration.loadConfiguration(getFile());
        final File defConfigStream = new File(PLUGIN.getDataFolder(), this.getName() + ".yml");
        if(defConfigStream != null) { // TODO: File#exists() check?
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
