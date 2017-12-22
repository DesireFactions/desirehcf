package com.desiremc.hcf.session;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.EntryRegistry;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.DeathBan;
import com.desiremc.core.session.DeathBanHandler;
import com.desiremc.core.session.PVPClass;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.Ticker;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.handler.SOTWHandler;
import com.desiremc.hcf.session.faction.ClaimSession;
import com.desiremc.hcf.session.faction.Faction;
import com.desiremc.hcf.session.faction.FactionChannel;
import com.desiremc.hcf.session.faction.FactionHandler;
import com.desiremc.hcf.session.faction.FactionRank;
import com.desiremc.hcf.session.faction.FactionSetting;
import com.desiremc.hcf.util.FactionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity(value = "hcf_sessions", noClassnameStored = true)
public class FSession
{

    @Transient
    private static final boolean DEBUG = false;

    @Id
    private int id;

    @Indexed
    private UUID uuid;

    @Indexed
    private String server;

    @Property("safe_timer")
    private long safeTimer;

    private int lives;

    private double balance;

    @Property("last_seen")
    private long lastSeen;

    @Reference(ignoreMissing = true)
    private List<DeathBan> deathBans;

    @Embedded
    private List<Ticker> kills;

    @Embedded
    private List<Ticker> deaths;

    @Embedded
    private OreData currentOre;

    private Map<Integer, Integer> kitUses;

    private Map<Integer, Long> kitCooldowns;

    @Transient
    private Faction parsedFaction;
    private int factionId = -1;

    private FactionRank factionRank;

    private FactionChannel channel;

    private List<FactionSetting> factionSettings;

    @Transient
    private ClaimSession claimSession;

    @Transient
    private Session session;

    @Transient
    private PVPTimer pvpTimer;

    @Transient
    private Player player;

    @Transient
    private PVPClass pvpClass;

    @Transient
    private Faction lastLocation;

    public FSession()
    {
        deathBans = new LinkedList<>();
        kills = new LinkedList<>();
        deaths = new LinkedList<>();
        kitUses = new HashMap<>();
        kitCooldowns = new HashMap<>();
        factionSettings = new LinkedList<>();
        pvpTimer = new PVPTimer();
    }

    protected void assignDefaults(UUID uuid, String server)
    {
        this.id = FSessionHandler.getNextId();
        this.uuid = uuid;
        this.server = server;
        this.safeTimer = DesireCore.getConfigHandler().getInteger("timers.pvp.time") * 1000;
        this.factionId = FactionHandler.getWilderness().getId();
        this.parsedFaction = FactionHandler.getWilderness();
    }

    public void applyValues(FSession fSession)
    {
        safeTimer = fSession.safeTimer;
        lives = fSession.lives;
        balance = fSession.balance;
        lastSeen = fSession.lastSeen;

        kills = fSession.kills;
        deaths = fSession.deaths;
        currentOre = fSession.currentOre;
        kitUses = fSession.kitUses;
        kitCooldowns = fSession.kitCooldowns;
        factionId = fSession.factionId;
        factionRank = fSession.factionRank;
        channel = fSession.channel;
        factionSettings = fSession.factionSettings;
    }

    protected void setId(int id)
    {
        this.id = id;
    }

    @IdGetter
    protected int getId()
    {
        return id;
    }

    @IdGetter
    public UUID getUniqueId()
    {
        return uuid;
    }

    protected void setUniqueId(UUID uuid)
    {
        this.uuid = uuid;
    }

    /**
     * A single player can play on multiple instances of the factions servers. This is the literal string name of the
     * server. In the future this may be converted to an enum or other more reliable design pattern, so use with
     * caution.
     *
     * @return the server this HCFSession is for.
     */
    public String getServer()
    {
        return server;
    }

    /**
     * Returns the amount of time, in milliseconds, that is left on the player's safe timer. This is used to keep track
     * of whether or not they are allowed to partake in pvp.
     *
     * @return the amount of safe time left.
     */
    public long getSafeTimeLeft()
    {
        return safeTimer;
    }

    /**
     * Sets the amount of time left on a player's safe timer.
     *
     * @param safeTimer the new amount of safe time left.
     */
    public void setSafeTimeLeft(int safeTimer)
    {
        this.safeTimer = safeTimer;
    }

    /**
     * @return the amount of lives a player has.
     */
    public int getLives()
    {
        return lives;
    }

    /**
     * @param lives the amount of lives to set for the player.
     */
    public void setLives(int lives)
    {
        this.lives = lives;
    }

    /**
     * @param lives the amount of lives to remove from a player.
     */
    public void takeLives(int lives)
    {
        this.lives -= lives;
    }

    /**
     * @param lives the amount of lives to add to a player.
     */
    public void addLives(int lives)
    {
        this.lives += lives;
    }

