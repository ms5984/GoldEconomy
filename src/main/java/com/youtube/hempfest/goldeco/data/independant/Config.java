package com.youtube.hempfest.goldeco.data.independant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;

public class Config {
    private final String n;
    private FileConfiguration fc;
    private File file;
    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(Config.class);
    private static final ArrayList<Config> CONFIGS = new ArrayList<>();

    public Config(@NotNull final String n) {
        this.n = n;
        CONFIGS.add(this);
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

    public String getName() { // TODO: explain this code
/*        if(this.n == null) {
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return this.n;*/
        return (n == null) ? "" : n; // Should we let the NPE pass through or stop it like this?
    }

/*    private static JavaPlugin getInstance() { // Should let an NPE do its job
        if(PLUGIN == null) { // this method doesn't even try to reinitialize the field
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return PLUGIN;
    }*/


    public static Config getConfig(final String n) {
        for(final Config c: Config.CONFIGS) {
            if(c.getName().equals(n)) {
                return c;
            }
        }

        return new Config(n);
    }

    public boolean delete() {
        return getFile().delete();
    }

    public boolean exists() {
        if(this.fc == null || this.file == null) {
            final File temp = new File(getDataFolder(), getName() + ".yml");
            if(!temp.exists()) {
                return false;
            }
            this.file = temp;
        }
        return true;
    }

    public File getFile() {
        if(file == null) {
            file = new File(getDataFolder(), getName() + ".yml"); //create method get data folder
            if(!file.exists()) {
                try {
                    file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public FileConfiguration getConfig() {
        if(this.fc == null) {
            this.fc = YamlConfiguration.loadConfiguration(getFile());
        }
        return this.fc;
    }

    private static File getDataFolder() { // TODO: add clarifying comments
        final File dir = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        final File d = new File(dir.getParentFile().getPath(), PLUGIN.getName() + "/Configuration/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public void reload() {
/*        if(this.file == null) { // Why are we only running this code if file is null?
            this.file = new File(getDataFolder(), this.getName() + ".yml");
            if(!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }

            this.fc = YamlConfiguration.loadConfiguration(this.file);
            final File defConfigStream = new File(PLUGIN.getDataFolder(), this.getName() + ".yml");
//            if(defConfigStream != null) { // defConfigStream is never null, is this supposed to be .exists()?
                final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                this.fc.setDefaults(defConfig);
//            }
        }*/
        this.fc = YamlConfiguration.loadConfiguration(getFile());
        final File defConfigStream = new File(PLUGIN.getDataFolder(), this.getName() + ".yml");
        final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        this.fc.setDefaults(defConfig);
    }

    public void saveConfig() {
        try {
            getConfig().save(getFile());
        }catch(final IOException e) {
            e.printStackTrace();
        }
    }






}
