package me.borawski.hcf;import org.bukkit.ChatColor;import org.bukkit.command.CommandExecutor;import org.bukkit.plugin.java.JavaPlugin;import me.borawski.hcf.api.LangHandler;import me.borawski.hcf.command.CustomCommandHandler;import me.borawski.hcf.command.commands.AchievementCommand;import me.borawski.hcf.command.commands.BanCommand;import me.borawski.hcf.command.commands.FStatCommand;import me.borawski.hcf.command.commands.FriendsCommand;import me.borawski.hcf.command.commands.InfoCommand;import me.borawski.hcf.command.commands.KothCommand;import me.borawski.hcf.command.commands.ManualCommand;import me.borawski.hcf.command.commands.RankCommand;import me.borawski.hcf.command.commands.SeasonCommand;import me.borawski.hcf.command.commands.SettingsCommand;import me.borawski.hcf.connection.Mongo;import me.borawski.hcf.listener.ListenerManager;import me.borawski.hcf.listener.PlayerListener;import me.borawski.hcf.manuel.ManualManager;import me.borawski.hcf.punishment.PunishmentManager;import me.borawski.hcf.session.AchievementManager;import me.borawski.hcf.util.ManualUtil;import me.borawski.koth.Plugin;import me.finestdev.components.Components;/** * Created by Ethan on 3/8/2017. */public class Core extends JavaPlugin implements CommandExecutor {    /**     * Instance     */    private static Core instance;    /**     * Variables     */    private Mongo mongo;    private ListenerManager listenerManager;    private CustomCommandHandler customCommandHandler;    private PunishmentManager punishmentManager;    private AchievementManager achievementManager;    private ManualManager manualManager;    private static LangHandler lang;    /**     * Components     */    private Components components;    /**     * Koth     */    private Plugin koth;    @Override    public void onEnable() {        instance = this;        saveDefaultConfig();        this.mongo = new Mongo();        this.listenerManager = new ListenerManager(this);        listenerManager.addListener(new PlayerListener(this));        listenerManager.registerAll();        customCommandHandler = new CustomCommandHandler();        registerCommands();        this.punishmentManager = new PunishmentManager(this);        this.achievementManager = new AchievementManager(this);        this.manualManager = new ManualManager(this);        registerManuals();        this.components = new Components(this);        components.onEnable();        this.koth = new Plugin(this);        koth.onEnable();    }    /**     * Gets the singleton instance of the Core class.     *      * @return     */    public static Core getInstance() {        return instance;    }    public void registerCommands() {        customCommandHandler.registerCommand(new AchievementCommand());        customCommandHandler.registerCommand(new BanCommand());        customCommandHandler.registerCommand(new FriendsCommand());        customCommandHandler.registerCommand(new FStatCommand());        customCommandHandler.registerCommand(new InfoCommand());        customCommandHandler.registerCommand(new KothCommand());        customCommandHandler.registerCommand(new ManualCommand());        customCommandHandler.registerCommand(new RankCommand());        customCommandHandler.registerCommand(new SeasonCommand());        customCommandHandler.registerCommand(new SettingsCommand());    }    public void registerManuals() {        ManualUtil.initializeManuals(manualManager.getManualMap());    }    /**     * Gets the instance of MongoDB maintained by the plugin.     *      * @return     */    public Mongo getMongo() {        return mongo;    }    /**     * Gets the instance of the listener manager. Used to add more listeners or     * update existing ones.     *      * @return     */    public ListenerManager getListenerManager() {        return listenerManager;    }    public CustomCommandHandler getCommandHandler() {        return customCommandHandler;    }    public PunishmentManager getPunishmentManager() {        return punishmentManager;    }    public AchievementManager getAchievementManager() {        return achievementManager;    }    public ManualManager getManualManager() {        return manualManager;    }    public static LangHandler getLangHandler() {        return lang;    }    public String getPrefix() {        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF" + ChatColor.RESET + "" + ChatColor.GRAY + " ";    }}