    /**
     * @return a player's financial balance.
     */
    public double getBalance()
    {
        return balance;
    }

    /**
     * @param balance a player's new financial balance.
     */
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    /**
     * @param amount the amount to add to the player's balance.
     */
    public void depositBalance(double amount)
    {
        this.balance += amount;
    }

    /**
     * @param amount the amount to remove from a player's balance.
     */
    public void withdrawBalance(double amount)
    {
        this.balance -= amount;
    }

    /**
     * @return the last time a player was seen on the server in the form of a unix time stamp.
     */
    public long getLastSeen()
    {
        return lastSeen;
    }

    /**
     * Sets the last time a player was seen. This is saved both when the player connects to the server as well as when
     * they disconnect from the server.
     *
     * @param lastSeen the last time the player was seen.
     */
    public void setLastSeen(long lastSeen)
    {
        this.lastSeen = lastSeen;
    }

    public boolean hasDeathBan()
    {
        return getActiveDeathBan() != null;
    }

    public long getDeathBanEnd()
    {
        DeathBan ban = getActiveDeathBan();
        return ban != null ? ban.getStartTime() : -1;
    }

    public DeathBan getActiveDeathBan()
    {
        for (DeathBan ban : deathBans)
        {
            if (!ban.wasStaffRevived() && !ban.isRevived() && ban.getStartTime() + session.getRank().getDeathBanTime() > System.currentTimeMillis())
            {
                return ban;
            }
        }
        return null;
    }

    public void revive(String reson, boolean staffRevived, UUID reviver)
    {
        DeathBan ban = getActiveDeathBan();
        if (ban == null)
        {
            throw new IllegalStateException("Player does not have a deathban.");
        }
        ban.setRevived(true);
        ban.setStaffRevive(staffRevived);
        ban.setReviveReason(reson);
        ban.setReviver(reviver);
        ban.save();
    }

    public void addKill(UUID victim)
    {
        Ticker tick = null;
        for (Ticker t : kills)
        {
            if (t.getUniqueId().equals(victim))
            {
                tick = t;
            }
        }
        if (tick == null)
        {
            tick = new Ticker(victim);
            kills.add(tick);
        }
        else
        {
            tick.setCount(tick.getCount() + 1);
        }

        if (hasFaction())
        {
            getFaction().addKill();
        }
    }

