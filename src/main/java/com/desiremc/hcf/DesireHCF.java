package com.desiremc.hcf;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.CustomCommandHandler;
import com.desiremc.core.listeners.ListenerManager;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.hcf.barrier.BarrierTask;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.commands.CobbleCommand;
import com.desiremc.hcf.commands.CoordsCommand;
import com.desiremc.hcf.commands.CrowbarCommand;
import com.desiremc.hcf.commands.EnderChestCommand;
import com.desiremc.hcf.commands.HCFReloadCommand;
import com.desiremc.hcf.commands.LogoutCommand;
import com.desiremc.hcf.commands.OreCommand;
import com.desiremc.hcf.commands.PVPCommand;
import com.desiremc.hcf.commands.fstat.FStatCommand;
import com.desiremc.hcf.commands.lives.LivesCommand;
import com.desiremc.hcf.commands.lives.ReviveCommand;
import com.desiremc.hcf.commands.region.RegionCommand;
import com.desiremc.hcf.commands.setend.SetEndCommand;
import com.desiremc.hcf.handler.BrewingSpeedHandler;
import com.desiremc.hcf.handler.CombatLoggerHandler;
import com.desiremc.hcf.handler.EnchantmentLimiterHandler;
import com.desiremc.hcf.handler.EnderchestHandler;
import com.desiremc.hcf.handler.EnderpearlHandler;
import com.desiremc.hcf.handler.FurnaceSpeedHandler;
import com.desiremc.hcf.handler.GappleHandler;
import com.desiremc.hcf.handler.LootingBuffHandler;
import com.desiremc.hcf.handler.PotionLimiterHandler;
import com.desiremc.hcf.handler.TablistHandler;
import com.desiremc.hcf.listener.BlockListener;
import com.desiremc.hcf.listener.ChatListener;
import com.desiremc.hcf.listener.CombatListener;
import com.desiremc.hcf.listener.ConnectionListener;
import com.desiremc.hcf.listener.CreatureSpawnListener;
import com.desiremc.hcf.listener.CrowbarHandler;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.listener.PickupListener;
import com.desiremc.hcf.listener.classes.ArcherListener;
import com.desiremc.hcf.listener.classes.ArmorListener;
import com.desiremc.hcf.listener.classes.BardListener;
import com.desiremc.hcf.listener.classes.ClassListener;
import com.desiremc.hcf.listener.classes.MinerListener;
import com.desiremc.hcf.listener.classes.RogueListener;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.session.HCFSession;
import com.desiremc.hcf.session.HCFSessionHandler;
import com.desiremc.hcf.session.RegionHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

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

        HCFSessionHandler.initialize();
        FactionSessionHandler.initialize();
        RegionHandler.initialize();
        TagHandler.initialize();
        StaffHandler.initialize();
        CustomCommandHandler.initialize();
        DesireCore.getInstance().getMongoWrapper().getDatastore().ensureIndexes();
        BarrierTask.initialize();

        registerListeners();
        registerCommands();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable()
        {
            @Override
            public void run()
            {
                for (HCFSession session : HCFSessionHandler.getSessions())
                {
                    HCFSessionHandler.getInstance().save(session);
                }
            }
        }, 10, 600);
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
        listenerManager.addListener(new BardListener(), this);
        listenerManager.addListener(new MinerListener(), this);
        listenerManager.addListener(new RogueListener(), this);
        listenerManager.addListener(new TablistHandler(), this);
        listenerManager.addListener(new PickupListener(), this);
        listenerManager.addListener(new FurnaceSpeedHandler(), this);
        listenerManager.addListener(new BlockListener(), this);
        // EVERYTHING BELOW HERE IS UNTESTED
        listenerManager.addListener(new EnderpearlHandler(), this);
        listenerManager.addListener(new GappleHandler(), this);
        listenerManager.addListener(new CombatLoggerHandler(), this);
        listenerManager.addListener(new EnderchestHandler(), this);
        listenerManager.addListener(new BrewingSpeedHandler(), this);
        listenerManager.addListener(new LootingBuffHandler(), this);
        listenerManager.addListener(new EnchantmentLimiterHandler(), this);
        listenerManager.addListener(new PotionLimiterHandler(), this);
    }

    private void registerCommands()
    {
        CustomCommandHandler customCommandHandler = CustomCommandHandler.getInstance();
        customCommandHandler.registerCommand(new CrowbarCommand(), this);
        customCommandHandler.registerCommand(new EnderChestCommand(), this);
        customCommandHandler.registerCommand(new FStatCommand(), this);
        customCommandHandler.registerCommand(new HCFReloadCommand(), this);
        customCommandHandler.registerCommand(new LivesCommand(), this);
        customCommandHandler.registerCommand(new PVPCommand(), this);
        customCommandHandler.registerCommand(new RegionCommand(), this);
        customCommandHandler.registerCommand(new SetEndCommand(), this);
        customCommandHandler.registerCommand(new LogoutCommand(), this);
        customCommandHandler.registerCommand(new ReviveCommand(), this);
        customCommandHandler.registerCommand(new CoordsCommand(), this);
        customCommandHandler.registerCommand(new CobbleCommand(), this);
        customCommandHandler.registerCommand(new OreCommand(), this);
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
