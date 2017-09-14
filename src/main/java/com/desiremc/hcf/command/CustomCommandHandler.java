package com.desiremc.hcf.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.hcf.DesireCore;
import com.desiremc.hcf.session.Session;
import com.desiremc.hcf.session.SessionHandler;

/**
 * @author Michael Ziluck
 *
 */
public class CustomCommandHandler implements CommandExecutor
{

    private static CustomCommandHandler instance;
    
    private static CommandMap commandMapInstance = getCommandMap();

    private LinkedList<ValidCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        ValidCommand command = getCustomCommand(label);
        if (command != null)
        {
            Session s = sender instanceof Player ? SessionHandler.getSession(sender) : null;
            if (s == null || s.getRank().getId() >= command.getRequiredRank().getId())
            {
                command.run(sender, label, args);
            }
            else
            {
                DesireCore.getLangHandler().sendString(sender, "no-permissions");
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * @param command
     */
    public void registerCommand(ValidCommand command)
    {
        registerCommand(command, DesireCore.getInstance());
    }

    public void registerCommand(ValidCommand command, JavaPlugin plugin)
    {
        if (commands == null)
        {
            commands = new LinkedList<>();
        }

        PluginCommand bukkitCommand = createBukkitCommand(command.getName(), plugin);
        bukkitCommand.setAliases(Arrays.asList(command.getAliases()));
        commandMapInstance.register(plugin.getDescription().getName(), bukkitCommand);
        plugin.getCommand(command.getName()).setExecutor(this);

        commands.add(command);
    }

    private PluginCommand createBukkitCommand(String name, JavaPlugin plugin)
    {
        PluginCommand command = null;
        try
        {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex)
        {
            ex.printStackTrace();
        }

        return command;
    }

    private static CommandMap getCommandMap()
    {
        CommandMap commandMap = null;

        try
        {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager)
            {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return commandMap;
    }

    private ValidCommand getCustomCommand(String cmd)
    {
        for (ValidCommand command : commands)
        {
            if (command.matches(cmd)) { return command; }
        }
        return null;
    }

    public static void initialize() 
    {
        instance = new CustomCommandHandler();
    }
    
    public static CustomCommandHandler getInstance()
    {
        return instance;
    }

}