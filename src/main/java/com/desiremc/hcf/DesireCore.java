package com.desiremc.hcf;

import java.io.File;
import java.util.UUID;

import com.desiremc.hcf.command.commands.AchievementCommand;
import com.desiremc.hcf.command.commands.BanCommand;
import com.desiremc.hcf.command.commands.CrowbarCommand;
import com.desiremc.hcf.command.commands.EnderChestCommand;
import com.desiremc.hcf.command.commands.HCFReloadCommand;
import com.desiremc.hcf.command.commands.InfoCommand;
import com.desiremc.hcf.command.commands.PVPCommand;
import com.desiremc.hcf.command.commands.SettingsCommand;
import com.desiremc.hcf.command.commands.TempBanCommand;
import com.desiremc.hcf.command.commands.UnbanCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.hcf.api.FileHandler;
import com.desiremc.hcf.api.LangHandler;
import com.desiremc.hcf.barrier.TagHandler;
import com.desiremc.hcf.command.CustomCommandHandler;
import com.desiremc.hcf.command.commands.friends.FriendsCommand;
import com.desiremc.hcf.command.commands.fstat.FStatCommand;
import com.desiremc.hcf.command.commands.lives.LivesCommand;
import com.desiremc.hcf.command.commands.rank.RankCommand;
import com.desiremc.hcf.command.commands.region.RegionCommand;
import com.desiremc.hcf.command.commands.setend.SetEndCommand;
import com.desiremc.hcf.command.commands.staff.StaffCommand;
import com.desiremc.hcf.command.commands.ticket.TicketCommand;
import com.desiremc.hcf.connection.MongoWrapper;
import com.desiremc.hcf.gui.MenuAPI;
import com.desiremc.hcf.listener.ChatListener;
import com.desiremc.hcf.listener.CombatListener;
import com.desiremc.hcf.listener.ConnectionListener;
import com.desiremc.hcf.listener.CreatureSpawnListener;
import com.desiremc.hcf.listener.CrowbarHandler;
import com.desiremc.hcf.listener.InteractListener;
import com.desiremc.hcf.listener.ListenerManager;
import com.desiremc.hcf.listener.MovementListener;
import com.desiremc.hcf.punishment.PunishmentHandler;
import com.desiremc.hcf.session.AchievementManager;
import com.desiremc.hcf.session.FactionSessionHandler;
import com.desiremc.hcf.session.RegionHandler;
import com.desiremc.hcf.session.SessionHandler;
import com.desiremc.hcf.session.StaffHandler;
import com.desiremc.hcf.tickets.TicketHandler;
import com.desiremc.hcf.util.ItemDb;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import net.milkbowl.vault.economy.Economy;

public class DesireCore extends JavaPlugin
{

	private static final UUID CONSOLE = UUID.fromString("00000000-0000-0000-0000-000000000000");

	private static DesireCore instance;

	private static MongoWrapper mongoWrapper;
	private static RegisteredServiceProvider<Economy> economyProvider;

	private static LangHandler lang;
	private static FileHandler config;
	private static ItemDb itemHandler;

	@Override
	public void onEnable()
	{
		instance = this;

		saveDefaultConfig();
		saveResource("lang.yml", false);

		config = new FileHandler(new File(getDataFolder(), "config.yml"));
		lang = new LangHandler(new File(getDataFolder(), "lang.yml"));

		saveResource("items.csv", false);
		itemHandler = new ItemDb();

		mongoWrapper = new MongoWrapper();

		SessionHandler.initialize();
		PunishmentHandler.initialize();
		FactionSessionHandler.initialize();
		RegionHandler.initialize();
		TagHandler.initialize();
		TicketHandler.initialize();
		StaffHandler.initialize();
		MenuAPI.initialize();
		AchievementManager.initialize();
		CustomCommandHandler.initialize();
		ListenerManager.initialize();

		registerListeners();
		registerCommands();

		economyProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

		for (Player p : Bukkit.getOnlinePlayers())
		{
			Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));
		}
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
		customCommandHandler.registerCommand(new FriendsCommand());
		customCommandHandler.registerCommand(new FStatCommand());
		customCommandHandler.registerCommand(new LivesCommand());
		customCommandHandler.registerCommand(new RankCommand());
		customCommandHandler.registerCommand(new RegionCommand());
		customCommandHandler.registerCommand(new SetEndCommand());
		customCommandHandler.registerCommand(new AchievementCommand());
		customCommandHandler.registerCommand(new TempBanCommand());
		customCommandHandler.registerCommand(new BanCommand());
		customCommandHandler.registerCommand(new UnbanCommand());
		customCommandHandler.registerCommand(new CrowbarCommand());
		customCommandHandler.registerCommand(new EnderChestCommand());
		customCommandHandler.registerCommand(new HCFReloadCommand());
		customCommandHandler.registerCommand(new SettingsCommand());
		customCommandHandler.registerCommand(new TicketCommand());
		customCommandHandler.registerCommand(new PVPCommand());
		customCommandHandler.registerCommand(new StaffCommand());
		customCommandHandler.registerCommand(new InfoCommand());
	}

	public MongoWrapper getMongoWrapper()
	{
		return mongoWrapper;
	}

	public static LangHandler getLangHandler()
	{
		return lang;
	}

	public static FileHandler getConfigHandler()
	{
		return config;
	}

	public static RegisteredServiceProvider<Economy> getEconomy()
	{
		return economyProvider;
	}

	public static UUID getConsoleUUID()
	{
		return CONSOLE;
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

	public static ItemDb getItemHandler()
	{
		return itemHandler;
	}

	public static DesireCore getInstance()
	{
		return instance;
	}

}
