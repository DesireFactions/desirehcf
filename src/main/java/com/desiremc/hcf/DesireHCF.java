package com.desiremc.hcf;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.commands.UnbanCommand;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.session.HCFSessionHandler;
import com.desiremc.core.session.StaffHandler;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.commands.CrowbarCommand;
import com.desiremc.hcf.commands.EnderChestCommand;
import com.desiremc.hcf.commands.HCFReloadCommand;
import com.desiremc.hcf.commands.LogoutCommand;
import com.desiremc.hcf.commands.PVPCommand;
import com.desiremc.hcf.commands.fstat.FStatCommand;
import com.desiremc.hcf.commands.lives.LivesCommand;
import com.desiremc.hcf.commands.region.RegionCommand;
import com.desiremc.hcf.commands.setend.SetEndCommand;
import com.desiremc.hcf.listener.classes.ArcherListener;
import com.desiremc.hcf.listener.classes.ArmorListener;
import com.desiremc.hcf.listener.ChatListener;
import com.desiremc.hcf.listener.classes.BardListener;
import com.desiremc.hcf.listener.classes.ClassListener;
import com.desiremc.hcf.listener.CombatListener;
import com.desiremc.hcf.listener.ConnectionListener;
import com.desiremc.hcf.listener.CreatureSpawnListener;
import com.desiremc.hcf.listener.CrowbarHandler;
import com.desiremc.hcf.listener.InteractListener;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.listener.classes.MinerListener;
import com.desiremc.hcf.listener.classes.RogueListener;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.util.PlayerCache;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DesireHCF extends JavaPlugin
{

    private static DesireHCF instance;

    private final PlayerCache playerCache = new PlayerCache();

    private static LangHandler lang;
    private static FileHandler config;

    private static RegisteredServiceProvider<Economy> economyProvider;

    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();
        saveResource("lang.yml", false);
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"), this);
        config = new FileHandler(new File(getDataFolder(), "config.yml"), this);

        HCFSessionHandler.initialize();
        FactionSessionHandler.initialize();
        RegionHandler.initialize();
        TagHandler.initialize();
        StaffHandler.initialize();
        CustomCommandHandler.initialize();
        DesireCore.getInstance().getMongoWrapper().getDatastore().ensureIndexes();

        registerListeners();
        registerCommands();

        economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
    }

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ChatListener());
        listenerManager.addListener(new ConnectionListener());
        listenerManager.addListener(new MovementListener());
        listenerManager.addListener(new CombatListener());
        listenerManager.addListener(new InteractListener());
        listenerManager.addListener(new CrowbarHandler());
        listenerManager.addListener(new CreatureSpawnListener());
        listenerManager.addListener(new ArmorListener(getConfigHandler().getStringList("blocked")));
        listenerManager.addListener(new ClassListener());
        listenerManager.addListener(new ArcherListener());
        listenerManager.addListener(new BardListener());
        listenerManager.addListener(new MinerListener());
        listenerManager.addListener(new RogueListener());
    }

    private void registerCommands()
    {
        CustomCommandHandler customCommandHandler = CustomCommandHandler.getInstance();
        customCommandHandler.registerCommand(new CrowbarCommand(), instance);
        customCommandHandler.registerCommand(new EnderChestCommand(), instance);
        customCommandHandler.registerCommand(new FStatCommand(), instance);
        customCommandHandler.registerCommand(new HCFReloadCommand(), instance);
        customCommandHandler.registerCommand(new LivesCommand(), instance);
        customCommandHandler.registerCommand(new PVPCommand(), instance);
        customCommandHandler.registerCommand(new RegionCommand(), instance);
        customCommandHandler.registerCommand(new SetEndCommand(), instance);
        customCommandHandler.registerCommand(new UnbanCommand(), instance);
        customCommandHandler.registerCommand(new LogoutCommand(), instance);
    }

    public static RegisteredServiceProvider<Economy> getEconomy()
    {
        return economyProvider;
    }

    public PlayerCache getPlayerCache()
    {
        return playerCache;
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

    public static DesireHCF getInstance()
    {
        return instance;
    }

    public static LangHandler getLangHandler()
    {
        return lang;
    }

    public static FileHandler getConfigHandler()
    {
        return config;
    }

}
