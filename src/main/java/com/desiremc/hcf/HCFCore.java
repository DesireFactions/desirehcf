package com.desiremc.hcf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.commands.UnbanCommand;
import com.desiremc.core.listeners.ConnectionListener;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.session.AchievementManager;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.commands.AchievementCommand;
import com.desiremc.hcf.commands.CrowbarCommand;
import com.desiremc.hcf.commands.EnderChestCommand;
import com.desiremc.hcf.commands.HCFReloadCommand;
import com.desiremc.hcf.commands.PVPCommand;
import com.desiremc.hcf.commands.SettingsCommand;
import com.desiremc.hcf.commands.fstat.FStatCommand;
import com.desiremc.hcf.commands.lives.LivesCommand;
import com.desiremc.hcf.commands.region.RegionCommand;
import com.desiremc.hcf.commands.setend.SetEndCommand;
import com.desiremc.hcf.commands.ticket.TicketCommand;
import com.desiremc.hcf.listener.ChatListener;
import com.desiremc.hcf.listener.CombatListener;
import com.desiremc.hcf.listener.CreatureSpawnListener;
import com.desiremc.hcf.listener.CrowbarHandler;
import com.desiremc.hcf.listener.InteractListener;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.tickets.TicketHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.milkbowl.vault.economy.Economy;

public class HCFCore extends JavaPlugin
{

    private static HCFCore instance;

    private static RegisteredServiceProvider<Economy> economyProvider;

    @Override
    public void onEnable()
    {
        instance = this;

        HCFSessionHandler.initialize();
        FactionSessionHandler.initialize();
        RegionHandler.initialize();
        TagHandler.initialize();
        TicketHandler.initialize();
        StaffHandler.initialize();
        AchievementManager.initialize();
        CustomCommandHandler.initialize();

        registerListeners();
        registerCommands();

        economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ConnectionListener());
        listenerManager.addListener(new ChatListener());
        listenerManager.addListener(new MovementListener());
        listenerManager.addListener(new CombatListener());
        listenerManager.addListener(new InteractListener());
        listenerManager.addListener(new CrowbarHandler());
        listenerManager.addListener(new CreatureSpawnListener());
    }

    private void registerCommands()
    {
        CustomCommandHandler customCommandHandler = CustomCommandHandler.getInstance();
        customCommandHandler.registerCommand(new AchievementCommand());
        customCommandHandler.registerCommand(new CrowbarCommand());
        customCommandHandler.registerCommand(new EnderChestCommand());
        customCommandHandler.registerCommand(new FStatCommand());
        customCommandHandler.registerCommand(new HCFReloadCommand());
        customCommandHandler.registerCommand(new LivesCommand());
        customCommandHandler.registerCommand(new PVPCommand());
        customCommandHandler.registerCommand(new RegionCommand());
        customCommandHandler.registerCommand(new SetEndCommand());
        customCommandHandler.registerCommand(new SettingsCommand());
        customCommandHandler.registerCommand(new TicketCommand());
        customCommandHandler.registerCommand(new UnbanCommand());
    }

    public static RegisteredServiceProvider<Economy> getEconomy()
    {
        return economyProvider;
    }

    public static WorldEditPlugin getWorldEdit()
    {
        Plugin p = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (p == null)
        {
            System.out.println("This could would crash if that were to happen.");
            return null;
        }
        return (WorldEditPlugin) p;
    }

    public static HCFCore getInstance()
    {
        return instance;
    }

}
