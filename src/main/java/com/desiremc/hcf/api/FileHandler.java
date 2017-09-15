package com.desiremc.hcf.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.desiremc.hcf.DesireCore;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sun.security.krb5.internal.crypto.Des;

/**
 * @author Michael Ziluck
 */
public class FileHandler
{

    private static List<FileHandler> instances = new LinkedList<>();

    protected File file;

    protected FileConfiguration fileConfig;

    protected HashMap<String, Object> history;

    /**
     * Construct a new optimized file handler.
     *
     * @param file
     */
    public FileHandler(File file)
    {
        instances.add(this);
        history = new HashMap<>();
        this.file = file;
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Reloads the {@link}. This resets the file and clears the
     * history object.
     *
     * @param
     */
    public void reload()
    {
        this.fileConfig = YamlConfiguration.loadConfiguration(file);
        history.clear();
    }

    /**
     * Gets a formatted string from the config file. Replaces any color
     * placeholders as well. If the string does not exist in the config, returns
     * null.
     *
     * @param key
     * @return the formatted string.
     */
    public String getString(String key)
    {
        key = key.toLowerCase();
        String message = null;
        Object o = history.get(key);
        if (o != null && o instanceof String)
        {
            return (String) o;
        }
        message = fileConfig.getString(key);
        if (message != null)
        {
            message = ChatColor.translateAlternateColorCodes('&', fileConfig.getString(key));
            history.put(key, message);
            return message;
        } else
        {
            return key;
        }
    }

    /**
     * Gets a double value from history or the config. If it does not exist,
     * returns 0.
     *
     * @param key
     * @return the value.
     */
    public double getDouble(String key)
    {
        key = key.toLowerCase();
        double value;
        Object o = history.get(key);
        if (o != null && o instanceof Double)
        {
            return (Double) o;
        }
        value = fileConfig.getDouble(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a integer value from history or the config. If it does not exist,
     * returns 0.
     *
     * @param key
     * @return the value.
     */
    public int getInteger(String key)
    {
        key = key.toLowerCase();
        int value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer)
        {
            return (Integer) o;
        }
        value = fileConfig.getInt(key);
        history.put(key, value);
        return value;
    }

    public ConfigurationSection getConfigurationSection(String key)
    {
        return fileConfig.getConfigurationSection(key);
    }

    /**
     * Gets a boolean value from history or the config. If it does not exist,
     * returns 0.
     *
     * @param key
     * @return the value.
     */
    public boolean getBoolean(String key)
    {
        key = key.toLowerCase();
        boolean value;
        Object o = history.get(key);
        if (o != null && o instanceof Integer)
        {
            return (Boolean) o;
        }
        value = fileConfig.getBoolean(key);
        history.put(key, value);
        return value;
    }

    /**
     * Gets a formatted string list from the config file. Replaces any color
     * placeholders as well. If the string list does not exist in the config,
     * returns null.
     *
     * @param key
     * @return the formatted string list.
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(String key)
    {
        key = key.toLowerCase();
        Object o = history.get(key);
        if (o != null && o instanceof List<?>)
        {
            return (List<String>) o;
        }
        List<String> list = new LinkedList<>();
        for (String str : fileConfig.getStringList(key))
        {
            list.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        if (list.size() == 0)
        {
            list.add(getString(key));
        }
        return list;
    }

    public void setString(String key, String value)
    {
        key = key.toLowerCase();
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setBoolean(String key, boolean value)
    {
        key = key.toLowerCase();
        fileConfig.set(key, value);
        history.put(key, value);
        try
        {
            fileConfig.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void reloadAll()
    {
        for (FileHandler lang : instances)
        {
            lang.reload();
        }
    }

}