package com.youtube.hempfest.goldeco.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private FileConfiguration fc;
    private File file;

    private static final JavaPlugin PLUGIN = JavaPlugin.getProvidingPlugin(PlayerData.class);
    private static final ArrayList<PlayerData> PLAYER_DATA = new ArrayList<>();

    private PlayerData(@NotNull final UUID uuid) {
        this.uuid = uuid;
        PLAYER_DATA.add(this);
    }

/*    public static void copy(InputStream in, File file) { // unused method
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
/*        if(this.u == null) { // it is better to let NPE happen, our object data should be sanitized
            try {
                throw new Exception();
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return this.u.toString();*/
        return (uuid == null) ? null : uuid.toString(); // TODO: decide if this is the right approach
    }

/*    public static JavaPlugin getInstance() { // method doesn't attempt to solve problem
        if(plugin == null) {
            try {
                throw new Exception(); // Better to let NPE go than throw our own
            }catch(final Exception e) {
                e.printStackTrace();
            }
        }
        return plugin;
    }*/


    public static PlayerData get(@NotNull final UUID uuid) {
        for(final PlayerData c: PlayerData.PLAYER_DATA) {
            if(c.uuid.equals(uuid)) {
                return c;
            }
        }
        return new PlayerData(uuid);
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
        final File dir = new File(PlayerData.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
        final File d = new File(dir.getParentFile().getPath(), PLUGIN.getName() + "/Players/");
        if(!d.exists()) {
            d.mkdirs();
        }
        return d;
    }

    public void reload() {
/*        if(this.file == null) { // So we can only reload if file is null?
            this.file = new File(getDataFolder(), this.getName() + ".yml");
            if(!this.file.exists()) {
                try {
                    this.file.createNewFile();
                }catch(final IOException e) {
                    e.printStackTrace();
                }
            }
        }*/ // really just a duplication of getFile(), so
        this.fc = YamlConfiguration.loadConfiguration(getFile());
        final File defConfigStream = new File(PLUGIN.getDataFolder(), this.getName() + ".yml");
        if(defConfigStream != null) { // TODO: is this is supposed to be an exists() check? this is a File obj
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
