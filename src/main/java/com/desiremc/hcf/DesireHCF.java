package com.desiremc.hcf;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.newcommands.CommandHandler;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.hcf.barrier.BarrierTask;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.commands.CobbleCommand;
import com.desiremc.hcf.commands.CoordsCommand;
import com.desiremc.hcf.commands.CraftCommand;
import com.desiremc.hcf.commands.CrowbarCommand;
import com.desiremc.hcf.commands.EnderChestCommand;
import com.desiremc.hcf.commands.HCFReloadCommand;
import com.desiremc.hcf.commands.LogoutCommand;
import com.desiremc.hcf.commands.OreCommand;
import com.desiremc.hcf.commands.PVPCommand;
import com.desiremc.hcf.commands.SetWinnerCommand;
import com.desiremc.hcf.commands.StartOfTheWorldCommand;
import com.desiremc.hcf.commands.factions.FactionsCommand;
import com.desiremc.hcf.commands.fstat.FStatCommand;
import com.desiremc.hcf.commands.kit.KitCommand;
import com.desiremc.hcf.commands.kit.KitManagementCommand;
import com.desiremc.hcf.commands.lives.LivesCommand;
import com.desiremc.hcf.commands.lives.ReviveCommand;
import com.desiremc.hcf.commands.region.RegionCommand;
import com.desiremc.hcf.commands.setend.SetEndCommand;
import com.desiremc.hcf.handler.CombatLoggerHandler;
import com.desiremc.hcf.handler.CrowbarHandler;
import com.desiremc.hcf.handler.EnchantmentLimiterHandler;
import com.desiremc.hcf.handler.EnderchestHandler;
import com.desiremc.hcf.handler.EnderpearlHandler;
import com.desiremc.hcf.handler.FurnaceSpeedHandler;
import com.desiremc.hcf.handler.GappleHandler;
import com.desiremc.hcf.handler.LootingBuffHandler;
import com.desiremc.hcf.handler.PotionLimiterHandler;
import com.desiremc.hcf.handler.SOTWHandler;
import com.desiremc.hcf.handler.TablistHandler;
import com.desiremc.hcf.listener.BlockListener;
import com.desiremc.hcf.listener.ChatListener;
import com.desiremc.hcf.listener.CombatListener;
import com.desiremc.hcf.listener.ConnectionListener;
import com.desiremc.hcf.listener.CreatureSpawnListener;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.listener.PickupListener;
import com.desiremc.hcf.listener.classes.ArcherListener;
import com.desiremc.hcf.listener.classes.ArmorListener;
import com.desiremc.hcf.listener.classes.ClassListener;
import com.desiremc.hcf.listener.classes.MinerListener;
import com.desiremc.hcf.listener.classes.RogueListener;
import com.desiremc.hcf.listener.factions.FactionListener;
import com.desiremc.hcf.listener.factions.PlayerListener;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.session.FSessionHandler;
import com.desiremc.hcf.session.HKitHandler;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class DesireHCF extends JavaPlugin
{

    private static DesireHCF instance;

    private static LangHandler lang;
    private static FileHandler config;

    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();
        saveResource("lang.yml", false);
        lang = new LangHandler(new File(getDataFolder(), "lang.yml"), this);
        config = new FileHandler(new File(getDataFolder(), "config.yml"), this);

        FSessionHandler.initialize();
        FactionHandler.initialize();
        RegionHandler.initialize();
        TagHandler.initialize();
        StaffHandler.initialize();
        DesireCore.getInstance().getMongoWrapper().getDatastore().ensureIndexes();
        BarrierTask.initialize();
        HKitHandler.initialize();

        new SOTWHandler();

        registerListeners();
        registerCommands();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable()
        {
            @Override
            public void run()
            {
                for (FSession fSession : FSessionHandler.getOnlineFSessions())
                {
                    fSession.save();
                }
            }
        }, 600, 600);
    }
    
    public void onDisable()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            FactionHandler.takeClaimWand(player);
        }
    }
    

    private void registerListeners()
    {
        ListenerManager listenerManager = ListenerManager.getInstace();
        listenerManager.addListener(new ChatListener(), this);
        listenerManager.addListener(new ConnectionListener(), this);
        listenerManager.addListener(new MovementListener(), this);
        listenerManager.addListener(new CombatListener(), this);
        listenerManager.addListener(new CrowbarHandler(), this);
        listenerManager.addListener(new CreatureSpawnListener(), this);
        listenerManager.addListener(new ArmorListener(getConfigHandler().getStringList("blocked")), this);
        listenerManager.addListener(new ClassListener(), this);
        listenerManager.addListener(new ArcherListener(), this);
        listenerManager.addListener(new MinerListener(), this);
        listenerManager.addListener(new RogueListener(), this);
        listenerManager.addListener(new TablistHandler(), this);
        listenerManager.addListener(new PickupListener(), this);
        listenerManager.addListener(new FurnaceSpeedHandler(), this);
        listenerManager.addListener(new BlockListener(), this);

        // faction listeners
        listenerManager.addListener(new com.desiremc.hcf.listener.factions.ConnectionListener(), this);
        listenerManager.addListener(new FactionListener(), this);
        listenerManager.addListener(new PlayerListener(), this);

        // EVERYTHING BELOW HERE IS UNTESTED
        listenerManager.addListener(new EnderpearlHandler(), this);
        listenerManager.addListener(new GappleHandler(), this);
        listenerManager.addListener(new CombatLoggerHandler(), this);
        listenerManager.addListener(new EnderchestHandler(), this);
        //listenerManager.addListener(new BrewingSpeedHandler(), this);
        listenerManager.addListener(new LootingBuffHandler(), this);
        listenerManager.addListener(new EnchantmentLimiterHandler(), this);
        listenerManager.addListener(new PotionLimiterHandler(), this);
    }

    private void registerCommands()
    {
        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.registerCommand(new FStatCommand(), this);
        commandHandler.registerCommand(new SetWinnerCommand(), this);
        commandHandler.registerCommand(new RegionCommand(), this);
        commandHandler.registerCommand(new FactionsCommand(), this);
        //untested below this point
        commandHandler.registerCommand(new KitManagementCommand(), this);
        commandHandler.registerCommand(new OreCommand(), this);
        commandHandler.registerCommand(new CobbleCommand(), this);
        commandHandler.registerCommand(new ReviveCommand(), this);
        commandHandler.registerCommand(new LogoutCommand(), this);
        commandHandler.registerCommand(new PVPCommand(), this);
        commandHandler.registerCommand(new EnderChestCommand(), this);
        commandHandler.registerCommand(new HCFReloadCommand(), this);
        commandHandler.registerCommand(new SetEndCommand(), this);
        commandHandler.registerCommand(new LivesCommand(), this);
        commandHandler.registerCommand(new KitCommand(), this);
        commandHandler.registerCommand(new CrowbarCommand(), this);
        commandHandler.registerCommand(new CoordsCommand(), this);
        commandHandler.registerCommand(new StartOfTheWorldCommand());
        //commandHandler.registerCommand(new FactionFocusCommand());
        commandHandler.registerCommand(new CraftCommand());
    }

    public static WorldGuardPlugin getWorldGuard()
    {
        Plugin p = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (p == null)
        {
            System.out.println("This could would crash if that were to happen.");
            return null;
        }
        return (WorldGuardPlugin) p;
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
