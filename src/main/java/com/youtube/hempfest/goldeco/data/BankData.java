package com.youtube.hempfest.goldeco.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;

public class BankData {
    private final String n;
    private FileConfiguration fc;
    private File file;
    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(BankData.class);
    private static ArrayList<BankData> configs;

    static {
        BankData.configs = new ArrayList<BankData>();
    }

    public BankData(final String n) {
        this.n = n;
        BankData.configs.add(this);
    }

    public static void copy(InputStream in, File file) {
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
    }

    public String getName() {
        if(this.n == null) {
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return this.n;
    }

    public static JavaPlugin getInstance() {
        if(plugin == null) {
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return plugin;
    }


    public static BankData getConfig(final String n) {
        for(final BankData c: BankData.configs) {
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
            final File temp = new File(this.getDataFolder(), String.valueOf(this.getName()) + ".yml");
            if(!temp.exists()) {
                return false;
            }
            this.file = temp;
        }
        return true;
    }

    public File getFile() {
        if(this.file == null) {
            this.file = new File(this.getDataFolder(), String.valueOf(this.getName() + ".yml")); //create method get data folder
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
        final File d = new File(dir.getParentFile().getPath(), getInstance().getName() + "/Banks/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public void reload() {
        if(this.file == null) {
            this.file = new File(this.getDataFolder(), String.valueOf(this.getName() + ".yml"));
            if(!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }

            this.fc = YamlConfiguration.loadConfiguration(this.file);
            final File defConfigStream = new File(this.plugin.getDataFolder(), String.valueOf(this.getName())+ ".yml");
            if(defConfigStream != null) {
                final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.fc.setDefaults(defConfig);
            }
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
