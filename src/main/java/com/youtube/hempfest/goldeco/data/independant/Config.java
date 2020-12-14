package com.youtube.hempfest.goldeco.data.independant;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    public final String name; // strings are immutable; sanitized in constructor = can safely be public
    private FileConfiguration fc;
    private File file;
    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(Config.class);
    private static final Map<String, Config> CONFIGS = new ConcurrentHashMap<>();

    private Config(@NotNull final String name) {
        this.name = Objects.requireNonNull(name);
    }

    public static Config get(@NotNull final String name) {
        return CONFIGS.computeIfAbsent(name, Config::new);
    }

    public void copyFromResource(InputStream in) { // Converted to instance method
        try {
            final OutputStream outputStream = new FileOutputStream(getFile());
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                outputStream.write(buf,0,len);
            }
            outputStream.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean delete() {
        return getFile().delete();
    }

    public boolean exists() {
        if(this.file == null) { // if we haven't established a file; we might have a config but no backing
            final File temp = new File(getDataFolder(), name + ".yml");
            if(!temp.exists()) {
                return false;
            }
            this.file = temp;
        }
        return true;
    }

    private File getFile() { // better encapsulation
        if(file == null) {
            file = new File(getDataFolder(), name + ".yml"); //create method get data folder
            if(!file.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
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
            //noinspection ResultOfMethodCallIgnored
            d.mkdirs();
        }
        return d;
    }

    public void reload() {
        this.fc = YamlConfiguration.loadConfiguration(getFile());
        final File defConfigStream = new File(PLUGIN.getDataFolder(), name + ".yml");
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