    public int getTotalKills()
    {
        int count = 0;
        for (Ticker ticker : kills)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public void addDeath(UUID killer)
    {
        if (DEBUG)
        {
            System.out.println("addDeath() called with server " + server + " and killer " + (killer == null ? "null" : killer.toString()) + ".");
        }

        deathBans.add(DeathBanHandler.createDeathBan(getUniqueId()));

        if (killer != null)
        {
            Ticker tick = null;
            for (Ticker t : deaths)
            {
                if (t.getUniqueId().equals(killer))
                {
                    tick = t;
                }
            }
            if (tick == null)
            {
                tick = new Ticker(killer);
                deaths.add(tick);
            }
            else
            {
                tick.setCount(tick.getCount() + 1);
            }
            deaths.add(new Ticker(killer));
        }
    }

    public int getTotalDeaths()
    {
        int count = 0;
        for (Ticker ticker : deaths)
        {
            count += ticker.getCount();
        }
        return count;
    }

    public OreData getCurrentOre()
    {
        if (currentOre == null)
        {
            currentOre = new OreData();
        }
        return currentOre;
    }

    public void useKit(HKit kit)
    {
        Integer val = kitUses.get(kit.getId());
        if (val == null)
        {
            val = 1;
        }
        kitUses.put(kit.getId(), val);
        kitCooldowns.put(kit.getId(), System.currentTimeMillis());
    }

    public long getKitCooldown(HKit kit)
    {
        Long val = kitCooldowns.get(kit.getId());

        if (val == null)
        {
            return 0;
        }

        return Long.max(0, (kit.getCooldown() * 1000) - (System.currentTimeMillis() - val));
    }

    public boolean hasKitCooldown(HKit kit)
    {
        return getKitCooldown(kit) > 0;
    }

    /**
     * If {@link #parsedFaction} is null, it means it has not been properly initialized. This ensures the faction is
     * loaded properly already.
     */
    private void checkFaction()
    {
        if (parsedFaction == null)
        {
            parsedFaction = FactionsUtils.getFaction(factionId);
        }
    }

    /**
     * @return {@code true} if this player is in a faction.<br>
     * {@code false} if this player is not in a faction.
     */
    public boolean hasFaction()
    {
        checkFaction();
        return parsedFaction != null && !parsedFaction.isWilderness();
    }

    /**
     * Returns this player's faction. If they are not in a faction this will return null.
     *
     * @return this player's faction.
     */
    public Faction getFaction()
    {
        checkFaction();
        return parsedFaction;
    }

    /**
     * Sets this palyer's {@link Faction} to the given value. This does nothing more than updating a value.
     *
     * @param faction the new {@link Faction}.
     */
    public void setFaction(Faction faction)
    {
        this.factionId = faction.getId();
        this.parsedFaction = faction;
    }

    /**
     * @return the player's active {@link ClaimSession}.
     */
    public ClaimSession getClaimSession()
    {
        return claimSession;
    }

    /**
     * @return {@code true} if the player has a {@link ClaimSession}.
     */
    public boolean hasClaimSession()
    {
        return claimSession != null;
    }

    /**
     * Clears out the player's current active {@link ClaimSession}.
     */
    public void clearClaimSession()
    {
        // TODO clean up their old claim session.
        claimSession = null;
    }

    /**
     * Set the player's claimSession to the given variable.
     *
     * @param claimSession the new claim session.
     */
    public void setClaimSession(ClaimSession claimSession)
    {
        // TODO clean up their old claim session.
        this.claimSession = claimSession;
    }

    /**
     * Returns the rank that this player holds in their faction. If they are not in a faction this will return null.
     *
     * @return the rank a player holds in their faction.
     */
    public FactionRank getFactionRank()
    {
        return factionRank;
    }

    /**
     * Sets this player's faction rank to the given value. This does nothing more than updating a value.
     *
     * @param factionRank
     */
    public void setFactionRank(FactionRank factionRank)
    {
        this.factionRank = factionRank;
    }

    /**
     * @param setting the setting to check if it's enabled.
     * @return {@code true} if the settings contains the parameter.<br>
     * {@code false} if the settings does not contain the parameter.
     */
    public boolean hasSetting(FactionSetting setting)
    {
        if (factionSettings.contains(setting))
        {
            return true;
        }
        return false;
    }

    /**
     * @param setting the setting to add.
     */
    public void addSetting(FactionSetting setting)
    {
        factionSettings.add(setting);
    }

    /**
     * @param setting the setting to remove.
     */
    public void removeSetting(FactionSetting setting)
    {
        factionSettings.remove(setting);
    }

    /**
     * @return a player's session.
     */
    public Session getSession()
    {
        return session;
    }

    /**
     * Set a player's session. This should only be done when the HCFSession is loaded, which why it is protected rather
     * than public.
     *
     * @param session the corresponding Session.
     */
    protected void setSession(Session session)
    {
        this.session = session;
    }

    public PVPTimer getSafeTimer()
    {
        return pvpTimer;
    }

    public void resetPVPTimer()
    {
        pvpTimer = new PVPTimer();
        safeTimer = DesireCore.getConfigHandler().getInteger("timers.pvp.time") * 1000;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof FSession))
        {
            return false;
        }
        return ((FSession) o).getUniqueId().equals(uuid);
    }

    public String[] getKillDisplay()
    {
        Collections.sort(kills);
        String[] array = new String[kills.size()];
        int i = 0;
        for (Ticker tick : kills)
        {
            array[i] = PlayerUtils.getName(tick.getUniqueId()) + " x" + tick.getCount();
        }
        return array;
    }

    /**
     * @return the last faction whose claim this player was last seen at.
     */
    public Faction getLastFactionLocation()
    {
        return lastLocation;
    }

    /**
     * This method does several things. First it will alert the player that they have changed faction locations unless
     * the {@link Faction} reference stored in lastLocation is null, meaning the player only just connected to the
     * server.
     *
     * @param lastLocation the last faction whose claim this player was last seen at.
     */
    public void setLastLocation(Faction lastLocation)
    {
        if (this.lastLocation != lastLocation)
        {
            if (this.lastLocation != null)
            {
                sendFactionLocationMessage(lastLocation);
            }
            this.lastLocation = lastLocation;
        }
    }

    // ========================================================
    // | The following methods are all convenience methods    |
    // | to easily access things from Session without having  |
    // | to manually call getSession()                        |
    // ========================================================

    /**
     * A convenience method for {@link Session#getRank()}.
     *
     * @return the player's rank.
     */
    public Rank getRank()
    {
        return session.getRank();
    }

    /**
     * A convenience method for {@link Session#getPlayer()}.
     *
     * @return the Bukkit player instance.
     */
    public Player getPlayer()
    {
        return session.getPlayer() != null ? session.getPlayer() : Bukkit.getPlayer(session.getUniqueId());
    }

    /**
     * A convenience method for {@link Session#isOnline()}.
     *
     * @return {@code true} if the player is online. {@code false} otherwise.
     */
    public boolean isOnline()
    {
        return session.isOnline();
    }

    /**
     * A convenience method for {@link Session#getName()}.
     *
     * @return the player's name.
     */
    public String getName()
    {
        return session.getName();
    }

    /**
     * A convenience method for {@link Session#getTokens()}.
     *
     * @return the tokens
     */
    public int getTokens()
    {
        return session.getTokens();
    }

    /**
     * A convenience method for {@link Session#sendMessage(String)}.
     *
     * @param message
     */
    public void sendMessage(String message)
    {
        session.sendMessage(message);
    }

    /**
     * A convenience method for {@link Session#hasAchievement(Achievement)}.
     *
     * @param achievement
     * @return {@code true} if the player has the achievement.
     */
    public boolean hasAchievement(Achievement achievement)
    {
        return session.hasAchievement(achievement);
    }

    /**
     * A convenience method for {@link Session#awardAchievement(Achievement, boolean)}.
     *
     * @param achievement the achievement.
     * @param inform      whether to inform the player.
     */
    public void awardAchievement(Achievement achievement, boolean inform)
    {
        session.awardAchievement(achievement, inform);
    }

    /**
     * A convenience method for {@link Session#getSender()}.
     *
     * @return the {@link CommandSender} of this session.
     */
    public CommandSender getSender()
    {
        return getSession().getSender();
    }

    /**
     * A convenience method for {@link #getPlayer()} and then {@link Player#getLocation()}. If the FSession is not a
     * player, this method will return null and fail gracefully.
     *
     * @return the location of the player.
     */
    public Location getLocation()
    {
        if (isOnline())
        {
            return getPlayer().getLocation();
        }
        else
        {
            return null;
        }
    }

    // ========================================================
    // | The following methods are all convenience methods    |
    // | that are things that are done to HCFSessions a lot,  |
    // | that would be nice to have a standard system for.    |
    // ========================================================

    /**
     * Send a message to the player alerting them that the new location they have moved to is owned by a different
     * faction than the one they were in before.
     *
     * @param factionTo the faction the player is moving to.
     */
    public void sendFactionLocationMessage(Faction factionTo)
    {
        DesireHCF.getLangHandler().sendRenderMessage(getSender(), "factions.moved_location",
                "{fromColor}", getFaction().getRelationshipTo(getLastFactionLocation()).getChatColor(),
                "{fromName}", getLastFactionLocation().getName(),
                "{fromType}", getLastFactionLocation().getType().toString(),
                "{toColor}", getFaction().getRelationshipTo(factionTo).getChatColor(),
                "{toName}", factionTo.getName(),
                "{toType}", factionTo.getType().toString());
    }

    // ========================================================
    // | End of convenience methods                           |
    // ========================================================

    private static final DecimalFormat df = new DecimalFormat("00");

    public class PVPTimer implements Runnable
    {

        private long lastRunTime;

        private boolean paused;

        @Override
        public void run()
        {
            if (player == null || !player.isOnline() || SOTWHandler.getSOTW())
            {
                return;
            }

            if (!paused && safeTimer > 0)
            {
                Bukkit.getScheduler().runTaskLater(DesireCore.getInstance(), this, 5);
            }
            if (DEBUG)
            {
                System.out.println("PVPTimer.run() safeTimer = " + safeTimer);
            }
            safeTimer -= System.currentTimeMillis() - lastRunTime;
            lastRunTime = System.currentTimeMillis();
            if (safeTimer <= 0)
            {
                EntryRegistry.getInstance().removeValue(getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"));
                safeTimer = 0;
            }
            else
            {
                setScoreboard();
            }
        }

        public void setScoreboard()
        {
            EntryRegistry.getInstance().setValue(getPlayer(), DesireHCF.getLangHandler().getStringNoPrefix("pvp.scoreboard"), getTimeLeftFormatted());
        }

        public String getTimeLeftFormatted()
        {
            long time = (safeTimer / 1000);
            return "§b" + df.format(time / 60) + ":" + df.format(time % 60);
        }

        public void pause()
        {
            paused = true;
        }

        public void resume()
        {
            lastRunTime = System.currentTimeMillis();
            paused = false;
            run();
        }

    }

    public PVPClass getPvpClass()
    {
        return pvpClass;
    }

    public void setPvpClass(PVPClass pvpClass)
    {
        this.pvpClass = pvpClass;
        save();
    }

    /**
     * Returns the channel that this player is currently in.
     *
     * @return the channel a player is currently in
     */
    public FactionChannel getChannel()
    {
        return channel;
    }

    /**
     * Sets this player's faction channel to the given value. This does nothing more than updating a value.
     *
     * @param channel
     */
    public void setChannel(FactionChannel channel)
    {
        this.channel = channel;
    }

    /**
     * Save this HCFSession asynchronously.
     */
    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                FSessionHandler.getInstance().save(FSession.this);
            }
        });
    }
}
